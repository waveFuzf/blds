package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.entity.HzAddress;
import com.example.blds.entity.HzSlide;
import com.example.blds.entity.HzSupplementReport;
import com.example.blds.service.*;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * 病理申请医生（下级医生）或者运营中心操作病历控制层
 */
@Api(tags = "病理申请医生（下级医生）或者运营人员操作病历")
@RequestMapping("/bldoctor")
@ResponseBody
@Controller
public class BlDoctorController {

    @Autowired
    private HzSlidesService slidesService;
    @Autowired
    private HzAddressService hzAddressService;
    @Autowired
    private HzConsultService consultService;
    @Autowired
    private HzConsultDoctorService hzConsultDoctorService;
    @Autowired
    private HzSupplementReportService supplementReportService;
    @Autowired
    private HzConsultAddressService consultAddressService;


    /**
     *
     * 管理员提交补充切片
     */
    @ApiOperation(value = "管理员补充切片上传")
    @PostMapping("/editSlide.htm")
    @UserTokenAop
    public Result editSlide(
            @ApiParam(name = "consult_id", value = "加密consultId") @RequestParam(value = "consult_id") String consult_id,
            @ApiParam(name = "slideList", example = "{fileType:文件类型。jpg、excel、word、pdf、ppt、txt、rar,path:文件路径,url:文件下载地址,size:文件大小}", value = "病理切片列表，格式如下：{fileType:文件类型。jpg、excel、word、pdf、ppt、txt、rar,path:文件路径,url:文件下载地址,size:文件大小}") @RequestParam(name = "slideList") String slideList
    ){
        Integer consultid=Crypt.desDecryptByInteger(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        slidesService.deleteSlidesByConsultId(consultid);
        JSONArray array = JSONArray.fromObject(slideList);
//        LoginUser loginUser= (LoginUser) request.getSession().getAttribute("loginUser");
        for (int i = 0; i < array.size(); i++) {
            JSONObject slideJSON = array.optJSONObject(i);
            HzSlide slide = new HzSlide();
            slide.setConsultId(consultid);
            slide.setHospitalId(10086L);
            int processStatus = slideJSON.optInt("processStatus", -1);
            slide.setProcessStatus(processStatus == -1 ? 0 : processStatus);
            //将type设为补充检查——type为1
            slide.setType(1);
            slide.setSlideName(slideJSON.optString("slideName"));
            slide.setUuid(UUID.randomUUID().toString());
            slide.setCreateTime(new Date());
            slide.setClientSlidePath(slideJSON.optString("clientSlidePath"));
            slide.setCosSlidePath(slideJSON.optString("cosSlidePath"));
            slide.setSlideSize(slideJSON.optString("slideSize"));
            slide.setFactoryUuid(slideJSON.optString("factoryUuid"));
            if (slidesService.insertBySlide(slide)== 0) {
                return ResultGenerator.genFailResult("上传切片失败！");
            }
        }
        return null;
    }

    @ApiOperation(value = "申请医生编辑地址")
    @PostMapping("/editAddress.htm")
    @UserTokenAop
    public Result editAddress(
            @ApiParam(name = "address_id", value = "加密addressId") @RequestParam(value = "address_id", required = false) String address_id,
            @ApiParam(name = "province", value = "省份") @RequestParam(value = "province") String province,
            @ApiParam(name = "city", value = "城市") @RequestParam(value = "city") String city,
            @ApiParam(name = "address", value = "地址") @RequestParam(value = "address") String address,
            @ApiParam(name = "name", value = "名字") @RequestParam(value = "name") String name,
            @ApiParam(name = "phone", value = "手机号码") @RequestParam(value = "phone") String phone,
            @ApiParam(name = "isDefault", value = "是否是默认地址") @RequestParam(value = "isDefault") boolean isDefault,
            @ApiParam(name = "userId", value = "用户id") @RequestParam(value = "userId") Long userId
    ) throws Exception {
        HzAddress hzAddress=new HzAddress();
        if (address_id==null){
            hzAddress.setId(Crypt.desDecryptByInteger(address_id, Enumeration.SECRET_KEY.ADDRESS_ID_KEY));
        }
        hzAddress.setProvince(province);
        hzAddress.setCity(city);
        hzAddress.setCity(address);
        hzAddress.setName(name);
        hzAddress.setPhone(phone);
        hzAddress.setUserId(userId);
        hzAddressService.editAddress(hzAddress,isDefault);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "申请医生删除地址")
    @PostMapping("/delAddress.htm")
    @UserTokenAop
    public Result delAddress(
            @ApiParam(name = "address_id", value = "加密addressId") @RequestParam(value = "address_id", required = false) String address_id
    ) throws Exception {
       return hzAddressService.deleteByAddressId(Crypt.desDecryptByInteger(address_id, Enumeration.SECRET_KEY.ADDRESS_ID_KEY))==1
               ?ResultGenerator.genSuccessResult("success"):ResultGenerator.genFailResult("fail");
    }

    @ApiOperation(value = "取消冰冻预约")
    @PostMapping("/editOrder.htm")
    @UserTokenAop
    public Result cancelOrder(
            @ApiParam(name = "consult_id", value = "加密consultId") @RequestParam(value = "consult_id") String consult_id,
            @ApiParam(name = "type",value = "取消或者同意冰冻预约") @RequestParam(value = "type")Integer type,
            HttpServletRequest request
    ) throws Exception {
        return consultService.updateCaseStatus(Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY),type)
                ==1?ResultGenerator.genSuccessResult("success"):ResultGenerator.genFailResult("fail");

    }

    @ApiOperation(value = "拒绝冰冻预约")
    @PostMapping("/refuseOrder.htm")
    @UserTokenAop
    public Result refuseOrder(
            @ApiParam(name = "consult_id", value = "加密consultId") @RequestParam(value = "consult_id", required = false) String consult_id,
            @ApiParam(name = "reason_cancel", value = "拒绝原因") @RequestParam(value = "reason_cancel", required = false) String reason_cancel,
            HttpServletRequest request
    ) throws Exception {
        if (StringUtils.isBlank(reason_cancel)) {
             reason_cancel = "预约时间与我安排不相符";
        }
        return consultService.updateConsult(Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY),11,reason_cancel)
                ==1?ResultGenerator.genSuccessResult("success"):ResultGenerator.genFailResult("fail");

    }

    @ApiOperation(value = "冰冻预约更换专家")
    @PostMapping("/expertAssignment.htm")
    @UserTokenAop
    public Result expertAssignment(
            @ApiParam(name = "consult_id", value = "加密consultId") @RequestParam(value = "consult_id", required = false) String consult_id,
            @ApiParam(name = "expertDoctorId", value = "医生Id") @RequestParam(value = "expertDoctorId", required = false) Long expertDoctorId,
            @ApiParam(name = "test", value = "医生留言") @RequestParam(value = "test", required = false) String test,
            HttpServletRequest request
    ) throws Exception {
        return null;

    }

    @ApiOperation(value = "修改典型病例收藏")
    @PostMapping("/editConsultCollection.htm")
    @UserTokenAop
    public Result editConsultCollection(
            @ApiParam(name = "doctorType", value = "医生类别。0申请，1专家，2复审专家。") @RequestParam(value =
                    "doctorType", required = true) Integer doctorType,
            @ApiParam(name = "consult_id", value = "加密consultId") @RequestParam(value = "consult_id", required = false) String consult_id,
            @ApiParam(name = "collection", value = "是否收藏，0，取消收藏，1，收藏") @RequestParam(value = "collection", required = false) Integer collection,
            HttpServletRequest request
    ) throws Exception {
        int n = 0;
        Integer consultId = Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        n=hzConsultDoctorService.updateIsCollection(consultId,doctorType,collection);
        return n==1?ResultGenerator.genSuccessResult("success"):ResultGenerator.genFailResult("fail");

    }

    @ApiOperation(value = "获取医生信息")
    @PostMapping("/getDoctorInfoByConsultId.htm")
    @UserTokenAop
    public Result getApplyDoctorInfoByConsultId(
            @ApiParam(name = "consult_id", value = "加密consult_id") @RequestParam(value = "consult_id", required = true) String consult_id,
            HttpServletRequest request
    ) throws Exception {
        Integer consultId = Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        return ResultGenerator.genSuccessResult(hzConsultDoctorService.getConsultDoctorByConsultId(consultId));
    }
            /*----------------------------------补充检查  Start------------------------------*/

    /**
     * 保存 提交添加切片弹窗
     */
    @ApiOperation(value = "补充检查-添加切片弹窗保存/提交")
    @PostMapping("/setBCJCByConsultId.htm")
    @UserTokenAop
    public Result setBCJCByConsultId(
            //补充检查-添加切片弹窗保存/提交
            @ApiParam(name = "sign", value = "0保存 1提交") @RequestParam(value = "sign") Integer sign,
            @ApiParam(name = "signtype", value = "0申请提交保存1专家提交保存") @RequestParam(value = "signtype") Integer signtype,
            @ApiParam(name = "consult_id", value = "加密consult_id") @RequestParam(value = "consult_id") String consult_id,
            @ApiParam(name = "specialistId", value = "专家id") @RequestParam(value = "specialistId", required = false) String specialistId,
            @ApiParam(name = "censorate", value = "检查机构") @RequestParam(value = "censorate", required = false) String censorate,
            @ApiParam(name = "initialJudgement", value = "初步判定") @RequestParam(value = "initialJudgement", required = false) String initialJudgement,
            @ApiParam(name = "remarkDoctor", value = "申请医生备注") @RequestParam(value = "remarkDoctor", required = false) String remarkDoctor,
            @ApiParam(name = "slideList", example = "{fileType:文件类型。jpg、excel、word、pdf、ppt、txt、rar,path:文件路径,url:文件下载地址,size:文件大小}", value = "病理切片列表，格式如下：{fileType:文件类型。jpg、excel、word、pdf、ppt、txt、rar,path:文件路径,url:文件下载地址,size:文件大小}") @RequestParam(name = "slideList", required = false) String slideList,
            @ApiParam(name = "ultimateJudgement", value = "判定结果") @RequestParam(value = "ultimateJudgement", required = false) String ultimateJudgement,
            @ApiParam(name = "supplementaryOpinion", value = "补充意见") @RequestParam(value = "supplementaryOpinion", required = false) String supplementaryOpinion,
            @ApiParam(name = "reportPath", value = "报告地址") @RequestParam(value = "reportPath", required = false) String reportPath,
            @ApiParam(name = "supplementSlideType", value = "补充检查切片模式") @RequestParam(value = "supplementSlideType", required = false) Integer supplementSlideType,
            @ApiParam(name="priceTypeId", value="价格分类 (分类id (301.常规 302.冰冻 303.细胞 304 补充))", required = false) @RequestParam(value="priceTypeId", required=false) Integer priceTypeId,
            @ApiParam(name="doctorPositionId", value = "医生职位ID") @RequestParam(value = "doctorPositionId",required = false) Integer doctorPositionId,
            HttpServletRequest request
    ) throws Exception {

        HzSupplementReport supplementReport = new HzSupplementReport();
        supplementReport.setIsDelete(0);
        supplementReport.setConsultId(Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY));
        supplementReport.setCensorate(censorate);
        supplementReport.setInitialJudgement(initialJudgement);
        supplementReport.setRemarkDoctor(remarkDoctor);
        supplementReport.setUltimateJudgement(ultimateJudgement);
        supplementReport.setSupplementaryOpinion(supplementaryOpinion);
        supplementReport.setReportPath(reportPath);

        return supplementReportService.saveSupplementReport(specialistId,sign, signtype, supplementReport, slideList,
               supplementSlideType,priceTypeId,doctorPositionId);

    }

    /**
     * 保存 提交添加切片弹窗
     */
    @ApiOperation(value = "补充检查-回显添加切片弹窗")
    @PostMapping("/getBCJCsectionByConsId.htm")
    @UserTokenAop
    public Result getBCJCsectionByConsId(
            @ApiParam(name = "consult_id", value = "加密consult_id") @RequestParam(value = "consult_id") String consult_id) throws Exception {
        return ResultGenerator.genSuccessResult(supplementReportService.selectSuppleReport(consult_id));
    }


}