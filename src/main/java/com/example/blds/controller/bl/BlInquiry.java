package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.entity.*;
import com.example.blds.service.*;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import com.example.blds.util.TokenUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * 病理所有查询接口
 *
 * @author root
 */
@Api(tags = "【病理】【查询】病理所有查询接口")
@RequestMapping("/blinquiry")
@ResponseBody
@Controller
public class BlInquiry {
	private final Logger logger = LoggerFactory.getLogger(BlInquiry.class);
	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private HzConsultService consultService;

	@Autowired
	private HzAddressService addressService;

	@Autowired
	private HzDiagnoseService diagnoseService;

	@Autowired
	private HzConsultAddressService consultAddressService;

	@Autowired
	private HzFileService fileService;

	@Autowired
	private HzConsultDoctorService consultDoctorService;

	@UserTokenAop
	@ApiOperation(value = "获取病历详情(包括图片，切片)")
	@PostMapping("/consultDetail.htm")
	public Result consultDetail(
			@ApiParam(name = "consultId", value = "加密consultId") @RequestParam(value = "consultId") String consult_id,
			@ApiParam(name = "token", value = "token")@RequestParam(value = "token") String token
	) throws Exception {
		Integer consultId = Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
		HzConsult consult = consultService.selectById(consultId);
		consult.setConsult_id(consult_id);
		return ResultGenerator.genSuccessResult(consult);
	}

	@UserTokenAop
	@ApiOperation(value = "获取诊断详情")
	@PostMapping("/getDiagnoseByConsultId.htm")
	public Result getDiagnoseByConsultId(
            @ApiParam(name = "consult_id", value = "加密consultId") @RequestParam(value = "consult_id") String consult_id,
            HttpServletRequest request, HttpServletResponse response
			) throws Exception {
		Integer consultId = Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
		return ResultGenerator.genSuccessResult(diagnoseService.getDiagnoseByConsultId(consultId));
	}

	@ApiOperation(value = "获取用户回寄地址")
	@RequestMapping(method = RequestMethod.POST, value = "/getAddresses.htm")
	public Result getAddresses(HttpServletRequest httpServletRequest) {
		String str=tokenUtil.checkToken(httpServletRequest.getCookies()[1].getValue());
		if (str.equals("token无效")){
			return ResultGenerator.genFailResult(str);
		}
		JSONObject jsonObject=JSONObject.fromObject(str);
		List<HzAddress> addresses = addressService.selectByUserId(jsonObject.optString("userId"));
		return ResultGenerator.genSuccessResult(addresses);
	}

	@ApiOperation(value = "查询获取统一寄送回寄地址")
	@RequestMapping(method = RequestMethod.POST, value = "/getSendAddresses.htm")
	public Result getSendAddresses() {
		List<HzAddress> addresses = addressService.selectSendAddress();
		if (addresses.size() > 0) {
			return ResultGenerator.genSuccessResult(addresses);
		} else {
			return ResultGenerator.genFailResult("未配置寄送地址");
		}
	}

	@ApiOperation(value = "医生端获取病例列表")
	@PostMapping("/doctorGetConsultList.htm")
	@ResponseBody
	public Result getConsultListToApplyMe(
			@ApiParam(name = "userId", value = "用户id") @RequestParam(value =
					"userId") Integer userId,
			@ApiParam(name = "pageSize", value = "页面大小") @RequestParam(value = "pageSize", required = false,
					defaultValue = "10") Integer pageSize,
			@ApiParam(name = "pageNum", value = "页码数") @RequestParam(value = "pageNum", required = false,
					defaultValue = "1") Integer pageNum,
			@ApiParam(name = "isCancel", value = "是否取消",example = "1") @RequestParam(value = "isCancel", required = false) Integer
					isCancel,
			@ApiParam(name = "doctorType", value = "类型") @RequestParam(value = "doctorType") Integer
					doctorType
	) throws UnsupportedEncodingException {
		List<Integer> consultStatusList=null;
		if (pageSize==null){
			pageSize=10;
		}
		if (pageNum==null){
			pageNum=1;
		}
		if (doctorType==1){
			consultStatusList= Arrays.asList(3,4,6,8,9);
		}

		Page<HzUser> pageInfo = PageHelper.startPage(pageNum, pageSize);
		List<HzConsult> consultList=consultService.getConsultListByInfo(userId,consultStatusList,isCancel,doctorType);
		consultList=Crypt.desEncryptConsultList(consultList,Enumeration.SECRET_KEY.CONSULT_ID_KEY);
		return ResultGenerator.genSuccessResult(pageInfo,pageInfo.getTotal());
	}

	@ApiOperation(value = "获取收藏列表")
	@PostMapping("/getConsultCollection.htm")
	@ResponseBody
	public Result getConsultCollection(
			@ApiParam(name = "pageSize", value = "页面大小",example = "1") @RequestParam(value = "pageSize", required = false,
					defaultValue = "10") Integer pageSize,
			@ApiParam(name = "pageNum", value = "页码数",example = "1") @RequestParam(value = "pageNum", required = false,
					defaultValue = "1") Integer pageNum,
			HttpServletRequest request
	) throws UnsupportedEncodingException {
		String token=request.getCookies()[1].getValue();
		String res=tokenUtil.checkToken(token);
		if (res.equals("token无效")){
			return ResultGenerator.genFailResult(res);
		}
		Page<HzConsult> pageInfo =PageHelper.startPage(pageNum, pageSize);

		HzConsultDoctor consultDoctor = new HzConsultDoctor();
		consultDoctor.setCollection(1);
		consultDoctor.setDoctorId(JSONObject.fromObject(res).optLong("userId"));
		List<HzConsult> list = consultService.getConsultList(consultDoctor);
		return ResultGenerator.genSuccessResult(pageInfo,pageInfo.getTotal());
	}

	@ApiOperation(value = "运营获取病例列表")
	@PostMapping("/adminGetConsultList.htm")
	@ResponseBody
	@UserTokenAop
	public Result getConsultListToOperate(
			@ApiParam(name = "hospitalId", value = "医院ID") @RequestParam(value = "hospitalId", required = false) String hospitalId,
			@ApiParam(name = "statusType") @RequestParam(value = "statusType") String
					statusType,
			@ApiParam(name = "startTime") @RequestParam(value = "startTime",
					required = false) String startTime,
			@ApiParam(name = "endTime") @RequestParam(value = "endTime",
					required = false) String endTime,
			@ApiParam(name = "pageSize", value = "页面大小", required = true,example = "1") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@ApiParam(name = "pageNum", value = "页码数", required = true,example = "1") @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum
	) {
		List<Integer> consultStatusList=null;
		if (statusType.equals("1")){
			consultStatusList=Arrays.asList(2,3,4,8,9);
		}else if (statusType.equals("2")){
			consultStatusList=Arrays.asList(6);
		}else if (statusType.equals("3")){
			consultStatusList=Arrays.asList(7);
		}
		List<HzConsult> consults=consultService.selectByFormInfo(hospitalId,consultStatusList,startTime,endTime,pageSize,pageNum);
		return ResultGenerator.genSuccessResult(consults);
	}

	/**
	 * 通过状态获取数量
	 *
	 * @param doctorType
	 * @param
	 * @return unfinishedCount 未完成
	 * @throws UnsupportedEncodingException
	 */
	@ApiOperation(value = "获取状态数量")
	@PostMapping("/getCountByStatus.htm")
	@ResponseBody
	@UserTokenAop
	public Result getCountByStatus(
			@ApiParam(name = "doctorType", value = "医生类型0申请 1专家",example = "1") @RequestParam(value = "doctorType") Integer doctorType,
			@ApiParam(name = "user_id",value = "用户id") @RequestParam(value = "user_id") Long userId
	) throws UnsupportedEncodingException {
		HzConsultDoctor consultDoctor = new HzConsultDoctor();
		consultDoctor.setDoctorType(doctorType);
		consultDoctor.setDoctorId(userId);
		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("unfinishedCount", consultService.getCountByStatus(consultDoctor, 0));
//		jsonObject.put("finishedCount", consultService.getCountByStatus(consultDoctor, 1));

		return ResultGenerator.genSuccessResult(jsonObject);
	}


	@ApiOperation(value = "通过ID设置删除状态")
	@PostMapping("/updateIsDeleteByConsultId.htm")
	@ResponseBody
	@UserTokenAop
	public Result updateIsDeleteByConsultId(
			@ApiParam(name = "id", value = "加密consult_id") @RequestParam(value = "id") String id

	) {

		Integer consId = Crypt.desDecryptByInteger(id, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
		Integer consult = consultService.updateIsDeleteByConsultId(consId);
		return ResultGenerator.genSuccessResult(consult);
	}

//	@ApiOperation(value = "个人中心我的会诊统计")
//	@PostMapping("/getMyDiagnose.htm")
//	public Result getMyDiagnose(
//			@ApiIgnore HttpSession session
//	) {
//		LoginUser loginUser = (LoginUser) session.getAttribute(Constants.LOGIN_USER);
//		String res = HttpClient.sendGetToken(Constants.PTURL + "/api/DjUser/GetUserInfo","", session.getAttribute(Constants.TOKEN).toString());
//		com.alibaba.fastjson.JSONObject resObj = com.alibaba.fastjson.JSONObject.parseObject(res);
//		Integer doctorType = 0;
//		if(resObj.getJSONObject("ret_data").getIntValue("user_type") > 2){
//			doctorType = 1;
//		}
//        Long doctorId = loginUser.getDoctorId();
//		List<Consult> constantsList = consultService.getConsultAndConsultDoctorWithDoctorId(doctorId, doctorType);
//		Long undiagnosed = constantsList.stream().filter((e) -> e.getIsCancel().equals(0) && e.getConsultStatus().equals(CONSULT_STATUS.UNDIAGNOSED)).count();
//		Long diagnosed = constantsList.stream().filter((e) -> e.getIsCancel().equals(0) && e.getConsultStatus().equals(CONSULT_STATUS.DIAGNOSED)).count();
//		Long returned = constantsList.stream().filter((e) -> e.getIsCancel().equals(0) && e.getConsultStatus().equals(CONSULT_STATUS.RETURNED)).count();
//		Long canceled = constantsList.stream().filter((e) -> e.getIsCancel().equals(1)).count();
//
//		Integer sumCount = 0;
//		Integer monthCount = 0;
//		if(doctorType.equals(1)){
//			sumCount = constantsList.stream().filter(e -> e.getConsultStatus().equals(CONSULT_STATUS.DIAGNOSED) && e.getVerifyStatus().equals(VERIFY_STATUS.FEE_CLEARED) && e.getIsCancel().equals(0)).map(Consult::getTopDoctorFee).reduce(0, Integer::sum);
//			monthCount = constantsList.stream().filter(e -> e.getConsultStatus().equals(CONSULT_STATUS.DIAGNOSED) && e.getVerifyStatus().equals(VERIFY_STATUS.FEE_CLEARED) && e.getIsCancel().equals(0)).filter((e) -> e.getDiagnose() != null).filter((e) -> {
//				LocalDate localDate = e.getDiagnose().getDiagnoseTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//				return localDate.getYear() == LocalDate.now().getYear() && localDate.getMonthValue() == LocalDate.now().getMonth().getValue();
//			}).map(Consult::getTopDoctorFee).reduce(0, Integer::sum);
//		}
//
//		//,获取近一年的12个月份
//		LocalDate localDate = LocalDate.now();
//		List<LocalDate> localDates = new ArrayList<>();
//		//上一年的月份数
//		int lastYearMonth = Month.DECEMBER.getValue() - localDate.getMonthValue();
//
//		if (localDate.getMonth().getValue() == Month.DECEMBER.getValue()) {
//			for (int i = 1; i <= Month.DECEMBER.getValue(); i++) {
//				LocalDate tempLocalDate = LocalDate.of(localDate.getYear(), i, 1);
//				localDates.add(tempLocalDate);
//			}
//		} else {
//			for (int i = lastYearMonth; i > 0; i--) {
//				LocalDate tempLocalDate = LocalDate.of((localDate.getYear() - 1), Month.DECEMBER.getValue() - i + 1, 1);
//				localDates.add(tempLocalDate);
//			}
//			for (int i = 1; i <= localDate.getMonthValue(); i++) {
//				LocalDate tempLocalDate = LocalDate.of(localDate.getYear(), i, 1);
//				localDates.add(tempLocalDate);
//			}
//		}
//
//		JSONArray jsonArray = new JSONArray();
//		for (LocalDate ld : localDates) {
//			JSONObject jsonObject = new JSONObject();
//			long l = constantsList.stream().filter((e) -> e.getConsultStatus().equals(CONSULT_STATUS.DIAGNOSED) && e
//					.getIsCancel().equals(0))
//					.filter(e -> e.getDiagnose() != null)
//					.filter(e -> e.getDiagnose().getDiagnoseTime() != null)
//					.filter(e -> {
//						LocalDate tempLD = e.getDiagnose().getDiagnoseTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//						return ld.getYear() == tempLD.getYear() && ld.getMonthValue() == tempLD.getMonthValue();
//					}).count();
//			jsonObject.put("month", String.valueOf(String.valueOf(ld.getMonthValue()).concat("月")));
//			jsonObject.put("count", l);
//			jsonArray.add(jsonObject);
//		}
//
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("undiagnosed", undiagnosed);
//		jsonObject.put("diagnosed", diagnosed);
//		jsonObject.put("canceled", canceled);
//		jsonObject.put("returned", returned);
//		jsonObject.put("sumCount", sumCount);
//		jsonObject.put("monthCount", monthCount);
//		jsonObject.put("statistics", jsonArray);
//		return new ResultBean<>(jsonObject);
//	}


	/**
	 * 查询寄件地址
	 *
	 * @param consultId
	 * @throws UnsupportedEncodingException
	 */
	@ApiOperation(value = "根据consultId查询寄件地址")
	@PostMapping("/getAddressByConsultId.htm")
	public Result getAddressByConsultId(
			@ApiParam(name = "consultId", value = "加密的consultId") @RequestParam(value = "consultId") String consultId,
			@RequestParam("type")Integer type
	) throws Exception {
		HzConsultAddress consultAddress = consultAddressService.selectByConsultId( Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY),type);
		consultAddress.setAddressId(Crypt.desEncrypt(String.valueOf(consultAddress.getId()),Enumeration.SECRET_KEY.ADDRESS_ID_KEY));
		consultAddress.setConsultId(null);
		consultAddress.setId(null);
		return ResultGenerator.genSuccessResult(consultAddress);
	}


	/**
	 * 查看快递单
	 *
	 * @param consultId
	 * @throws UnsupportedEncodingException
	 * @author liujingguang
	 */
	@ApiOperation(value = "根据consultId查询资料/快递单")
	@PostMapping("/getExpressDeliveryByConsultId.htm")
	public Result getExpressDeliveryByConsultId(
			@ApiParam(name = "consultId", value = "加密的consultId") @RequestParam(value = "consultId") String consultId,
			@ApiParam(name = "type", value = "查询类型 0.资料 1.快递单",example = "1") @RequestParam(value = "type") Integer type,
			HttpSession session
	) throws UnsupportedEncodingException {
		List<HzFile> expressDelivery = fileService.getExpressDeliveryByConsultId(Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY), type);
		return ResultGenerator.genSuccessResult(expressDelivery);
	}


    /*@ApiOperation(value = "根据consult_id获取上级医生信息")
	@PostMapping("/getDoctorByConsultId.htm")
    public ResultBean<ConsultDoctor> getDoctorByConsultId(
            @ApiParam(name = "consult_id", value = "加密的consultId") @RequestParam(value = "consult_id", required = true) String consult_id
    ){
        Integer consultId = Crypt.desDecryptByInteger(consult_id, SECRET_KEY.CONSULT_ID_KEY);
        ConsultDoctor consultDoctor = consultDoctorService.selectByConsultIdAndDoctorType(consultId,1);
        return new ResultBean<>(consultDoctor);
    }*/


	@UserTokenAop
	@ApiOperation(value = "通过consult_id查找Consult")
	@PostMapping("/getConsultByConsultId.htm")
	public Result getConsultByConsultId(
			@ApiParam(name = "consult_id", value = "加密id") @RequestParam(value = "consult_id", required = false) String consult_id
	) throws Exception {
		Integer id = Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
		HzConsult consult = consultService.selectById(id);
		return ResultGenerator.genSuccessResult(consult);
	}

	@UserTokenAop
	@ApiOperation(value = "根据userId获取完成/未完成的数量")
	@PostMapping("/getCount.htm")
	public Result getCount(
			@ApiParam(name = "userId", value = "用户id") @RequestParam(value = "userId") String user_id,
			@ApiParam(name = "doctorType", value = "type") @RequestParam(value = "doctorType") Integer doctor_type
	){
		CountResult countResult=consultService.getCount(user_id,doctor_type);
		return ResultGenerator.genSuccessResult(countResult);
	}

	/**
	 * 获取医院名称和ID
	 *
	 * @return
	 * @throws Exception
	 */
//	@UserTokenAop
//	@ApiOperation(value = "获取医院名称和ID")
//	@PostMapping("/getHospitalNameAndId.htm")
//	public ResultBean<List<ConsultDoctor>> getHospitalNameAndId() throws Exception {
//		List<ConsultDoctor> consultDoctor = consultDoctorService.getHospitalNameAndId();
//		return new ResultBean<List<ConsultDoctor>>(consultDoctor);
//	}

}