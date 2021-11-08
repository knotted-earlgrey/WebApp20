/*====================
 * MyUtil.java
 * 게시판 페이징 처리
 ====================*/

// 지금 같이 확인해보고자 하는 페이징 처리 기법은 다양한 방법들 중 한 가지(그나마 쉬운 것)
// 학습을 마친 이후에 꼭 추가적으로 개념을 정리하고 확장시키고, 다른 방법도 찾아보고 공부하기


package com.util;

public class MyUtil
{
	// ■ 전체 페이지 수를 구하는 메소드
	public int getPageCount(int numPerPage, int dataCount)
	{
		int pageCount=0;
		
		pageCount = dataCount/numPerPage;
		
		if (dataCount % numPerPage != 0)
			pageCount++;
		
		return pageCount;
	}
	// 한 페이지에 10개의 게시물을 출력할 때, 총 32개의 게시물이 있다고 가정하면
	// 32/10의 연산 결과에다 +1을 해 줘야 30개의 게시물 + 2개의 게시물을 전부 나타낼 수 있다.
	// pageCount = dataCount(32)/numPerPage(10) +1
	// 게시물이 30개라면 +1을 해주지 않아도 되므로 if문 처리
	
	// ■ 페이징 처리 기능의 메소드
	// currentPage : 현재 표시할 페이지
	// totalPage : 전체 페이지 수
	// listUrl : 링크를 설정할 url
	public String pageIndexList(int currentPage, int totalPage, String listUrl)
	{
		// 실제 페이징을 저장할 StringBuffer 변수 
		StringBuffer strList = new StringBuffer();
		
		int numPerBlock = 10;
		// 페이징 처리 시 게시물 리스트 하단의 숫자를 10개씩 보여주겠다.
		int currentPageSetup;
		// 현재 페이지(이 페이지를 기준으로 보여주는 숫자가 달라짐)
		int page;
		int n;
		// 이전 페이지 블럭과 같은 처리에서 이동하기 위한 변수
		// (만약 6페이지에서 next▶ 를 눌러 10페이지 이동하면 16페이지가 나오도록)
		
		// 페이징 처리가 별도로 필요하지 않은 경우
		// (데이터가 존재하지 않거나, 데이터의 수가 1페이지도 못 채우는 경우는
		// 별도로 페이징 처리를 할 필요가 없다)
		if (currentPage==0)
			return "";
		
		// ※ 페이지 요청을 처리하는 과정에서 URL 경로의 패턴에 대한 처리
		/*
		 	① 클라이언트 요청의 형태가 List.jsp
		 	→ List.jsp + 『?』
		 	
		 	② 클라이언트 요청의 형태가 List.jsp?subject(키)=study(값)
		 	→ List.jsp?subject=study + 『&』
		 */
		
		// ○ 링크를 설정할 URL에 대한 선가공 처리
		if (listUrl.indexOf("?") != -1) // 링크 설정할 URL에 『?』가 들어있다면
		{
			listUrl = listUrl + "&";	// ②번의 상황 List.jsp?키=값 + 『&』
		}
		else
		{
			listUrl = listUrl + "?";	// ①번의 상황 List.jsp + 『?』
		}
		// 예를 들어 검색값이 존재하면
		// 이미 request 값에 searchKey 값과 searchValue 값이 들어있는 상황이므로
		// 『&』를 붙여서 추가해 주어야 한다.
		
		// currentPageSetup = 표시할 첫 페이지 -1
		currentPageSetup = (currentPage / numPerBlock) * numPerBlock;
		// 만약 현재 페이지가 5페이지, 리스트 하단에 보여줄 페이지 갯수가 10이면
		// 『5/10 = 0』이며, 여기에 *10을 하면 0이다.
		// 하지만, 현재 페이지가 11페이지라면 『11/10=1』이며, *10을 하면 10이 된다.
		// 그러면 currentPageSetup은 10이 되는 것이다.
		
		/* currentPageSetup은 현재 표시되는 페이지 목록들의 번호대라고 생각하면 될듯...?
		1페이지~10페이지까지는 0번대, 11페이지~20페이지는 10번대, ... */
		
		if (currentPage % numPerBlock == 0)
		{
			currentPageSetup = currentPageSetup - numPerBlock;
			// 현재 페이지가 20, 리스트 하단에 보여줄 페이지 갯수가 10이면
			// 이 페이지의 번호대는 10번대여야 하는데 연산 결과 20이 나옴(20/10*10)
			// 따라서 페이지 갯수만큼 빼준다
		}
		
		
		// 1 페이지
		if ( (totalPage>numPerBlock) && (currentPageSetup>0) )
		{
			strList.append(" <a href='" + listUrl + "pageNum=1'>1</a>");
		}
		// listUrl은 라인 64에서 이미 전처리가 끝난 상황이므로
		// 끝에 오는 글자가 『?』나 『&』이므로 결과는 『?pageNum=1』 or 『&pageNum=1』
		
		
		// Prev
		n = currentPage - numPerBlock;
		// n : 해당 페이지만큼 앞으로 가기 위한 변수
		if ( (totalPage>numPerBlock) && (currentPageSetup>0) )
		{
			strList.append(" <a href='" + listUrl + "pageNum=" + n + "'>Prev</a>");
		}
		// currentPageSetup이 0보다 큰 경우는 이미 페이지가 10 이상이라는 의미,
		// 이 때 현재 페이지(currentPage)가 11 이상일 경우 『Prev』 를 붙이기 위한 구문.
		// Prev 를 클릭하면 n 변수 페이지로 이동하는데,
		// 12페이지에서 Prev 클릭 시 2페이지로, 35페이지에서 Prev 클릭 시 25페이지로 이동

		
		// 각 페이지 바로가기
		page = currentPageSetup + 1;
		/* page는 반복문으로 표시할 숫자 */
		/* 10번대의 페이지 10개(11~20페이지) 출력한다고 하면 currentPageSetup은 10이고
		page=10+1부터 page++하여 20까지 출력 */
		
		while ( (page<=totalPage) && (page<=currentPageSetup+numPerBlock) )
		// 없는 페이지는 출력하지 않음 && 10번대 페이지를 출력한다면 21 페이지는 출력되지 않기
		{
			if (page==currentPage)
			{
				strList.append(" <span style='color:orange; font-weight:bold;'>" + page + "</span>");
			}
			else
			{
				strList.append(" <a href='" + listUrl + "pageNum=" + page + "'>" + page + "</a>");
			}
			
			page++;
		}
		
		// Next
		n = currentPage + numPerBlock;
		if ( (totalPage-currentPageSetup) > numPerBlock )
		{
			strList.append(" <a href='" + listUrl + "pageNum=" + n + "'>Next</a>");
		}
		
		// 마지막 페이지
		if ( (totalPage>numPerBlock) && (currentPageSetup+numPerBlock)<totalPage )
		{
			strList.append(" <a href='" + listUrl + "pageNum=" + totalPage + "'>" + totalPage + "</a>");
		}
		
		
		return strList.toString();
	}//end pageIndexList(int currentPage, int totalPage, String listUrl)
	
	
}
