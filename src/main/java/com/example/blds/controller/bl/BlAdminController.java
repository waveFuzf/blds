package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.dao.HzHospitalMapper;
import com.example.blds.dao.HzSlideMapper;
import com.example.blds.dao.HzSupplementReportMapper;
import com.example.blds.dao.HzUserMapper;
import com.example.blds.entity.*;
import com.example.blds.service.*;
import com.example.blds.util.ChineseCharacterUtil;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import com.example.blds.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = "【管理员】")
@RequestMapping("/bladmin")
@Controller
public class BlAdminController {

    @Autowired
    private HzConsultService consultService;

    @Autowired
    private HzDiagnoseService diagnoseService;

    @Autowired
    private HzSlidesService slidesService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private HzSupplementReportMapper supplementReportMapper;
    @Autowired
    private HzHospitalService hzHospitalService;

    @Autowired
    private HzSlideMapper slideMapper;

    @Autowired
    private HzHospitalMapper hzHospitalMapper;


    @Autowired
    private HzUserService hzUserService;

    @Autowired
    private HzUserMapper hzUserMapper;


    @ApiOperation(value = "回退")
    @ResponseBody
    @PostMapping("backToUnDiagnose.htm")
    public Result backToUnDiagnose(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consultId,
            @ApiParam(name="type", value="type")@RequestParam(value="type") boolean type,
            HttpServletRequest request
    ){
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        int i;
        if(consultService.isConsultStatus(Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY),10)){
            i = consultService.updateCaseStatus(Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY),type?5:4);
        } else {
            return ResultGenerator.genFailResult("该状态不能改变");
        }
        return ResultGenerator.genSuccessResult(i==1?"该状态已改变":"该状态改变失败");
    }

    @ApiOperation(value = "结算")
    @ResponseBody
    @PostMapping("isSettlement.htm")
    public Result isSettlement(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consultId,
            @ApiParam(name="type", value="type")@RequestParam(value="type") boolean type,
            HttpServletRequest request
    ) throws Exception {
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        String consid=Crypt.desDecrypt(consultId,Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        if (!consultService.isConsultStatus(Integer.valueOf(consid),6)){
            return ResultGenerator.genFailResult("该状态不能改变");
        }
        HzConsult hzConsult=new HzConsult();
        hzConsult.setId(Integer.valueOf(consid));
        hzConsult.setIsSettlement(type?1:0);
        int i=consultService.updateByConsult(hzConsult);
        return ResultGenerator.genSuccessResult(i==1?"该状态已改变":"该状态改变失败");
    }

    @UserTokenAop
    @ApiOperation(value = "取消、恢复会诊")
    @ResponseBody
    @PostMapping("cancelToggle.htm")
    public Result cancelToggle(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consult_id,
            @ApiParam(name="isCancel", value="是否取消会诊，1取消，0恢复")@RequestParam(value="isCancel") Integer isCancel,
            HttpServletRequest request
    ){
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        Integer i = consultService.cancelToggle(Crypt.desDecryptByInteger(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY),isCancel);
        return ResultGenerator.genSuccessResult(i);
    }




    @ApiOperation(value = "管理员上传切片")
    @ResponseBody
    @PostMapping("confirmRecevie.htm")
    public Result confirmRecevie(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consult_id,
            @ApiParam(name="type", value="类型")@RequestParam(value="type") Integer type,
            @ApiParam(name="uploadList", value="上传切片")@RequestParam(value="uploadList") String uploadList,
            HttpServletRequest request

    ) throws Exception {
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        Integer consid=Integer.valueOf(Crypt.desDecrypt(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY));
        slidesService.save(JSONArray.toList(JSONArray.fromObject(uploadList),UploadSlides.class),consid , type);
        consultService.updateCaseStatus(consid,type==0?4:9);
        HzDiagnose hzDiagnose=diagnoseService.getDiagnoseByConsultId(consid);
        HzSupplementReport hzSupplementReport=new HzSupplementReport();
        hzSupplementReport.setConsultId(consid);
        hzSupplementReport.setCommitTime(new Date());
        hzSupplementReport.setIsCandle(hzDiagnose.getIsCandle());
        hzSupplementReport.setMaterialNum(hzDiagnose.getMaterialNum());
        hzSupplementReport.setIsDelete(0);
        supplementReportMapper.insert(hzSupplementReport);
        return ResultGenerator.genSuccessResult("good job!");
    }



    @ApiOperation(value = "管理员查找切片")
    @ResponseBody
    @PostMapping("getSlidesByConsultId.htm")
    public Result getSlidesByConsultId(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consult_id,
            HttpServletRequest request

    ) throws Exception {
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        Example example=new Example(HzSlide.class);
        example.createCriteria().andEqualTo("consultId",
                Crypt.desDecrypt(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY)).andEqualTo("isDelete",0);
        List<HzSlide> slides=slideMapper.selectByExample(example);
        return ResultGenerator.genSuccessResult(slides);
    }

    @ApiOperation(value = "管理员新增医院")
    @ResponseBody
    @PostMapping("saveHospital.htm")
    public Result saveHospital(
            @RequestBody  HzHospital hzHospital,
            HttpServletRequest request

    ){
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        hzHospital.setIsDelete(0);
        if (hzHospitalMapper.select(hzHospital).size()>0){
            return ResultGenerator.genFailResult("重复数据.");
        }
        hzHospital.setCreateTime(new Date());
        if (hzHospitalMapper.insert(hzHospital)==0){
            return ResultGenerator.genFailResult("插入失败");
        }else {
            hzUserService.createAdmin(hzHospital);
        }
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "管理员更新医院")
    @ResponseBody
    @PostMapping("updateHospital.htm")
    public Result updateHospital(
            @RequestBody  HzHospital hzHospital,
            HttpServletRequest request

    ){
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        return ResultGenerator.genSuccessResult(hzHospitalMapper.updateByPrimaryKeySelective(hzHospital));
    }


    @ApiOperation(value = "管理员查询医院")
    @ResponseBody
    @PostMapping("getHospitals.htm")
    public Result getHospitals(
            @RequestParam(required = false) String hospitalName,
            HttpServletRequest request

    ){
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        List<HzHospital> hzHospitals=hzHospitalService.getHospitalList(hospitalName);
        return ResultGenerator.genSuccessResult(hzHospitals);
    }

    @ApiOperation(value = "管理员删除医院")
    @ResponseBody
    @PostMapping("deleteHospital.htm")
    public Result deleteHospital(
            @RequestParam Integer hospitalId,
            HttpServletRequest request

    ){
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        HzHospital hzHospital=hzHospitalService.getByHospitalId(hospitalId);
        if (hzHospital==null){
            return ResultGenerator.genFailResult("医院不存在!");
        }
        hzHospital.setIsDelete(1);
        hzHospitalMapper.updateByPrimaryKeySelective(hzHospital);
        return ResultGenerator.genSuccessResult("删除成功!");
    }

    @PostMapping(value = "/test.htm")
    public Result importUsersList(){
        List<HzHospital> hzHospitals=hzHospitalService.getHospitalList("");
        hzHospitals.forEach((e)->{
            if (e.getAdmin()==null){
                Integer id=loginInfoService.createAdmin(e);
                hzUserService.createAdmin(e,id);
            }
        });
        return null;
    }
    @ResponseBody
    @PostMapping(value = "/importUserList.htm")
    public Result importUsersList(
            @ApiParam(value = "file detail") @RequestPart("file") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response){
        try {
            String login_name=tokenUtil.checkToken(request.getCookies()[1].getValue());
            if (login_name.equals("token无效")){
                return ResultGenerator.genFailResult(login_name);
            }
            JSONObject jsonObject=JSONObject.fromObject(login_name);
            if (jsonObject.optString("isSuper").equals("0")){
                return ResultGenerator.genFailResult("权限不足");
            }
            if (file != null) {
                String fileName = file.getOriginalFilename();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());
                String sufix = fileName.substring(fileName.lastIndexOf(".") + 1);
                Workbook workbook = null;
                if (sufix.equals("xlsx")) {
                    workbook = new XSSFWorkbook(bufferedInputStream);
                } else if (sufix.equals("xls")) {
                    workbook = new HSSFWorkbook(bufferedInputStream);
                }
                if (workbook != null) {
                    List errorLists=getUsersFromFile(workbook,jsonObject);
                    bufferedInputStream.close();
                    if (errorLists.size()>0)
                        return ResultGenerator.genFailResult(errorLists);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult("导入成功！");

    }

        private List getUsersFromFile(Workbook workbook,JSONObject obj) {
        List errorLists=new ArrayList();
        Sheet sheet = workbook.getSheetAt(0);
        DecimalFormat decimalFormat=new DecimalFormat("#");
        String str=ChineseCharacterUtil.convertHanzi2Pinyin(obj.optString("hospitalName"),false);
        Row row;
        HzLoginInfo hzLoginInfo=new HzLoginInfo();
        hzLoginInfo.setIsDelete(0);
        hzLoginInfo.setCreateTime(new Date());
        HzUser hzUser=new HzUser();
        hzUser.setIsDelete(0);
        hzUser.setHospitalName(obj.optString("hospitalName"));
        hzUser.setHospitalId(obj.optLong("hospitalId"));
        hzUser.setUserState(0);
        hzUser.setIsSuper(0);
        hzUser.setCreateTime(new Date());
        for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
            row = sheet.getRow(j);
            String loginName=str.concat(decimalFormat.format(sheet.getRow(j).getCell(5).getNumericCellValue()));
            if (loginInfoService.getByUsername(loginName)!=null){
                errorLists.add(row.getCell(0).getStringCellValue());
                continue;
            }
            hzLoginInfo.setLoginName(loginName);
            hzLoginInfo.setPassword(new SimpleHash("md5", "123456", ByteSource.Util.bytes(""),
                    2).toHex());
            Integer id=loginInfoService.save(hzLoginInfo);
            hzUser.setUserId(Long.valueOf(id));
            hzUser.setUserState(0);
            hzUser.setName(row.getCell(0).getStringCellValue());
            hzUser.setPhone(decimalFormat.format(row.getCell(1).getNumericCellValue()));
            hzUser.setPosition(row.getCell(2).getStringCellValue());
            hzUser.setSex(row.getCell(3).getStringCellValue());
            hzUser.setDepartment(row.getCell(4).getStringCellValue());
            hzUserMapper.insert(hzUser);
            hzUser.setId(null);
            hzLoginInfo.setUid(null);
        }
        return errorLists;
    }



}
