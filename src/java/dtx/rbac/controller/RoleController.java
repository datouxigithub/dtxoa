package dtx.rbac.controller;

import java.util.List;

import dtx.rbac.bean.Role;
import dtx.rbac.bean.RoleTree;
import java.util.Map;

public interface RoleController {

	public Role getRoleById(String roleId);
	
	public List<Role> getByStatus(boolean status);
	
	public List<Role> getChilds(String parentId);
        
        public List<Role> getChilds(String parentId,boolean status);
        
        //Node:Map
	public RoleTree getAllChilds(String parentId);
	
	public RoleTree getAllRoles();
        
        public RoleTree getAllChilds(String parentid,boolean status);
        
        public RoleTree getAllRoles(boolean status);
	
	//全面更新role
	public boolean updateRole(Role role);
	
	//只更新role的基本信息
	public boolean updateRoleMessage(Role role);
	
	public boolean updateParent(Role role);
	
	public boolean updateStatus(Role role);
	
	public boolean deleteRole(Role role);
	
	public boolean deleteRole(String id);
	
	public String addRole(Role role);
	
}
