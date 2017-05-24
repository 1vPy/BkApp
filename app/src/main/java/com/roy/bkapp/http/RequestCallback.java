package com.roy.bkapp.http;

/**
 * Created by 1vPy(Roy) on 2017/4/10.
 */

public interface RequestCallback<T> {
    void onSuccess(T t);

    void onFailure(String message);
}
