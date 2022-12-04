package org.ics.utils;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileUtil
{
    @Resource
    private ConfigGetter configGetter;
    @Resource
    Logger log;


    private long tempImageExpiredTime = 3600000 * 24 * 3;  //3天后删除

    /**
     * @Description 文件保存
     * 1:登录注册密码图像临时文件上传
     * @Params [file, pathMode, argvs]
     * @Return 0或者具体内容表示成功  -1失败
     **/
    public String saveFile(MultipartFile file, Integer pathMode, String... argvs)
    {
        tempImageGC();  //  清理
        switch (pathMode)
        {
            case 1:
                return uploadUserTempImage(file, argvs[0]);
            case 2:
                break;
        }
        return "-1";
    }

    /**
     * @Description 上传登录注册临时密码图像
     * @Params [file, username]
     * @Return
     **/
    private String uploadUserTempImage(MultipartFile file, String username)
    {
        String path = (String) configGetter.properties.get("tempImageDir"); //上传的图片存放位置
        File dir = getDir(path);
        if (null == dir)
            return "-1";
        String originName = file.getOriginalFilename();
        String[] originNameSplitArray = originName.split("\\.");
        String type = originNameSplitArray[originNameSplitArray.length - 1];
        String fileName = username + "_" + System.currentTimeMillis() + "." + type;

        // 将文件保存到指定路径的功能
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(path + fileName);
            out.write(file.getBytes());
            return path + fileName;
        } catch (IOException e)
        {
            e.printStackTrace();
            return "-1";
        } finally
        {
            try
            {
                out.flush();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                out.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    /**
     * @Description 垃圾回收器
     **/
    private void tempImageGC()
    {
        //清除登录注册密码图像临时文件
        String path = (String) configGetter.properties.get("tempImageDir"); //上传的图片存放位置
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory())
        {
            File[] files = dir.listFiles();
            try
            {
                long now = System.currentTimeMillis();
                for (File temp : files)
                {
                    if (temp.isFile())
                    {
                        String name = temp.getName();
                        String[] splitArray = name.split("\\.");
                        splitArray = splitArray[splitArray.length - 2].split("_");
                        long time = Long.parseLong(splitArray[splitArray.length - 1]);
                        if (now - time >= tempImageExpiredTime)
                            temp.delete();
                    }
                }
            } catch (RuntimeException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description 获取目录 不存在就创建
     * @Params [path]
     **/
    private File getDir(String path)
    {
        File dir = new File(path);
        if (!dir.isDirectory() || !dir.exists())
        {
            //不存在文件，创建
            if (!dir.mkdirs())
            {
                return null;
            }
        }
        return dir;
    }


}
