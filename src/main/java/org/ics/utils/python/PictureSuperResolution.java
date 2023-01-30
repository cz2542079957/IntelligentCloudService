package org.ics.utils.python;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class PictureSuperResolution extends BasePythonRunUtil
{
    public String superResolution(String imgPath, String outputName) throws IOException
    {
        String[] command = new String[5];
        command[0] = configGetter.properties.getProperty("condaPath");
        //python文件
        command[1] = configGetter.properties.getProperty("basePythonFilePath") + "superResolution.py";
        //权重文件
        command[2] = configGetter.properties.getProperty("baseModelFilePath") + "superResolution.pth";
        //输入图片
        command[3] = imgPath;
        //输出文件
        command[4] = configGetter.properties.getProperty("outputFileDir") + outputName;

        Map<String, Object> res = runCommand(command);
        if (res.containsKey("data"))
        {
            res.put("data", outputName);
            return (String) res.get("data");
        }
        return null;
    }
}
