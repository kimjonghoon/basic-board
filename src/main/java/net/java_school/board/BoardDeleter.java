package net.java_school.board;

import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;

import net.java_school.db.dbpool.*;
import net.java_school.util.*;

public class BoardDeleter extends HttpServlet {

	private static final long serialVersionUID = 664510406708983868L;

	OracleConnectionManager dbmgr = null;

	@Override
	public void init() throws ServletException {
		ServletContext sc = getServletContext();
		dbmgr = (OracleConnectionManager)sc.getAttribute("dbmgr");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		Log log = new Log();

		int no = Integer.parseInt(req.getParameter("no"));
		String curPage = req.getParameter("curPage");

		Connection con = dbmgr.getConnection();
		PreparedStatement stmt = null;
		String sql = "DELETE FROM article WHERE articleno = ?";

		try {
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, no);
			stmt.executeUpdate();
		} catch (SQLException e) {
			log.debug("Error Source: BoardDeleter.java : SQLException");
			log.debug("SQLState: " + e.getSQLState());
			log.debug("Message: " + e.getMessage());
			log.debug("Oracle Error Code: " + e.getErrorCode());
			log.debug("sql: " + sql);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				dbmgr.freeConnection(con);
			}
			log.close();
			String path = req.getContextPath();
			resp.sendRedirect(path + "/board/list.jsp?curPage=" + curPage);
		}
	}
}