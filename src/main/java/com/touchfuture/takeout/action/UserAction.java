package com.touchfuture.takeout.action;

/**
 * Created by user on 2016/11/2.
 */

import com.touchfuture.takeout.bean.User;
import com.touchfuture.takeout.common.*;
import com.touchfuture.takeout.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
public class UserAction {

    Log logger  = LogFactory.getLog(UserAction.class);

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/api/user/login" ,method = RequestMethod.POST)
    public Object login(HttpServletRequest request,@RequestBody User user){
        int status;
        String message = "";
        status = userService.login(user);
        if(status == Status.SUCCESS){
            message = "用户登陆成功";
            ActionUtil.setCurrentUser(request,user);
            return new Response(status,message,user);
        }
        message = "用户登陆失败";
        return new Response(status,message,user);
    }

    @ResponseBody
    @RequestMapping(value = "/api/user/add",method = RequestMethod.POST)
    public Object add(HttpServletRequest request,@RequestBody User user){
        int status;
        String message = "";
        status = userService.add(user);
        if(status == Status.SUCCESS){
            message = "用户添加成功";
            return new Response(status,message,user);
        }
        message = "用户添加失败";
        return new Response(status,message,user);
    }

    @ResponseBody
    @RequestMapping(value = "/api/user/delete/{id}",method = RequestMethod.DELETE)
    public Object delete(HttpServletRequest request,@PathVariable int id){
        int status;
        String message = "";
        User user = new User();
        user.setId(id);
        status = userService.delete(user);
        if(status == Status.SUCCESS){
            message = "用户删除成功";
            return new Response(status,message,user);
        }
        message = "用户删除失败";
        return new Response(status,message,user);
    }

    @ResponseBody
    @RequestMapping(value = "/api/user/update",method = RequestMethod.POST)
    public Object update(HttpServletRequest request,@RequestBody User user){
        int status;
        String message = "";
        status = userService.update(user);
        if(status == Status.SUCCESS){
            message = "用户更新成功";
            return new Response(status,message,user);
        }
        message = "用户更新失败";
        return new Response(status,message,user);
    }

    @ResponseBody
    @RequestMapping(value = "/api/user/get/{id}",method = RequestMethod.POST)
    public Object get(HttpServletRequest request,@PathVariable int id){
        int status;
        String message = "";
        User user = userService.get(id);
        if(user != null){
            status = Status.SUCCESS;
            message = "用户查询成功";
            return new Response(status,message,user);
        }
        status = Status.ERROR;
        message = "用户查询失败";
        return new Response(status,message,null);
    }

    @ResponseBody
    @RequestMapping(value = "/api/user/query",method = RequestMethod.GET)
    public Object query(HttpServletRequest request,QueryBase query){
        String message;
        if(query == null){
            query = new QueryBase();
        }
        userService.query(query);
       // String password = EncryptionUtil.encrypt(query.)
        return new Response(Status.SUCCESS,query.getResults(),query.getTotalRow());
    }

    @ResponseBody
    @RequestMapping(value = "api/user/loginOut",method = RequestMethod.GET)
    public Object loginOut(HttpServletRequest request){
        ActionUtil.removeCurrentUser(request);
        String message = "用户退出成功";
        return new Response(Status.SUCCESS,message);
    }



}
