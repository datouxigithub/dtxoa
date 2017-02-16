package dtx.rbac.controller;

import java.util.List;

import dtx.rbac.bean.Node;
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
        
        public Map getAllChilds(String parentId);
        
        public Map getAllChilds(String parentId,boolean status);
        
        public Map getAllChildsByType(String parentId,int nodeType);
        
        public Map getAllChildsByType(String parentId,int nodeType,boolean status);
	
//	public List<Node> getAllNodes();
        public Map getAllNodes();
        
        public Map getAllNodes(boolean status);
        
        public Map getAllNodesByType(int nodeType);
        
        public Map getAllNodesByType(int nodeType,boolean status);
        
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
