/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.role;

import dtx.db.ControllerFactory;
import dtx.rbac.controller.impl.DefaultRoleControllerImpl;
import dtx.rbac.controller.impl.SessionRBACController;
import dtxoa.servlet.ValidateServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

/**
 *
 * @author datouxi
 */
public class AddServlet extends ValidateServlet {

    @Override
    protected Template handleRquestCallBack(HttpServletRequest request, HttpServletResponse response, Context context) {
        context.put("action", SessionRBACController.getPageAddress("/role/save-add", request));
        DefaultRoleControllerImpl rc=(DefaultRoleControllerImpl) ControllerFactory.getRoleController();
        context.put("parents", rc.getAllRoles());
        return getTemplate("role.html");
    }

}
