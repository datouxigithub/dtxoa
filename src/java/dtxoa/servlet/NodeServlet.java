/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Node;
import dtx.rbac.controller.NodeController;
import dtx.rbac.controller.impl.SessionRBACController;
import java.io.IOException;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author datouxi
 */
public class NodeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameterMap().entrySet().size()<=0)return;
        String nodeId=(String) ((Entry)request.getParameterMap().entrySet().iterator().next()).getKey();
        NodeController nc=ControllerFactory.getNodeController();
        Node node=nc.getNodeById(nodeId);
        if(node!=null){
            request.setAttribute(JumpServlet.MESSAGESHOWTIME, 0);
            request.setAttribute(JumpServlet.PAGETO, node.getAddress());
            SessionRBACController.jump(request, response);
        }else{
            response.sendRedirect(SessionRBACController.getDefaultPage(request));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
