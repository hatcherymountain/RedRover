package com.askredrover.ventures;

import javax.servlet.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DailyCheckinService implements ServletContextListener {

	private ScheduledExecutorService scheduler;

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		scheduler.shutdownNow();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		// scheduler.scheduleAtFixedRate(new Reminders(event), 0, 1, TimeUnit.HOURS);
		scheduler.scheduleAtFixedRate(new Checkin(event), 0, 6, TimeUnit.HOURS);
	}

}