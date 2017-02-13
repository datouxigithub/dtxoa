/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.validate;

import dtx.db.ControllerFactory;
import dtx.rbac.controller.impl.DefaultNodeControllerImpl;
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
public class NodeAddressExistsValidateServlet extends HttpServlet {

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String address=request.getParameter("address");
        if(address==null)return;
        DefaultNodeControllerImpl nc=(DefaultNodeControllerImpl) ControllerFactory.getNodeController();
        JSONObject json=new JSONObject();
        boolean result=!nc.nodeAddressIsExists(address.trim());
        try {
            json.put("valid", result);
        } catch (JSONException ex) {
            Logger.getLogger(NodeTitleExistsValidateServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.getWriter().write(json.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
