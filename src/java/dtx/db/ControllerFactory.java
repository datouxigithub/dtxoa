package dtx.db;

import dtx.rbac.controller.NodeController;
import dtx.rbac.controller.RBACController;
import dtx.rbac.controller.RoleController;
import dtx.rbac.controller.RoleNodeController;
import dtx.rbac.controller.RoleUserController;
import dtx.rbac.controller.UserController;
import dtx.rbac.controller.impl.DefaultNodeControllerImpl;
import dtx.rbac.controller.impl.DefaultRBACControllerImpl;
import dtx.rbac.controller.impl.DefaultRoleControllerImpl;
import dtx.rbac.controller.impl.DefaultRoleNodeControllerImpl;
import dtx.rbac.controller.impl.DefaultRoleUserControllerImpl;
import dtx.rbac.controller.impl.DefaultUserControllerImpl;

public class ControllerFactory {

	public static RoleNodeController getRoleNodeController(){
		return new DefaultRoleNodeControllerImpl();
	}
	
	public static RoleUserController getRoleUserController(){
		return new DefaultRoleUserControllerImpl();
	}
	
	public static RoleController getRoleController(){
		return new DefaultRoleControllerImpl();
	}
	
	public static NodeController getNodeController(){
		return new DefaultNodeControllerImpl();
	}
	
	public static UserController getUserController(){
		return new DefaultUserControllerImpl();
	}
	
	public static RBACController getRBACController(){
		return new DefaultRBACControllerImpl();
	}
}
