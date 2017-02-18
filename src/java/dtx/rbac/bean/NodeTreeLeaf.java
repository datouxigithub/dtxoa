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

/**
 *
 * @author datouxi
 */
public class NodeTreeLeaf {
    
    private Node entityNode;
    private List<NodeTreeLeaf> leaves;
    
    public NodeTreeLeaf(Node node){
        this.entityNode=node;
        leaves=new ArrayList<>();
        List<Node> childs=ControllerFactory.getNodeController().getChilds(node.getUuid());
        for(Node child:childs){
            leaves.add(new NodeTreeLeaf(child));
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
    
}
