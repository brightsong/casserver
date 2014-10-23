/**
 * Project Name:casServerHandler
 * File Name:CaptchaImageCreateController.java
 * Package Name:org.jasig.cas.authentication.handler.captchaImage
 * Date:2013-4-28下午03:04:06
 * Copyright (c) 2013, riambsoft All Rights Reserved.
 *
 */
                                                  
package org.jasig.cas.authentication.handler.captchaImage;
import java.io.IOException;
                                                  
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
                                                  
import org.jasig.cas.authentication.handler.util.ValidatorCodeUtil;
import org.jasig.cas.authentication.handler.util.ValidatorCodeUtil.ValidatorCode;
                                                  
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
                                                  
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
// import com.wokejia.flow.ValidatorCodeUtil.ValidatorCode;
                                                  
public class CaptchaImageCreateController implements Controller, InitializingBean {
                                                      
    public void afterPropertiesSet() throws Exception {
                                                          
    }
                                                  
    public ModelAndView handleRequest(HttpServletRequest arg0,
            HttpServletResponse response) throws Exception {
        ValidatorCode codeUtil = ValidatorCodeUtil.getCode();
        System.out.println("code="+codeUtil.getCode());
                                                          
        arg0.getSession().setAttribute("code", codeUtil.getCode());
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
                                                  
        ServletOutputStream sos = null;
        try {
            // 将图像输出到Servlet输出流中。
            sos = response.getOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(sos);
            encoder.encode(codeUtil.getImage()); 
            sos.flush();
            sos.close();
        } catch (Exception e) {
        } finally {
            if (null != sos) {
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
