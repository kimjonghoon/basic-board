package net.java_school.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import net.java_school.db.dbpool.OracleConnectionManager;

public class MyServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		OracleConnectionManager dbmgr = new OracleConnectionManager();
		sc.setAttribute("dbmgr", dbmgr);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		sc.removeAttribute("dbmgr");
	}

}