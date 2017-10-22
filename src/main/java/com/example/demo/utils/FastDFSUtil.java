package com.example.demo.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * FastDFS工具类
 *
 * @author gaojing
 * @date 2017-5-24
 */
public class FastDFSUtil {

    private final static Logger log = LoggerFactory.getLogger(FastDFSUtil.class);

    private static String CONFIG_NAME = "fileUpload.properties";//配置文件名

    public static String GROUP_NAME; //组名
    public static String ALIAS; //前缀
    public static String DOWNLOAD_PATH;//下载地址

    static {
        Properties props = new Properties();
        try {
            props = PropertiesLoaderUtils.loadAllProperties(CONFIG_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ALIAS = props.getProperty("alias");
        GROUP_NAME = props.getProperty("group_name");
        DOWNLOAD_PATH = props.getProperty("download_path");
    }


    /**
     * MultipartFile 文件上传
     *
     * @param file MultipartFile 文件
     * @return 返回文件的服务器路径(该路径直接复制, 可用来下载)
     * @throws Exception
     */
    public static String upload(MultipartFile file) throws Exception {
        return upload(file.getBytes(), file.getOriginalFilename());
    }

    /**
     * 文件上传
     *
     * @param  file:上传文件
     * @param fileName:上传文件的文件名,例"test.jpg"
     * @return 返回文件的服务器路径(该路径直接复制, 可用来下载)
     * @throws Exception
     */
    public static String upload(File file, String fileName) throws Exception {
        InputStream inStream = new FileInputStream(file);
        byte[] fileByte = getFileByte(inStream);
        return upload(fileByte, fileName);
    }

    /**
     * 文件上传
     *
     * @param fullPathfileName:上传文件的全路径,例"d:\\test.jpg"
     * @param fileName:上传文件的文件名,例"test.jpg"
     * @return 返回文件的服务器路径(该路径直接复制, 可用来下载)
     */
    public static String upload(String fullPathfileName, String fileName) throws IOException, MyException {

        InputStream inStream = new FileInputStream(new File(fullPathfileName));
        byte[] fileByte = getFileByte(inStream);
        return upload(fileByte, fileName);

    }

    /**
     * 文件下载
     *
     * @param fileName:文件在服务器的位置: 例"group1/M00/00/00/rBEiZlNcwJ6x0hyuAABTb_F6UwE363.jpg"
     * @return 返回用于流输出的byte数组;该byte数组可以直接放到流中输出
     */
    public static byte[] download(String fileName) throws Exception {
        if (fileName != null && fileName.length() > 7) {
            fileName = fileName.substring(7);
        } else {
            log.debug("error message >>>>>>>>>>fileName is null or fileName <= 7");
            return null;
        }
        try {
            //获取项目路径下的配置文件的路径
            String configPath = FastDFSUtil.class.getClassLoader().getResource("fileUpload.properties").getPath();

        /*初始化ClientGlobal的配置属性，因为ClientGlobal存放着所有的配置信息，所以这个方法必须要执行，
         如果不执行，在允许的过程中会报空指针异常。*/
            // 建立连接
            ClientGlobal.init(configPath);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;

            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            byte[] b = storageClient.download_file(GROUP_NAME, fileName);//下载文件
            return b;
        } catch (Exception e) {
            log.error("下载文件异常", e);
            throw e;
        }
    }

    /**
     * 删除文件
     *
     * @param fileName:文件在服务器的位置: 例"group1/M00/00/00/rBEiZlNcwJ6x0hyuAABTb_F6UwE363.jpg"
     * @throws Exception
     */
    public static int deleteFile(String fileName) throws Exception {

        //获取项目路径下的配置文件的路径
        String configPath = FastDFSUtil.class.getClassLoader().getResource("fileUpload.properties").getPath();

        /*初始化ClientGlobal的配置属性，因为ClientGlobal存放着所有的配置信息，所以这个方法必须要执行，
         如果不执行，在允许的过程中会报空指针异常。*/
        // 建立连接
        ClientGlobal.init(configPath);
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);

        int i = storageClient.delete_file(GROUP_NAME, fileName);//删除文件
        log.info(i == 0 ? "删除成功" : "删除失败:" + i + "条");
        return i;
    }

    /**
     * 获取文件信息
     * @param fileName:文件在服务器的位置: 例"group1/M00/00/00/rBEiZlNcwJ6x0hyuAABTb_F6UwE363.jpg"
     * @return
     */
    public static FileInfo getFile(String fileName) {

        try {
            //获取项目路径下的配置文件的路径
            String configPath = FastDFSUtil.class.getClassLoader().getResource("fileUpload.properties").getPath();

        /*初始化ClientGlobal的配置属性，因为ClientGlobal存放着所有的配置信息，所以这个方法必须要执行，
         如果不执行，在允许的过程中会报空指针异常。*/
            // 建立连接
            ClientGlobal.init(configPath);
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            return storageClient.get_file_info(GROUP_NAME, fileName);//获取文件信息
        } catch (IOException e) {
            log.error("IO Exception: 得到快速DFS文件失败", e);
        } catch (Exception e) {
            log.error("非 IO Exception: 得到快速DFS文件失败 ", e);
        }
        return null;
    }

    /**
     * 文件上传
     *
     * @param fileByte Byte数组
     * @param fileName 上传文件的文件名，例"test.jpg"
     * @return
     * @throws IOException
     * @throws MyException
     */
    public static String upload(byte[] fileByte, String fileName) throws IOException, MyException {
        String filePath = ""; //返回文件路径
        String fileExtName = "";
        if (fileName.contains(".")) {
            fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            log.info("不能上传文件，因为文件名的格式是非法的。");
            return "";
        }
        //获取项目路径下的配置文件的路径
        String configPath = FastDFSUtil.class.getClassLoader().getResource("fileUpload.properties").getPath();

        /*初始化ClientGlobal的配置属性，因为ClientGlobal存放着所有的配置信息，所以这个方法必须要执行，
         如果不执行，在允许的过程中会报空指针异常。*/
        // 建立连接
        ClientGlobal.init(configPath);
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageClient1 client = new StorageClient1(trackerServer, null);

        // 设置元信息
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("fileName", fileName);
        metaList[1] = new NameValuePair("fileExtName", fileExtName);
        metaList[2] = new NameValuePair("fileLength", String.valueOf(fileByte.length));

        try {
            filePath = client.upload_file1(GROUP_NAME, fileByte, fileExtName, metaList);// 上传文件
        } catch (IOException e) {
            log.error("IO Exception uploadind 文件时异常: " + fileName, e);
            return "";
        } catch (Exception e) {
            log.error("非IO Exception uploadind 文件时异常: " + fileName, e);
            return "";
        }
        trackerServer.close();
        if (!StringUtil.isEmpty(filePath)) {
            if (ALIAS != null && ALIAS.equals("")) {
                log.info("------" + ALIAS + "------");
                filePath= ALIAS + filePath;
            }
            log.info("文件上传成功");
            log.info("------文件路径:" + filePath + "------");
            return filePath;
        }
        return filePath;
    }

    public static byte[] getFileByte(InputStream is) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = is.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}

