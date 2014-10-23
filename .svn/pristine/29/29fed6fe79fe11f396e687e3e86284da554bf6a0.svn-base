/**   
*    
* @project_name cas-server  
* @Title ImageVaditeAuthenticationViaFormAction.java  
* @Package org.jasig.cas.authentication.handler 
* @author  songxl
* @date 2014-9-26 下午03:26:00 
* @version v1.0 2014-9-26 下午03:26:00
* @history 历史修改记录 
* <作者>　          <日期>　　                <版本>　  <描述> 
*  songxl　　　2014-9-26 下午03:26:00　　V1.0　　 build此模块  
*/ 

package org.jasig.cas.authentication.handler;
                                     
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
                                     
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.execution.RequestContext;
                                     
/**
 * 包含验证码
 * Action to authenticate credentials and retrieve a TicketGrantingTicket for those credentials. If there is a request for renew, then it also generates the Service Ticket required.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public class ImageVaditeAuthenticationViaFormAction {
                                     
    // 验证码参数：
    //private String code = "code";
                                     
    /**
     * Binder that allows additional binding of form object beyond Spring defaults.
     */
    private CredentialsBinder credentialsBinder;
                                     
    /** Core we delegate to for handling all ticket related tasks. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;
                                     
    @NotNull
    private CookieGenerator warnCookieGenerator;
                                     
    protected Logger logger = LoggerFactory.getLogger(getClass());
                                     
    public final void doBind(final RequestContext context, final Credentials credentials) throws Exception {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
                                     
        if (this.credentialsBinder != null && this.credentialsBinder.supports(credentials.getClass())) {
            this.credentialsBinder.bind(request, credentials);
        }
    }
    
    //使用统一认证登录页面，并增加了验证码
    public final String submit(final RequestContext context, final Credentials credentials, final MessageContext messageContext) throws Exception {
                                     
        // 检测验证码
        if (credentials instanceof CaptchaImageLoginCredentials) {
            // 这个类也是我们自己搞的，里面能取到验证码
            CaptchaImageLoginCredentials rmupc = (CaptchaImageLoginCredentials) credentials;
            // 从session中取出生成验证码的时候就保存在session中的验证码
            String sessionCode = (String) WebUtils.getHttpServletRequest(context).getSession().getAttribute("code");
                                     
            // 如果验证码为null
            if (rmupc.getCode() == null || "".equals(rmupc.getCode())) {
                // 写入日志
                logger.warn("验证码为空");
                // 错误信息，会在配置文件（messages_zh_CN.properties）里面先定义好
                final String code = "login.code.tip";
                // 发送错误信息到前台
                messageContext.addMessage(new MessageBuilder().error().code(code).arg("").defaultText(code).build());
                return "error";
            }
            // 如果验证码不正确
            if (!rmupc.getCode().toUpperCase().equals(sessionCode.toUpperCase())) {
                logger.warn("验证码检验有误");
                final String code = "login.code.error";
                messageContext.addMessage(new MessageBuilder().error().code(code).arg("").defaultText(code).build());
                return "error";
            }
                                     
        }
                                     
        // Validate login ticket
        final String authoritativeLoginTicket = WebUtils.getLoginTicketFromFlowScope(context);
        final String providedLoginTicket = WebUtils.getLoginTicketFromRequest(context);
        if (!authoritativeLoginTicket.equals(providedLoginTicket)) {
            this.logger.warn("Invalid login ticket " + providedLoginTicket);
            final String code = "INVALID_TICKET";
            messageContext.addMessage(new MessageBuilder().error().code(code).arg(providedLoginTicket).defaultText(code).build());
            return "error";
        }
                                     
        final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
        final Service service = WebUtils.getService(context);
        if (StringUtils.hasText(context.getRequestParameters().get("renew")) && ticketGrantingTicketId != null && service != null) {
                                     
            try {
                final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service, credentials);
                WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);
                putWarnCookieIfRequestParameterPresent(context);
                return "warn";
            } catch (final TicketException e) {
                if (isCauseAuthenticationException(e)) {
                    populateErrorsInstance(e, messageContext);
                    return getAuthenticationExceptionEventId(e);
                }
                                     
                this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempted to generate a ServiceTicket using renew=true with different credentials", e);
                }
            }
        }
                                     
        try {
            WebUtils.putTicketGrantingTicketInRequestScope(context, this.centralAuthenticationService.createTicketGrantingTicket(credentials));
            putWarnCookieIfRequestParameterPresent(context);
            return "success";
        } catch (final TicketException e) {
            populateErrorsInstance(e, messageContext);
            if (isCauseAuthenticationException(e))
                return getAuthenticationExceptionEventId(e);
            return "error";
        }
    }
    
    //songxl 
    //客户端有自定义的登录页面
    public final String submit(final RequestContext context, final MessageContext messageContext) throws Exception {
        // Validate login ticket
    	System.out.println("AuthenticationViaFormAction   submit=====包含验证码======");
    	final String authoritativeLoginTicket = WebUtils.getLoginTicketFromFlowScope(context);
        final String providedLoginTicket = WebUtils.getLoginTicketFromRequest(context);
        if (!authoritativeLoginTicket.equals(providedLoginTicket)) {
            this.logger.warn("Invalid login ticket " + providedLoginTicket);
            final String code = "INVALID_TICKET";
            messageContext.addMessage(new MessageBuilder().error().code(code).arg(providedLoginTicket).defaultText(code).build());
            logger.info("=========INVALID_TICKET");
            return "error";
        }
        final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
        final Service service = WebUtils.getService(context);
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        //org.jasig.cas.authentication.principal.UsernamePasswordCredentials credentials = new org.jasig.cas.authentication.principal.UsernamePasswordCredentials();
        org.jasig.cas.authentication.handler.CaptchaImageLoginCredentials credentials = new org.jasig.cas.authentication.handler.CaptchaImageLoginCredentials();
        credentials.setPassword(request.getParameter("password"));
        credentials.setUsername(request.getParameter("username"));
        credentials.setCode(request.getParameter("code"));//验证码
        
        //如果把验证码放在客户端生成，存入session，那在这里取不到session中的code，
        //所以客户端自定义登录页面中的code使用统一认证系统生成
        //从统一认证中获取验证码 springmvn_cas_client1项目中使用
        String sessionCode1 = (String) request.getSession().getAttribute("code");//
        
        //从客户端获取验证码   mybatisweb_cas_client1项目中使用
        String sessionCode2 = request.getParameter("codeSession");
        
        String sessionCode = sessionCode2!=null && !"".equals(sessionCode2)?sessionCode2:sessionCode1;
        // 如果验证码为null
        if (credentials.getCode() == null || "".equals(credentials.getCode())) {
            // 写入日志
            logger.warn("验证码为空");
            // 错误信息，会在配置文件（messages_zh_CN.properties）里面先定义好
            final String code = "login.code.tip";
            // 发送错误信息到前台
            messageContext.addMessage(new MessageBuilder().error().code(code).arg("").defaultText(code).build());
            request.setAttribute("remoteLoginMessage",code);
            return "error";
        }
        // 如果验证码不正确
        if (!credentials.getCode().toUpperCase().equals(sessionCode.toUpperCase())) {
            logger.warn("验证码检验有误");
            final String code = "login.code.error";
            messageContext.addMessage(new MessageBuilder().error().code(code).arg("").defaultText(code).build());
            request.setAttribute("remoteLoginMessage",code);
            return "error";
        }
        
        if (StringUtils.hasText(context.getRequestParameters().get("renew")) && ticketGrantingTicketId != null && service != null) {
            try {
                final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service, credentials);
                WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);
                putWarnCookieIfRequestParameterPresent(context);
                return "warn";
            } catch (final TicketException e) {
                if (e.getCause() != null && AuthenticationException.class.isAssignableFrom(e.getCause().getClass())) {
                    populateErrorsInstance(e, messageContext);
                    //songxl add
                    request.setAttribute("remoteLoginMessage", e.getMessage());
                    logger.info("=========TicketException1=="+e.getMessage());
                    return "error";
                }
                this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempted to generate a ServiceTicket using renew=true with different credentials", e);
                }
            }
        }
        try {
            WebUtils.putTicketGrantingTicketInRequestScope(context, this.centralAuthenticationService.createTicketGrantingTicket(credentials));
            putWarnCookieIfRequestParameterPresent(context);
            return "success";
        } catch (final TicketException e) {
            populateErrorsInstance(e, messageContext);
            //songxl add
            request.setAttribute("remoteLoginMessage", e.getMessage());
            logger.info("=========TicketException2=="+e.getMessage()+",==="+messageContext);
            return "error";
        }
    }

    
    private void populateErrorsInstance(final TicketException e, final MessageContext messageContext) {
                                     
        try {
            messageContext.addMessage(new MessageBuilder().error().code(e.getCode()).defaultText(e.getCode()).build());
        } catch (final Exception fe) {
            logger.error(fe.getMessage(), fe);
        }
    }
                                     
    private void putWarnCookieIfRequestParameterPresent(final RequestContext context) {
        final HttpServletResponse response = WebUtils.getHttpServletResponse(context);
                                     
        if (StringUtils.hasText(context.getExternalContext().getRequestParameterMap().get("warn"))) {
            this.warnCookieGenerator.addCookie(response, "true");
        } else {
            this.warnCookieGenerator.removeCookie(response);
        }
    }
                                     
    private AuthenticationException getAuthenticationExceptionAsCause(final TicketException e) {
        return (AuthenticationException) e.getCause();
    }
                                     
    private String getAuthenticationExceptionEventId(final TicketException e) {
        final AuthenticationException authEx = getAuthenticationExceptionAsCause(e);
                                     
        if (this.logger.isDebugEnabled())
            this.logger.debug("An authentication error has occurred. Returning the event id " + authEx.getType());
                                     
        return authEx.getType();
    }
                                     
    private boolean isCauseAuthenticationException(final TicketException e) {
        return e.getCause() != null && AuthenticationException.class.isAssignableFrom(e.getCause().getClass());
    }
                                     
    public final void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
                                     
    /**
     * Set a CredentialsBinder for additional binding of the HttpServletRequest to the Credentials instance, beyond our default binding of the Credentials as a Form Object in Spring WebMVC parlance. By the time we invoke this CredentialsBinder, we have already engaged in default binding such that for each HttpServletRequest parameter, if there was a JavaBean property of the Credentials implementation of the same name, we have set that property to be the value of the corresponding request parameter. This CredentialsBinder plugin point exists to allow consideration of things other than HttpServletRequest parameters in populating the Credentials (or more sophisticated consideration of the HttpServletRequest parameters).
     * 
     * @param credentialsBinder
     *            the credentials binder to set.
     */
    public final void setCredentialsBinder(final CredentialsBinder credentialsBinder) {
        this.credentialsBinder = credentialsBinder;
    }
                                     
    public final void setWarnCookieGenerator(final CookieGenerator warnCookieGenerator) {
        this.warnCookieGenerator = warnCookieGenerator;
    }
}
