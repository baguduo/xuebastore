package com.touchfuture.takeout.aspect;

import com.touchfuture.takeout.bean.Admin;
import com.touchfuture.takeout.common.ActionUtil;
import com.touchfuture.takeout.common.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by onglchen on 12/12/15.
 */
@Aspect
public class ActionIntercepters {
    @Resource(name="invalidOperationResponse")  //在Spring-MVC文件中有注解
            Response invalidResponse;



    @Around("@annotation(com.touchfuture.takeout.annotation.AdminLoginAuthorized)")
    public Object checkLoginedUserAuthorized(ProceedingJoinPoint point) throws Throwable{

        try{
            HttpServletRequest request=(HttpServletRequest) point.getArgs()[0];
            Object obj= ActionUtil.getCurrentAdmin(request);
            if(obj!=null&&obj instanceof Admin){
                Admin admin=(Admin) obj;
                    return point.proceed();
            }else {
                //request.getRequestDispatcher("/web/html/page/login.html").forward(request, response);
               // response.sendRedirect("/takeout/web/html/page/login.html");
              //  return  new ModelAndView("/takeout/web/html/page/login.html");
                return  invalidResponse;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return  invalidResponse;

    }
}
