package dtx.rbac.controller;

import java.util.List;

import dtx.rbac.bean.RoleNode;

public interface RoleNodeController {

    public List<RoleNode> queryByNodeId(String nodeId);

    public List<RoleNode> queryByRoleId(String roleId);

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
