package com.tangovideos.services.Interfaces;

import com.tangovideos.models.UserProfile;
import org.apache.shiro.authz.Permission;

import java.util.Set;

public interface VideoService {
    void addVideo(String videoId, String userId);
}
