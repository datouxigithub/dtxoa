package dtx.rbac.controller.impl;

import dtx.db.ControllerFactory;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import dtx.db.HibernateUtil;
import dtx.rbac.bean.Role;
import dtx.rbac.bean.RoleUser;
import dtx.rbac.bean.User;
import dtx.rbac.controller.RoleController;
import dtx.rbac.controller.RoleUserController;
import java.util.ArrayList;

public class DefaultRoleUserControllerImpl implements RoleUserController {

	@Override
	public List<RoleUser> queryByUserId(String userId){
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM RoleUser role_user WHERE role_user.userId=:user_id");
		query.setString("user_id", userId);
		@SuppressWarnings("unchecked")
		List<RoleUser> result=query.list();
		session.getTransaction().commit();
		return result;
	}
	
	@Override
	public List<RoleUser> queryByRoleId(String roleId){
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM RoleUser role_user WHERE role_user.roleId=:role_id");
		query.setString("role_id", roleId);
		@SuppressWarnings("unchecked")
		List<RoleUser> result=query.list();
		session.getTransaction().commit();
		return result;
	}
	
	@Override
	public RoleUser queryById(String id){
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		RoleUser rn=(RoleUser)session.get(RoleUser.class, id);
		session.getTransaction().commit();
		return rn;
	}
	
	@Override
	public boolean deleteByUserId(String userId){
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("DELETE FROM RoleUser role_user WHERE role_user.userId=:user_id");
		query.setString("user_id", userId);
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}
	
	@Override
	public boolean deleteByRoleId(String roleId){
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("DELETE FROM RoleUser role_user WHERE role_user.roleId=:role_id");
		query.setString("role_id", roleId);
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}
	
	@Override
	public boolean delete(String id){
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("DELETE FROM RoleUser role_user WHERE role_user.uuid=:id");
		query.setString("id", id);
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}
	
	@Override
	public boolean delete(RoleUser ru){
		return delete(ru.getUuid());
	}
	
	@Override
	public String addRoleUser(String user_id,String role_id){
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		RoleUser ru=new RoleUser();
		ru.setUserId(user_id);
		ru.setRoleId(role_id);
		String result=(String) session.save(ru);
		session.getTransaction().commit();
		return result;
	}
	
	@Override
	public String addRoleUser(RoleUser ru){
		return addRoleUser(ru.getUserId(), ru.getRoleId());
	}

	@Override
	public boolean deleteByUserId(RoleUser ru) {
		return deleteByUserId(ru.getUserId());
	}

	@Override
	public boolean deleteByRoleId(RoleUser ru) {
		return deleteByRoleId(ru.getRoleId());
	}

    @Override
    public void addRoleUsers(String user_id, String[] roles) {
        for(String role:roles)
            if(addRoleUser(user_id, role)==null)return;
    }

    @Override
    public List<Role> getRoleByUser(User user) {
        List<Role> result=new ArrayList<>();
        if(ControllerFactory.getUserController().isAdmin(user)){
            result=ControllerFactory.getRoleController().getChilds(DefaultRoleControllerImpl.ROOTROLEID);
        }else{
            List<RoleUser> rus=queryByUserId(user.getUuid());
            RoleController rc=ControllerFactory.getRoleController();
            for(RoleUser ru:rus){
                result.add(rc.getRoleById(ru.getRoleId()));
            }
        }
        return result;
    }

}
