package dtx.rbac.controller.impl;

import dtx.db.ControllerFactory;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import dtx.db.HibernateUtil;
import dtx.rbac.bean.Node;
import dtx.rbac.bean.Role;
import dtx.rbac.bean.RoleNode;
import dtx.rbac.controller.NodeController;
import dtx.rbac.controller.RoleNodeController;
import java.util.ArrayList;
import java.util.Iterator;

public class DefaultRoleNodeControllerImpl implements RoleNodeController {

	@Override
	public List<RoleNode> queryByNodeId(String nodeId) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM RoleNode role_node WHERE role_node.nodeId=:node_id");
		query.setString("node_id", nodeId);
		@SuppressWarnings("unchecked")
		List<RoleNode> result=query.list();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public List<RoleNode> queryByRoleId(String roleId) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("FROM RoleNode role_node WHERE role_node.roleId=:role_id");
		query.setString("role_id", roleId);
		@SuppressWarnings("unchecked")
		List<RoleNode> result=query.list();
		session.getTransaction().commit();
		return result;
	}

	@Override
	public RoleNode queryById(String id) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		RoleNode rn=(RoleNode)session.get(RoleNode.class, id);
		session.getTransaction().commit();
		return rn;
	}

	@Override
	public boolean deleteByNodeId(String nodeId) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("DELETE FROM RoleNode role_node WHERE role_node.nodeId=:node_id");
		query.setString("node_id", nodeId);
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}

	@Override
	public boolean deleteByRoleId(String roleId) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("DELETE FROM RoleNode role_node WHERE role_node.roleId=:role_id");
		query.setString("role_id", roleId);
		int result=query.executeUpdate();
		session.getTransaction().commit();
//		return result>0 ? true:false;
                return true;
	}

	@Override
	public boolean delete(String id) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("DELETE FROM RoleNode role_node WHERE role_node.uuid=:id");
		query.setString("id", id);
		int result=query.executeUpdate();
		session.getTransaction().commit();
		return result>0 ? true:false;
	}

	@Override
	public boolean delete(RoleNode rn) {
		return delete(rn.getUuid());
	}

	@Override
	public String addRoleNode(String node_id, String role_id) {
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		RoleNode rn=new RoleNode();
		rn.setRoleId(role_id);
		rn.setNodeId(node_id);
		String result=(String) session.save(rn);
		session.getTransaction().commit();
		return result;
	}

	@Override
	public String addRoleNode(RoleNode rn) {
		return addRoleNode(rn.getNodeId(), rn.getRoleId());
	}

	@Override
	public boolean deleteByNodeId(RoleNode rn) {
		return deleteByNodeId(rn.getNodeId());
	}

	@Override
	public boolean deleteByRoleId(RoleNode rn) {
		return deleteByRoleId(rn.getRoleId());
	}

    @Override
    public List<Node> getNodesByRole(String roleId) {
        List<Node> nodes=new ArrayList<>();
        NodeController nc=ControllerFactory.getNodeController();
        Iterator<RoleNode> iter=queryByRoleId(roleId).iterator();
        while(iter.hasNext()){
            nodes.add(nc.getNodeById(((RoleNode)iter.next()).getNodeId()));
        }
        return nodes;
    }

    @Override
    public List<Node> getNodesByRole(Role role) {
        if(role==null)return new ArrayList<Node>();
        return getNodesByRole(role.getUuid());
    }

}
