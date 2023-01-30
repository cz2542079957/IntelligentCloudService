package org.ics.utils.python;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CIFARPredict extends BasePythonRunUtil
{
    /**
     * @Description 低精确度预测
     * @Params [imgPath]
     **/
    public String lowCIFAR10Predict(String imgPath) throws IOException
    {
        String[] command = new String[4];
        command[0] = configGetter.properties.getProperty("condaPath");
        command[1] = configGetter.properties.getProperty("basePythonFilePath") + "cifar10LowPredict.py";
        //权重文件
        command[2] = configGetter.properties.getProperty("baseModelFilePath") + "lowCIFAR10.pt";
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
    public String highCIFAR10Predict(String imgPath) throws IOException
    {
        String[] command = new String[4];
        command[0] = configGetter.properties.getProperty("condaPath");
        command[1] = configGetter.properties.getProperty("basePythonFilePath") + "cifar10HighPredict.py";
        //权重文件
        command[2] = configGetter.properties.getProperty("baseModelFilePath") + "highCIFAR10.pt";
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
     * @Description cifar100预测
     * @Params [imgPath]
     **/
    public String CIFAR100Predict(String imgPath) throws IOException
    {
        String[] command = new String[4];
        command[0] = configGetter.properties.getProperty("condaPath");
        command[1] = configGetter.properties.getProperty("basePythonFilePath") + "cifar100Predict.py";
        //权重文件
        command[2] = configGetter.properties.getProperty("baseModelFilePath") + "CIFAR100.pt";
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
