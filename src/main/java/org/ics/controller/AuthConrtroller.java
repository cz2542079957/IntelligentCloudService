package org.ics.controller;

import org.ics.exception.DBException;
import org.ics.model.User;
import org.ics.utils.ReturnStates;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ics/auth")
public class AuthConrtroller extends BaseController
{

    /**
     * @Description 注册
     * @Params [username 用户名 , file 密码图像]
     **/
    @PostMapping("/signup")
    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> signup(@RequestParam("username") String username, @RequestParam("file") MultipartFile file, @RequestHeader Map<String, String> headers)
    {
        Map<String, Object> ret = new HashMap<>();
        try
        {
            if (username.length() < 3 || username.length() > 24)
            {
                return setReturnState(ret, ReturnStates.authUsernameInvalid);
            }
            if (authService.checkUserExist(username) != 0)
            {
                return setReturnState(ret, ReturnStates.authUserExist);
            }
            String path = (String) configGetter.properties.get("tempImageDir"); //上传的图片存放位置
            File dir = new File(path);
            if (!dir.isDirectory() || !dir.exists())
            {
                //不存在文件，创建
                if (!dir.mkdirs())
                {
                    return setReturnState(ret, ReturnStates.authDirCreateError);
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
            String password = parsePasswordImage.run(path + fileName);  //通过python解析密码图像
            User user = new User(username, password);
            if (authService.addUser(user) == 0)
            {
                // 添加失败
                throw new DBException("添加用户失败");
            }
            Integer id = user.getId();  // 获取id
            //返回的数据
            Map<String, Object> data = new HashMap<>();
            data.put("password", password);
            data.put("token", jwtService.createToken(username));
            data.put("id", id);
            //打包返回数据
            return setReturnState(ret, ReturnStates.success, data);
        } catch (IOException e)
        {
            e.printStackTrace();
            return setReturnState(ret, ReturnStates.authParsePasswordImageError);
        } catch (DBException e)
        {
            e.printStackTrace();
            return setReturnState(ret, ReturnStates.authAddUserError);
        }
    }

    /**
     * @Description 用户登录接口
     * @Params [username 用户名, file 密码图像]
     **/
    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestParam("username") String username, @RequestParam("file") MultipartFile file, @RequestHeader Map<String, String> headers)
    {
        Map<String, Object> ret = new HashMap<>();
        try
        {
            if (authService.checkUserExist(username) == 0)
            {   //用户不存在
                return setReturnState(ret, ReturnStates.authUserNotExist);
            }
            String path = (String) configGetter.properties.get("tempImageDir"); //上传的图片存放位置
            File dir = new File(path);
            if (!dir.isDirectory() || !dir.exists())
            {
                //不存在文件，创建
                if (!dir.mkdirs())
                {
                    return setReturnState(ret, ReturnStates.authDirCreateError);
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
            String password = parsePasswordImage.run(path + fileName);  //通过python解析密码图像

            User user = new User(username, password);
            if (0 == authService.checkUser(user))
            {
                //密码错误
                return setReturnState(ret, ReturnStates.authPasswordError);
            }
            String token = jwtService.createToken(username);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return setReturnState(ret, ReturnStates.success, data);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return setReturnState(ret, ReturnStates.authParsePasswordImageError);
        } catch (IOException e)
        {
            e.printStackTrace();
            return setReturnState(ret, ReturnStates.authIOError);
        }
    }

    @PostMapping("/signinByToken")
    public Map<String, Object> signinByToken(@RequestBody Map<String, Object> req, @RequestHeader Map<String, String> headers)
    {
        Map<String, Object> ret = new HashMap<>();
        if (!req.containsKey("username"))
        {
            // 参数错误 缺少username
            return setReturnState(ret, ReturnStates.authUsernameLost);
        }
        String username = (String) req.get("username");  // 获取username
        String token = headersChecker.getToken(headers);
        if (null == token)
        {
            return setReturnState(ret, ReturnStates.authTokenLost);
        }
        Integer res = jwtService.verifyToken(token, username);
        if (res == -1)
        {
            // token 验证不通过
            return setReturnState(ret, ReturnStates.authTokenError);
        }
        if (res == -2)
        {
            // token 过期
            return setReturnState(ret, ReturnStates.authTokenExpired);
        }
        return setReturnState(ret, ReturnStates.success);
    }

}
