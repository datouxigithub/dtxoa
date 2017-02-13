/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.user;

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
public class AddUser extends ValidateServlet {

    @Override
    protected Template handleRquestCallBack(HttpServletRequest request, HttpServletResponse response, Context context) {
        context.put("action", SessionRBACController.getPageAddress("/user/save-add", request));
        context.put("roles", SessionRBACController.getRoleChilds(request));
        return getTemplate("user.html");
    }
    
}
