package com.tangovideos.services;

import java.util.HashSet;
import java.util.Set;

import com.tangovideos.services.Interfaces.UserService;
import com.tangovideos.services.neo4j.Neo4DB;
import com.tangovideos.services.neo4j.Neo4JServiceFactory;
import com.tangovideos.services.neo4j.TangoVideosServiceFactory;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


// https://github.com/SchulteMarkus/tiramisu/blob/d8b247f8a8f4fd7350b723f09aabde571f61bcba/src/main/java/com/bestellbaer/services/Neo4jUserShiroRealm.java
public class TangoVideosAuthRealm extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) {
        final UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        final String username = upToken.getUsername();
        final String password = String.valueOf(upToken.getPassword());

        if (TangoVideosServiceFactory.getUserService().verifyCredentials(username, password)) {
            return new SimpleAuthenticationInfo(upToken.getUsername(), password, this.getName());
        }

        throw new IncorrectCredentialsException("No matching login found");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("Principal is null.");
        }





        final Set<Permission> permissions = TangoVideosServiceFactory.getUserService().getAllRoles("admin");
        permissions.add(new WildcardPermission("company:readOrders:whatever"));
        final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setObjectPermissions(permissions);
        return info;
    }
}
