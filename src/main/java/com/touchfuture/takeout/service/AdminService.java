package com.touchfuture.takeout.service;

import com.touchfuture.takeout.bean.Admin;

/**
 * Created by user on 2016/10/30.
 */
public interface AdminService extends BasicService<Admin> {
     int login(Admin admin);

     Admin getByName(String name);

}
