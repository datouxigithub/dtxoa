/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet.rolenode;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Node;
import dtx.rbac.bean.Role;
import dtx.rbac.bean.RoleNode;
import dtx.rbac.bean.User;
import dtx.rbac.controller.NodeController;
import dtx.rbac.controller.RoleNodeController;
import dtx.rbac.controller.impl.SessionRBACController;
import dtx.rbac.util.MapUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author datouxi
 */
public class GetNodesServlet1 extends HttpServlet {
    
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
        RoleNodeController rnc=ControllerFactory.getRoleNodeController();
        NodeController nc=ControllerFactory.getNodeController();
        
        Role role=ControllerFactory.getRoleController().getRoleById(request.getParameter(roleId));
        
        List<RoleNode> rnList=rnc.queryByRoleId(role.getUuid());
        
        List<Node> nodes=null;
        if(ControllerFactory.getUserController().isAdmin(loginInfo)&&role!=null&&"".equals(role.getParentId())){
            nodes=MapUtil.toList(nc.getAllNodes(true));
        } else{
            nodes=new ArrayList<>();
            List<RoleNode> parentRNList=rnc.queryByRoleId(role.getParentId());
            for(RoleNode rn:parentRNList){
                Node node=nc.getNodeById(rn.getNodeId());
                if(node.getStatus())
                    nodes.add(node);
            }
        }

        Map result=new LinkedHashMap();
        while(!nodes.isEmpty()){
            Iterator<Node> iter=nodes.iterator();
            Map childs=new LinkedHashMap();
            Node parent=null;
            String parentId=null;
            while(iter.hasNext()){
                Node node=iter.next();
                if(childs.isEmpty()||parentId.equals(node.getParentId())){
                    Map valueMap=new HashMap();
                    valueMap.put("title", node.getTitle());
                    valueMap.put("parentId", node.getParentId());
                    for(RoleNode roleNode:rnList){
                        if(node.getUuid().equals(roleNode.getNodeId())){
                            valueMap.put("selected", true);
                            break;
                        }
                    }
                    valueMap.put("childs", new HashMap());
                    childs.put(node.getUuid(), valueMap);
                    parentId=parentId==null ? node.getParentId():parentId;
                    iter.remove();
                }else{
                    if(parentId.equals(node.getUuid())){
                        parent=node;
                        iter.remove();
                    }
                }
            }
            //将父子关系添加到result
            Iterator<String> nodeIter=result.keySet().iterator();
            while(nodeIter.hasNext()){
                String uuid=nodeIter.next();
                Node parentNode=nc.getNodeById(nc.getParentId(uuid));
                if(parentNode==null)continue;
                if(childs.containsKey(parentNode.getUuid())){
                    if(insertNode(childs, parentNode.getUuid(), uuid, (Map) result.get(uuid)))
                        iter.remove();
                }
            }
            if(parent!=null&&!insertNode(result, nc.getNodeById(parent.getParentId()).getUuid(), parent.getUuid(), childs)){
                Map valueMap=new HashMap();
                valueMap.put("title", parent.getTitle());
                valueMap.put("parentId", parent.getParentId());
                valueMap.put("childs", childs);
                result.put(parent.getUuid(), valueMap);
            }else{
                Iterator uuidIter=childs.keySet().iterator();
                while(uuidIter.hasNext()){
                    String uuid=(String) uuidIter.next();
                    if(!insertNode(result, parentId, uuid, (Map) childs.get(uuid)))
                        result.put(uuid, childs.get(uuid));
                }
            }
        }
        response.getWriter().write(new JSONObject(result).toString());
    }
    
    private boolean containUUID(Map map,String uuid){
        if(map.containsKey(uuid))return true;
        else{
            Iterator iter=map.keySet().iterator();
            while(iter.hasNext()){
                Map subMap=(Map) ((Map)map.get(iter.next())).get("childs");
                if(containUUID(subMap,uuid))
                    return true;
            }
        }
        return false;
    }
    
    private boolean insertNode(Map map,String posUUID,String keyUUID,Map valueMap){
        if(!containUUID(map, posUUID))return false;
        if(map.get(posUUID)!=null){
            ((Map)((Map)map.get(posUUID)).get("childs")).put(keyUUID, valueMap);
            return true;
        }
        Iterator iter=map.keySet().iterator();
        while(iter.hasNext()){
            if(insertNode((Map) ((Map) map.get(iter.next())).get("childs"), posUUID, keyUUID, valueMap))
                return true;
        }
        return false;
    }

}
