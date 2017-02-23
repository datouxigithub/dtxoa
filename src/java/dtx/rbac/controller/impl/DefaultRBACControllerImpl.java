package dtx.rbac.controller.impl;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Node;
import dtx.rbac.bean.NodeTree;
import dtx.rbac.bean.NodeTreeLeaf;
import dtx.rbac.bean.RoleTree;
import dtx.rbac.bean.User;
import dtx.rbac.controller.RBACController;
import dtx.rbac.controller.UserController;
import dtx.rbac.util.StringUtil;
import java.sql.Timestamp;

public class DefaultRBACControllerImpl implements RBACController {

    private User loginInfo;
    private final UserController userController=ControllerFactory.getUserController();
    private NodeTree nodeTree;
    private RoleTree roleTree;

    @Override
    public boolean authenticate(String account, String password) {
        loginInfo=null;
        User user=userController.getUserByAccount(account);
        if(user==null)
            return false;
        if(!user.getPassword().equals(StringUtil.getMD5String(password)))
            return false;
        loginInfo=user;
        saveAccessList();
        return true;
    }
    
    @Override
    public boolean accessDecision(String nodeId) {
        if(!isLogin())return false;
        if(userController.isAdmin(loginInfo))return true;
        NodeTreeLeaf leaf=nodeTree.find(nodeId);
        return leaf==null ? false:true;
    }

    @Override
    public boolean accessDecision(Node node) {
        return accessDecision(node.getUuid());
    }

    public NodeTree getAccessList(){
        if(!isLogin())return null;
        return nodeTree;
    }

    @Override
    public boolean isLogin() {
        return loginInfo!=null;
    }
    
    @Override
    public void saveAccessList() {
        nodeTree=new NodeTree(this, true);
        roleTree=new RoleTree(this, true);
    }

    @Override
    public void loginOut() {
        nodeTree=null;
        roleTree=null;
        loginInfo=null;
    }

    @Override
    public void updateLoginInfo(Timestamp loginTime, String loginIP) {
        if(!isLogin())return;
        loginInfo.setLoginTime(loginTime);
        loginInfo.setLoginIp(loginIP);
        userController.updateLoginMessage(loginInfo);
    }

    @Override
    public User getLoginInfo() {
        return loginInfo;
    }

    @Override
    public RoleTree getRoleChilds() {
        return roleTree;
    }

}
