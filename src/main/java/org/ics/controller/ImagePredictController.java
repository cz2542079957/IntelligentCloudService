package org.ics.controller;

import org.ics.utils.ReturnStates;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ics/predict")
public class ImagePredictController extends BaseController
{
    @PostMapping("/lowMNIST")
    public Map<String, Object> lowMNISTPredict(@RequestHeader Map<String, String> headers, @RequestParam("username") String username, @RequestParam("file") MultipartFile file)
    {
        Map<String, Object> ret = new HashMap<>();
        //检查用户
        if (!headersChecker.needValidToken(headers, username))
        {
            return setReturnState(ret, ReturnStates.tokenError);
        }
        //文件上传
        String res = fileUtil.saveFile(file, 1, username);
        if (res.equals("-1"))
        {
            //失败
            return setReturnState(ret, ReturnStates.authDirCreateError);
        }
        //文件上传成功，res值为文件路径  
        try
        {
            res = mnistPredict.lowPredict(res);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/highMNIST")
    public Map<String, Object> highMNISTPredict(@RequestHeader Map<String, String> headers, @RequestParam("username") String username, @RequestParam("file") MultipartFile file)
    {
        Map<String, Object> ret = new HashMap<>();
        //检查用户
        if (!headersChecker.needValidToken(headers, username))
        {
            return setReturnState(ret, ReturnStates.tokenError);
        }
        //文件上传
        String res = fileUtil.saveFile(file, 1, username);
        if (res.equals("-1"))
        {
            //失败
            return setReturnState(ret, ReturnStates.authDirCreateError);
        }
        //文件上传成功，res值为文件路径
        try
        {
            res = mnistPredict.highPredict(res);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
