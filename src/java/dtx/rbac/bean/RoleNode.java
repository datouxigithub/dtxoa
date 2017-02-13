package dtx.rbac.bean;

import java.io.Serializable;

public class RoleNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String uuid,roleId,nodeId;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	@Override
	public String toString(){
		return "uuid:"+((uuid==null) ? "[null]":uuid)+";"+
				"role_id:"+((roleId==null) ? "[null]":roleId)+";"+
				"node_id:"+((nodeId==null) ? "[null]":nodeId);
	}

    @Override
    public boolean equals(Object obj) {
        if(obj==null||!(obj instanceof RoleNode))return false;
        return uuid==null ? false:uuid.equals(((RoleNode)obj).getUuid());
    }

    @Override
    public int hashCode() {
        if(uuid==null)
            return super.hashCode();
        int result=0;
        for(int i=0,len=uuid.length();i<len;i++)
            result+=uuid.charAt(i);
        return result;
    }
        
        
}
