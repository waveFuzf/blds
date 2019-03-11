package com.example.blds.controller;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.dao.HzConsultAddressMapper;
import com.example.blds.dao.UploadSlidesMapper;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultAddress;
import com.example.blds.entity.UploadSlides;
import com.example.blds.service.HzConsultAddressService;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import com.example.blds.util.TokenUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.UUID;

@CrossOrigin
@Controller
@RequestMapping("/file")
public class FileController {
    @Value("${file.url}")
    private String fileUrl;
    @Value("${url}")
    private String url;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UploadSlidesMapper uploadSlidesMapper;
    @Autowired
    private HzConsultAddressService hzConsultAddressService;
    @Autowired
    private HzConsultAddressMapper hzConsultAddressMapper;

    @PostMapping(value = "/upload")
    @ResponseBody
    public Result upload(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException {
        //1切片
        String str=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (str.equals("token无效")){
            return ResultGenerator.genFailResult("用户token无效");
        }
        if (!file.getContentType().equals("image/png")){
            return ResultGenerator.genFailResult("文件格式错误!");
        }
        JSONObject jsonObject=JSONObject.fromObject(str);
        String uuid=UUID.randomUUID().toString().replace("-","");

        try {
            File targetFile =new File(fileUrl+"\\slides\\",uuid+".png");
            file.transferTo(targetFile);
            UploadSlides uploadSlides=new UploadSlides();
            uploadSlides.setCreateTime(new Date());
            uploadSlides.setPath(url+"slides/"+uuid+".png");//待修改
            uploadSlides.setUuid(uuid);
            uploadSlides.setUploaderId(jsonObject.optInt("userId"));
            if (uploadSlidesMapper.insert(uploadSlides)==1){
                return ResultGenerator.genSuccessResult(uploadSlides);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(value = "/uploadExpress")
    @ResponseBody
    public Result uploadExpress(@RequestPart("file") MultipartFile file, HttpServletRequest request,@RequestParam String consultId,@RequestParam Integer type) throws IOException {
        String str=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (str.equals("token无效")){
            return ResultGenerator.genFailResult("用户token无效");
        }
        Example example=new Example(HzConsultAddress.class);
        example.createCriteria().andEqualTo("type",type).andEqualTo("consultId",Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY));
        HzConsultAddress hzConsultAddress=hzConsultAddressMapper.selectOneByExample(example);
        String uuid=UUID.randomUUID().toString().replace("-","");
        String filePath=null;
        try {
            File targetFile =new File(fileUrl+"\\express\\",uuid+".png");
            file.transferTo(targetFile);
            filePath=url+"express/"+uuid+".png";
            hzConsultAddress.setMailPdf(filePath);

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genSuccessResult(hzConsultAddressMapper.updateByPrimaryKey(hzConsultAddress)==1?filePath:null);
    }



    @GetMapping(value = "/download")
    @ResponseBody
    public void Download(HttpServletResponse response){
        String fileName="emmm.txt";
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File("C:\\Users\\YFZX-FZF-1777\\Desktop\\图片上传\\"
                    + fileName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/downloadTxt", method = RequestMethod.GET)
    public void getDownload(HttpServletRequest request, HttpServletResponse response) {
        String fullPath = "C:\\Users\\YFZX-FZF-1777\\Desktop\\图片上传\\emmm.txt";
        File downloadFile = new File(fullPath);

        ServletContext context = request.getServletContext();
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
        response.setHeader("Accept-Ranges", "bytes");
        long downloadSize = downloadFile.length();
        long fromPos = 0, toPos = 0;
        if (request.getHeader("Range") == null) {
            response.setHeader("Content-Length", downloadSize + "");
        } else {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String range = request.getHeader("Range");
            String bytes = range.replaceAll("bytes=", "");
            String[] ary = bytes.split("-");
            fromPos = Long.parseLong(ary[0]);
            if (ary.length == 2) {
                toPos = Long.parseLong(ary[1]);
            }
            int size;
            if (toPos > fromPos) {
                size = (int) (toPos - fromPos);
            } else {
                size = (int) (downloadSize - fromPos);
            }
            response.setHeader("Content-Length", size + "");
            downloadSize = size;
        }
        RandomAccessFile in = null;
        OutputStream out = null;
        try {
            in = new RandomAccessFile(downloadFile, "rw");
            if (fromPos > 0) {
                in.seek(fromPos);
            }
            int bufLen = (int) (downloadSize < 1024*1024*128 ? downloadSize : 1024*1024*128);//buflen=1Mb
            byte[] buffer = new byte[bufLen];
            int num;
            int count = 0;
            out = response.getOutputStream();
            while ((num = in.read(buffer)) != -1) {
                out.write(buffer, 0, num);
                count += num;
                if (downloadSize - count < bufLen) {
                    bufLen = (int) (downloadSize-count);
                    if(bufLen==0){
                        break;
                    }
                    buffer = new byte[bufLen];
                }
            }
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
