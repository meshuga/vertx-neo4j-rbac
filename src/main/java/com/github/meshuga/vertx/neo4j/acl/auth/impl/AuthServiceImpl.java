package com.github.meshuga.vertx.neo4j.acl.auth.impl;

import com.github.meshuga.vertx.neo4j.acl.MainVerticle;
import com.github.meshuga.vertx.neo4j.acl.auth.AuthService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;

public class AuthServiceImpl implements AuthService {
    private Vertx vertx;

    public AuthServiceImpl(io.vertx.core.Vertx vertx) {
        this.vertx = new Vertx(vertx);
    }

    public void hasPermission(String userName, String permission, Handler<AsyncResult<Boolean>> resultHandler) {
        JsonObject query = new JsonObject()
                .put("query", "MATCH (:User{ name: '" + userName + "'})-[:memberOf]->()-[:childOf*0..]->(r:Role)," +
                        " r-[]->(p:Permission {name: '" + permission + "'})\n" +
                        "RETURN count(p)");
        vertx.eventBus()
                .sendObservable(MainVerticle.NEO4J_ADDRESS, query)
        .subscribe(res -> {
            JsonObject body = (JsonObject)res.body();
            Integer hasPermission = body.getJsonArray("data").getJsonArray(0).getInteger(0);
            resultHandler.handle(Future.succeededFuture(hasPermission != 0));
        }, err -> {
            resultHandler.handle(Future.failedFuture(err));
        });
    }
}
