package dtx.rbac.bean;

import java.io.Serializable;

public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String uuid,roleName,remark,parentId;
	protected boolean status;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Role))return false;
		if(uuid==null)return false;
		return uuid.equals(((Role)obj).getUuid());
	}
	
	@Override
	public int hashCode(){
		if(uuid==null)return super.hashCode();
		int result=0;
		for(int i=0;i<uuid.length();i++)
			result+=uuid.charAt(i);
		return result;
	}
}
