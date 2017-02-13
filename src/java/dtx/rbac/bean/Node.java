package dtx.rbac.bean;

import java.io.Serializable;

public class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final static int NODETYPESIGLENODE=1;
	public final static int NODETYPEGROUP=2;
	
	protected String uuid,title,address,remark,parentId;
	protected boolean status;
	protected int nodeType;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public int getNodeType() {
		return nodeType;
	}
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Node))return false;
		if(uuid==null)return false;
		return uuid.equals(((Node)obj).getUuid());
	}
	
	@Override
	public int hashCode(){
		if(uuid==null)return super.hashCode();
		int result=0;
		for(int i=0;i<uuid.length();i++)
			result+=uuid.charAt(i);
		return result;
	}

    @Override
    public String toString() {
        return uuid;
    }
        
        
	
}
