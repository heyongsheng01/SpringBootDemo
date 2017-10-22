package com.example.demo.controller;

import com.example.demo.utils.Base64Util;
import com.example.demo.utils.FastDFSUtil;
import com.example.demo.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yongsheng.he
 * @describe 文件上传和下载
 * @date 2017/10/18 17:16
 */
@Controller
public class UploadAndDownloadContorller {

    @RequestMapping("/toUpload")
    public String toUpload() {
        return "upload";
    }

    @RequestMapping("/doUpload")
    public String doUpload(HttpServletRequest request, ModelMap modelMap) {

        StringBuffer pictureUrl = new StringBuffer(); // 图片地址
        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());

            // 判断是否有文件上传
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
                Map<String, MultipartFile> fileMap = mRequest.getFileMap();
                System.out.println("图片集合的长度：" + fileMap.size());
                Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
                int i = 0;
                while (it.hasNext()) {
                    Map.Entry<String, MultipartFile> entry = it.next();
                    MultipartFile mFile = entry.getValue();
                    System.out.println("第" + (i + 1) + "次进入上传图片方法");

                    if (!mFile.isEmpty()) {
                        if (i != 0) {
                            pictureUrl.append(",");
                        }
                        // 返回文件保存路径
                        String path = FastDFSUtil.upload(mFile);
                        if (!StringUtil.isEmpty(path)) {
                            pictureUrl.append(path);
                        } else {
                            System.out.println("图片上传失败");
                        }
                    }
                    i++;
                }
            } else {
                System.out.println("没有文件上传");
            }
        } catch (Exception e) {
            System.out.println("register Exception:[" + e.getMessage() + "]");
        }
        System.out.println("下载地址：" + FastDFSUtil.DOWNLOAD_PATH);
        modelMap.put("pictureUrl", FastDFSUtil.DOWNLOAD_PATH + pictureUrl);
        return "download";
    }

    @RequestMapping("/doUpload1")
    public String doUpload1(HttpServletRequest request) {
        String imgOne = request.getParameter("imgOne");
        try {

        }catch (Exception e) {
            System.out.println("register Exception:[" + e.getMessage() + "]");
        }
        Base64Util.generateImage(imgOne);
        return "upload";
    }
}
