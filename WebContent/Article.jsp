<%@page import="com.test.BoardDTO"%>
<%@page import="com.test.BoardDAO"%>
<%@page import="com.util.DBConn"%>
<%@page import="java.sql.Connection"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
%>
<%
	Connection conn = DBConn.getConnection();
	BoardDAO dao = new BoardDAO(conn);
	
	// 이전 페이지(List.jsp → 목록 페이지)로부터 데이터(num,pageNum) 수신
	String pageNum = request.getParameter("pageNum");		// 페이지 번호
	String strNum = request.getParameter("num");			// 게시물 번호
	int num = Integer.parseInt(strNum);
	
	// 해당 게시물의 조회수 증가
	dao.updateHitCount(num);
	
	// 이전, 다음 게시물 번호 확인
	int beforeNum = dao.getBeforeNum(num);
	int nextNum = dao.getNextNum(num);
	
	BoardDTO dtoBefore = null;
	BoardDTO dtoNext = null;
	
	if (beforeNum != -1)	// 이전 게시물이 존재하지 않으면 dao.getBeforeNum(num)은 -1을 반환함
		dtoBefore = dao.getReadData(beforeNum);
	if (nextNum != -1)
		dtoNext = dao.getReadData(nextNum);
	
	// 해당 게시물의 상세 내용 가져오기
	BoardDTO dto = dao.getReadData(num);
	
	if (dto==null)	// 없는 게시글을 요청했을 때 리다이렉팅
		response.sendRedirect("List.jsp");
	
	
	// 게시물 본문 라인 수 확인
	int lineSu = dto.getContent().split("\n").length;
	// dto.getContent().split("\n") 은 \n를 기준으로 쪼개진 String을 담아두는 배열
	
	// 게시물 내용
	dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
	// 기존의 내용을 getter로 받아와 개행을 하고 다시 setter 해주는 구조
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Article.jsp</title>
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/style.css">
<link rel="stylesheet" type="text/css" href="<%=cp%>/css/article.css">
</head>
<body>

<div id="bbs">
	<div id="bbs_title">게 시 판 (JDBC 연동 버전)
	</div>
	
	<div id="bbsArticle">
		<div id="bbsArticle_header">
			<!-- 게시물의 제목입니다. -->
			<%=dto.getSubject() %>
		</div>
		
		<div class="bbsArticle_bottomLine">
			<dl>
				<dt>작성자</dt>
				<dd><!-- 정미화 --><%=dto.getName() %></dd>
				<dt>라인수</dt>
				<dd><!-- 1 --><%=lineSu %></dd>
			</dl>
		</div>
		
		<div class="bbsArticle_bottomLine">
			<dl>
				<dt>등록일</dt>
				<dd><!-- 2021-11-05 --><%=dto.getCreated() %></dd>
				<dt>조회수</dt>
				<dd><!-- 251 --><%=dto.getHitCount() %></dd>
			</dl>
		</div>
		
		<div id="bbsArticle_content">
			<table style="width: 600;">
				<tr>
					<td style="padding: 10px 40px 10px 10px; vertical-align: top; height: 150;">
					<!-- 어쩌구 저쩌구 -->
					<%=dto.getContent() %></td>
				</tr>
			</table>
		</div>
		
		<div class="bbsArticle_bottomLine">
			<!-- 이전글 : (104) 취미 분야 게시물 -->
			<%
			if (beforeNum != -1)
			{
			%>
			<a href='<%=cp%>/Article.jsp?pageNum=<%=pageNum %>&num=<%=beforeNum %>'>이전글 : (<%=beforeNum %>) <%=dtoBefore.getSubject() %></a>
			<%
			} else {
			%>
			이전글 : 없음
			<%
			}
			%>
		</div>
		
		<div class="bbsArticle_noLine">
			<!-- 다음글 : (106) 학습 분야 게시물 -->
			<%
			if (nextNum != -1)
			{
			%>
			<a href='<%=cp%>/Article.jsp?pageNum=<%=pageNum %>&num=<%=nextNum %>'>다음글 : (<%=nextNum %>) <%=dtoNext.getSubject() %></a>
			<%
			} else {
			%>
			다음글 : 없음
			<%
			}
			%>
		</div>
	</div><!-- close #bbsArticle -->
	
	<div class="bbsArticle_noLine" style="text-align: right;">
		<!-- From : 211.238.142.165 -->
		From : <%=dto.getIpAddr() %>
	</div>
	
	<div id="bbsArticle_footer">
		<div id="leftFooter">
			<input type="button" value="수정" class="btn2" 
			onclick="javascript:location.href='<%=cp%>/Updated.jsp?num=<%=dto.getNum()%>&pageNum=<%=pageNum%>'">
			<input type="button" value="삭제" class="btn2" 
			onclick="javascript:location.href='<%=cp%>/Delete.jsp?num=<%=dto.getNum()%>'">
		</div>
		
		<div id="rightFooter">
			<input type="button" value="리스트" class="btn2" 
			onclick="javascript:location.href='<%=cp%>/List.jsp?pageNum=<%=pageNum%>'">
		</div>
	</div><!-- close #bbsArticle_footer -->

</div><!-- close #bbs -->

</body>
</html>