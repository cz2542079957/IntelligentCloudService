package org.ics.controller;

import org.ics.utils.ReturnStates;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ics/predict")
public class PredictController extends BaseController
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

    @PostMapping("/lowCIFAR10")
    public Map<String, Object> lowCIFAR10Predict(@RequestHeader Map<String, String> headers, @RequestParam("username") String username, @RequestParam("file") MultipartFile file)
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
            res = cifarPredict.lowCIFAR10Predict(res);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/highCIFAR10")
    public Map<String, Object> highCIFAR10Predict(@RequestHeader Map<String, String> headers, @RequestParam("username") String username, @RequestParam("file") MultipartFile file)
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
            res = cifarPredict.highCIFAR10Predict(res);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/CIFAR100")
    public Map<String, Object> CIFAR100Predict(@RequestHeader Map<String, String> headers, @RequestParam("username") String username, @RequestParam("file") MultipartFile file)
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
            res = cifarPredict.CIFAR100Predict(res);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/poetry")
    public Map<String, Object> buildPoetry(@RequestHeader Map<String, String> headers, @RequestBody Map<String, String> req)
    {
        Map<String, Object> ret = new HashMap<>();
        if (!req.containsKey("username") || !req.containsKey("startText") || !req.containsKey("prefixText"))
        {
            return setReturnState(ret, ReturnStates.predictParamsLost);
        }

        String username = req.get("username");
        String startText = req.get("startText");
        String prefixText = req.get("prefixText");
        //检查用户
        if (!headersChecker.needValidToken(headers, username))
        {
            return setReturnState(ret, ReturnStates.tokenError);
        }
        try
        {
            String res = poetryBuilder.buildPoetry(startText, prefixText);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/insideDefogging")
    public Map<String, Object> insideDefogging(@RequestHeader Map<String, String> headers, @RequestParam("username") String username, @RequestParam("file") MultipartFile file)
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
            String outputName = username + "_" + System.currentTimeMillis() + ".png";
            res = pictureDefogging.insideDefogging(res, outputName);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/outsideDefogging")
    public Map<String, Object> outsideDefogging(@RequestHeader Map<String, String> headers, @RequestParam("username") String username, @RequestParam("file") MultipartFile file)
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
            String outputName = username + "_" + System.currentTimeMillis() + ".png";
            res = pictureDefogging.outsideDefogging(res, outputName);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/superResolution")
    public Map<String, Object> superResolution(@RequestHeader Map<String, String> headers, @RequestParam("username") String username, @RequestParam("file") MultipartFile file)
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
            String outputName = username + "_" + System.currentTimeMillis() + ".png";
            res = pictureSuperResolution.superResolution(res, outputName);
            return setReturnState(ret, ReturnStates.success, res);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
