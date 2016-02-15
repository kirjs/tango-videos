package com.tangovideos.services.Interfaces;

import com.tangovideos.models.UserProfile;
import org.apache.shiro.authz.Permission;
import org.neo4j.graphdb.Node;

import java.util.Set;

public interface UserService {

    boolean userExists(String id);
    boolean verifyCredentials(String id, String password);
    void list();

    UserProfile getUserProfile(String id);

    Set<String> getAllPermissionsAsStrings(String id);

    Set<Permission> getAllPermissions(String id);

    Node addUser(String admin, String password, Node role);
}
