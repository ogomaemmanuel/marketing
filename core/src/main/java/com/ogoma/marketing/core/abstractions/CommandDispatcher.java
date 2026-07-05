package com.ogoma.marketing.core.abstractions;

public interface CommandDispatcher {

     <C extends Command<R>,R > R dispatch(C command);
}
