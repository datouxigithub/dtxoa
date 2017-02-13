package dtx.rbac.controller.impl;

import java.util.Iterator;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Node;
import dtx.rbac.bean.RoleNode;
import dtx.rbac.bean.RoleUser;
import dtx.rbac.bean.User;
import dtx.rbac.controller.NodeController;
import dtx.rbac.controller.RBACController;
import dtx.rbac.controller.RoleController;
import dtx.rbac.controller.RoleNodeController;
import dtx.rbac.controller.RoleUserController;
import dtx.rbac.controller.UserController;
import dtx.rbac.util.MapUtil;
import dtx.rbac.util.StringUtil;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DefaultRBACControllerImpl implements RBACController {

    private Map accessMap,roleChilds;
    private User loginInfo;
    private final RoleController roleController=ControllerFactory.getRoleController();
    private final NodeController nodeController=ControllerFactory.getNodeController();
    private final RoleUserController roleUserController=ControllerFactory.getRoleUserController();
    private final RoleNodeController roleNodeController=ControllerFactory.getRoleNodeController();
    private final UserController userController=ControllerFactory.getUserController();

    @Override
    public boolean authenticate(String account, String password) {
        loginInfo=null;
        User user=userController.getUserByAccount(account);
        if(user==null)
            return false;
        if(!user.getPassword().equals(StringUtil.getMD5String(password)))
            return false;
        loginInfo=user;
        saveAccessList();
        return true;
    }

    /*
    @Override
    public boolean accessDecision(String nodeId) {
        if(!isLogin())return false;
        if(userController.isAdmin(loginInfo))return true;
        Iterator<Node> iter=accessList.iterator();
        while(iter.hasNext()){
            if(iter.next().getUuid().equals(nodeId))
                return true;
        }
        return false;
    }
    */
    
    @Override
    public boolean accessDecision(String nodeId) {
        if(!isLogin())return false;
        if(userController.isAdmin(loginInfo))return true;
        Node node=new Node();
        node.setUuid(nodeId);
        try{
            return MapUtil.containObject(accessMap, node);
        }catch(NullPointerException e){
            return false;
        }
    }

    @Override
    public boolean accessDecision(Node node) {
        return accessDecision(node.getUuid());
    }

    /*
    @Override
    public List<Node> getAccessList() {
        if(!isLogin())return null;
        return accessList;
    }
    */
    public Map getAccessList(){
        if(!isLogin())return null;
        return accessMap;
    }

    @Override
    public boolean isLogin() {
        return loginInfo!=null;
    }

    
    /*
    @Override
    public void saveAccessList() {
        if(!isLogin())return;
        if(userController.isAdmin(loginInfo))return;
        accessMap=new LinkedHashMap();
        List<Role> roles=new ArrayList<>();
        Iterator<RoleUser> ruIter=roleUserController.queryByUserId(loginInfo.getUuid()).iterator();
        while(ruIter.hasNext()){
            roles.add(roleController.getRoleById(ruIter.next().getRoleId()));
        }
        for(Role role:roles){
            Iterator<RoleNode> roleNodeIter=roleNodeController.queryByRoleId(role.getUuid()).iterator();
            while(roleNodeIter.hasNext()){
                RoleNode roleNode=roleNodeIter.next();
                Node node=nodeController.getNodeById(roleNode.getNodeId());
                if(accessList.contains(node)||(!node.getStatus())) {
                    continue;
                }
                accessList.add(node);
                List<Node> nodeList=nodeController.getAllChilds(node.getUuid());
                for(Node n:nodeList){
                    if(!n.getStatus())continue;
                    accessList.add(n);
                }
            }
        }
    }
    */
    
    @Override
    public void saveAccessList() {
        if(!isLogin())return;
        if(userController.isAdmin(loginInfo))return;
        accessMap=new LinkedHashMap();
        roleChilds=new LinkedHashMap();
        Iterator<RoleUser> ruIter=roleUserController.queryByUserId(loginInfo.getUuid()).iterator();
        Set<RoleNode> rnSet=new HashSet<>();
        while(ruIter.hasNext()){
            String roleId=ruIter.next().getRoleId();
            roleChilds.putAll(roleController.getAllChilds(roleId));
            rnSet.addAll(roleNodeController.queryByRoleId(roleId));
        }
        while(!rnSet.isEmpty()){
            Iterator<RoleNode> iter=rnSet.iterator();
            Map childs=new LinkedHashMap();
            Node parent=null;
            String parentId=null;
            while(iter.hasNext()){
                RoleNode rn=iter.next();
                if(childs.isEmpty()||parentId.equals(nodeController.getParentId(rn.getNodeId()))){
                    childs.put(nodeController.getNodeById(rn.getNodeId()), new LinkedHashMap());
                    parentId=parentId==null ? nodeController.getParentId(rn.getNodeId()):parentId;
                    iter.remove();
                }else{
                    if(parentId.equals(rn.getNodeId())){
                        parent=nodeController.getNodeById(rn.getNodeId());
                        iter.remove();
                    }
                }
            }
            //将父子关系添加到accessMap
            Iterator<Node> nodeIter=accessMap.keySet().iterator();
            while(nodeIter.hasNext()){
                Node node=nodeIter.next();
                Node parentNode=nodeController.getNodeById(nodeController.getParentId(node.getUuid()));
                if(childs.containsKey(parentNode)){
                    if(insertNode(childs, parentNode, node, accessMap.get(node)))
                        iter.remove();
                }
            }
            if(parent!=null&&!insertNode(accessMap, nodeController.getNodeById(parent.getParentId()), parent, childs))
                accessMap.put(parent, childs);
            else
                accessMap.putAll(childs);
        }
    }
    
    private boolean insertNode(Map map,Object pos,Object key,Object value){
        if(!MapUtil.containObject(map, pos))return false;
        if(map.get(pos)!=null){
            ((Map)map.get(pos)).put(key, value);
            return true;
        }
        Iterator iter=map.keySet().iterator();
        while(iter.hasNext()){
            if(insertNode((Map) map.get(iter.next()), pos, key, value))
                return true;
        }
        return false;
    }

    @Override
    public void loginOut() {
        loginInfo=null;
//        accessList=null;
        accessMap=null;
    }

    @Override
    public void updateLoginInfo(Timestamp loginTime, String loginIP) {
        if(!isLogin())return;
        loginInfo.setLoginTime(loginTime);
        loginInfo.setLoginIp(loginIP);
        userController.updateLoginMessage(loginInfo);
    }

    @Override
    public User getLoginInfo() {
        return loginInfo;
    }

    @Override
    public Map getRoleChilds() {
        return roleChilds;
    }

}
