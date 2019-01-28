package com.example.blds.controller.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Api(tags="【公共】【图形验证码】")
@RequestMapping("/code")
@Controller
@ResponseBody
@ApiIgnore
public class CodeController {

    private int width = 110;//定义图片的width
    private int height = 34;//定义图片的height
    private int codeCount = 4;//定义图片上显示验证码的个数
    private int xx = 18;
    private int fontHeight = 25;
    private int codeY = 28;
    @ApiOperation(value="【生成图形验证码】")
    @RequestMapping(method = RequestMethod.GET, value="code.htm")
    public void imageCode(
            @ApiParam(name = "code", value="传入随机数,例如SDRG", required = true) @RequestParam(value = "code") String code,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        int width = this.width;
        int height = this.height;
        //String type = request.getParameter("type");
        //String codeString = request.getParameter("code");
        //String codeString = code;

        if (StringUtils.isNotBlank(request.getParameter("width"))) {
            width = Integer.parseInt(request.getParameter("width"));
        }

        if (StringUtils.isNotBlank(request.getParameter("height"))) {
            height = Integer.parseInt(request.getParameter("height"));
        }

        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Graphics2D gd = buffImg.createGraphics();
        //Graphics2D gd = (Graphics2D) buffImg.getGraphics();
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);

        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);

        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 32; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;

        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            String codeString = code.substring(i, i + 1);
            //产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);

            //用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(codeString, 10 + i * xx, codeY);

            //将产生的四个随机数组合在一起。
            randomCode.append(codeString);
        }

        // 将四位数字的验证码保存到Session中。
        /*HttpSession session = request.getSession();
        LOG.info(randomCode);
        if(StringUtils.isNotBlank(type)) {
            switch (type) {
                case "1":
                    session.setAttribute(Constants.CODE_LOGIN_IMAGE, randomCode.toString());
                    break;
                default:
                    break;
            }
        }*/

        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
        /*ResultBean<String> resultBean = new ResultBean<String>();
        resultBean.setRetCode(Enumeration.RET_CODE.SUCCESS);
        resultBean.setRetData("/code/code.htm?code=" + code);
        return  resultBean;*/
    }
}
