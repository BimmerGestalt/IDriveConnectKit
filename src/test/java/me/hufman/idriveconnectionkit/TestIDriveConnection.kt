package me.hufman.idriveconnectionkit

import de.bmw.idrive.BaseBMWRemotingClient
import de.bmw.idrive.BaseBMWRemotingServer
import org.junit.Test

import org.junit.Assert.*

class TestIDriveConnection {

	class MockServer: BaseBMWRemotingServer()
	class MockClient: BaseBMWRemotingClient()

	@Test
	fun testMockEtchConnection() {
		/**
		 * Verifies that the IDriveConnection helper can return a mock connection
		 */
		val mockServer = MockServer()
		val mockClient = MockClient()
		IDriveConnection.mockRemotingServer = mockServer
		val connected = IDriveConnection.getEtchConnection("unused", 5, mockClient)
		assertEquals(mockServer, connected)
		assertEquals(mockClient, IDriveConnection.mockRemotingClient)
	}
}