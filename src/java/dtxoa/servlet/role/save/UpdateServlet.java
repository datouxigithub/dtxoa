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
import dtxoa.servlet.role.EditServlet;
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
        Role role=new Role();
        role.setUuid(request.getParameter(EditServlet.ROLEID));
        role.setRoleName(request.getParameter("role-name"));
        role.setRemark(request.getParameter("remark"));
        role.setParentId(request.getParameter("parent")==null ? "":request.getParameter("parent"));
        role.setStatus(request.getParameter("ison")!=null);
        boolean result=ControllerFactory.getRoleController().updateRole(role);
        String message="更新角色成功";
        if(!result)
            message="更新角色失败";
        request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
        request.setAttribute(JumpServlet.MESSAGE, message);
        SessionRBACController.jump(request, response);
    }

}
