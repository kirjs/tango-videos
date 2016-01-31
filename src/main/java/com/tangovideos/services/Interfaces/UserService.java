package com.tangovideos.services.Interfaces;

import com.tangovideos.models.UserProfile;
import org.apache.shiro.authz.Permission;

import java.util.Set;

public interface UserService {

    boolean userExists(String id);
    boolean verifyCredentials(String id, String password);
    void list();

    UserProfile getUserProfile(String id);

    Set<Permission> getAllRoles(String id);
}
