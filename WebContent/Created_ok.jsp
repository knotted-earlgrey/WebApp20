<%@page import="com.test.BoardDTO"%>
<%@page import="com.util.DBConn"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.test.BoardDAO"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	//Created_ok.jsp
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
%>
<jsp:useBean id="dto" class="com.test.BoardDTO" scope="page"></jsp:useBean>
<jsp:setProperty property="*" name="dto"/>
<%
	/*	jsp:setProperty를 통해 이전 페이지들의 값 다 받아옴
	
	String subject = request.getParameter("subject");
	String name = request.getParameter("name");
	String email = request.getParameter("email");
	String content = request.getParameter("content");
	String pwd = request.getParameter("pwd"); 
	*/
	
	// 수업시간엔 try-catch문 사용하지 않음
	try
	{
		Connection conn = DBConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		
		// 게시물 현재 상태의 최대값 얻어오기
		int num = dao.getMaxNum();
		
		/* 		
		BoardDTO dto = new BoardDTO();
		dto.setNum(num);
		dto.setSubject(subject);
		dto.setName(name);
		dto.setEmail(email);
		dto.setContent(content);
		dto.setPwd(pwd);
		*/
		
		// 게시물 번호 최대값에 1을 더해서 set하는 과정
		dto.setNum(num+1);
		
		// IP Address 확인 → request.getRemoteAddr(); : 클라이언트(브라우저)의 IP Address 확인
		dto.setIpAddr(request.getRemoteAddr());
		
		
		// 게시물 작성 메소드 실행
		dao.insertData(dto);
		
	}
	catch (Exception e)
	{
		System.out.println(e.toString());
	}
	finally
	{
		DBConn.close();
	}
	
	// 리다이렉팅
	response.sendRedirect(cp+"/List.jsp");
%>