/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.node;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Node;
import dtx.rbac.controller.NodeController;
import dtx.rbac.controller.impl.SessionRBACController;
import dtxoa.servlet.JumpServlet;
import dtxoa.servlet.ValidateServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

/**
 *
 * @author datouxi
 */
public class EditServlet extends ValidateServlet {

    public final static String NODEID="node-id";
    
    @Override
    protected Template handleRquestCallBack(HttpServletRequest request, HttpServletResponse response, Context context) {
        NodeController nc=ControllerFactory.getNodeController();
        Node node=nc.getNodeById(request.getParameter(EditServlet.NODEID));
        if(node==null){
            request.setAttribute(JumpServlet.PAGETO, SessionRBACController.getDefaultPage(request));
            request.setAttribute(JumpServlet.MESSAGESHOWTIME, 0);
            SessionRBACController.jump(request, response);
        }
        context.put("action", SessionRBACController.getPageAddress("/node/save-edit?"+NODEID+"="+node.getUuid(), request));
        context.put("nodeTypes", nc.getNodeTypes());
        context.put("parents", nc.getAllChildsByType("", Node.NODETYPEGROUP));
        context.put("editNode", node);
        return getTemplate("node.html");
    }

}
