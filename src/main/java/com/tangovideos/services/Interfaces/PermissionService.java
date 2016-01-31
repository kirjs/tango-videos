package com.tangovideos.services.Interfaces;

import org.apache.shiro.authz.Permission;

import java.util.Set;

public interface PermissionService {

    Set<Permission> getUserPermissions(String id);

}
