/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtx.rbac.bean;

import dtx.db.ControllerFactory;
import dtx.rbac.controller.NodeController;
import dtx.rbac.controller.RBACController;
import dtx.rbac.controller.RoleNodeController;
import dtx.rbac.controller.impl.DefaultNodeControllerImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author datouxi
 */
public class NodeTree {
    private List<NodeTreeLeaf> rootLeaves=new ArrayList<>();
    private int treeDepth=Integer.MIN_VALUE;
    private RBACController rbac;
    
    private NodeTree(RBACController rbac){
        this.rbac=rbac;
    }
    
    public NodeTree(List<Role> roles,RBACController rbac){
        this(rbac);
        init(roles);
    }
    
    public NodeTree(List<Role> roles,boolean status,RBACController rbac){
        this(rbac);
        init(roles, status);
    }
    
    public NodeTree(Node[] nodes,RBACController rbac){
        this(rbac);
        init(nodes);
    }
    
    public NodeTree(Node[] nodes,boolean status,RBACController rbac){
        this(rbac);
        init(nodes, status);
    }
    
    private void init(RBACController rbac){
        if(!rbac.isLogin())return;
        NodeController nc=ControllerFactory.getNodeController();
        if(ControllerFactory.getUserController().isAdmin(rbac.getLoginInfo())){
            Iterator<Node> iter=nc.getChilds(DefaultNodeControllerImpl.ROOTNODEID).iterator();
            while(iter.hasNext()){
                rootLeaves.add(new NodeTreeLeaf((Node)iter.next()));
            }
        }else{
            List<Role> roles=ControllerFactory.getRoleUserController().getRoleByUser(rbac.getLoginInfo());
            RoleNodeController rnc=ControllerFactory.getRoleNodeController();
            List<RoleNode> rns=new ArrayList<>();
            for(Role role:roles){
                List<Node> nodes=rnc.getNodesByRole(role);
                for(Node node:nodes){
                    rootLeaves.add(new NodeTreeLeaf(node));
                }
                rns.addAll(rnc.queryByRoleId(role.getUuid()));
            }
            List<NodeTreeLeaf> leafList=toList();
            for(NodeTreeLeaf leaf:leafList){
                String nodeId=null;
                Iterator<RoleNode> rnIter=rns.iterator();
                while(rnIter.hasNext()){
                    RoleNode rn=rnIter.next();
                    if(rn.getNodeId().equals(leaf.getEntity().getUuid())){
                        nodeId=rn.getNodeId();
                        break;
                    }
                }
                if(nodeId!=null)
                    delete(nodeId);
            }
        }
        checkRepeat();
    }
    
    private void init(List<Role> roles){
        List<Node> nodes=new ArrayList<>();
        RoleNodeController rnc=ControllerFactory.getRoleNodeController();
        for(Role role:roles){
            nodes.addAll(rnc.getNodesByRole(role));
        }
        init(nodes.toArray(new Node[nodes.size()]));
    }
    
    private void init(List<Role> roles,boolean status){
        List<Node> nodes=new ArrayList<>();
        RoleNodeController rnc=ControllerFactory.getRoleNodeController();
        for(Role role:roles){
            nodes.addAll(rnc.getNodesByRole(role));
        }
        init(nodes.toArray(new Node[nodes.size()]), status);
    }
    
    private void init(Node[] nodes){
        rootLeaves=new ArrayList<>();
        for(Node node:nodes){
            rootLeaves.add(new NodeTreeLeaf(node));
        }
        checkRepeat();
    }
    
    private void init(Node[] nodes,boolean status){
        rootLeaves=new ArrayList<>();
        for(Node node:nodes){
            if(node.getStatus()!=status)continue;
            rootLeaves.add(new NodeTreeLeaf(node,status));
        }
        checkRepeat();
    }
    
    private void checkRepeat(){
        List<NodeTreeLeaf> newLeaves=new ArrayList<>();
        for(int i=0,len=rootLeaves.size();i<len;i++){
            List<NodeTreeLeaf> leaves=new ArrayList<>();
            for(int j=0;j<i;j++)
                leaves.add(rootLeaves.get(j));
            for(int j=i+1;j<len;j++)
                leaves.add(rootLeaves.get(j));
            if(!isExists(rootLeaves.get(i), leaves))
                newLeaves.add(rootLeaves.get(i));
        }
        rootLeaves=newLeaves;
    }
    
    private boolean isExists(NodeTreeLeaf targetLeaf,List<NodeTreeLeaf> leaves){
        Iterator<NodeTreeLeaf> leafIter=leaves.iterator();
        while(leafIter.hasNext()){
            NodeTreeLeaf leaf=leafIter.next();
            if(targetLeaf.equals(leaf)||isExists(targetLeaf, leaf.getLeaves()))return true;
        }
        return false;
    }
    
    private NodeTreeLeaf find(String nodeId,List<NodeTreeLeaf> leaves){
        Iterator<NodeTreeLeaf> leafIter=leaves.iterator();
        while(leafIter.hasNext()){
            NodeTreeLeaf leaf=leafIter.next();
            try{
                if(nodeId.equals(leaf.getEntity().getUuid()))return leaf;
            }catch(NullPointerException e){
                continue;
            }
            NodeTreeLeaf result=find(nodeId, leaf.getLeaves());
            if(result!=null)
                return result;
        }
        return null;
    }
    
    public NodeTreeLeaf find(String nodeId){
        return find(nodeId, rootLeaves);
    }
    
    private boolean delete(String nodeId,List<NodeTreeLeaf> leaves){
        for(NodeTreeLeaf leaf:leaves){
            if(leaf.getEntity().getUuid().equals(nodeId))return true;
            if(delete(nodeId,leaf.getLeaves()))return true;
        }
        return false;
    }
    
    private void delete(String nodeId){
        delete(nodeId, rootLeaves);
    }
    
    public List<NodeTreeLeaf> getRoots(){
        return rootLeaves;
    }
    
    private int getDepth(NodeTreeLeaf leaf){
        if(leaf==null||leaf.getEntity()==null)return 0;
        int maxDepth=0;
        Iterator<NodeTreeLeaf> leafIter=leaf.leafIterator();
        while(leafIter.hasNext()){
            NodeTreeLeaf childLeaf=leafIter.next();
            int depth=getDepth(childLeaf);
            maxDepth=maxDepth<depth ? depth:maxDepth;
        }
        return ++maxDepth;
    }
    
    public int getDepth(){
        if(treeDepth!=Integer.MIN_VALUE)return treeDepth;
        int maxDepth=0;
        for(NodeTreeLeaf leaf:rootLeaves){
            int depth=getDepth(leaf);
            maxDepth=maxDepth<depth ? depth:maxDepth;
        }
        treeDepth=maxDepth!=0 ? maxDepth:treeDepth;
        return maxDepth;
    }
    
    private List<NodeTreeLeaf> toList(List<NodeTreeLeaf> leaves){
        List<NodeTreeLeaf> leafList=new ArrayList<>();
        for(NodeTreeLeaf leaf:leaves){
            leafList.add(leaf);
            leafList.addAll(leaf.getLeaves());
        }
        return leafList;
    }
    
    public List<NodeTreeLeaf> toList(){
        return toList(rootLeaves);
    }
}
