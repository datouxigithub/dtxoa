package dtx.rbac.controller.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import dtx.db.ControllerFactory;
import dtx.db.HibernateUtil;
import dtx.rbac.bean.Role;
import dtx.rbac.controller.RoleController;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultRoleControllerImpl implements RoleController {

    public final static String ROOTROLEID="";
    
	@Override
	public Role getRoleById(String roleId) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Role role=(Role) session.get(Role.class, roleId);
		session.getTransaction().commit();
		return role;
	}

	@Override
	public List<Role> getByStatus(boolean status) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM Role role WHERE role.status=:status");
		query.setBoolean("status", status);
		@SuppressWarnings("unchecked")
		List<Role> result=query.list();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public List<Role> getChilds(String parentId) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM Role role WHERE role.parentId=:parent_id");
		query.setString("parent_id", parentId);
		@SuppressWarnings("unchecked")
		List<Role> result=query.list();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public boolean updateRole(Role role) {
		boolean result=true;
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		try{
			session.update(role);
		}catch(HibernateException e){
			result=false;
		}
		session.getTransaction().commit();
		return result;
	}

	@Override
	public boolean updateRoleMessage(Role role) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("UPDATE Role role SET role.roleName=:role_name,role.remark=:remark WHERE role.uuid=:id");
		query.setString("role_name", role.getRoleName());
		query.setString("remark", role.getRemark());
		query.setString("id", role.getUuid());
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}

	@Override
	public boolean updateParent(Role role) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("UPDATE Role role SET role.parentId=:parent_id WHERE role.uuid=:id");
		query.setString("parent_id", role.getParentId());
		query.setString("id", role.getUuid());
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}

	@Override
	public boolean updateStatus(Role role) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("UPDATE Role role SET role.status=:status WHERE role.uuid=:id");
		query.setBoolean("status", role.getStatus());
		query.setString("id", role.getUuid());
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}

	@Override
	public boolean deleteRole(Role role) {
		return deleteRole(role.getUuid());
	}

	@Override
	public boolean deleteRole(String id) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("DELETE FROM Role role WHERE role.uuid=:id");
		query.setString("id", id);
		int result=query.executeUpdate();
		session.getTransaction().commit();
		if(result<=0)return false;
		return ControllerFactory.getRoleNodeController().deleteByRoleId(id)&&ControllerFactory.getRoleUserController().deleteByRoleId(id);
	}

	@Override
	public String addRole(Role role) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		String result=(String) session.save(role);
		session.getTransaction().commit();
		return result;
	}

        /*
	@Override
	public List<Role> getAllChilds(String parentId) {
		List<Role> result=new ArrayList<>();
		Iterator<Role> childs=getChilds(parentId).iterator();
		while(childs.hasNext()){
			Role role=childs.next();
			result.add(role);
			result.addAll(getAllChilds(role.getUuid()));
		}
		return result;
	}
        */
        
        @Override
        public Map getAllChilds(String parentId){
            LinkedHashMap result=new LinkedHashMap();
            
            Iterator<Role> childs=getChilds(parentId).iterator();
            while(childs.hasNext()){
                Role role=childs.next();
                result.put(role, getAllChilds(role.getUuid()));
            }
            
            return result;
        }

    @Override
    public Map getAllRoles() {
        return getAllChilds(ROOTROLEID);
    }
        /*
	@Override
	public List<Role> getAllRoles() {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM Role role WHERE role.parentId IS NULL");
		@SuppressWarnings("unchecked")
		List<Role> roots=query.list();
		session.getTransaction().commit();
		
		List<Role> result=new ArrayList<>();
		Iterator<Role> iter=roots.iterator();
		while(iter.hasNext()){
			Role role=iter.next();
			result.add(role);
			result.addAll(getAllChilds(role.getUuid()));
		}
		return roots;
	}
        */
}
