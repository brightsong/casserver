/**   
*    
* @project_name cas-server  
* @Title CaptchaImageLoginCredentials.java  
* @Package org.jasig.cas.authentication.handler 
* @author  songxl
* @date 2014-9-26 下午03:26:41 
* @version v1.0 2014-9-26 下午03:26:41
* @history 历史修改记录 
* <作者>　          <日期>　　                <版本>　  <描述> 
*  songxl　　　2014-9-26 下午03:26:41　　V1.0　　 build此模块  
*/ 

package org.jasig.cas.authentication.handler;

               
import java.util.Map;
                   
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
                   
import org.jasig.cas.authentication.principal.RememberMeUsernamePasswordCredentials;
                   
/**
 * ClassName:CaptchaImageLoginCredentials <br/> 
 * 改造的org.jasig.cas.authentication.principal.UsernamePasswordCredentials.java，
 * 只是增加了一个private String code;
 * @author Administrator
 * @version
 * @since JDK 1.5
 * @see
 */
public class CaptchaImageLoginCredentials extends RememberMeUsernamePasswordCredentials {
    private static final long serialVersionUID = 1L;
                   
    private Map<String, Object> param;
                   
    /** The username. */
    @NotNull
    @Size(min = 1, message = "验证码为空")
    private String code;
                   
    public String getCode() {
        return code;
    }
                   
    public void setCode(String code) {
        this.code = code;
    }
                   
    public Map<String, Object> getParam() {
        return param;
    }
                   
    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}

