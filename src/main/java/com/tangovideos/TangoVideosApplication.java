package com.tangovideos;

import org.glassfish.jersey.server.ResourceConfig;
import org.secnod.shiro.jersey.AuthInjectionBinder;
import org.secnod.shiro.jersey.AuthorizationFilterFeature;
import org.secnod.shiro.jersey.SubjectFactory;

public class TangoVideosApplication extends ResourceConfig {

    public TangoVideosApplication() {
        register(new AuthorizationFilterFeature());
            register(new SubjectFactory());
            register(new AuthInjectionBinder());
            packages("com.tangovideos");
        }
    }

