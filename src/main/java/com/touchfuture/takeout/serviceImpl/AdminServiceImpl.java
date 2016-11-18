package com.touchfuture.takeout.serviceImpl;

import com.touchfuture.takeout.bean.Admin;
import com.touchfuture.takeout.common.EncryptionUtil;
import com.touchfuture.takeout.common.QueryBase;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.mapper.AdminMapper;
import com.touchfuture.takeout.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by user on 2016/10/30.
 */
@Service
public class AdminServiceImpl implements AdminService {

    private Log logger = LogFactory.getLog(AdminServiceImpl.class);

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public int add(Admin obj){
        if(adminMapper.isExitByName(obj.getUsername())){
            logger.warn("管理员" + obj.getUsername() + "已经存在");
            return Status.EXISTS;
        }
        String encryPassword = EncryptionUtil.encrypt(obj.getPassword());
        obj.setUsername(obj.getUsername());
        obj.setPassword(encryPassword);
        if(adminMapper.insert(obj) > 0){
            logger.warn("管理员"+ obj.getUsername()  +"添加成功");
            return Status.SUCCESS;
        }
        logger.warn("管理员"+ obj.getUsername()  +"添加失败");
        return Status.ERROR;
    }

    @Override
    public int login(Admin obj){
//        String name = obj.getUsername();
//        Admin admin_db = adminMapper.selectByName(obj.getUsername());
//        String password = EncryptionUtil.encrypt(obj.getPassword());
//        logger.warn("欲登陆管理员姓名" + obj.getUsername() + "欲登陆管理员密码" + password);
//        if(admin_db != null)
//         logger.warn("数据库中管理员姓名" + admin_db.getUsername() + "数据库中管理员密码" + admin_db.getPassword());
//        if(password.equals(admin_db.getPassword())){
//            logger.warn("管理员"+ obj.getUsername()  +"登陆成功");
//            return Status.SUCCESS;
//        }
//        logger.warn("管理员"+ obj.getUsername()  +"登陆失败");
//        return Status.PASSWD_NOT_MATCH;

        if( !adminMapper.isExitByName(obj.getUsername())){
            logger.warn("管理员不存在");
            return Status.NOT_EXISTS;
        }
        Admin admin_db = adminMapper.selectByName(obj.getUsername());
        String password_db = admin_db.getPassword();
        String password = EncryptionUtil.encrypt(obj.getPassword());
        if(password_db.equals(password)){
            logger.warn("登陆成功");
            return Status.SUCCESS;
        }
        logger.warn("登陆失败");
        return Status.PASSWD_NOT_MATCH;
    }

    @Override
    public int delete(Admin obj){
        int id = obj.getId();
        if(adminMapper.isExitById(id)){
            adminMapper.deleteByPrimaryKey(id);
            return Status.SUCCESS;
        }
        return Status.NO_DELETE;
    }

    @Override
    public int update(Admin obj){
        if(!adminMapper.isExitById(obj.getId())){
            logger.warn("管理员不存在");
            return Status.NOT_EXISTS;
        }
        if(adminMapper.isExitByName(obj.getUsername())){
            logger.debug("用户名已存在");
            return Status.EXISTS;
        }
        Admin admin_db = adminMapper.selectByPrimaryKey(obj.getId());
        String password_new = EncryptionUtil.encrypt(obj.getPassword());
        obj.setPassword(password_new);
        if(adminMapper.updateByPrimaryKey(obj) >  0){
            logger.debug("管理员信息更新成功");
            return Status.SUCCESS;
        }
        logger.debug("管理员信息更新失败");
        return Status.NO_UPDATE;
    }


    public Admin getByName(String name){
        Admin admin_db = adminMapper.selectByName(name);
        return admin_db;
    }

    @Override
    public Admin get(int id){
       return null;
    }

    @Override
    public void query(QueryBase queryBase){

    }

}
