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
import dtx.rbac.controller.NodeController;
import dtx.rbac.controller.RoleNodeController;
import java.io.IOException;
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
        RoleNodeController rnc=ControllerFactory.getRoleNodeController();
        NodeController nc=ControllerFactory.getNodeController();
        List<RoleNode> rnList=rnc.queryByRoleId(request.getParameter(roleId));
        Map result=new LinkedHashMap();
        while(!rnList.isEmpty()){
            Iterator<RoleNode> iter=rnList.iterator();
            Map childs=new LinkedHashMap();
            Node parent=null;
            String parentId=null;
            while(iter.hasNext()){
                RoleNode rn=iter.next();
                if(childs.isEmpty()||parentId.equals(nc.getParentId(rn.getNodeId()))){
                    Map valueMap=new HashMap();
                    Node node=nc.getNodeById(rn.getNodeId());
                    valueMap.put("title", node.getTitle());
                    valueMap.put("parentId", node.getParentId());
                    valueMap.put("childs", new HashMap());
                    childs.put(node.getUuid(), valueMap);
                    parentId=parentId==null ? nc.getParentId(rn.getNodeId()):parentId;
                    iter.remove();
                }else{
                    if(parentId.equals(rn.getNodeId())){
                        parent=nc.getNodeById(rn.getNodeId());
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
                Map subMap=(Map) ((Map) map.get(iter.next())).get("childs");
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
            if(insertNode((Map) map.get(iter.next()), posUUID, keyUUID, valueMap))
                return true;
        }
        return false;
    }

}
