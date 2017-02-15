package dtx.rbac.controller.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import dtx.db.ControllerFactory;
import dtx.db.HibernateUtil;
import dtx.rbac.bean.Node;
import dtx.rbac.controller.NodeController;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultNodeControllerImpl implements NodeController {
    
    public final static String ROOTNODEID="";
    
    public boolean nodeTitleIsExists(String title){
        if(title==null)return false;
        boolean isExists=false;
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("FROM Node node WHERE node.title=:title");
        query.setString("title", title);
        List<Node> result=query.list();
        for(Node node:result){
            if(title.equals(node.getTitle())){
                isExists=true;
                break;
            }
        }
        session.getTransaction().commit();
        return isExists;
    }
    
    public boolean nodeAddressIsExists(String address){
        if(address==null)return false;
        boolean isExists=false;
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("FROM Node node WHERE node.address=:address");
        query.setString("address", address);
        List<Node> result=query.list();
        for(Node node:result){
            if(address.equals(node.getAddress())){
                isExists=true;
                break;
            }
        }
        session.getTransaction().commit();
        return isExists;
    }

    @Override
    public Node getNodeById(String nodeId) {
        if(nodeId==null)return null;
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Node node=(Node) session.get(Node.class, nodeId);
        session.getTransaction().commit();
        return node;
    }

    @Override
    public List<Node> getByStatus(boolean status) {
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("FROM Node node WHERE node.status=:status");
        query.setBoolean("status", status);
        @SuppressWarnings("unchecked")
        List<Node> result=query.list();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public List<Node> getByType(int nodeType) {
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("FROM Node node WHERE node.nodeType=:type");
        query.setInteger("type", nodeType);
        @SuppressWarnings("unchecked")
        List<Node> result=query.list();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public List<Node> getChilds(String parentId) {
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("FROM Node node WHERE node.parentId=:parent_id");
        query.setString("parent_id", parentId);
        @SuppressWarnings("unchecked")
        List<Node> result=query.list();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public boolean updateNode(Node node) {
        boolean result=true;
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        try{
                session.update(node);
        }catch(HibernateException e){
                result=false;
        }
        session.getTransaction().commit();
        return result;
    }

    @Override
    public boolean updateNodeMessage(Node node) {
        if(node.getTitle()==null||"".equals(node.getTitle()))return false;
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("UPDATE Node node SET node.title=:title,node.address=:address,node.remark=:remark WHERE node.uuid=:id");
        query.setString("title", node.getTitle());
        query.setString("address", node.getAddress());
        query.setString("remark", node.getRemark());
        query.setString("id", node.getUuid());
        int result=query.executeUpdate();
        session.getTransaction().commit();
        return result>0 ? true:false;
    }

    @Override
    public boolean updateParent(Node node) {
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("UPDATE Node node SET node.parentId=:parent_id WHERE node.uuid=:id");
        query.setString("parent_id", node.getParentId());
        query.setString("id", node.getUuid());
        int result=query.executeUpdate();
        session.getTransaction().commit();
        return result>0;
    }

    @Override
    public boolean updateStatus(Node node) {
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("UPDATE Node node SET node.status=:status WHERE node.uuid=:id");
        query.setBoolean("status", node.getStatus());
        query.setString("id", node.getUuid());
        int result=query.executeUpdate();
        session.getTransaction().commit();
        return result>0;
    }

    @Override
    public String addNode(Node node) {
        if(node.getTitle()==null||"".equals(node.getTitle()))return null;
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        String result=(String) session.save(node);
        session.getTransaction().commit();
        return result;
    }

    @Override
    public boolean delete(String id) {
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("DELETE FROM Node node WHERE node.uuid=:id");
        query.setString("id", id);
        int result=query.executeUpdate();
        session.getTransaction().commit();
        if(result<=0)return false;
        return ControllerFactory.getRoleNodeController().deleteByNodeId(id);
    }

    @Override
    public boolean delete(Node node) {
        return delete(node.getUuid());
    }

    @Override
    public LinkedHashMap<Integer, String> getNodeTypes() {
        LinkedHashMap<Integer,String> nodeTypes=new LinkedHashMap();
        nodeTypes.put(Node.NODETYPEGROUP, "节点组");
        nodeTypes.put(Node.NODETYPESIGLENODE, "独立节点");
        return nodeTypes;
    }
    
    @Override
    public List<Node> getChildsByType(String parentId, int nodeType) {
        List<Node> result=getChilds(parentId);
        Iterator<Node> iter=result.iterator();
        while(iter.hasNext()){
            if(iter.next().getNodeType()!=nodeType)
                iter.remove();
        }
        return result;
    }

    @Override
    public Map getAllChilds(String parentId) {
        LinkedHashMap result=new LinkedHashMap();
        Iterator<Node> iter=getChilds(parentId).iterator();
        while(iter.hasNext()){
            Node node= iter.next();
            result.put(node, getAllChilds(node.getUuid()));
        }
        return result;
    }

    @Override
    public Map getAllChildsByType(String parentId, int nodeType) {
        LinkedHashMap result=new LinkedHashMap();
        Iterator<Node> iter=getChildsByType(parentId, nodeType).iterator();
        while(iter.hasNext()){
            Node node=iter.next();
            result.put(node, getAllChildsByType(node.getUuid(), nodeType));
        }
        return result;
    }

    @Override
    public Map getAllNodes() {
        return getAllChilds(ROOTNODEID);
    }

    @Override
    public Map getAllNodesByType(int nodeType) {
        return getAllChildsByType(ROOTNODEID, nodeType);
    }

    @Override
    public String getParentId(String nodeId) {
        Node node=getNodeById(nodeId);
        return node==null ? null:node.getParentId();
    }

    @Override
    public List<Node> getChilds(String parentId, boolean status) {
        Session session=HibernateUtil.getSession();
        session.beginTransaction();
        Query query=session.createQuery("FROM Node node WHERE node.parentId=:parent_id AND status=:status");
        query.setString("parent_id", parentId);
        query.setBoolean("status", status);
        @SuppressWarnings("unchecked")
        List<Node> result=query.list();
        session.getTransaction().commit();
        return result;
    }

    @Override
    public List<Node> getChildsByType(String parentId, int nodeType, boolean status) {
        List<Node> result=getChilds(parentId,status);
        Iterator<Node> iter=result.iterator();
        while(iter.hasNext()){
            if(iter.next().getNodeType()!=nodeType)
                iter.remove();
        }
        return result;
    }

    @Override
    public Map getAllChilds(String parentId, boolean status) {
        LinkedHashMap result=new LinkedHashMap();
        Iterator<Node> iter=getChilds(parentId,status).iterator();
        while(iter.hasNext()){
            Node node= iter.next();
            result.put(node, getAllChilds(node.getUuid()));
        }
        return result;
    }

    @Override
    public Map getAllChildsByType(String parentId, int nodeType, boolean status) {
        LinkedHashMap result=new LinkedHashMap();
        Iterator<Node> iter=getChildsByType(parentId, nodeType, status).iterator();
        while(iter.hasNext()){
            Node node=iter.next();
            result.put(node, getAllChildsByType(node.getUuid(), nodeType));
        }
        return result;
    }

    @Override
    public Map getAllNodes(boolean status) {
        return getAllChilds(ROOTNODEID, status);
    }

    @Override
    public Map getAllNodesByType(int nodeType, boolean status) {
        return getAllChildsByType(ROOTNODEID, nodeType, status);
    }
    
}
