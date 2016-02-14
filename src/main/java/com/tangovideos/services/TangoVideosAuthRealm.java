package com.tangovideos.services;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;


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

        final Set<Permission> permissions = TangoVideosServiceFactory.getUserService().getAllPermissions("admin");
        permissions.add(new WildcardPermission("videos:write"));
        final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setObjectPermissions(permissions);
        return info;
    }
}
