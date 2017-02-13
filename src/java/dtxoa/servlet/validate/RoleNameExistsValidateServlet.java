/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.validate;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Role;
import dtx.rbac.controller.RoleController;
import dtx.rbac.util.MapUtil;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author datouxi
 */
public class RoleNameExistsValidateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roleName=request.getParameter("rolename");
        boolean result=false;
        if(roleName==null){
            result=true;
        }else{
            roleName=roleName.trim();
            RoleController rc=ControllerFactory.getRoleController();
            List<Role> roleList=MapUtil.toList(rc.getAllRoles());
            for(Role role:roleList){
                if(roleName.equals(role.getRoleName())){
                    result=true;
                    break;
                }
            }
        }
        JSONObject json=new JSONObject();
        try {
            json.put("valid", !result);
        } catch (JSONException ex) {
            Logger.getLogger(RoleNameExistsValidateServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.getWriter().write(json.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    
}
