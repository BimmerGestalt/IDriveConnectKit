package io.bimmergestalt.idriveconnectkit

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BaseBMWRemotingServer
import io.bimmergestalt.idriveconnectkit.RHMIUtils.rhmi_setResourceCached
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream

class TestRHMIUtils {
	@Test
	fun testRhmiUncached() {
		val mockServer = MockBMWRemoteServer()

		val dataArray = ByteArray(64)
		val data = ByteArrayInputStream(dataArray)
		mockServer.rhmi_setResourceCached(40, BMWRemoting.RHMIResourceType.DESCRIPTION, data)
		Assert.assertEquals(40, mockServer.handle)
		Assert.assertEquals(64, mockServer.size)
		Assert.assertEquals("", mockServer.name)
		Assert.assertEquals(BMWRemoting.RHMIResourceType.DESCRIPTION, mockServer.type)
		Assert.assertArrayEquals(dataArray, mockServer.resourceData)
	}
	@Test
	fun testRhmiCached() {
		val mockServer = MockBMWRemoteServer()
		mockServer.isCached = true

		val dataArray = ByteArray(64)
		val data = ByteArrayInputStream(dataArray)
		mockServer.rhmi_setResourceCached(40, BMWRemoting.RHMIResourceType.DESCRIPTION, data)
		Assert.assertEquals(40, mockServer.handle)
		Assert.assertEquals(64, mockServer.size)
		Assert.assertEquals("", mockServer.name)
		Assert.assertEquals(BMWRemoting.RHMIResourceType.DESCRIPTION, mockServer.type)
		Assert.assertEquals(null, mockServer.resourceData)
	}
}


class MockBMWRemoteServer: BaseBMWRemotingServer() {
	var hash: ByteArray? = null
	var handle: Int? = null
	var size: Int? = null
	var name: String? = null
	var type: BMWRemoting.RHMIResourceType? = null
	var isCached = false
	// whether a resource was pushed
	var resourceData: ByteArray? = null
	override fun rhmi_checkResource(
		hash: ByteArray?,
		handle: Int?,
		size: Int?,
		name: String?,
		type: BMWRemoting.RHMIResourceType?
	): Boolean {
		this.hash = hash
		this.handle = handle
		this.size = size
		this.name = name
		this.type = type
		return isCached
	}

	override fun rhmi_setResource(
		handle: Int?,
		data: ByteArray?,
		type: BMWRemoting.RHMIResourceType?
	) {
		resourceData = data
	}
}