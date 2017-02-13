package dtx.rbac.controller;

import java.util.List;

import dtx.rbac.bean.RoleUser;

public interface RoleUserController {

    public List<RoleUser> queryByUserId(String userId);

    public List<RoleUser> queryByRoleId(String roleId);

    public RoleUser queryById(String id);

    public boolean deleteByUserId(String userId);

    public boolean deleteByRoleId(String roleId);

    public boolean deleteByUserId(RoleUser ru);

    public boolean deleteByRoleId(RoleUser ru);

    public boolean delete(String id);

    public boolean delete(RoleUser ru);

    //返回新插入的uuid
    public String addRoleUser(String user_id,String role_id);

    public String addRoleUser(RoleUser ru);

    public void addRoleUsers(String user_id,String[] roles);

}