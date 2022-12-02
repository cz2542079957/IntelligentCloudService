package org.ics.utils.python;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.*;
import java.util.Map;

@Component
public class CheckPassword extends BasePythonRunUtil
{

    /**
     * @Description 利用卷积神经网络解析图像获取80位数字秘钥
     * @Params []
     * @Return 获取80位数字秘钥
     **/
    public String run(String imgPath) throws IOException
    {
        String[] command = new String[3];
        command[0] = condaPath;
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
