/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.github.meshuga.vertx.neo4j.rbac.auth.rxjava;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;


public class AuthService {

  final com.github.meshuga.vertx.neo4j.rbac.auth.AuthService delegate;

  public AuthService(com.github.meshuga.vertx.neo4j.rbac.auth.AuthService delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static AuthService create(Vertx vertx) { 
    AuthService ret= AuthService.newInstance(com.github.meshuga.vertx.neo4j.rbac.auth.AuthService.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  public static AuthService createProxy(Vertx vertx, String address) { 
    AuthService ret= AuthService.newInstance(com.github.meshuga.vertx.neo4j.rbac.auth.AuthService.createProxy((io.vertx.core.Vertx) vertx.getDelegate(), address));
    return ret;
  }

  public void hasPermission(String userName, String permission, Handler<AsyncResult<Boolean>> resultHandler) { 
    this.delegate.hasPermission(userName, permission, resultHandler);
  }

  public Observable<Boolean> hasPermissionObservable(String userName, String permission) { 
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    hasPermission(userName, permission, resultHandler.toHandler());
    return resultHandler;
  }


  public static AuthService newInstance(com.github.meshuga.vertx.neo4j.rbac.auth.AuthService arg) {
    return arg != null ? new AuthService(arg) : null;
  }
}
