package com.example.blds.controller;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.dao.HzUserMapper;
import com.example.blds.entity.HzUser;
import com.example.blds.service.HzUserService;
import com.example.blds.util.AlipayUtil;
import com.example.blds.util.TokenUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Controller
public class UserController {
    @Autowired
    private HzUserMapper userMapper;
    @Autowired
    private HzUserService userService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private AlipayUtil alipayUtil;

    private static final String bordRoomExcel="C:\\Users\\YFZX-FZF-1777\\Desktop\\下载文件\\用户示例文件.xls";

    private static final String password=new SimpleHash("md5",
            "A123456", ByteSource.Util.bytes(""), 2).toHex();



    @RequestMapping("toAuthPage.html")
    public ModelAndView getUserInfoFromAL(HttpServletRequest request,ModelAndView modelAndView){
        Map<String,String[]> map=request.getParameterMap();
        String token=map.get("state")[0];
        modelAndView.setViewName("index.html");
        String str=tokenUtil.checkToken(token);
        if (str.equals("token无效")){
            return modelAndView;
        }

        String res=JSONObject.fromObject(alipayUtil.getAutoInfo(map.get("auth_code")[0])).optJSONObject("alipay_user_info_share_response").toString();
        HzUser user=userService.getUserInfoByUid(JSONObject.fromObject(str).optInt("userId"));
        user.setAlipayAccount(res);
        userMapper.updateAlipayAccount(user.getId(),res);
        tokenUtil.updateToken(token,user);
        return modelAndView;
    }
//    @PostMapping("getAll")
//    public Result<List<HzUser>> getUsers(@ApiParam(value = "第几页",example = "1")@RequestParam Integer pageNo,
//                                         @ApiParam(value = "用户token")@RequestParam String token){
//        String str=tokenUtil.checkToken(token);
//        JSONObject jsonObject=JSONObject.fromObject(str);
//        if (jsonObject.optString("isSuper").equals("0")){
//            return ResultGenerator.genFailResult("权限不足");
//        }
////        List<HzUser> users=userService.getUsers(5,pageNo);
//        return ResultGenerator.genSuccessResult(null);
//    }
//
//    @PostMapping("getUserCount")
//    public Integer getUserCount(@ApiParam(value = "用户token")@RequestParam String token){
//        String str=tokenUtil.checkToken(token);
//        JSONObject jsonObject=JSONObject.fromObject(str);
//        if (jsonObject.optString("isSuper").equals("0")){
//            return 0;
//        }
//        List<User> users=userService.getUsers(null,null);
//        return users.size();
//    }
//
//    @PostMapping("countUsersByName")
//    public Integer UsersCountByName(@ApiParam(value = "用户token")@RequestParam String token,
//                                    @ApiParam(value = "搜索关键字")@RequestParam String name){
//        String str=tokenUtil.checkToken(token);
//        JSONObject jsonObject=JSONObject.fromObject(str);
//        if (str.equals("token无效")){
//            return 0;
//        }
//        List<User> users=userService.getUsersByName(name);
//        return users.size();
//    }
//
//    @PostMapping("select")
//    public Result<List<User>> select(@ApiParam("name")@RequestParam String name,
//                                     @ApiParam(value = "页面size",example = "1")@RequestParam Integer pageSize,
//                                     @ApiParam(value = "第几页",example = "1")@RequestParam Integer pageNo,
//                                     @ApiParam(value = "用户token")@RequestParam String token){
//        String login_name=tokenUtil.checkToken(token);
//        if (login_name.equals("token无效")){
//            return ResultGenerator.genFailResult(login_name);
//        }
//        List<User> users=userService.getUsersByName(name,pageSize,pageNo);
//        return ResultGenerator.genSuccessResult(users);
//    }
//
//    @PostMapping("getDirectors")
//    public Result<List<User>> getDirectors(@ApiParam(value = "用户token")@RequestParam String token){
//        String login_name=tokenUtil.checkToken(token);
//        if (login_name.equals("token无效")){
//            return ResultGenerator.genFailResult(login_name);
//        }
//        List<User> users=userService.getDirectors();
//        return ResultGenerator.genSuccessResult(users);
//    }
//
//    @PostMapping("/userInfo")
//    public Result updateUserInfo(@ApiParam(value = "用户信息")@RequestBody UserInfo userInfo,
//                                 @ApiParam(value = "用户token",required = true)@RequestParam String token){
//        String login_name=tokenUtil.checkToken(token);
//        if (login_name.equals("token无效")){
//            return ResultGenerator.genFailResult(login_name);
//        }
//        JSONObject jsonObject=JSONObject.fromObject(login_name);
//        if (!(jsonObject.optString("isSuper").equals("1")||
//                Objects.equals(jsonObject.optInt("id"),userInfo.getId()))){
//            return ResultGenerator.genFailResult("权限不足。需要本人或者管理员操作。");
//        }
//        try {
//            User user=userService.getByUserId(userInfo.getId());
//            if (user==null){
//                return ResultGenerator.genFailResult("该账号已被删除，无法更新。请联系管理员。");
//            }
//            userService.updateUserInfo(userInfo);
//        }catch (Exception e){
//            LOG.info("更新用户操作失败！"+e.getMessage());
//            return ResultGenerator.genFailResult("更新出错");
//        }
//        return  ResultGenerator.genSuccessResult("更新成功");
//    }
    @PostMapping("/deleteUser")
    @ResponseBody
    public Result delete(@ApiParam(value = "用户Id",example = "1")@RequestParam Integer userId,
                         @ApiParam(value = "用户token",required = true)@RequestParam String token){
        String login_name=tokenUtil.checkToken(token);
        if (login_name.equals("token无效")){
            return ResultGenerator.genFailResult(login_name);
        }
        JSONObject jsonObject=JSONObject.fromObject(login_name);
        if (jsonObject.optString("isSuper").equals("0")){
            return ResultGenerator.genFailResult("权限不足");
        }
        if (userService.deleteByUserId(userId,jsonObject.optString("hospitalId"))){
            return ResultGenerator.genSuccessResult("成功删除用户");
        }
        return ResultGenerator.genSuccessResult("用户不存在");
    }
//
//    @GetMapping("getUserExcelFile")
//    public void getUserExcelFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//        String fileName="用户示例文件";
//        File file =new File(bordRoomExcel);
//        String userAgent = request.getHeader("User-Agent");
//        if (StringUtils.contains(userAgent, "MSIE")) {// IE浏览器
//            fileName = URLEncoder.encode(fileName, "UTF8");
//        } else if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
//            fileName = new String(fileName.getBytes(),
//                    "ISO8859-1");
//        } else {
//            fileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
//        }
//        response.setHeader("content-type", "application/octet-stream");
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment;filename=" +fileName+".xls");
//        try {
//            byte[] bytes= FileUtils.readFileToByteArray(file);
//            ServletOutputStream sout = response.getOutputStream();
//            sout.write(bytes);
//            sout.flush();
//        } catch (IOException e) {
//            LOG.info("========mady========");
//        }
//    }


    @PostMapping("/getMyInfo")
    @ResponseBody
    public Result getMyInfo(@ApiParam(value = "用户token",required = true)@RequestParam String token){
        String login_name=tokenUtil.checkToken(token);
        if (login_name.equals("token无效")){
            return ResultGenerator.genFailResult(login_name);
        }
        JSONObject jsonObject=JSONObject.fromObject(login_name);
        return ResultGenerator.genSuccessResult(jsonObject);
    }

    @PostMapping("/getExpertsInfo")
    @ResponseBody
    public Result getExpertsInfo(@ApiParam("name")@RequestParam(required = false) String name,
                                 @ApiParam(value = "页面size",example = "1")@RequestParam(required = false) Integer pageSize,
                                 @ApiParam(value = "第几页",example = "1")@RequestParam(required = false) Integer pageNo,
                                 @ApiParam(value = "类型",example = "1")@RequestParam(required = false) Integer caseTypeId){
        if (caseTypeId == null){
            List<HzUser> hzUsers = userService.getExpertsInfo(name,pageSize,pageNo);
            return ResultGenerator.genSuccessResult(hzUsers,((Page) hzUsers).getTotal());
        }else  {
            List<HzUser> hzUsers = userService.getExpertsInfoByName(name,pageSize,pageNo,caseTypeId);
            return ResultGenerator.genSuccessResult(hzUsers,((Page) hzUsers).getTotal());
        }

    }

    @PostMapping("/getExpertsInfoById")
    @ResponseBody
    public Result getExpertsInfoById(@ApiParam("医师Id")@RequestParam Integer doctorId){
        HzUser hzUser = userMapper.getExpertsInfoById(doctorId);
        return ResultGenerator.genSuccessResult(hzUser);
    }

    @PostMapping("/getExpertsInfoByName")
    @ResponseBody
    public Result getExpertsInfoByName(@ApiParam("name")@RequestParam(required = false) String name,
                                 @ApiParam(value = "页面size",example = "1")@RequestParam(required = false) Integer pageSize,
                                 @ApiParam(value = "第几页",example = "1")@RequestParam(required = false) Integer pageNo,
                                       @ApiParam(value = "类型",example = "1")@RequestParam(required = false) Integer caseTypeId){
        List<HzUser> hzUsers = userService.getExpertsInfoByName(name,pageSize,pageNo,caseTypeId);
        return ResultGenerator.genSuccessResult(hzUsers,((Page) hzUsers).getTotal());
    }




}