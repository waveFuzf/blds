package com.example.blds.controller.common;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.dao.HzConsultAddressMapper;
import com.example.blds.dao.HzSupplementReportMapper;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzSupplementReport;
import com.example.blds.service.HzConsultService;
import com.example.blds.service.HzDiagnoseService;
import com.example.blds.service.HzSlidesService;
import com.example.blds.util.Crypt;
import com.example.blds.util.HtmlGenerator;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 会诊中心所有业务公共接口
 */
@Controller
@Api(tags = "会诊中心所有业务公共接口")
@RequestMapping("/common")
@ResponseBody
public class Common {

    @Autowired
    private HzConsultService consultService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private HzDiagnoseService diagnoseService;
    @Autowired
    private HzSlidesService slideService;
    @Autowired
    private HzSupplementReportMapper supplementReportMapper;
    @Autowired
    private HzConsultAddressMapper consultAddressMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @ApiOperation(value = "获取支付地址")
    @PostMapping("/consultlist.htm")
    @UserTokenAop
    public Result getPayPath(
            @ApiParam(name = "consult_id", value = "加密consultId", required = true) @RequestParam(value = "consult_id", required = true) String consult_id,
            @ApiParam(name = "payType", value = "支付方式。0微信，1支付宝，2线下", required = true) @RequestParam(value = "payType", required = true) int payType,
            @ApiParam(name = "bcjc", value = "1补充检查 0普通病例") @RequestParam(value = "bcjc", required = false) int bcjc,
            @ApiParam(name = "fee",value="费用")@RequestParam(value = "fee",required = false) Integer fee,
            @ApiParam(name = "returnPageUrl", value = "回调页面地址") @RequestParam(value = "returnPageUrl", required = false) String returnPageUrl
            ) throws Exception {
        JSONObject jsonObject = new JSONObject();
        int consultId = Integer.parseInt(Crypt.desDecrypt(consult_id, com.example.blds.util.Enumeration.SECRET_KEY.CONSULT_ID_KEY));
        HzConsult consult = consultService.selectById(consultId);
        //应付费用
        if (bcjc == 1) {
            jsonObject.put("fee", fee!=null?fee:consult.getSupplementPrice());
        } else{
            jsonObject.put("fee", fee!=null?fee:consult.getPreferentialPrice());
        }

        if (payType == 2) {
            if (consult.getSupplementSlideType()==null){
                consult.setConsultStatus(consult.getSlideType() == 1 ? 3 : 4);
            } else {
                consult.setConsultStatus(consult.getSupplementSlideType()==0?14:3);
            }
            consult.setConsultNo("BL20190128001");
            int n = consultService.updateByConsult(consult);
            if (n == 0) {
                return ResultGenerator.genFailResult("数据库更新失败");
            }
            return ResultGenerator.genSuccessResult("支付成功!");
        }
        return ResultGenerator.genFailResult("暂未开通支付功能");
    }


    @ApiOperation(value = "生成pdf")
    @PostMapping("/createPdf.htm")
    @UserTokenAop
    public Result createPdf(
            @ApiParam(name = "consult_id", value = "加密consultId", required = true) @RequestParam(value = "consult_id") String consult_id,
            @ApiParam(name = "type", value = "pdf模板类型（0：病理，1：补充检查诊断报告 2:补充检查送检单 3:病理+免疫组化）", required = true) @RequestParam(value = "type") int type,
            HttpSession session
    ) throws Exception {

        int consultId = Crypt.desDecryptByInteger(consult_id, com.example.blds.util.Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        HzConsult consult = consultService.selectById(consultId);
        if (type != 2) {
            if (consult.getConsultStatus() != com.example.blds.util.Enumeration.CONSULT_STATUS.DIAGNOSED && consult.getConsultStatus() != com.example.blds.util.Enumeration.CONSULT_STATUS.SECONDARYDBC) { //判断是否为补充检查
                return  ResultGenerator.genFailResult("该病例未诊断完成");
            }
        }
        HzSupplementReport supplementReport = new HzSupplementReport();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (type == 1 || type == 2) {
            supplementReport.setConsultId(consultId);
            supplementReport = supplementReportMapper.selectByConsultid(supplementReport);
        }

        String templatePath = session.getServletContext().getRealPath("/");
        if(type==0){
            templatePath+="pdf/bl-pdf.html";
        }else if(type==1){
            templatePath+="pdf/bcjc-pdf.html";
        }else if(type==2){
            templatePath+="pdf/bcjcsjsq-pdf.html";
        }else if(type==3){
            templatePath+="pdf/blmyzh-pdf.html";
        }

        return null;
    }

    @ApiOperation("日报表打印")
    @GetMapping(value = "/printDailyExcelByFtl.htm")
    public void printDailyExcelByFtl(
            @ApiParam(name = "startTime", value = "开始时间", required = true) @RequestParam(value = "startTime") String startTime,
            @ApiParam(name = "endTime", value = "开始时间", required = true) @RequestParam(value = "endTime") String endTime,
            HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        String finalFileName=null;
        String fileName="互联网医院日报";
        String userAgent = request.getHeader("User-Agent");

        if (StringUtils.contains(userAgent, "MSIE")) {// IE浏览器
            finalFileName = URLEncoder.encode(fileName, "UTF8");
        } else if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
            finalFileName = new String(fileName.getBytes(),
                    "ISO8859-1");
        } else {
            finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
        }
//        List<DailySummaryInfo> dailySummarys=consultService.getDailyConsultInfo(startTime,endTime, hosiptalId);

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" +finalFileName+".xlsx");
        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        try{
            Map<String, Object> map = new HashMap<>();
//            map = getDailySummaryMap(consultService,startTime,endTime,map);
            String htmlName="dailySummary.ftl";
            String htmlStr = HtmlGenerator.generate(htmlName, map);
            fileOut.write(htmlStr.getBytes());
            ServletOutputStream sout = response.getOutputStream();
            sout.write(fileOut.toByteArray());
            sout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
