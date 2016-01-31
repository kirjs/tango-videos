package com.tangovideos;

import com.tangovideos.resources.Index;
import com.tangovideos.resources.Login;
import com.tangovideos.resources.Video;
import org.glassfish.jersey.server.ResourceConfig;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.secnod.shiro.jersey.AuthInjectionBinder;
import org.secnod.shiro.jersey.AuthorizationFilterFeature;
import org.secnod.shiro.jersey.SubjectFactory;

import java.io.File;

public class TangoVideosApplication extends ResourceConfig {

    public TangoVideosApplication() {
        register(new AuthorizationFilterFeature());
            register(new SubjectFactory());
            register(new AuthInjectionBinder());
            packages("com.tangovideos");
        }
    }

