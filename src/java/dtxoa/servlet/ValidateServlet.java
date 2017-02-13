/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet;

import dtx.rbac.controller.impl.SessionRBACController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

/**
 *
 * @author datouxi
 */
public abstract class ValidateServlet extends VelocityViewServlet{

    @Override
    protected Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context ctx) {
        //用户是否已经登录，如果已经登录，判断是否有权限进行此项操作
        if(!SessionRBACController.isLogin(request)){
            if(request.getQueryString()==null)
                request.getSession(false).setAttribute(JumpServlet.PAGEFROM, SessionRBACController.getPageAddress(request.getServletPath(), request));
            else
                request.getSession(false).setAttribute(JumpServlet.PAGEFROM, SessionRBACController.getPageAddress(request.getServletPath()+"?"+request.getQueryString(), request));
            request.setAttribute(JumpServlet.MESSAGE, "请先登录再进行相应操作!");
            request.setAttribute(JumpServlet.PAGETO, SessionRBACController.getLoginPage(request));
            SessionRBACController.jump(request, response);
            return getTemplate("login.html");
        }
        if(!SessionRBACController.accessDecision((String) request.getAttribute(SessionRBACController.NODEID), request)){
            request.setAttribute(JumpServlet.MESSAGE, "没有进行相应操作!");
            request.setAttribute(JumpServlet.PAGETO, SessionRBACController.getDefaultPage(request));
            SessionRBACController.jump(request, response);
            return getTemplate("login.html");
        } else
            return handleRquestCallBack(request, response, ctx);
    }
    
    protected abstract Template handleRquestCallBack(HttpServletRequest request,HttpServletResponse response,Context context);
    
}
