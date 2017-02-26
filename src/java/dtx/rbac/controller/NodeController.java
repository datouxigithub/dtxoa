package dtx.rbac.controller;

import java.util.List;

import dtx.rbac.bean.Node;
import dtx.rbac.bean.NodeTree;
import java.util.LinkedHashMap;
import java.util.Map;

public interface NodeController {

	public Node getNodeById(String nodeId);
	
	public List<Node> getByStatus(boolean status);
	
	public List<Node> getByType(int nodeType);
	
	public List<Node> getChilds(String parentId);
        
        public List<Node> getChilds(String parentId,boolean status);
        
        public List<Node> getChildsByType(String parentId,int nodeType);
        
        public List<Node> getChildsByType(String parentId,int nodeType,boolean status);
	
//	public List<Node> getAllChilds(String parentId);
//        
//        public LinkedHashMap getAllChildsByType(String parentId,int nodeType);
        
        public NodeTree getAllChilds(String parentId);
        
        public NodeTree getAllChilds(String parentId,boolean status);
        
        public NodeTree getAllChildsByType(String parentId,int nodeType);
        
        public NodeTree getAllChildsByType(String parentId,int nodeType,boolean status);
	
//	public List<Node> getAllNodes();
        public NodeTree getAllNodes();
        
        public NodeTree getAllNodes(boolean status);
        
        public NodeTree getAllNodesByType(int nodeType);
        
        public NodeTree getAllNodesByType(int nodeType,boolean status);
        
        public LinkedHashMap<Integer,String> getNodeTypes();
        
        public String getParentId(String nodeId);
	
	//全面更新node
	public boolean updateNode(Node node);
	
	//只更新node的基本信息
	public boolean updateNodeMessage(Node node);
	
	public boolean updateParent(Node node);
	
	public boolean updateStatus(Node node);
	
	public boolean delete(String id);
	
	public boolean delete(Node node);
	
	public String addNode(Node node);
	
}
