package com.github.meshuga.vertx.neo4j.acl.auth;

import com.github.meshuga.vertx.neo4j.acl.auth.impl.AuthServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;

@ProxyGen
@VertxGen
public interface AuthService {
    String SERVICE_ADDRESS = "vertx-neo4j-acl-service";

    static AuthService create(Vertx vertx) {
        return new AuthServiceImpl(vertx);
    }

    static AuthService createProxy(Vertx vertx, String address) {
        return ProxyHelper.createProxy(AuthService.class, vertx, address);
    }

    void hasPermission(String userName, String permission, Handler<AsyncResult<Boolean>> resultHandler);
}
