package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.entity.*;
import com.example.blds.service.*;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
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
			@ApiParam(name = "consult_id", value = "加密consultId") @RequestParam(value = "consult_id") String consult_id
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

	@UserTokenAop
	@ApiOperation(value = "获取用户回寄地址")
	@RequestMapping(method = RequestMethod.POST, value = "/getAddresses.htm")
	public Result getAddresses(@ApiParam(name = "user_id",value = "用户id") @RequestParam(value = "user_id") String userId) {
		List<HzAddress> addresses = addressService.selectByUserId(userId);
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
	@UserTokenAop
	public Result getConsultListToApplyMe(
			@ApiParam(name = "doctorType", value = "医生类别。0申请，1专家，2复审专家。") @RequestParam(value =
					"doctorType", required = true) Integer doctorType,
			@ApiParam(name = "patientName", value = "患者姓名") @RequestParam(value = "patientName", required = false)
					String patientName,
			@ApiParam(name = "consultStatusList", value = "病例状态,1草稿；2待支付；3待收货；4待诊断；5已退回；6已诊断；12待补充；13待二次诊断 。60未审核 61审核通过（已诊断+审核通过） " +
					"62表示审核不通过 63表示已结算") @RequestParam(value = "consultStatusList", required = false) String
					consultStatusList,
			@ApiParam(name = "commitStartTime", value = "开始时间 yyyy-MM-dd") @RequestParam(value = "commitStartTime",
					required = false) String commitStartTime,
			@ApiParam(name = "commitEndTime", value = "结束时间 yyyy-MM-dd") @RequestParam(value = "commitEndTime",
					required = false) String commitEndTime,
			@ApiParam(name = "pageSize", value = "页面大小") @RequestParam(value = "pageSize", required = false,
					defaultValue = "10") Integer pageSize,
			@ApiParam(name = "pageNum", value = "页码数") @RequestParam(value = "pageNum", required = false,
					defaultValue = "1") Integer pageNum,
			@ApiParam(name = "isCancel", value = "是否取消",example = "1") @RequestParam(value = "isCancel", required = false) Integer
					isCancel,
			@ApiParam(name = "statusType", value = "statusType",example = "1") @RequestParam(value = "statusType", required =
					false) Integer statusType,
			HttpServletRequest request,
			HttpSession session
	) throws UnsupportedEncodingException {
		return null;
	}

	@ApiOperation(value = "获取收藏列表")
	@PostMapping("/getConsultCollection.htm")
	@ResponseBody
	@UserTokenAop
	public Result getConsultCollection(
			@ApiParam(name = "doctorType", value = "医生类别。0申请，1专家，2复审专家。", required = true,example = "1") @RequestParam(value =
					"doctorType", required = true) Integer doctorType,
			@ApiParam(name = "pageSize", value = "页面大小",example = "1") @RequestParam(value = "pageSize", required = false,
					defaultValue = "10") Integer pageSize,
			@ApiParam(name = "pageNum", value = "页码数",example = "1") @RequestParam(value = "pageNum", required = false,
					defaultValue = "1") Integer pageNum,
			@ApiParam(name = "userId", value = "用户id") @RequestParam(value = "userId") Long userId
	) throws UnsupportedEncodingException {
		Page<HzConsult> pageInfo =PageHelper.startPage(pageNum, pageSize);

		HzConsultDoctor consultDoctor = new HzConsultDoctor();
		consultDoctor.setCollection(1);
		consultDoctor.setDoctorType(doctorType);
		consultDoctor.setDoctorId(userId);
		List<HzConsult> list = consultService.getConsultList(consultDoctor);
		return ResultGenerator.genSuccessResult(pageInfo);
	}

	@ApiOperation(value = "运营获取病例列表")
	@PostMapping("/adminGetConsultList.htm")
	@ResponseBody
	@UserTokenAop
	public Result getConsultListToOperate(
			@ApiParam(name = "caseTypeName", value = "[\'常规\',\'细胞\',\'冰冻\']") @RequestParam(value = "caseTypeName",
					required = false) String caseTypeNameList,
			@ApiParam(name = "nameOrParts", value = "患者姓名或部位") @RequestParam(value = "nameOrParts", required = false)
					String nameOrParts,
			@ApiParam(name = "type", value = "医院类型,0申请医院；1专家医院",example = "1") @RequestParam(value = "type",
					required = false) Integer type,
			@ApiParam(name = "isCancel", value = "取消",example = "1") @RequestParam(value = "isCancel",
					required = false, defaultValue = "0") Integer isCancel,
			@ApiParam(name = "hospitalId", value = "医院ID") @RequestParam(value = "hospitalId",
					required = false) String hospitalId,
			@ApiParam(name = "consultStatusList", value = "病例状态,1草稿；2待支付；3待收货；4待诊断；5已退回；6已诊断。60未审核 61审核通过（已诊断+审核通过） " +
					"62表示审核不通过 63表示已结算") @RequestParam(value = "consultStatusList", required = false) String
					consultStatusList,
			@ApiParam(name = "commitStartTime", value = "开始时间 yyyy-MM-dd") @RequestParam(value = "commitStartTime",
					required = false) String commitStartTime,
			@ApiParam(name = "commitEndTime", value = "结束时间 yyyy-MM-dd") @RequestParam(value = "commitEndTime",
					required = false) String commitEndTime,
			@ApiParam(name = "pageSize", value = "页面大小", required = true,example = "1") @RequestParam(value = "pageSize", required = true,
					defaultValue = "10") Integer pageSize,
			@ApiParam(name = "pageNum", value = "页码数", required = true,example = "1") @RequestParam(value = "pageNum", required = true,
					defaultValue = "1") Integer pageNum
	) throws UnsupportedEncodingException {
		return null;
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
			@ApiParam(name = "consultId", value = "加密的consultId") @RequestParam(value = "consultId") String consultId
	) throws UnsupportedEncodingException {
		HzConsultAddress consultAddress = consultAddressService.selectByConsultId(Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY));

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

	@ApiOperation(value = "获取病例地址")
	@PostMapping("/getConsultAddress.htm")
	@UserTokenAop
	public Result getConsultAddress(
			@ApiParam(name = "consult_id", value = "病例id") @RequestParam(value = "consult_id", required = false) String consult_id
	) {
		Integer consultId = Crypt.desDecryptByInteger(consult_id, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
		HzConsultAddress consultAddress = consultAddressService.selectByConsultId(consultId);
		return ResultGenerator.genSuccessResult(consultAddress);
	}

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