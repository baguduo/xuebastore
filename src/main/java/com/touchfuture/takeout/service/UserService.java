package com.touchfuture.takeout.service;

import com.touchfuture.takeout.bean.User;

/**
 * Created by user on 2016/11/1.
 */
public interface UserService extends BasicService<User> {
    int login(User user);
}
