package dtx.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map.Entry;

public class DB {
	
	public static Connection getConnection(){
		return getConnection("dtxoa","127.0.0.1",3306,"root","449449");
	}

	public static Connection getConnection(String dbName,String dbAddress,int port,String userName,String userPassword){
		Connection conn=null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn=DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s?characterEncoding=utf8", dbAddress,port), userName, userPassword);
		} catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
			System.err.println("找不到mysql jdbc驱动文件");
			e.printStackTrace();
		}catch (SQLException e) {
			System.err.println(String.format("连接不上%s数据库", dbName));
		}	
		return conn;
	}
	
	public static void closeConnection(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean executeBatch(Connection conn,String[] sqls){
		boolean result=false;
		try {
			Statement st=conn.createStatement();
			for(String sql:sqls){
				st.addBatch(sql);
			}
			int rs[]=st.executeBatch();
			result=true;
			for(int i:rs){
				if(i<=0){
					result=false;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean update(Connection conn,String sql){
		boolean result=false;
		try{
			Statement st=conn.createStatement();
			result=st.executeUpdate(sql)>0 ? true:false;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean insert(Connection conn,String tableName,List<Entry<String,Object>> dataList){
		boolean result=false;
		StringBuilder builder=new StringBuilder("INSERT INTO "+tableName+"(");
		String values="";
		for(int i=0,len=dataList.size();i<len;i++){
			Entry<String,Object> entry=dataList.get(i);
			builder.append(entry.getKey());
			values=values+"?";
			if(i!=len-1){
				builder.append(",");
				values=values+",";
			}
		}
		builder.append(") VALUES("+values+")");
		try {
			PreparedStatement pst=conn.prepareStatement(builder.toString());
			for(int i=0,len=dataList.size();i<len;i++){
				Object value=dataList.get(i).getValue();
				if(value instanceof Integer)
					pst.setInt(i+1, (Integer)value);
				else if(value instanceof Float)
					pst.setFloat(i+1, (Float)value);
				else if(value instanceof Short)
					pst.setShort(i+1, (Short)value);
				else if(value instanceof Long)
					pst.setLong(i+1, (Long)value);
				else if(value instanceof Double)
					pst.setDouble(i+1, (Double)value);
				else if(value instanceof Boolean)
					pst.setBoolean(i+1, (Boolean)value);
				else if(value instanceof Date)
					pst.setDate(i+1, (Date)value);
				else if(value instanceof String)
					pst.setString(i+1, (String)value);
			}
			result=pst.executeUpdate()>0 ? true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static ResultSet query(Connection conn,String sql){
		ResultSet result=null;
		try{
			Statement st=conn.createStatement();
			result=st.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	
}
