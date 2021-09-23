package io.bimmergestalt.idriveconnectkit

import de.bmw.idrive.BMWRemotingClient
import de.bmw.idrive.BMWRemotingHelper
import de.bmw.idrive.BMWRemotingServer
import de.bmw.idrive.RemoteBMWRemotingServer

object IDriveConnection {
	var mockRemotingServer: BMWRemotingServer? = null
	var mockRemotingClient: BMWRemotingClient? = null

	fun getEtchConnection(host: String, port: Int, callbackClient: BMWRemotingClient): BMWRemotingServer {
		val mockRemotingServer = IDriveConnection.mockRemotingServer
		if (mockRemotingServer != null) {
			mockRemotingClient = callbackClient
			return mockRemotingServer
		}

		var uri = "tcp://" + host + ":" + port
		uri = "$uri?Packetizer.maxPktSize=8388608&TcpTransport.noDelay=true"

		val server = BMWRemotingHelper.newServer(uri, null, { _ -> callbackClient })

		server._startAndWaitUp(4000)

		return server
	}

	fun disconnectEtchConnection(server: BMWRemotingServer) {
		if (server is RemoteBMWRemotingServer)
			server._stopAndWaitDown(4000)
	}
}