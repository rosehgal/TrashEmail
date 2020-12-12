package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TelegramConnectorStatsTest {
	private TelegramConnectorStats telegramConnectorStats; 
	
	@BeforeEach
	public void setUp() {
		telegramConnectorStats = new TelegramConnectorStats();
	}
	
	@Test
	public void gettersNullTest() {
		assertNull(telegramConnectorStats.getActiveUsers());
		assertNull(telegramConnectorStats.getTotalNumberOfUsers());
		assertNull(telegramConnectorStats.getActiveEmailIds());
		assertNull(telegramConnectorStats.getTotalNumberOfEmailIds());
	}
	
	@Test
	public void settersTest() {
		Long activeUsers = new Long(20);
		telegramConnectorStats.setActiveUsers(activeUsers);
		assertEquals(activeUsers, telegramConnectorStats.getActiveUsers());
		
		Long totalNumberOfUsers = new Long(150);
		telegramConnectorStats.setTotalNumberOfUsers(totalNumberOfUsers);
		assertEquals(totalNumberOfUsers, telegramConnectorStats.getTotalNumberOfUsers());
		
		Long activeEmailIds = new Long(15);
		telegramConnectorStats.setActiveEmailIds(activeEmailIds);
		assertEquals(activeEmailIds, telegramConnectorStats.getActiveEmailIds());
		
		Long totalNumberOfEmailIds = new Long(100);
		telegramConnectorStats.setTotalNumberOfEmailIds(totalNumberOfEmailIds);
		assertEquals(totalNumberOfEmailIds, telegramConnectorStats.getTotalNumberOfEmailIds());
	}
}