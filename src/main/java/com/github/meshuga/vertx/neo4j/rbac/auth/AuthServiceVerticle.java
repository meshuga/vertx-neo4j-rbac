package com.github.meshuga.vertx.neo4j.rbac.auth;

import com.github.meshuga.vertx.neo4j.rbac.auth.impl.AuthServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ProxyHelper;

public class AuthServiceVerticle  extends AbstractVerticle {

    AuthService service;

    @Override
    public void start() throws Exception {
        // Create the client object
        service = new AuthServiceImpl(vertx);
        // Register the handler
        ProxyHelper.registerService(AuthService.class, vertx, service, AuthService.SERVICE_ADDRESS);
    }

}