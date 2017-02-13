package dtx.rbac.bean;

import java.io.Serializable;

public class RoleUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected String uuid,userId,roleId;
	
	public RoleUser(){}
	
	public RoleUser(String id,String userId,String roleId){
		setUuid(id);
		setUserId(userId);
		setRoleId(roleId);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Override
	public String toString(){
		return "uuid:"+((uuid==null) ? "[null]":uuid)+";"+
				"user_id:"+((roleId==null) ? "[null]":userId)+";"+
				"role_id:"+((roleId==null) ? "[null]":roleId);
	}
}
