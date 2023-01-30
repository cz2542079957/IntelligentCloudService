package org.ics.utils.python;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class PoetryBuilder extends BasePythonRunUtil
{
    public String buildPoetry(String startText, String prefixText) throws IOException
    {
        String[] command = new String[6];
        command[0] = configGetter.properties.getProperty("condaPath");
        command[1] = configGetter.properties.getProperty("basePythonFilePath") + "poetry.py";
        //权重文件
        command[2] = configGetter.properties.getProperty("baseModelFilePath") + "poetry.pth";
        command[3] = configGetter.properties.getProperty("baseModelFilePath") + "poetry.npz";
        //藏头
        command[4] = startText;
        //意境
        command[5] = prefixText;
        Map<String, Object> res = runCommand(command);
        if (res.containsKey("data"))
        {
            return (String) res.get("data");
        }
        return null;
    }

}
