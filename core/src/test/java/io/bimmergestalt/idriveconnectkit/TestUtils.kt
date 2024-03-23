package io.bimmergestalt.idriveconnectkit

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BaseBMWRemotingServer
import io.bimmergestalt.idriveconnectkit.Utils.etchAsInt
import io.bimmergestalt.idriveconnectkit.Utils.rhmi_setResourceCached
import org.junit.Test

import org.junit.Assert.*
import java.io.ByteArrayInputStream

class TestUtils {

	@Test
	fun testEtchByteAsInt() {
		val wasByte = etchAsInt(4.toByte())
		assertTrue(wasByte is Int)
		assertEquals(4, wasByte)
	}

	@Test
	fun testEtchShortAsInt() {
		val wasShort = etchAsInt(4.toShort())
		assertTrue(wasShort is Int)
		assertEquals(4, wasShort)
	}

	@Test
	fun testEtchIntAsInt() {
		val wasInt = etchAsInt(4.toInt())
		assertTrue(wasInt is Int)
		assertEquals(4, wasInt)
	}

	@Test
	fun testEtchStringAsInt() {
		val wasString = etchAsInt("string")
		assertTrue(wasString is Int)
		assertEquals(0, wasString)

		val wasStringDefault = etchAsInt("string", 5)
		assertTrue(wasStringDefault is Int)
		assertEquals(5, wasStringDefault)
	}

	@Test
	fun testRhmiUncached() {
		val mockServer = MockBMWRemoteServer()

		val dataArray = ByteArray(64)
		val data = ByteArrayInputStream(dataArray)
		mockServer.rhmi_setResourceCached(40, BMWRemoting.RHMIResourceType.DESCRIPTION, data)
		assertEquals(40, mockServer.handle)
		assertEquals(64, mockServer.size)
		assertEquals("", mockServer.name)
		assertEquals(BMWRemoting.RHMIResourceType.DESCRIPTION, mockServer.type)
		assertArrayEquals(dataArray, mockServer.resourceData)
	}
	@Test
	fun testRhmiCached() {
		val mockServer = MockBMWRemoteServer()
		mockServer.isCached = true

		val dataArray = ByteArray(64)
		val data = ByteArrayInputStream(dataArray)
		mockServer.rhmi_setResourceCached(40, BMWRemoting.RHMIResourceType.DESCRIPTION, data)
		assertEquals(40, mockServer.handle)
		assertEquals(64, mockServer.size)
		assertEquals("", mockServer.name)
		assertEquals(BMWRemoting.RHMIResourceType.DESCRIPTION, mockServer.type)
		assertEquals(null, mockServer.resourceData)
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