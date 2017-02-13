package dtx.rbac.controller.impl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import dtx.db.ControllerFactory;
import dtx.db.HibernateUtil;
import dtx.rbac.bean.User;
import dtx.rbac.controller.UserController;
import dtx.rbac.util.StringUtil;

public class DefaultUserControllerImpl implements UserController {

	@Override
	public User getUserById(String userId) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		User user=(User) session.get(User.class, userId);
		session.getTransaction().commit();
		return user;
	}

	@Override
	public User getUserByAccount(String account) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM User user WHERE user.account=:account");
		query.setString("account", account);
                List<User> userList=query.list();
		User user=null;
                if(!userList.isEmpty())user=userList.get(0);
		session.getTransaction().commit();
		return user;
	}

	@Override
	public List<User> getUsersByStatus(boolean status) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM User user WHERE user.status=:status");
		query.setBoolean("status", status);
		@SuppressWarnings("unchecked")
		List<User> result=query.list();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public String addUser(User user) {
            user.setPassword(StringUtil.getMD5String(user.getPassword()));
            Session session=HibernateUtil.getSession();
            session.beginTransaction();
            String result=(String) session.save(user);
            session.getTransaction().commit();
            return result;
	}

	@Override
	public boolean deleteUser(User user) {
		return deleteUser(user.getUuid());
	}
	
	@Override
	public boolean deleteUser(String userId) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("DELETE FROM User user WHERE user.uuid=:id");
		query.setString("id", userId);
		int result=query.executeUpdate();
		session.getTransaction().commit();
		if(result<=0)return false;
		return ControllerFactory.getRoleUserController().deleteByUserId(userId);
	}

	@Override
	public boolean updateUser(User user) {
            user.setPassword(StringUtil.getMD5String(user.getPassword()));
            Session session=HibernateUtil.getSession();
            session.beginTransaction();
            Query query=session.createQuery("UPDATE User user SET user.password=:pwd,user.status=:status,user.remark=:remark,user.loginTime=:login_time,user.loginIp=:login_ip WHERE user.uuid=:id");
            query.setString("pwd", user.getPassword());
            query.setBoolean("status", user.getStatus());
            query.setString("remark", user.getRemark());
            query.setString("id", user.getUuid());
            query.setString("login_ip", user.getLoginIp());
            query.setTimestamp("login_time", user.getLoginTime());
            int result=query.executeUpdate();
            session.getTransaction().commit();
            return result>0 ? true:false;
	}

	@Override
	public boolean updateLoginMessage(User user) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("UPDATE User user SET user.loginTime=:login_time,user.loginIp=:login_ip WHERE user.uuid=:id");
		query.setTimestamp("login_time", new Timestamp(System.currentTimeMillis()));
		query.setString("login_ip", user.getLoginIp());
		query.setString("id", user.getUuid());
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}

    @Override
    public boolean isAdmin(String account) {
        final String ADMINNAME="admin";
        return ADMINNAME.equals(account);
    }

    @Override
    public boolean isAdmin(User user) {
        return isAdmin(user.getAccount());
    }

}
