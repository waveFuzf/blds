package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.entity.HzPriceConfig;
import com.example.blds.service.HzConfigService;
import com.example.blds.service.HzPriceServices;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import com.example.blds.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Api(tags = "价格管理  (价格列表、条件查询 添加价格 设置分成)")
@RequestMapping("/blprice")
@Controller
public class BlPriceController {

    @Autowired
    HzPriceServices priceServices;
    @Autowired
    HzConfigService configService;

    @Autowired
    private TokenUtil tokenUtil;

    private static HashMap<Integer,String> hashMap=new HashMap<Integer, String>(){
        {
            put(301,"常规病理会诊");
            put(302,"细胞病理会诊");
            put(303,"术中冰冻会诊");
            put(304,"补充检查");
        }
    };

    @ApiOperation(value = "价格列表、条件查询")
    @PostMapping("/selectPriceList.htm")
    @ResponseBody
    public Result selectPriceList(
            @ApiParam(name="priceTypeId", value="价格分类 (分类id (301.常规 302.冰冻 303.细胞))") @RequestParam(value="priceTypeId") Integer priceTypeId
    ) throws Exception {

        List<HzPriceConfig> priceList = priceServices.selectPriceListByType(priceTypeId);

        priceList.forEach((e)->{
            try {
                e.setPrice_id(Crypt.desEncrypt(e.getId().toString(), Enumeration.SECRET_KEY.PRICE_ID_KEY));
                e.setId(null);
            } catch (Exception e1) {
                throw new RuntimeException("加密错误");
            }
        });

        return ResultGenerator.genSuccessResult(priceList);
    }

    @ApiOperation(value = "添加价格")
    @PostMapping("/addPriceList.htm")
    @ResponseBody
    public Result addPriceList(HttpServletRequest request,
                                            @ApiParam(name="priceTypeId", value="价格分类id (分类 (301.常规 302.冰冻 303.细胞))") @RequestParam(value="priceTypeId") Integer priceTypeId,
                                            @ApiParam(name="positionName", value="职称名称") @RequestParam(value="positionName") String positionName,
                                            @ApiParam(name="price", value="价格 (单位 分)") @RequestParam(value="price") Integer price
    ) throws Exception{

        List<HzPriceConfig> isExist = priceServices.selectByPositionId(positionName, priceTypeId);

        if(isExist.size()>0){
            return ResultGenerator.genFailResult("该职称已设置价格");
        }

        HzPriceConfig priceConfig = new HzPriceConfig();
        //设置默认优先级 优先级。越大越高。医联体默认100，医院默认200，科室300，医生500
        priceConfig.setPriceTypeId(priceTypeId);
        priceConfig.setPriceTypeName(hashMap.get(priceTypeId));
        priceConfig.setPositionName(positionName);
        priceConfig.setPrice(price);
        priceConfig.setCreateTime(new Date());
        priceConfig.setIsDelete(0);

        Integer insertno = priceServices.insertPriceConfig(priceConfig);

        if(insertno == 0){
            return ResultGenerator.genFailResult("插入数据库失败！");
        }

        return ResultGenerator.genSuccessResult("价格添加成功");
    }

    @ApiOperation(value = "修改价格")
    @PostMapping("/updatePriceList.htm")
    @ResponseBody
    public Result updatePriceList(HttpServletRequest request,
               @ApiParam(name="id", value="加密的价格id") @RequestParam(value="id") String id,
               @ApiParam(name="price", value="价格 (单位 分)") @RequestParam(value="price") Integer price
    ) throws Exception{


        Integer pid = Crypt.desDecryptByInteger(id, Enumeration.SECRET_KEY.PRICE_ID_KEY);

        HzPriceConfig priceS = new HzPriceConfig();
        priceS.setId(pid);
        priceS.setPrice(price);

        Integer updateLine = priceServices.updatePriceById(priceS);

        if(updateLine==0){
            return ResultGenerator.genFailResult("价格修改失败");
        }

        return ResultGenerator.genSuccessResult("价格修改成功");
    }

//    @ApiOperation(value = "查询分成")
//    @PostMapping("/selDividedInto.htm")
//    @ResponseBody
//    public ResultBean<PriceConfig> selDividedInto(
//            @ApiParam(name="price_id", value="加密的价格id", ) @RequestParam(value="price_id", ) String price_id
//    ) throws Exception {
//
//        Integer pid = Crypt.desDecryptByInteger(price_id, Enumeration.SECRET_KEY.PRICE_ID_KEY);
//
//        PriceConfig price = priceServices.selectPriceById(pid);
//
//        if(price==null){
//            return new ResultBean<>(Enumeration.RET_CODE.FAIL,"此价格不存在",null);
//        }
//
//        PriceConfig priceRes = new PriceConfig();
//        priceRes.setDoctorFee(price.getDoctorFee());
//        priceRes.setHospitalFee(price.getHospitalFee());
//        priceRes.setTopDoctorFee(price.getTopDoctorFee());
//        priceRes.setTopHospitalFee(price.getTopHospitalFee());
//
//        return new ResultBean<PriceConfig>(priceRes);
//    }
//
//    @ApiOperation(value = "设置分成")
//    @PostMapping("/setDividedInto.htm")
//    @ResponseBody
//    public ResultBean<Integer> setDividedInto(
//            @ApiParam(name="id", value="加密的价格id", ) @RequestParam(value="id", ) String id,
//            @ApiParam(name="doctorFee", value="申请医生分成费用，单位分", required=false) @RequestParam(value="doctorFee", required=false) Integer doctorFee,
//            @ApiParam(name="hospitalFee", value="申请医院分成费用，单位分", required=false) @RequestParam(value="hospitalFee", required=false) Integer hospitalFee,
//            @ApiParam(name="topDoctorFee", value="专家医生分成费用，单位分", required=false) @RequestParam(value="topDoctorFee", required=false) Integer topDoctorFee,
//            @ApiParam(name="topHospitalFee", value="专家医院分成费用，单位分", required=false) @RequestParam(value="topHospitalFee", required=false) Integer topHospitalFee
//    ) throws Exception {
//
//        Integer pid = Crypt.desDecryptByInteger(id, Enumeration.SECRET_KEY.PRICE_ID_KEY);
//
//        PriceConfig PriceConfigc = priceServices.selectPriceById(pid);
//        if (PriceConfigc==null){
//            return new ResultBean<Integer>(Enumeration.RET_CODE.FAIL,"价格配置不存在");
//        }
//
//        PriceConfig priceConfig = new PriceConfig();
//        priceConfig.setId(pid);
//        priceConfig.setDoctorFee(doctorFee);
//        priceConfig.setHospitalFee(hospitalFee);
//        priceConfig.setTopDoctorFee(topDoctorFee);
//        priceConfig.setTopHospitalFee(topHospitalFee);
//
//        Integer updateno = priceServices.updatePriceById(priceConfig);
//
//        if(updateno == Constants.ZERO){
//            ResultBean resultBean = new ResultBean<String>(Enumeration.RET_CODE.FAIL,"数据库修改失败");
//            return  resultBean;
//        }
//
//        return new ResultBean<Integer>(updateno).setRetCode(Enumeration.RET_CODE.SUCCESS);
//    }



    @ApiOperation(value = "删除价格配置")
    @PostMapping("/deletePriceConfig.htm")
    @ResponseBody
    public Result deletePriceConfig(
            @ApiParam(name="id", value="加密的价格id") @RequestParam(value="id") String id,
            HttpServletRequest request
    ) throws Exception{
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        Integer pid = Crypt.desDecryptByInteger(id, Enumeration.SECRET_KEY.PRICE_ID_KEY);

        Integer updateno = priceServices.deletePriceConfigById(pid);

        if(updateno == 0){
            return ResultGenerator.genFailResult("删除失败!");
        }

        return ResultGenerator.genSuccessResult("删除成功!");
    }
//    @ApiOperation(value = "根据医院ID获取配置的支付方式")
//    @PostMapping("/getConfig.htm")
//    @ResponseBody
//    public ResultBean<Config> getConfig(
//            HttpSession session
//    ) {
//        LoginUser loginUser = (LoginUser) session.getAttribute(Constants.LOGIN_USER);
//        Long unitId = loginUser.getUnitId();
//        Config configs=configService.getConfigByUnionId(unitId);
//        return new ResultBean<>(configs);
//    }

}
