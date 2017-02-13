/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.user;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.User;
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
public class EditUser extends ValidateServlet {

    @Override
    protected Template handleRquestCallBack(HttpServletRequest request, HttpServletResponse response, Context context) {
        context.put("action", SessionRBACController.getPageAddress("/user/save-edit", request));
        User loginInfo=SessionRBACController.getLoginInfo(request);
        context.put("account", loginInfo.getAccount());
        context.put("status", String.valueOf(loginInfo.getStatus()));
        context.put("remark", loginInfo.getRemark());
        context.put("roles", SessionRBACController.getRoleChilds(request));
        context.put("select_roles", ControllerFactory.getRoleUserController().queryByUserId(loginInfo.getUuid()));
        return getTemplate("user.html");
    }

}
