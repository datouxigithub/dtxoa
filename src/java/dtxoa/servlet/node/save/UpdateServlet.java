/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.node.save;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Node;
import dtx.rbac.controller.impl.SessionRBACController;
import dtxoa.servlet.CURDServlet;
import dtxoa.servlet.JumpServlet;
import dtxoa.servlet.node.EditServlet;
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
        Node node=new Node();
        node.setUuid(request.getParameter(EditServlet.NODEID));
        node.setTitle(request.getParameter("title").trim());
        node.setAddress(request.getParameter("address").trim());
        node.setNodeType(Integer.valueOf(request.getParameter("type")));
        node.setParentId(request.getParameter("parent"));
        node.setStatus(request.getParameter("ison")!=null);
        node.setRemark(request.getParameter("remark"));
        boolean result=ControllerFactory.getNodeController().updateNode(node);
        String message="更新结点";
        if(result)
            message="更新结点";
        request.setAttribute(JumpServlet.MESSAGE, message);
        request.setAttribute(JumpServlet.PAGETO, request.getHeader("referer"));
        SessionRBACController.jump(request, response);
    }

}
