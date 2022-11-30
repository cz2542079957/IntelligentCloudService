package org.ics.utils.python;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BasePythonRunUtil
{
    public static Integer SUCCESS = 0;
    public static Integer ERROR = -1;

    public Map<String, Object> set(Map<String, Object> ret, Integer code)
    {
        ret.put("code", code);
        return ret;
    }

    public Map<String, Object> set(Map<String, Object> ret, Integer code, Object data)
    {
        ret.put("code", code);
        ret.put("data", data);
        return ret;
    }

    public Map<String, Object> set(Map<String, Object> ret, Integer code, Map<String, Object> datas)
    {
        ret.put("code", code);
        ret.putAll(datas);
        return ret;
    }

    /**
     * @Description 运行python
     * @Params [commands 指令]
     * @Return 返回python执行返回值
     **/
    Map<String, Object> runCommand(String[] command) throws IOException
    {
        Map<String, Object> ret = new HashMap<>();
        Process process = Runtime.getRuntime().exec(command);
        ArrayList<String> temp = handelPythonReturn(process.getInputStream());
        // 数据为空
        if (temp.size() == 0)
            return set(ret, ERROR);
        // 正常返回
        return set(ret, SUCCESS, temp.get(0));
    }

    /**
     * @Description 获取python执行的输出
     * @Params [input 输入流]
     * @Return 返回读取到的数据
     **/
    ArrayList<String> handelPythonReturn(final InputStream input){
        ArrayList<String> ret = new ArrayList<>();
        Reader reader = new InputStreamReader(input);
        BufferedReader bf = new BufferedReader(reader);
        String line = null;
        try
        {
            while ((line = bf.readLine()) != null)
            {
                ret.add(line);
            }
            reader.close();
            bf.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return ret;
    }

}
