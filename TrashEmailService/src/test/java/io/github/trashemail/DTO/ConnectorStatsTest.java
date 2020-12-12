package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConnectorStatsTest {
	private ConnectorStats connectorStats;

	@BeforeEach
	public void setUp() {
		connectorStats = new ConnectorStats();
	}
	
	@Test
	public void gettersNullTest() {
		assertNull(connectorStats.getConnectorName());
		assertNull(connectorStats.getActiveUsers());
		assertNull(connectorStats.getTotalNumberOfUsers());
		assertNull(connectorStats.getActiveEmailIds());
		assertNull(connectorStats.getTotalNumberOfEmailIds());
	}
	
	@Test
	public void settersTest() {
		String connectorName = "Name";
		connectorStats.setConnectorName(connectorName);
		assertEquals(connectorName, connectorStats.getConnectorName());
		
		Long activeUsers = new Long(100);
		connectorStats.setActiveUsers(activeUsers);
		assertEquals(activeUsers, connectorStats.getActiveUsers());
		
	    Long totalNumberOfUsers = new Long(54580);
	    connectorStats.setTotalNumberOfUsers(totalNumberOfUsers);
		assertEquals(totalNumberOfUsers, connectorStats.getTotalNumberOfUsers());
		
	    Long activeEmailIds = new Long(24);
	    connectorStats.setActiveEmailIds(activeEmailIds);
		assertEquals(activeEmailIds, connectorStats.getActiveEmailIds());
		
	    Long totalNumberOfEmailIds = new Long(22300);
	    connectorStats.setTotalNumberOfEmailIds(totalNumberOfEmailIds);
		assertEquals(totalNumberOfEmailIds, connectorStats.getTotalNumberOfEmailIds());
	}
}