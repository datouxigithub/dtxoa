/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.validate;

import dtx.rbac.bean.User;
import dtx.rbac.controller.impl.SessionRBACController;
import dtx.rbac.util.StringUtil;
import java.io.IOException;
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
public class PwdIscorrectServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldPwd=request.getParameter("oldpwd").trim();
        JSONObject json=new JSONObject();
        boolean result=false;
        User user=SessionRBACController.getLoginInfo(request);
        if(user.getPassword().equals(StringUtil.getMD5String(oldPwd)))result=true;
        try {
            json.put("valid", result);
        } catch (JSONException ex) {
            Logger.getLogger(PwdIscorrectServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.getWriter().write(json.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
