/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.role;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Role;
import dtx.rbac.controller.impl.DefaultRoleControllerImpl;
import dtx.rbac.controller.impl.SessionRBACController;
import dtx.rbac.util.MapUtil;
import dtxoa.servlet.JumpServlet;
import dtxoa.servlet.ValidateServlet;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

/**
 *
 * @author datouxi
 */
public class EditServlet extends ValidateServlet {

    public final static String ROLEID="role-id";
    
    @Override
    protected Template handleRquestCallBack(HttpServletRequest request, HttpServletResponse response, Context context) {
        DefaultRoleControllerImpl rc=(DefaultRoleControllerImpl) ControllerFactory.getRoleController();
        Role role=rc.getRoleById(request.getParameter(ROLEID));
        if(role==null){
            request.setAttribute(JumpServlet.PAGETO, SessionRBACController.getDefaultPage(request));
            request.setAttribute(JumpServlet.MESSAGESHOWTIME, 0);
            SessionRBACController.jump(request, response);
        }
        context.put("action", SessionRBACController.getPageAddress("/role/save-edit?"+ROLEID+"="+role.getUuid(), request));
        Map parents=rc.getAllRoles();
        MapUtil.removeObject(parents, role);
        context.put("parents", parents);
        context.put("editRole", role);
        return getTemplate("role.html");
    }

}
