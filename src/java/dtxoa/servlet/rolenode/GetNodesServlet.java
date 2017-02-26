/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.rolenode;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Node;
import dtx.rbac.bean.NodeTree;
import dtx.rbac.bean.NodeTreeLeaf;
import dtx.rbac.bean.Role;
import dtx.rbac.bean.User;
import dtx.rbac.controller.RoleController;
import dtx.rbac.controller.impl.DefaultNodeControllerImpl;
import dtx.rbac.controller.impl.SessionRBACController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author datouxi
 */
public class GetNodesServlet extends HttpServlet {
    
    private String roleId="role_id";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        User loginInfo=SessionRBACController.getLoginInfo(request);
        if(!SessionRBACController.isLogin(request)){
            response.getWriter().write("请先登录");
            return;
        }
        RoleController rc=ControllerFactory.getRoleController();
        Role role=rc.getRoleById(request.getParameter(roleId));
        if(role==null)return;
        NodeTree nodeTree=null;
        if(ControllerFactory.getUserController().isAdmin(loginInfo)&&"".equals(role.getParentId())){
            List<Node> childs=ControllerFactory.getNodeController().getChilds(DefaultNodeControllerImpl.ROOTNODEID, true);
            nodeTree=new NodeTree(childs.toArray(new Node[childs.size()]), true);
        } else{
            List<Role> roles=new ArrayList<>();
            roles.add(rc.getRoleById(role.getParentId()));
            nodeTree=new NodeTree(roles, true);
        }
        JSONArray jsonArr=nodeTree.toJSON();
        List<Node> nodes=ControllerFactory.getRoleNodeController().getNodesByRole(role);
        for(Node node:nodes){
            if(!node.getStatus())continue;
            for(int i=0;i<jsonArr.length();i++){
                try {
                    JSONObject result=find(node.getUuid(), jsonArr.getJSONObject(i));
                    if(result!=null){
                        result.put(NodeTreeLeaf.SELECTED, true);
                        break;
                    }
                } catch (JSONException ex) {
                    continue;
                }
            }
        }
        response.getWriter().write(jsonArr.toString());
    }
    
    private JSONObject find(String nodeId,JSONObject jsonObj) throws JSONException{
        JSONObject result=null;
        if(jsonObj.getString(NodeTreeLeaf.NODEID).equals(nodeId)){
            result=jsonObj;
        }else{
            JSONArray arr=jsonObj.getJSONArray(NodeTreeLeaf.CHILDS);
            for(int i=0,len=arr.length();i<len;i++){
                JSONObject obj=find(nodeId, arr.getJSONObject(i));
                if(obj!=null){
                    result=obj;
                    break;
                }
            }
        }
        return result;
    }

}
