/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.user.save;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.User;
import dtx.rbac.controller.RoleUserController;
import dtx.rbac.controller.impl.SessionRBACController;
import dtxoa.servlet.CURDServlet;
import dtxoa.servlet.JumpServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author datouxi
 */
public class UpdateServlet extends CURDServlet {
    
    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*if(request.getParameter("newpwd")==null||request.getParameter("repwd")==null||!request.getParameter("newpwd").equals(request.getParameter("repwd"))){
            request.setAttribute(JumpServlet.MESSAGESHOWTIME, 0);
            request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }else{
            User user=SessionRBACController.getLoginInfo(request);
            user.setPassword(request.getParameter("newpwd").trim());
            user.setRemark(request.getParameter("remark").trim());
            user.setStatus(request.getParameter("ison")!=null);
            boolean result=ControllerFactory.getUserController().updateUser(user);
            String message="";
            if(result)
                message="更新用户信息成功";
            else
                message="信息用户信息失败";
            SessionRBACController.loginOut(request);
            request.setAttribute(JumpServlet.MESSAGE,message);
            request.setAttribute(JumpServlet.PAGETO, SessionRBACController.getLoginPage(request));
            request.getSession(true).setAttribute(JumpServlet.PAGEFROM, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }*/
        
        if(request.getParameter("newpwd")==null||request.getParameter("repwd")==null||!request.getParameter("newpwd").equals(request.getParameter("repwd"))||request.getParameterValues("roles")==null||request.getParameterValues("roles").length<=0){
            request.setAttribute(JumpServlet.MESSAGESHOWTIME, 0);
            request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }else{
            User user=SessionRBACController.getLoginInfo(request);
            user.setPassword(request.getParameter("newpwd").trim());
            user.setRemark(request.getParameter("remark").trim());
            user.setStatus(request.getParameter("ison")!=null);
            boolean result=ControllerFactory.getUserController().updateUser(user);
            String message="";
            if(result){
                RoleUserController ruc=ControllerFactory.getRoleUserController();
                ruc.deleteByUserId(user.getUuid());
                ruc.addRoleUsers(user.getUuid(), request.getParameterValues("roles"));
                message="更新用户信息成功";
            }
            else
                message="信息用户信息失败";
            SessionRBACController.loginOut(request);
            request.setAttribute(JumpServlet.MESSAGE,message);
            request.setAttribute(JumpServlet.PAGETO, SessionRBACController.getLoginPage(request));
            request.getSession(true).setAttribute(JumpServlet.PAGEFROM, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }
    }
}
