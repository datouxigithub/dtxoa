package dtx.rbac.controller;

import dtx.rbac.bean.Node;
import dtx.rbac.bean.User;
import java.sql.Timestamp;
import java.util.Map;

public interface RBACController {

	//验证用户帐号密码，成功返回true,否则返回false
	public boolean authenticate(String account,String password);
	
	//验证是否有权限操作主键为nodeId的Node
	public boolean accessDecision(String nodeId);
	
	public boolean accessDecision(Node node);
	
	//根据登录信息返回权限列表
//	public List<Node> getAccessList();
	public Map getAccessList();
        
	//检查是否已经登录
	public boolean isLogin();
        
        public User getLoginInfo();
        
        //注销用户
        public void loginOut();
        
        public void saveAccessList();
        
        public void updateLoginInfo(Timestamp loginTime,String loginIP);
        
        public Map getRoleChilds();
}
