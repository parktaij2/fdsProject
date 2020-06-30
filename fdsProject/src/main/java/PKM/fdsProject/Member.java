// 고객번호, 고객명, 생년월일, 가입시간
package PKM.fdsProject;

import java.sql.*;
import java.util.Scanner;
import org.json.simple.JSONObject;

public class Member {
	Sql sql = new Sql();
	Scanner scan = new Scanner(System.in);
	JSONObject json = new JSONObject();
	
	String sqlQeury = "";
	
	PreparedStatement pstmt = null;
	Connection conn = null;
	ResultSet rs = null;
	
	public Member() {
		sql.load();
		conn = sql.getConn();
	}
	
	// 고객번호가 있는 고객인지 체크
	@SuppressWarnings("unchecked")
	public JSONObject checkMemberNo(JSONObject json) {
		sqlQeury = "SELECT MEMBERNO, COUNT(1) AS NUM FROM KAKAOBANK.REGIST WHERE MEMBERNAME = ?";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setString(1, (String)json.get("memberName"));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getInt("NUM") == 1) {
					System.out.println("이미 존재하는 고객입니다. 당신의 고객번호는 " + rs.getInt("MEMBERNO") + "입니다.");
					json.put("memberNo", rs.getInt("MEMBERNO"));
					return json;
				} else {
					json.put("memberNo", 0);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		return json;
	}
	
	// 고객번호 채번
	@SuppressWarnings("unchecked")
	public JSONObject  getMemberNo() {
		sqlQeury = "SELECT IFNULL(MAX(MEMBERNO), 0) + 1 AS NUM FROM KAKAOBANK.REGIST";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				json.put("memberNo", rs.getInt("NUM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		return json;
	}
	
	// 신규 고객 가입
	@SuppressWarnings("unchecked")
	public JSONObject regist() {
		System.out.print("고객명을 입력하세요 : ");
		json.put("memberName", scan.next());
		
		json = checkMemberNo(json);
		
		if((Integer)json.get("memberNo") != 0) {
			return json;
		}
		
		json = getMemberNo();
		
		System.out.print("생년월일을 입력하세요(YYYYMMDD) : ");
		json.put("memberBirthDay", scan.next());
		
		sqlQeury = "INSERT INTO KAKAOBANK.REGIST (MEMBERNO, MEMBERNAME, MEMBERBIRTHDAY, REGISTTIME) "
				 + "VALUES (?, ?, ?, SYSDATE())";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("memberNo"));
			pstmt.setString(2, (String)json.get("memberName"));
			pstmt.setString(3, (String)json.get("memberBirthDay"));
			int n = pstmt.executeUpdate();
			
			if(n > 0) {
				System.out.println("가입이 성공적으로 진행되었습니다.");
			} else {
				System.out.println("가입에 실패하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		
		return json;
	}
}
