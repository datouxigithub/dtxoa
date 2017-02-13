/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtx.test;

import dtx.rbac.bean.Node;
import dtx.rbac.util.StringUtil;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author datouxi
 */
public class Testmap {
    
    public static void main(String args[]){
        Node node=new Node();
        node.setUuid("123");
        node.setTitle("haha");
        Map map=new HashMap();
        map.put(node, "just test");
        
        Node testNode=new Node();
        testNode.setUuid("123");
        
        System.out.println(StringUtil.getMD5String("449449"));
    }
    
}
