package org.ics.utils.python;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;


/**
 * @Description 通过python解析图片生成80位数字密码
 **/
@Component

public class ParsePasswordImage extends BasePythonRunUtil
{

    /**
     * @Description 利用卷积神经网络解析图像获取80位数字秘钥
     * @Params []
     * @Return 获取80位数字秘钥
     **/
    public String run(String imgPath) throws IOException
    {
        String[] command = new String[3];
        command[0] =configGetter.properties.getProperty("condaPath");
        command[1] = configGetter.properties.getProperty("basePythonFilePath") + "parsePasswordImage.py";
        command[2] = imgPath;
        Map<String, Object> res = runCommand(command);
        if (res.containsKey("data"))
        {
            return (String) res.get("data");
        }
        return null;
    }
}
