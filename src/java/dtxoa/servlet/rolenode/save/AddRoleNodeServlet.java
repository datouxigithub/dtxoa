/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.rolenode.save;

import dtx.db.ControllerFactory;
import dtx.rbac.controller.RoleNodeController;
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
public class AddRoleNodeServlet extends CURDServlet {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roleId=request.getParameter("roles");
        String[] nodes=request.getParameterValues("nodes");
        if(roleId==null||"".equals(roleId)||nodes==null||nodes.length<=0){
            request.setAttribute(JumpServlet.MESSAGESHOWTIME, 0);
            request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }else{
            RoleNodeController rnc=ControllerFactory.getRoleNodeController();
            if(rnc.deleteByRoleId(roleId)){
                for(String nodeId:nodes){
                    rnc.addRoleNode(nodeId, roleId);
                }
                request.setAttribute(JumpServlet.MESSAGE, "添加角色权限成功!");
            }else
                request.setAttribute(JumpServlet.MESSAGE, "添加角色权限失败!");
            request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
            SessionRBACController.jump(request, response);
        }
    }
}
