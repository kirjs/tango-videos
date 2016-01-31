package com.tangovideos.services.Interfaces;

import org.apache.shiro.authz.Permission;

import java.util.Set;

public interface UserService {

    boolean userExists(String id);
    boolean verifyCredentials(String id, String password);
    void list();
    Set<Permission> getAllRoles(String id);
}
