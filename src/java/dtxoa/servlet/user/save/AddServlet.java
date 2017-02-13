/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.user.save;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.User;
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
public class AddServlet extends CURDServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("newpwd")==null||request.getParameter("repwd")==null||!request.getParameter("newpwd").equals(request.getParameter("repwd"))||request.getParameterValues("roles")==null||request.getParameterValues("roles").length<=0){
            request.setAttribute(JumpServlet.MESSAGESHOWTIME, 0);
            request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }else{
            User user=new User();
            user.setAccount(request.getParameter("username").trim());
            user.setPassword(request.getParameter("newpwd"));
            user.setRemark(request.getParameter("remark"));
            user.setStatus(request.getParameter("ison")!=null);
            String result=ControllerFactory.getUserController().addUser(user);
            String message="";
            if(result!=null){
                ControllerFactory.getRoleUserController().addRoleUsers(result, request.getParameterValues("roles"));
                message=String.format("注册用户成功,ID号%s", result);
            }
            else
                message="注册用户失败";
            request.setAttribute(JumpServlet.MESSAGE,message);
            request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }
    }
    
}
