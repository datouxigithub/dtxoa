/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet;

import dtx.rbac.controller.impl.SessionRBACController;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

/**
 *
 * @author datouxi
 */
public class CheckLoginServlet extends VelocityViewServlet {

    @Override
    protected Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context ctx) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CheckLoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean result=SessionRBACController.login(request.getParameter("username"), request.getParameter("userpwd"), request);
        if(!result){
            String msg="用户名或密码错误,请检查！";
            ctx.put("msg", msg);
            return getTemplate("login.html");
        }else{
            String url=SessionRBACController.getDefaultPage(request);
            if(request.getSession(false).getAttribute(JumpServlet.PAGEFROM)!=null)
                url=(String) request.getSession(false).getAttribute(JumpServlet.PAGEFROM);
            request.setAttribute(JumpServlet.MESSAGE, "登录成功!");
            request.setAttribute(JumpServlet.PAGETO, url);
            SessionRBACController.jump(request, response);
            return getTemplate("login.html");
        }
    }

}
