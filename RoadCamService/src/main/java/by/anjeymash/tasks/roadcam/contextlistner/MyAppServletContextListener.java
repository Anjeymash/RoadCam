package by.anjeymash.tasks.roadcam.contextlistner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.anjeymash.tasks.roadcam.dao.connection.ConnectionPool;
import by.anjeymash.tasks.roadcam.dao.exception.ConnectionPoolException;

public class MyAppServletContextListener implements ServletContextListener {
	private static final Logger log = LogManager.getRootLogger();

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("ServletContextListener destroyed");
	}


	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ConnectionPool conPool = ConnectionPool.getInstance();
		try {
			conPool.initPoolData();
		} catch (ConnectionPoolException e) {
			log.error("Exception in Connection Pool", e);
		}
		System.out.println("ServletContextListener started");
	}
}