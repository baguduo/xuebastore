package com.touchfuture.takeout.serviceImpl;

import com.touchfuture.takeout.bean.User;
import com.touchfuture.takeout.common.EncryptionUtil;
import com.touchfuture.takeout.common.QueryBase;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.mapper.UserMapper;
import com.touchfuture.takeout.service.UserService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by user on 2016/11/1.
 */
@Service
public class UserServiceImpl implements UserService {

    Log logger = LogFactory.getLog(UserService.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 添加用户
     * @param user 用户对象
     * @return 0-添加成功 12-密码为空 1-添加失败
     */
    @Override
    public int add(User user) {
        if(userMapper.isExitByName(user.getUsername())){
            logger.warn("用户名"+user.getUsername()+"已经存在，添加失败");
            return Status.EXISTS;
        }
        if(user.getPassword() == null){
            logger.warn("密码不能为空");
            return Status.PASSWORD_BLANK;
        }

        String password = EncryptionUtil.encrypt("apple",user.getPassword());
        user.setPassword(password);
        if(userMapper.insert(user) > 0){
            logger.warn("用户" + user.getUsername()+"添加成功");
            return Status.SUCCESS;
        }
        logger.warn("用户" + user.getUsername()+"添加失败");
        return Status.ERROR;
    }


    @Override
    public int delete(User user) {
        User user_db = userMapper.selectByPrimaryKey(user.getId());
        if(user_db == null){
            logger.warn("不存在该用户");
            return Status.NOT_EXISTS;
        }
        if(userMapper.deleteByPrimaryKey(user.getId()) > 0){
            logger.warn("用户" +user.getUsername() +"删除成功");
            return Status.SUCCESS;
        }
        logger.warn("用户" +user.getUsername() +"删除失败");
        return Status.ERROR;
    }

    @Override
    public int update(User user) {
        User user_db = userMapper.selectByPrimaryKey(user.getId());
        if(user_db == null){
            logger.warn("该用户不存在，更新失败");
            return Status.NOT_EXISTS;
        }
        if(userMapper.updateByPrimaryKeySelective(user) > 0){
            logger.warn("用户信息更新成功");
            return Status.SUCCESS;
        }
        logger.warn("用户信息更新失败");
        return Status.ERROR;
    }

    @Override
    public int login(User user){
        if( !userMapper.isExitByName(user.getUsername())){
            logger.warn("用户不存在");
            return Status.NOT_EXISTS;
        }
        User user_db = userMapper.selectByName(user.getUsername());
        String password_db = user_db.getPassword();
        String password = EncryptionUtil.encrypt("apple", user.getPassword());
        //String password = EncryptionUtil.encrypt(user.getPassword());
        if(password_db.equals(password)){
            logger.warn("登陆成功");
            return Status.SUCCESS;
        }
        logger.warn("登陆失败");
        return Status.PASSWD_NOT_MATCH;
    }

    @Override
    public User get(int id) {
        if(!userMapper.isExitById(id)){
            logger.warn("该id不存在");
            return null;
        }
        User user_db = userMapper.selectByPrimaryKey(id);
        return user_db;
    }

    @Override
    public void query(QueryBase queryBase) {
        if(logger.isDebugEnabled()){
            logger.debug("根据参数"+queryBase.getParameters()+"查询用户");
        }
        queryBase.getParameters().put("statusDelete","-1");
        List<User> userList  = userMapper.queryAll(queryBase);
        for(User userTemp : userList){
            //String password = EncryptionUtil.encrypt(userTemp.getPassword());
            String password1 = EncryptionUtil.decrypt("apple",userTemp.getPassword());
//            logger.warn("原始密码"+userTemp.getPassword());
//            logger.warn("解密密码"+password1);
            userTemp.setPassword(password1);
        }
        //queryBase.setResults(userMapper.queryAll(queryBase));
        queryBase.setResults(userList);
        queryBase.setTotalRow(userMapper.countUser(queryBase));
    }
}
