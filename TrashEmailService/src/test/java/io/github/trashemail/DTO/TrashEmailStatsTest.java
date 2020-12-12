package io.github.trashemail.DTO;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class TrashEmailStatsTest {
	private TrashEmailStats trashEmailStats;
	
	@BeforeEach
	public void setUp() {
		trashEmailStats = new TrashEmailStats();
	}
	
	@Test
	public void gettersNullTest() {
		assertNull(trashEmailStats.getNumberOfUsers());
		assertNull(trashEmailStats.getNumberOfEmailsRegistered());
		assertNull(trashEmailStats.getDomainsToNumbers());
		assertNull(trashEmailStats.getEmailIdsCreatedToday());
		assertNull(trashEmailStats.getEmailIdsCreatedInWeek());
		assertNull(trashEmailStats.getVersion());
		assertNull(trashEmailStats.getNumberOfEmailsProcessed());
		assertNull(trashEmailStats.getTotalNumberOfUsers());
		assertNull(trashEmailStats.getConnectorStats());
	}
	
	@Test
	public void settersTest() {
		Long numberOfUsers = 15l;
		trashEmailStats.setNumberOfUsers(numberOfUsers);
		assertEquals(numberOfUsers, trashEmailStats.getNumberOfUsers());
		
		Long numberOfEmailsRegistered = 100l;
		trashEmailStats.setNumberOfEmailsRegistered(numberOfEmailsRegistered);
		assertEquals(numberOfEmailsRegistered, trashEmailStats.getNumberOfEmailsRegistered());
		
		
		Map<String, Long> domainsToNumbers = new HashMap<String, Long>();
		domainsToNumbers.put("domain1", 15l);
		domainsToNumbers.put("domain2", 25l);
		trashEmailStats.setDomainsToNumbers(domainsToNumbers);
		assertEquals(domainsToNumbers, trashEmailStats.getDomainsToNumbers());
		
		Long emailIdsCreatedToday = 4l;
		trashEmailStats.setEmailIdsCreatedToday(emailIdsCreatedToday);;
		assertEquals(emailIdsCreatedToday, trashEmailStats.getEmailIdsCreatedToday());
		
	    List<Long> emailIdsCreatedInWeek = Arrays.asList(4l, 4l, 6l, 7l, 10l);
	    trashEmailStats.setEmailIdsCreatedInWeek(emailIdsCreatedInWeek);
		assertEquals(emailIdsCreatedInWeek, trashEmailStats.getEmailIdsCreatedInWeek());
		
		String version = "version";
		trashEmailStats.setVersion(version);
		assertEquals(version, trashEmailStats.getVersion());
		
		Long numberOfEmailsProcessed = 70l;
		trashEmailStats.setNumberOfEmailsProcessed(numberOfEmailsProcessed);
		assertEquals(numberOfEmailsProcessed, trashEmailStats.getNumberOfEmailsProcessed());
		
		Long totalNumberOfUsers = 1000l;
		trashEmailStats.setTotalNumberOfUsers(totalNumberOfUsers);
		assertEquals(totalNumberOfUsers, trashEmailStats.getTotalNumberOfUsers());
		
		ConnectorStats connectorStats1 = mock(ConnectorStats.class);
		ConnectorStats connectorStats2 = mock(ConnectorStats.class);
		List<ConnectorStats> connectorStats = Arrays.asList(connectorStats1, connectorStats2);
		trashEmailStats.setConnectorStats(connectorStats);
		assertEquals(connectorStats, trashEmailStats.getConnectorStats());
	}
}