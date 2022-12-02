package org.ics.controller;

import org.ics.service.AuthService;
import org.ics.utils.ConfigGetter;
import org.ics.utils.ReturnStates;
import org.ics.utils.python.CheckPassword;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ics/auth")
public class AuthConrtroller extends BaseController
{


    /**
     * @Description 用户登录接口
     * @Params [params]
     **/
    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody Map<String, Object> params)
    {
        Map<String, Object> ret = new HashMap<>();

        System.out.println(params);

        return ret;
    }


    @PostMapping("/signup")
    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> signup(@RequestParam("username") String username, @RequestParam("file") MultipartFile file)
    {
        Map<String, Object> ret = new HashMap<>();
        if (null == file)
        {
            //图片上传出错
            return setReturnState(ret, ReturnStates.signupFileError);
        }
        try
        {
            String path = (String) configGetter.properties.get("tempImageDir"); //上传的图片存放位置

            File dir = new File(path);
            if (!dir.isDirectory() || !dir.exists())
            {
                //不存在文件，创建
                if (!dir.mkdirs())
                {
                    return setReturnState(ret, ReturnStates.signupDirCreateError);
                }
            }
            String originName = file.getOriginalFilename();
            String[] originNameSplitArray = originName.split("\\.");
            String type = originNameSplitArray[originNameSplitArray.length - 1];
            String fileName = username + "_" + System.currentTimeMillis() + "." + type;

            // 将文件保存到指定路径的功能
            FileOutputStream out = new FileOutputStream(path + fileName);
            out.write(file.getBytes());
            out.flush();
            out.close();
            String password = checkPassword.run(path + fileName);
            authService.addUser(username, password);

            return setReturnState(ret, ReturnStates.success, password);
        } catch (Exception e)
        {
            e.printStackTrace();
            return setReturnState(ret, ReturnStates.signupParsePasswordImageError);
        }
    }
}
