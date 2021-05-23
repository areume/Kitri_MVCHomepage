package com.java.member.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.java.database.ConnectionProvider;
import com.java.database.JdbcUtil;

public class MemberDAO {	// Data Access Object
	// singleton pattern : 단 한개의 객체만을 가지고 구현 한다.
	private static MemberDAO instance=new MemberDAO();
	public static MemberDAO getInstance() {
		return instance;
	}
	
	public int insert(MemberDTO memberdto) {
		
		Connection conn=null;
		PreparedStatement pstmt=null;
		int value=0;
		
		try {
			
			String sql="insert into member values(member_num_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
			conn=ConnectionProvider.getConnection();
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1,memberdto.getId());
			pstmt.setString(2,memberdto.getPassword());
			pstmt.setString(3,memberdto.getName());
			pstmt.setString(4,memberdto.getJumin1());
			pstmt.setString(5,memberdto.getJumin2());
			
			pstmt.setString(6,memberdto.getEmail());
			pstmt.setString(7,memberdto.getZipcode());
			pstmt.setString(8,memberdto.getAddress());
			pstmt.setString(9,memberdto.getJob());
			pstmt.setString(10,memberdto.getMailing());
			
			pstmt.setString(11,memberdto.getInterest());
			pstmt.setString(12,memberdto.getMemberLevel());
			
			value=pstmt.executeUpdate();	// 문제가 없으면 1, 문제 있으면 0
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}
		
		return value;
	}
	
	public int idCheck(String id) {
		
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		int value=0;
		
		try {
			
			String sql="select id from member where id=?";
			conn=ConnectionProvider.getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,id);
			rs=pstmt.executeQuery();
			
			if(rs.next()) value=1;
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
				
		return value;
	}
	
	public ArrayList<ZipcodeDTO> zipcodeReader(String checkDong) {
		
		ArrayList<ZipcodeDTO> arraylist=null;
		
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			
			String sql="select * from zipcode where dong=?";
			conn=ConnectionProvider.getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,checkDong);
			rs=pstmt.executeQuery();
			
			arraylist=new ArrayList<ZipcodeDTO>();
			while(rs.next()) {
				
				ZipcodeDTO address=new ZipcodeDTO();
				address.setZipcode(rs.getString("zipcode"));
				address.setSido(rs.getString("sido"));
				address.setGugun(rs.getString("gugun"));
				address.setDong(rs.getString("dong"));
				address.setRi(rs.getString("ri"));
				address.setBunji(rs.getString("bunji"));
				arraylist.add(address);
				
			}
		
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
		
		
		return arraylist;
		
		
	}
	
	public String loginCheck(String id, String password) {
		
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;		
		String value=null;
		
		try {
			String sql="select member_level from member where id=? and password=?";
			
			conn=ConnectionProvider.getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,id);
			pstmt.setString(2,password);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				value=rs.getString("member_level");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
		
		return value;
	}
	
	public MemberDTO updateId(String id) {
		
		MemberDTO memberdto=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			
			String sql="select * from member where id=?";
			
			conn=ConnectionProvider.getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,id);
			rs=pstmt.executeQuery();
			
			if(rs.next()) { // DB에서 읽어오기
				memberdto=new MemberDTO();
				
				memberdto.setNumber(rs.getInt("num"));
				memberdto.setId(rs.getString("id"));
				memberdto.setPassword(rs.getString("password"));
				memberdto.setName(rs.getString("name"));
				memberdto.setJumin1(rs.getString("jumin1"));
				
				memberdto.setJumin2(rs.getString("jumin2"));
				memberdto.setEmail(rs.getString("email"));
				memberdto.setZipcode(rs.getString("zipcode"));
				memberdto.setAddress(rs.getString("address"));
				memberdto.setJob(rs.getString("job"));
				
				memberdto.setMailing(rs.getString("mailing"));
				memberdto.setMemberLevel(rs.getString("member_level"));
				memberdto.setInterest(rs.getString("interest"));
				
				// 날짜
				/*Timestamp ts=rs.getTimestamp("register_date");
				long time=ts.getTime();
				Date date=new Date(time);
				memberdto.setRegisterDate(date); */
				memberdto.setRegisterDate(new Date(rs.getTimestamp("register_date").getTime()));
			
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
		
		return memberdto;
		
	}
	
	public int update(MemberDTO memberdto) {
		
		Connection conn=null;
		PreparedStatement pstmt=null;	
		int value=0;
		
		try {
			
			String sql="update member set password=?,email=?,zipcode=?,address=?,job=?,mailing=?,interest=? where num=?";
			conn=ConnectionProvider.getConnection();
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1,memberdto.getPassword());
			pstmt.setString(2,memberdto.getEmail());
			pstmt.setString(3,memberdto.getZipcode());
			pstmt.setString(4,memberdto.getAddress());
			pstmt.setString(5,memberdto.getJob());
			pstmt.setString(6,memberdto.getMailing());
			pstmt.setString(7,memberdto.getInterest());
			
			pstmt.setInt(8,memberdto.getNumber());
			
			value=pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn);
			JdbcUtil.close(pstmt);
		}
		
		return value;
		
	}
	
	public int delete(String id, String password) {
		
		Connection conn=null;
		PreparedStatement pstmt=null;
		int value=0;
		
		try {
			
			String sql="delete from member where id=? and password=?";
			conn=ConnectionProvider.getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,id);
			pstmt.setString(2,password);
			
			value=pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn);
			JdbcUtil.close(pstmt);
		}
		return value;
	}

}
