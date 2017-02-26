package dtx.rbac.controller;

import dtx.rbac.bean.Node;
import dtx.rbac.bean.Role;
import java.util.List;

import dtx.rbac.bean.RoleNode;

public interface RoleNodeController {

    public List<RoleNode> queryByNodeId(String nodeId);

    public List<RoleNode> queryByRoleId(String roleId);
    
    public List<Node> getNodesByRole(String roleId);
    
    public List<Node> getNodesByRole(Role role);
    
    public List<Node> getNodesByRole(List<Role> roles);

    public RoleNode queryById(String id);

    public boolean deleteByNodeId(String nodeId);

    public boolean deleteByRoleId(String roleId);

    public boolean deleteByNodeId(RoleNode rn);

    public boolean deleteByRoleId(RoleNode rn);

    public boolean delete(String id);

    public boolean delete(RoleNode rn);

    public String addRoleNode(String node_id,String role_id);

    public String addRoleNode(RoleNode rn);
	
}
