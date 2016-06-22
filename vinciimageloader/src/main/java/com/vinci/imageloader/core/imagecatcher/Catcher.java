package com.vinci.imageloader.core.imagecatcher;

import com.vinci.imageloader.core.exception.VinciException;

/**
 * Created by SpringXu-Git on 15/11/10.
 */
public interface Catcher<T> {
    public T get(String urlKey) throws VinciException;

    public String getType();

    void close()throws VinciException;
}
