package dtx.rbac.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import sun.misc.BASE64Encoder;

public class StringUtil {

	public static String getMD5String(String str){
		try {
			MessageDigest md5=MessageDigest.getInstance("MD5");
			BASE64Encoder encoder=new BASE64Encoder();
			return encoder.encode(md5.digest(str.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			return str;
		}
	}
	
	public static String getUUID(){
		return UUID.randomUUID().toString();
	}
        
        public static void main(String args[]){
            System.out.println(getMD5String("admin123"));
        }
	
}
