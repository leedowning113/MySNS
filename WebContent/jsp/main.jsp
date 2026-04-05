<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="dao.*" %>

<%
	
	/*
	String sql = "SELECT * FROM feed ORDER BY ts DESC";
	
	Connection conn = ConnectionPool.get();
	PreparedStatement stmt = conn.prepareStatement(sql);
	ResultSet rs = stmt.executeQuery();
	*/
	
 	String str = (new FeedDAO()).getList();//getList는 데이터베이스 이차원테이블인 ResultSet을 리턴해야함.
	
	/*
	String str = "<table align=center>";
	str += "<tr height=40><td><b>작성글 리스트</b></td>";
	str += "<td align=right>";
	str += "<a href='feedAdd.html'><button>글쓰기</button></a>";
	str += "</td></tr>";
	//str += "<tr><th colspan=2>작성글 리스트</th></tr>";
	//for(int i=0;i<feeds.size();i++) {
	//FeedObj feed = feeds.get(i);
	for(FeedObj feed : feeds){
		String img = feed.getImages(), imgstr = "";
		if (img != null) {
		imgstr = "<img src='images/" + img + "' width=300>";
		}


		str += "<tr><td colspan=2><hr></td></tr>";
		str += "<tr>";
		str += "<td><small>" + feed.getId() + "</small></td>";
		str += "<td><small>&nbsp;(" + feed.getTs() + ")</small></td>";
		str += "</tr>";
		str += "<tr><td colspan=2>" + imgstr + "</td></tr>";
		str += "<tr><td colspan=2>" + feed.getContent() + "</td></tr>";
	}
	str += "</table>";
	*/
	
	out.print(str);
	
%>