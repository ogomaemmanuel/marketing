package com.ogoma.marketing.core.abstractions;

public interface MessageRouter {

     <M extends  Message> void route(M message);
}
