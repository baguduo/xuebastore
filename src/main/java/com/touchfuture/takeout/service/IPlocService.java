package com.touchfuture.takeout.service;

import java.io.UnsupportedEncodingException;

/**
 * Created by user on 2016/12/22.
 */
public interface IPlocService {
    public String getAddresses(String content, String encodingString)
            throws UnsupportedEncodingException;
}
