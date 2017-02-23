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
public class RoleTreeLeaf {
    private Role entityRole;
    private List<RoleTreeLeaf> leaves;
    
    public RoleTreeLeaf(Role role){
        this.entityRole=role;
        leaves=new ArrayList<>();
        List<Role> childs=ControllerFactory.getRoleController().getChilds(role.getUuid());
        for(Role child:childs){
            leaves.add(new RoleTreeLeaf(child));
        }
    }
    
    public RoleTreeLeaf(Role role,boolean status){
        this.entityRole=role;
        leaves=new ArrayList<>();
        List<Role> childs=ControllerFactory.getRoleController().getChilds(role.getUuid());
        for(Role child:childs){
            leaves.add(new RoleTreeLeaf(child,status));
        }
    }
    
    public Role getEntity(){
        return entityRole;
    }
    
    public List<RoleTreeLeaf> getLeaves(){
        return leaves;
    }
    
    public Iterator<RoleTreeLeaf> leafIterator(){
        return leaves.iterator();
    }
    
    public boolean hasLeaf(){
        return leaves.size()>0 ? true:false;
    }
}
