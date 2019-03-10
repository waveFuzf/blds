package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.dao.*;
import com.example.blds.entity.*;
import com.example.blds.service.*;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import com.example.blds.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private TokenUtil tokenUtil;
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
    @Autowired
    private HzDiagnoseService hzDiagnoseService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ConsultPatientService consultPatientService;

    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
    @Autowired
    private HzSupplementReportMapper supplementReportMapper;
    @Autowired
    private HzAddressMapper addressMapper;
    @Autowired
    private HzConsultAddressMapper  consultAddressMapper;

    @Autowired
    private HzPriceConfigService priceConfigService;

    @Autowired
    private HzConsultAddressService hzConsultAddressService;

    private static NumberFormat nf=NumberFormat.getInstance();

    static {
        nf.setMinimumIntegerDigits(3);
        nf.setGroupingUsed(false);
    }


    @ApiOperation(value="申请医生申请会诊")
    @PostMapping("/editConsult.htm")
    @UserTokenAop
    public Result editConsult(@RequestBody ConsultInfo consultInfo,@RequestParam String token) throws Exception {
        Integer consultId = null;
        try {
            Integer toDayNo = getDailySum();
            String consultNo = "BL" + simpleDateFormat.format(new Date()) + nf.format(toDayNo);

            HzConsult hzConsult = new HzConsult();
            hzConsult.setConsultStatus(consultInfo.getConsultStatus());
            hzConsult.setConsultNo(consultNo);
            hzConsult.setCaseTypeId(consultInfo.getCaseTypeId());
            hzConsult.setParts(consultInfo.getParts());
            hzConsult.setSubspecialityName(consultInfo.getSubspecialityName());
            hzConsult.setSlideType(consultInfo.getSlideType());
            hzConsult.setCaseTypeName(consultInfo.getCaseTypeName());
            hzConsult.setPhone(consultInfo.getPhone());
            hzConsult.setPrice(consultInfo.getExpertDoc().getPrice());
            hzConsult.setCasePresentation(consultInfo.getCasePresentation());
            hzConsult.setClinicalDiagnosis(consultInfo.getClinicalDiagnosis());
            hzConsult.setRemake(consultInfo.getPatientRemark());
            hzConsult.setOldDiagnosis(consultInfo.getOldDiagnosis());
            hzConsult.setPurpose(StringUtils.strip(consultInfo.getPurpose().toString(), "[]"));
            hzConsult.setIsDelete(0);
            hzConsult.setCreateTime(new Date());
            consultId = consultService.save(hzConsult);

            ConsultPatient consultPatient = new ConsultPatient();
            consultPatient.setAge(consultInfo.getAge());
            consultPatient.setSex(consultInfo.getSex());
            consultPatient.setMzNum(consultInfo.getParamId());
            consultPatient.setPatientName(consultInfo.getName());
            consultPatient.setConsultId(consultId);
            consultPatientService.save(consultPatient);

            hzConsultDoctorService.save(consultId, consultInfo.getExpertDoc(), consultInfo.getApplyDoc());

            slidesService.save(consultInfo.getUploadSlidesList(), consultId, 0);

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult(Crypt.desEncrypt(String.valueOf(consultId),Enumeration.SECRET_KEY.CONSULT_ID_KEY));
    }

    private synchronized Integer getDailySum(){
        Integer todayNo=Integer.valueOf((String) redisTemplate.opsForValue().get("dailyNum")) ;
        redisTemplate.opsForValue().set("dailyNum",String.valueOf(todayNo+1));
        return todayNo;
    }

//    /**
//     *
//     * 管理员提交补充切片
//     */
//    @ApiOperation(value = "管理员补充切片上传")
//    @PostMapping("/editSlide.htm")
//    @UserTokenAop
//    public Result editSlide(
//            @ApiParam(name = "consult_id", value = "加密consultId") @RequestParam(value = "consult_id") String consult_id,
//            @ApiParam(name = "slideList", example = "{fileType:文件类型。jpg、excel、word、pdf、ppt、txt、rar,path:文件路径,url:文件下载地址,size:文件大小}", value = "病理切片列表，格式如下：{fileType:文件类型。jpg、excel、word、pdf、ppt、txt、rar,path:文件路径,url:文件下载地址,size:文件大小}") @RequestParam(name = "slideList") String slideList
//    ){
//        Integer consultid=Crypt.desDecryptByInteger(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY);
//        slidesService.deleteSlidesByConsultId(consultid);
//        JSONArray array = JSONArray.fromObject(slideList);
////        LoginUser loginUser= (LoginUser) request.getSession().getAttribute("loginUser");
//        for (int i = 0; i < array.size(); i++) {
//            JSONObject slideJSON = array.optJSONObject(i);
//            HzSlide slide = new HzSlide();
//            slide.setConsultId(consultid);
//            slide.setHospitalId(10086L);
//            int processStatus = slideJSON.optInt("processStatus", -1);
//            slide.setProcessStatus(processStatus == -1 ? 0 : processStatus);
//            //将type设为补充检查——type为1
//            slide.setType(1);
//            slide.setSlideName(slideJSON.optString("slideName"));
//            slide.setUuid(UUID.randomUUID().toString());
//            slide.setCreateTime(new Date());
//            slide.setClientSlidePath(slideJSON.optString("clientSlidePath"));
//            slide.setCosSlidePath(slideJSON.optString("cosSlidePath"));
//            slide.setSlideSize(slideJSON.optString("slideSize"));
//            slide.setFactoryUuid(slideJSON.optString("factoryUuid"));
//            if (slidesService.insertBySlide(slide)== 0) {
//                return ResultGenerator.genFailResult("上传切片失败！");
//            }
//        }
//        return null;
//    }

    @ApiOperation(value = "申请医生编辑地址")
    @PostMapping("/editDefault.htm")
    public Result editDefault(HttpServletRequest httpServletRequest,@RequestParam("addressId") Integer addressId) throws Exception {
        String str=tokenUtil.checkToken(httpServletRequest.getCookies()[1].getValue());
        if (str.equals("token无效")){
            return ResultGenerator.genFailResult(str);
        }
        JSONObject jsonObject=JSONObject.fromObject(str);
        HzAddress hzAddress=new HzAddress();
        hzAddress.setId(addressId);
        hzAddress.setUserId(jsonObject.optLong("userId"));
        hzAddressService.editAddress(hzAddress,true);
        return ResultGenerator.genSuccessResult();
    }


    @ApiOperation(value = "申请医生编辑地址")
    @PostMapping("/editAddress.htm")
    public Result editAddress(
            @ApiParam(name = "address_id", value = "加密addressId") @RequestParam(value = "address_id", required = false) String address_id,
            @ApiParam(name = "province", value = "省份") @RequestParam(value = "province") String province,
            @ApiParam(name = "city", value = "城市") @RequestParam(value = "city") String city,
            @ApiParam(name = "area",value ="地区") @RequestParam(value="area") String area,
            @ApiParam(name = "address", value = "地址") @RequestParam(value = "address") String address,
            @ApiParam(name = "name", value = "名字") @RequestParam(value = "name") String name,
            @ApiParam(name = "phone", value = "手机号码") @RequestParam(value = "phone") String phone,
            @ApiParam(name = "isDefault", value = "是否是默认地址") @RequestParam(value = "isDefault",required = false) boolean isDefault,
            @ApiParam(name = "token", value = "用户token") @RequestParam(value = "token") String token
    ) throws Exception {
        String str=tokenUtil.checkToken(token);
        if (str.equals("token无效")){
            return ResultGenerator.genFailResult(str);
        }
        JSONObject jsonObject=JSONObject.fromObject(str);
        HzAddress hzAddress=new HzAddress();
        if (address_id!=null){
            hzAddress.setId(Crypt.desDecryptByInteger(address_id, Enumeration.SECRET_KEY.ADDRESS_ID_KEY));
        }else {
            hzAddress.setCreateTime(new Date());
            hzAddress.setIsDelete(0);
            hzAddress.setType(1);
        }
        hzAddress.setProvince(province);
        hzAddress.setCity(city);
        hzAddress.setAddress(area+address);
        hzAddress.setName(name);
        hzAddress.setPhone(phone);
        hzAddress.setUserId(jsonObject.optLong("userId"));
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

    @ApiOperation(value = "补充检查-诊断")
    @PostMapping("/editBCJCByConsultId.htm")
    public Result editBCJCByConsultId(
            @RequestBody SupplementInfo supplementInfo,
            HttpServletRequest request
    ) throws Exception {
        Integer consultId=Crypt.desDecryptByInteger(supplementInfo.getConsult_id(), Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        HzSupplementReport supplementReport = supplementReportService.selectSuppleReport(consultId);
        if (supplementReport==null){
            return ResultGenerator.genSuccessResult("别提交了瞎操作!");
        }
        supplementReport.setUltimateJudgement(supplementInfo.getUltimateJudgement());
        supplementReport.setSupplementaryOpinion(supplementInfo.getSupplymentaryOpinion());
        supplementReportMapper.updateByPrimaryKeySelective(supplementReport);
        return ResultGenerator.genSuccessResult("补充检查信息提交成功!");
    }

    /**
     * 保存 提交添加切片弹窗
     */
    @ApiOperation(value = "补充检查-送检申请")
    @PostMapping("/applyBCJCByExpress.htm")
    public Result applyBCJC(
            @RequestParam("consultId")String consultId,@RequestParam("addressId")Integer addressId,
            HttpServletRequest request
    ) throws Exception{
        String token=request.getCookies()[1].getValue();
        String res=tokenUtil.checkToken(token);
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        JSONObject obj=JSONObject.fromObject(res);
        HzAddress hzAddress=addressMapper.selectByPrimaryKey(addressId);
        HzConsultAddress hzConsultAddress=new HzConsultAddress();
        hzConsultAddress.setType(1);
        hzConsultAddress.setReturnProvince("浙江省");
        hzConsultAddress.setReturnCity("杭州市");
        hzConsultAddress.setReturnAddress("江干区天城路68号万事利大厦A座8楼");
        hzConsultAddress.setReturnPhone("17826863260");
        hzConsultAddress.setReturnName("病理会诊中心");
        hzConsultAddress.setMailProvince(hzAddress.getProvince());
        hzConsultAddress.setMailCity(hzAddress.getCity());
        hzConsultAddress.setMailAddress(hzAddress.getAddress());
        hzConsultAddress.setMailName(hzAddress.getName());
        hzConsultAddress.setMailPhone(hzAddress.getPhone());
        hzConsultAddress.setIsDelete(0);
        Integer consid=Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        hzConsultAddress.setConsultId(consid );
        HzConsultAddress e=hzConsultAddressService.selectByConsultId( consid);
        if (e == null) {
            hzConsultAddress.setCreateTime(new Date());
            consultAddressMapper.insert(hzConsultAddress);
        }else {
            hzConsultAddress.setId(e.getId());
            consultAddressMapper.updateByPrimaryKeySelective(hzConsultAddress);
        }
        Integer price=priceConfigService.selectPriceByDoctorId(304,obj.optString("position")).getPrice();
        Integer amount=hzDiagnoseService.getDiagnoseByConsultId(consid).getImmuneTag().split(",").length;
        HzConsult consult=new HzConsult();
        consult.setId(consid);
        consult.setConsultStatus(3);
        consult.setSupplementSlideType(2);
        consult.setSupplementPrice(price*amount);

        consultService.updateByConsult(consult);
        return  ResultGenerator.genSuccessResult("提交成功!");
    }






    /**
     * 保存 提交添加切片弹窗
     */
    @ApiOperation(value = "补充检查-添加信息")
    @PostMapping("/setBCJCByConsultId.htm")
    public Result setBCJCByConsultId(
            @RequestBody SupplementInfo supplementInfo,
            HttpServletRequest request
    ) throws Exception {
        String token=request.getCookies()[1].getValue();
        String res=tokenUtil.checkToken(token);
        JSONObject obj=JSONObject.fromObject(res);
        Integer consultId=Crypt.desDecryptByInteger(supplementInfo.getConsult_id(), Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        HzSupplementReport supplementReport = new HzSupplementReport();
        supplementReport.setIsDelete(0);
        supplementReport.setCommitTime(new Date());
        supplementInfo.setIsCandel(supplementInfo.getIsCandel());
        supplementInfo.setMaterialNum(supplementInfo.getMaterialNum());
        supplementReport.setConsultId(consultId);
        supplementReport.setCensorate(supplementInfo.getCensorate());
        supplementReport.setInitialJudgement(supplementInfo.getInitialJudgement());
        supplementReport.setRemarkDoctor(supplementInfo.getRemark());
        supplementReportMapper.insert(supplementReport);


        slidesService.save(supplementInfo.getUploadSlidesList(), consultId, 1);

        Integer price=priceConfigService.selectPriceByDoctorId(304,obj.optString("position")).getPrice();
        Integer amount=hzDiagnoseService.getDiagnoseByConsultId(consultId).getImmuneTag().split(",").length;
        HzConsult consult=new HzConsult();
        consult.setId(consultId);
        consult.setConsultStatus(9);
        consult.setSupplementSlideType(1);
        consult.setSupplementPrice(price*amount);

        consultService.updateByConsult(consult);

        return ResultGenerator.genSuccessResult("补充检查信息提交成功!");

    }

    public static void main(String[] args) {
        Integer i=1;
        Integer b=3;
        System.out.println(i * b);
    }

    /**
     * 保存 提交添加切片弹窗
     */
    @ApiOperation(value = "补充检查-回显添加切片弹窗")
    @PostMapping("/getBCJCsectionByConsId.htm")
    @UserTokenAop
    public Result getBCJCsectionByConsId(
            @ApiParam(name = "consult_id", value = "加密consult_id") @RequestParam(value = "consult_id") String consult_id) throws Exception {
        return ResultGenerator.genSuccessResult(supplementReportService.selectSuppleReport(Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY)));
    }


}