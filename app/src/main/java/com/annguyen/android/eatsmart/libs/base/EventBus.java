package com.annguyen.android.eatsmart.libs.base;

/**
 * Created by annguyen on 6/1/2017.
 */

public interface EventBus {
    void register(Object subscriber);
    void unregister(Object subscriber);
    void post(Object event);
}
