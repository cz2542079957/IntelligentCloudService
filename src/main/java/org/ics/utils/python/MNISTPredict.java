package org.ics.utils.python;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Description 传入图片识别手写数字
 **/
@Component
public class MNISTPredict extends BasePythonRunUtil
{

    /**
     * @Description 低精确度预测
     * @Params [imgPath]
     **/
    public String lowPredict(String imgPath) throws IOException
    {
        String[] command = new String[4];
        command[0] = condaPath;
        command[1] = configGetter.properties.getProperty("basePythonFilePath") + "mnistLowPredict.py";
        //权重文件
        command[2] = configGetter.properties.getProperty("baseModelFilePath") + "lowMNIST.pt";
        //图片文件
        command[3] = imgPath;
        Map<String, Object> res = runCommand(command);
        if (res.containsKey("data"))
        {
            return (String) res.get("data");
        }
        return null;
    }

    /**
     * @Description 高精确度预测
     * @Params [imgPath]
     **/
    public String highPredict(String imgPath) throws IOException
    {
        String[] command = new String[4];
        command[0] = condaPath;
        command[1] = configGetter.properties.getProperty("basePythonFilePath") + "mnistHighPredict.py";
        //权重文件
        command[2] = configGetter.properties.getProperty("baseModelFilePath") + "highMNIST.pt";
        //图片文件
        command[3] = imgPath;
        Map<String, Object> res = runCommand(command);
        if (res.containsKey("data"))
        {
            return (String) res.get("data");
        }
        return null;
    }

}

