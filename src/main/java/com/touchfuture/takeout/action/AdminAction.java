package com.touchfuture.takeout.action;

import com.touchfuture.takeout.bean.Admin;
import com.touchfuture.takeout.common.ActionUtil;
import com.touchfuture.takeout.common.Response;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.service.AdminService;
import com.touchfuture.takeout.serviceImpl.AdminServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * Created by user on 2016/10/30.
 */
@Controller
public class AdminAction {
    private final Log logger = LogFactory.getLog(AdminAction.class);

    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping(value = "/api/admin",method = RequestMethod.POST)
    public Object registUser(HttpServletRequest request,@RequestBody Admin admin){
        logger.info("要添加的管理员"+admin);
        int status;
        String message = "";
        status = adminService.add(admin);
        if(status == Status.SUCCESS){
            message = "管理员添加成功";
            return new Response(status,message,admin.getId());
        }
        message = "管理员添加失败";
        return new Response(status,message);
    }

    @ResponseBody
    @RequestMapping(value = "/api/admin/login",method = RequestMethod.POST)
    public Object login(HttpServletRequest request,@RequestBody Admin admin){
        logger.info("要登陆的管理员"+admin);
        int status;
        String message = "";
        status = adminService.login(admin);
        if(status == Status.SUCCESS){
            message = "管理员登陆成功";
            ActionUtil.setCurrentAdmin(request,admin);
            Admin admin_db = adminService.getByName(admin.getUsername());
            return new Response(status,message,admin_db);
        }
        message = "登陆失败";
        return new Response(status,message,admin.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/api/admin/delete",method = RequestMethod.POST)
    public Object delete(HttpServletRequest request,@RequestBody Admin admin){
        logger.info("要删除的管理员"+admin);
        int status;
        String message = "";
        status = adminService.delete(admin);
        if(status == Status.SUCCESS){
            message = "管理员删除成功";
            return new Response(status,message,admin.getId());
        }
        message = "管理员删除失败";
        return new Response(status,message,admin.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/api/admin/update",method = RequestMethod.POST)
    public Object update(HttpServletRequest request,@RequestBody Admin admin){
        logger.info("要更新的管理员信息"+admin);
        int status;
        String message = "";
        status = adminService.update(admin);
        if(status == Status.SUCCESS){
            message = "管理员更新成功";
            return new Response(status,message,admin.getId());
        }
        message = "管理员更新失败";
        return new Response(status,message,admin.getId());
    }

    @ResponseBody
    @RequestMapping(value = "api/admin/loginOut",method = RequestMethod.GET)
    public Object loginOut(HttpServletRequest request){
        ActionUtil.removeCurrentAdmin(request);
        String message = "管理员退出成功";
        return new Response(Status.SUCCESS,message);
    }


}
