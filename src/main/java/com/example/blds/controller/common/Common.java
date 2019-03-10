package com.example.blds.controller.common;

import com.example.blds.PdfHelper;
import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.dao.HzConsultAddressMapper;
import com.example.blds.dao.HzSupplementReportMapper;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzDiagnose;
import com.example.blds.entity.HzSupplementReport;
import com.example.blds.service.HzConsultService;
import com.example.blds.service.HzDiagnoseService;
import com.example.blds.service.HzSlidesService;
import com.example.blds.service.HzSupplementReportService;
import com.example.blds.util.Crypt;
import com.example.blds.util.HtmlGenerator;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


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
    private HzSupplementReportService supplementReportService;
    @Autowired
    private HzSupplementReportMapper hzSupplementReportMapper;
    @Autowired
    private HzConsultAddressMapper consultAddressMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

    @Value("${file.url}")
    private String fileUrl;
    @Value("${url}")
    private String url;

    @ApiOperation(value = "获取支付地址")
    @PostMapping("/consultlist.htm")
    @UserTokenAop
    public Result getPayPath(
            @ApiParam(name = "consult_id", value = "加密consultId", required = true) @RequestParam(value = "consult_id") String consult_id,
            @ApiParam(name = "payType", value = "支付方式。1支付宝，2线下", required = true) @RequestParam(value = "payType") int payType,
            @ApiParam(name = "bcjc", value = "1补充检查 0普通病例") @RequestParam(value = "bcjc") int bcjc,
            @ApiParam(name = "token", value = "token") @RequestParam(value = "token") String token
            ) throws Exception {
        JSONObject jsonObject = new JSONObject();
        int consultId = Integer.parseInt(Crypt.desDecrypt(consult_id, com.example.blds.util.Enumeration.SECRET_KEY.CONSULT_ID_KEY));
        HzConsult consult = consultService.selectById(consultId);
        //应付费用
        if (bcjc == 1) {
            jsonObject.put("fee", consult.getSupplementPrice());
        } else{
            jsonObject.put("fee", consult.getPrice());
        }

        if (payType == 2) {
            if (consult.getSupplementSlideType()==null){
                consult.setConsultStatus(consult.getSlideType() == 1 ? 3 : 4);
            } else {
                consult.setConsultStatus(consult.getSupplementSlideType()==1?14:3);
            }
//            consult.setConsultNo("BL20190128001");
            consult.setId(consultId);
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
    public Result createPdf(
            @ApiParam(name = "consult_id", value = "加密consultId", required = true) @RequestParam(value = "consult_id") String consult_id,
            @ApiParam(name = "type", value = "pdf模板类型（0：病理诊断报告，1：补充检查诊断报告）", required = true) @RequestParam(value = "type") int type
    ) throws Exception {

        int consultId = Crypt.desDecryptByInteger(consult_id, com.example.blds.util.Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        HzConsult consult = consultService.selectById(consultId);
        if (consult==null){
            return ResultGenerator.genFailResult("该病例不存在");
        }
        if (!(type==0 && (consult.getConsultStatus()==6||consult.getConsultStatus()==8))&&!(type==1&&consult.getConsultStatus()==6)){
            return ResultGenerator.genFailResult("该病例未结束");
        }
        String htmlName=null;
        if(type==0){
            htmlName="report.ftl";
        }else if(type==1){
            htmlName="reportBCJC.ftl";
        }
        HzDiagnose hzDiagnose=new HzDiagnose();
        HzSupplementReport hzSupplementReport=new HzSupplementReport();
        Map<String, Object> map = new HashMap<>();
        if(type==0){
            hzDiagnose=diagnoseService.getDiagnoseByConsultId(consultId);
            map.put("form",hzDiagnose);
        }else {
            hzSupplementReport =supplementReportService.selectSuppleReport(consultId);
            map.put("form",hzSupplementReport);
        }
//            map = getDailySummaryMap(consultService,startTime,endTime,map);
        map.put("consultDetail",consult);

        map.put("date",simpleDateFormat.format(new Date()));
        String htmlStr = HtmlGenerator.generate(htmlName, map);
        String uuid=UUID.randomUUID().toString().replace("-","");
        String fileName=fileUrl+"\\pdf\\"+uuid+".pdf";
        OutputStream out = new FileOutputStream(new File(fileName));
        generateToFile(out,htmlStr);
        if (type==0){
            hzDiagnose.setPdf(url+"pdf/"+uuid+".pdf");
            diagnoseService.updateByConsultIdSelective(hzDiagnose);
        }else {
            hzSupplementReport.setReportPath(fileName);
            hzSupplementReportMapper.updateByPrimaryKeySelective(hzSupplementReport);
        }

        return ResultGenerator.genSuccessResult(fileName);
    }

    public static void main(String[] args) throws Exception {
        String htmlStr = HtmlGenerator.generate("report.ftl", null);
        OutputStream out = null;
        out = new FileOutputStream(new File("C:\\Users\\90663\\Desktop\\emmm.pdf"));
        savePdf(out,htmlStr);
    }
    public static void generateToFile(OutputStream out, String html) throws Exception {

        ITextRenderer render = PdfHelper.getRender();

        render.setDocumentFromString(html);
        render.layout();
        render.createPDF(out);
        render.finishPDF();
        render = null;
        out.close();
    }

    public static void savePdf(OutputStream out, String html) {
        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
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
