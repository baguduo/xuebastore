package com.touchfuture.takeout.action;

import com.touchfuture.takeout.bean.Plane;
import com.touchfuture.takeout.bean.PlaneImage;
import com.touchfuture.takeout.common.QueryBase;
import com.touchfuture.takeout.common.RenameUtil;
import com.touchfuture.takeout.common.Response;
import com.touchfuture.takeout.common.Status;
import com.touchfuture.takeout.service.PlaneImageService;
import com.touchfuture.takeout.service.PlaneService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by user on 2016/12/23.
 */
@Controller
public class PlaneImageAction {

    Log logger = LogFactory.getLog(FlightAction.class);

    @Autowired
    @Qualifier("planeImageService")
    PlaneImageService  planeImageService;

    /**
     * 添加图片信息
     * @param request 请求
     * @param planeImage 机型
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/planeImg/add",method = RequestMethod.POST)
    public Object add(HttpServletRequest request,@RequestBody PlaneImage planeImage){
        int status;
        String message = "";
        status = planeImageService.add(planeImage);
        if(status == Status.SUCCESS){
            message = "图片添加成功";
            return new Response(status,message,planeImage);
        }
        message = "机型信息添加失败";
        return new Response(status,message,planeImage);
    }

    /**
     * 删除图片信息
     * @param request 请求
     * @param id 图片信息id
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/planeImg/delete/{id}",method = RequestMethod.DELETE)
    public Object delete(HttpServletRequest request,@PathVariable int id){
        int status;
        String message = "";
        PlaneImage planeImage = new PlaneImage();
        planeImage.setId(id);
        status = planeImageService.delete(planeImage);
        if(status == Status.SUCCESS){
            message = "图片信息删除成功";
            return new Response(status,message,planeImage);
        }
        message = "图片信息删除失败";
        return new Response(status,message,planeImage);
    }

    /**
     * 更新图片信息
     * @param request 请求
     * @param planeImage 要更新的图片信息
     * @return  0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/planeImg/update",method = RequestMethod.PATCH)
    public Object update(HttpServletRequest request,@RequestBody PlaneImage planeImage){
        int status;
        String message = "";
        status = planeImageService.update(planeImage);
        if(status == Status.SUCCESS){
            message = "图片信息更新成功";
            return new Response(status,message,planeImage);
        }
        message = "图片信息更新失败";
        return new Response(status,message,planeImage);
    }

    /**
     * 查询图片
     * @param request 请求
     * @param id 机型信息id
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/planeImg/get/{id}",method = RequestMethod.POST)
    public Object get(HttpServletRequest request,@PathVariable int id){
        int status;
        String message = "";
        PlaneImage planeImage = planeImageService.get(id);
        if(planeImage != null){
            status = Status.SUCCESS;
            message = "图片查询成功";
            return new Response(status,message,planeImage);
        }
        status = Status.ERROR;
        message = "图片查询失败";
        return new Response(status,message,null);
    }

    /**
     * 查询所有图片
     * @param request 请求
     * @param query 查询参数对象
     * @return 0-成功 1-失败
     */
    @ResponseBody
    @RequestMapping(value = "/api/planeImg/query",method = RequestMethod.GET)
    public Object query(HttpServletRequest request,QueryBase query){
        String message;
        if(query == null){
            query = new QueryBase();
        }
        planeImageService.query(query);
        message = "查询成功";
        logger.warn(query.getResults());

        return new Response(Status.SUCCESS,message,query.getResults(),query.getTotalRow());
    }

    /**
     * 上传飞机图片
     * @param request
     * @param multipartFile
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/upload", method = RequestMethod.POST)
    public Object upload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        String fileName = RenameUtil.rename(multipartFile);
        String dstPath1 = request.getSession().getServletContext().getRealPath("/") + "/images/"+ fileName;
//        String picAddr="/images/"+ fileName;//保存相对路径
        String dstPath = "F:/PFile/xuebastore/WebRoot/web/img/"+ fileName;
        String picAddr="/images/"+ fileName;//保存相对路径
        System.out.println("图片保存路径为： "+picAddr);
        System.out.println("图片保存路径distpath： " + dstPath1);
        File dstFile = new File(dstPath1);

        if (!dstFile.exists()) {
            dstFile.mkdirs();
        }
        try {
            multipartFile.transferTo(dstFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Response(Status.SUCCESS,"上传成功！",picAddr);
    }
}
