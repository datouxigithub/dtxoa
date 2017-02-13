/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.role.save;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Role;
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
        if(request.getParameter("rolename")==null||"".equals(request.getParameter("rolename").trim())){
            request.setAttribute(JumpServlet.MESSAGESHOWTIME, 0);
            request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }else{
            Role role=new Role();
            role.setRoleName(request.getParameter("rolename").trim());
            role.setParentId(request.getParameter("parent")==null ? "":request.getParameter("parent"));
            role.setRemark(request.getParameter("remark"));
            role.setStatus(request.getParameter("ison")!=null);
            String uuid=ControllerFactory.getRoleController().addRole(role);
            String message="添加角色失败";
            if(uuid!=null)
                message=String.format("添加角色成功，角色号%s", uuid);
            request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
            request.setAttribute(JumpServlet.MESSAGE, message);
            SessionRBACController.jump(request, response);
        }
    }

}
