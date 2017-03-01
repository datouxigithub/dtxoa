/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtx.rbac.bean;

import dtx.db.ControllerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author datouxi
 */
public class NodeTreeLeaf {
    
    private Node entityNode;
    private List<NodeTreeLeaf> leaves;
    public final static String NODEID="node_id";
    public final static String PARENTID="parent_id";
    public final static String TITLE="title";
    public final static String SELECTED="selected";
    public final static String CHILDS="childs";
    
    public NodeTreeLeaf(Node node){
        this.entityNode=node;
        leaves=new ArrayList<>();
        List<Node> childs=ControllerFactory.getNodeController().getChilds(node.getUuid());
        for(Node child:childs){
            leaves.add(new NodeTreeLeaf(child));
        }
    }
    
    public NodeTreeLeaf(Node node,int nodeType){
        this.entityNode=node;
        leaves=new ArrayList<>();
        List<Node> childs=ControllerFactory.getNodeController().getChildsByType(node.getUuid(), nodeType);
        for(Node child:childs){
            leaves.add(new NodeTreeLeaf(child, nodeType));
        }
    }
    
    public NodeTreeLeaf(Node node,boolean status){
        this.entityNode=node;
        leaves=new ArrayList<>();
        List<Node> childs=ControllerFactory.getNodeController().getChilds(node.getUuid(), status);
        for(Node child:childs){
            leaves.add(new NodeTreeLeaf(child,status));
        }
    }
    
    public NodeTreeLeaf(Node node,boolean status,int nodeType){
        this.entityNode=node;
        leaves=new ArrayList<>();
        List<Node> childs=ControllerFactory.getNodeController().getChilds(node.getUuid(), status);
        for(Node child:childs){
            if(child.getNodeType()==nodeType)
                leaves.add(new NodeTreeLeaf(child, status, nodeType));
        }
    }
    
    public Node getEntity(){
        return entityNode;
    }
    
    public List<NodeTreeLeaf> getLeaves(){
        return leaves;
    }
    
    public Iterator<NodeTreeLeaf> leafIterator(){
        return leaves.iterator();
    }
    
    public boolean hasLeaf(){
        return leaves.size()>0 ? true:false;
    }
    
    public JSONObject toJSON() throws JSONException{
        JSONObject obj=new JSONObject();
        obj.put(NODEID, entityNode.getUuid());
        obj.put(TITLE, entityNode.getTitle());
        obj.put(PARENTID, entityNode.getParentId());
        JSONArray childs=new JSONArray();
        for(NodeTreeLeaf leaf:leaves)
            childs.put(leaf.toJSON());
        obj.put(CHILDS, childs);
        return obj;
    }
    
}
