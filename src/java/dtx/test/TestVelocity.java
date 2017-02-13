/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtx.test;

import java.io.StringWriter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 *
 * @author datouxi
 */
public class TestVelocity {
    
    public static void main(String args[]) {
            Velocity.init();
            VelocityContext context=new VelocityContext();
            context.put("hello", "臭屎洋");
            StringWriter writer=new StringWriter();
            Velocity.mergeTemplate("vm/test.vm", "utf-8", context, writer);
            System.out.println(writer.toString());
    }
    
}
