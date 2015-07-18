package com.github.meshuga.vertx.neo4j.acl;

import com.github.meshuga.vertx.neo4j.acl.auth.AuthServiceVerticle;
import com.github.meshuga.vertx.neo4j.acl.auth.rxjava.AuthService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.functions.Action1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainVerticle extends AbstractVerticle {
    public static final String NEO4J_ADDRESS = "neo4j-graph.cypher.query";

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    public static void main(String[] args) throws Exception {
        Vertx mainVertx = new Vertx(io.vertx.core.Vertx.vertx());

        mainVertx.deployVerticleObservable("service:neo4jService")
                .map(res -> mainVertx.deployVerticleObservable(AuthServiceVerticle.class.getCanonicalName()))
                .map(res -> mainVertx.deployVerticleObservable(MainVerticle.class.getCanonicalName()))
                .subscribe(res -> logger.info("started"), logger::error);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        AuthService authService = AuthService.createProxy(vertx, com.github.meshuga.vertx.neo4j.acl.auth.AuthService.SERVICE_ADDRESS);

        Router router = Router.router(vertx);
        router.get("/init").handler(routingContext -> {
            String initialDatabase = null;
            try {
                initialDatabase = new String(Files.readAllBytes(Paths.get("initialDatabase.txt")));
            } catch (IOException e) {
                routingContext.response().end("error " + e);
                return;
            }

            vertx.eventBus()
                    .sendObservable(NEO4J_ADDRESS, new JsonObject().put("query", initialDatabase))
                    .subscribe(res -> routingContext.response().end("OK"),
                            errorHandler(routingContext));
        });

        router.get("/landingPage").handler(routingContext -> {
            String userName = routingContext.request().getParam("userName");
            authService.hasPermissionObservable(userName, "LandingPageViewing").subscribe(res -> {
                if (res == true) {
                    routingContext.response().end("Authorized");
                } else {
                    routingContext.response().setStatusCode(401).end("Not Authorized");
                }
            }, errorHandler(routingContext));
        });

        router.get("/moderatorPage").handler(routingContext -> {
            String userName = routingContext.request().getParam("userName");
            authService.hasPermissionObservable(userName, "ModeratorPageViewing").subscribe(res -> {
                if (res == true) {
                    routingContext.response().end("Authorized");
                } else {
                    routingContext.response().setStatusCode(401).end("Not Authorized");
                }
            }, errorHandler(routingContext));
        });

        router.get("/adminPage").handler(routingContext -> {
            String userName = routingContext.request().getParam("userName");
            authService.hasPermissionObservable(userName, "AdminPageViewing").subscribe(res -> {
                if (res == true) {
                    routingContext.response().end("Authorized");
                } else {
                    routingContext.response().setStatusCode(401).end("Not Authorized");
                }
            }, errorHandler(routingContext));
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private Action1<Throwable> errorHandler(RoutingContext routingContext) {
        return err -> routingContext.response().end("error " + err);
    }
}
