/*==================
 * BoardDAO.java
 ==================*/

package com.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO
{
	// 주요 속성 구성
	private Connection conn;
	
	// 생성자 정의
	public BoardDAO(Connection conn)
	{
		this.conn = conn;
	}
	
	// 게시물 번호의 최대값 얻어내기
	public int getMaxNum()
	{
		int result = 0;
		String sql = "SELECT NVL(MAX(NUM), 0) AS MAXNUM FROM TBL_BOARD";
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
			rs.close();
			pstmt.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}//end getMaxNum()
	
	// 게시물 작성 (데이터 입력)
	public int insertData(BoardDTO dto)
	{
		int result = 0;

		try
		{
			String sql = "INSERT INTO TBL_BOARD(NUM, NAME, PWD, EMAIL, SUBJECT, CONTENT, IPADDR, HITCOUNT, CREATED) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, 0, SYSDATE)";
			// hitCount는 기본값이 0으로 default 설정이 되어 생략 가능
			// created는 기본값이 sysdate로 default 설정이 되어 생략 가능
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNum());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPwd());
			pstmt.setString(4, dto.getEmail());
			pstmt.setString(5, dto.getSubject());
			pstmt.setString(6, dto.getContent());
			pstmt.setString(7, dto.getIpAddr());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}//end insertData()
	
	// DB 레코드의 갯수를 가져오는 메소드 정의
	// → 검색 기능을 작업하게 되면 수정하게 될 메소드
	public int getDataCount()
	{
		int result = 0;
		
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT COUNT(*) AS COUNT FROM TBL_BOARD";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
				result = rs.getInt(1);
			
			rs.close();
			stmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}//end getDataCount()
	
	// 특정 영역의(시작번호 ~ 끝번호) 게시물의 목록을 읽어오는 메소드 정의
	// → 검색 기능을 작업하게 되면 수정하게 될 메소드
	public List<BoardDTO> getLists(int start, int end)
	{
		List<BoardDTO> result = new ArrayList<BoardDTO>();
		
		try
		{
			String sql = "SELECT NUM, NAME, SUBJECT, HITCOUNT, CREATED "
					+ "FROM ( SELECT ROWNUM RNUM, DATA.* "
					+ "FROM ( SELECT NUM, NAME, SUBJECT, HITCOUNT, TO_CHAR(CREATED, 'YYYY-MM-DD') AS CREATED "
					+ "FROM TBL_BOARD ORDER BY NUM DESC ) DATA ) WHERE RNUM>=? AND RNUM<=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getInt(1));
				dto.setName(rs.getString(2));
				dto.setSubject(rs.getString(3));
				dto.setHitCount(rs.getInt(4));
				dto.setCreated(rs.getString(5));
				
				result.add(dto);
			}

			rs.close();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}//end getLists(int start, int end)
	
	// 특정 게시물 조회에 따른 조회 횟수 증가 메소드 정의
	public int updateHitCount(int num)
	{
		int result = 0;
		
		try
		{
			String sql = "UPDATE TBL_BOARD SET HITCOUNT = HITCOUNT+1 WHERE NUM=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}//end updateHitCount(int num)
	
	// 특정 게시물의 내용을 읽어오는 메소드 정의
	public BoardDTO getReadData(int num)
	{
		BoardDTO result = new BoardDTO();
		
		try
		{
			String sql = "SELECT NUM, NAME, PWD, EMAIL, SUBJECT, CONTENT, IPADDR, HITCOUNT"
					+ ", TO_CHAR(CREATED, 'YYYY-MM-DD') AS CREATED FROM TBL_BOARD WHERE NUM=?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				result.setNum(rs.getInt(1));
				result.setName(rs.getString(2));
				result.setPwd(rs.getString(3));
				result.setEmail(rs.getString(4));
				result.setSubject(rs.getString(5));
				result.setContent(rs.getString(6));
				result.setIpAddr(rs.getString(7));
				result.setHitCount(rs.getInt(8));
				result.setCreated(rs.getString(9));
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}//end getReadData()
	
	// 특정 게시물을 삭제하는 기능의 메소드
	public int deleteData(int num)
	{
		int result = 0;
		
		try
		{
			String sql = "DELETE FROM TBL_BOARD WHERE NUM=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}//end deleteData()
	
	// 특정 게시물의 내용을 수정하는 메소드 정의
	public int updateData(BoardDTO dto)
	{
		int result = 0;
		
		try
		{
			String sql = "UPDATE TBL_BOARD SET NAME=?, PWD=?, EMAIL=?, SUBJECT=?, CONTENT=? WHERE NUM=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setInt(6, dto.getNum());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}//end updateData()
	
	// 특정 게시물의 이전 게시물 번호 얻어내는 메소드 정의
	public int getBeforeNum(int num)
	{
		int result = 0;
		
		try
		{
			String sql = "SELECT NVL(MAX(NUM), -1) AS PRENUM FROM TBL_BOARD WHERE NUM<?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
			rs.close();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	// 특정 게시물의 다음 게시물 번호 얻어내는 메소드 정의
	public int getNextNum(int num)
	{
		int result = 0;
		
		try
		{
			String sql = "SELECT NVL(MIN(NUM), -1) AS NEXTNUM FROM TBL_BOARD WHERE NUM>?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
			rs.close();
			pstmt.close();
			
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}
		
		return result;
	}//end getNextNum()
	
	
	
	
}
