package PKM.fdsProject;

import java.sql.*;


public class Sql {
	public Connection conn = null;
	
	public void load() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//System.out.println("드라이버 검색 성공");
		} catch(ClassNotFoundException e) {
			//System.out.println("드라이버 검색 실패");
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kakaobank?useSSL=false", "root", "admin");
			//System.out.println("My-SQL 연결 성공");
		} catch(SQLException e) {
			//System.out.println("My-SQL 연결 실패");
			e.printStackTrace();
		}
	}
	
	public Connection getConn() {
		try {
			return this.conn;
		} catch(Exception e) {
			return null;
		}
	}
	
	public boolean disconnect() {
		try {
			if(this.conn != null)
				this.conn.close();
			
			this.conn = null;
			return true;
		} catch(SQLException se) {
			return false;
		} catch(Exception e) {
			return false;
		}
	}
}
