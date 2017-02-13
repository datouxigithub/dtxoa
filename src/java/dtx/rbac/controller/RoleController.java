package dtx.rbac.controller;

import java.util.List;

import dtx.rbac.bean.Role;
import java.util.Map;

public interface RoleController {

	public Role getRoleById(String roleId);
	
	public List<Role> getByStatus(boolean status);
	
	public List<Role> getChilds(String parentId);
	
//      public List<Role> getAllChilds(String parentId);
//	public List<Role> getAllRoles();
        
        //Node:Map
	public Map getAllChilds(String parentId);
	
	public Map getAllRoles();
	
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
