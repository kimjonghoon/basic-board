package net.java_school.db.dbpool;

import java.sql.*;
import java.util.*;

import net.java_school.util.Log;

public class DBConnectionPoolManager {
	static private DBConnectionPoolManager instance;
	private Vector<String> drivers = new Vector<String>();
	private Hashtable<String, DBConnectionPool> pools = new Hashtable<String, DBConnectionPool>();

	static synchronized public DBConnectionPoolManager getInstance() {
		if (instance == null) {
			instance = new DBConnectionPoolManager();
		}

		return instance;
	}

	private DBConnectionPoolManager() {}

	// Send Connection to Free List
	// @param name : Pool Name
	// @param con : Connection
	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(con);
		}

		Log.out("One Connection of " + name + " was freed");
	}

	// Obtain an Open Connection.
	// Creates a new connection when there is no currently open connection and the maximum number of connections is not in use.
	// Waits for the default wait time when there are no open connections currently and the maximum number of connections is in use
	// @param name : Pool Name
	// @return Connection : The connection or null
	public Connection getConnection(String name) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(10);
		}
		return null;
	}

	// Create new Connection Pool
	// @param poolName : Pool Name to create
	// @param url : DB URL
	// @param user : DB UserID
	// @param password : DB Password
	private void createPools(String poolName, 
			String url, 
			String user,
			String password, 
			int maxConn, 
			int initConn, 
			int maxWait) {

		DBConnectionPool pool = new DBConnectionPool(poolName, url, user, password, maxConn, initConn, maxWait);
		pools.put(poolName, pool);
		Log.out("Initialized pool " + poolName);
	}

	// Initialization
	public void init(String poolName, 
			String driver, 
			String url,
			String user, 
			String passwd, 
			int maxConn, 
			int initConn, 
			int maxWait) {

		loadDrivers(driver);
		createPools(poolName, url, user, passwd, maxConn, initConn, maxWait);
	}

	// JDBC Driver Loading
	// @param driverClassName
	private void loadDrivers(String driverClassName) {
		try {
			Class.forName(driverClassName);
			drivers.addElement(driverClassName);
			Log.out("Registered JDBC driver " + driverClassName);
		} catch (Exception e) {
			Log.err(e, "Can't register JDBC driver: " + driverClassName);
		}
	}

	public Hashtable<String,DBConnectionPool> getPools() {
		return pools;
	}

	public int getDriverNumber() {
		return drivers.size();
	}

}