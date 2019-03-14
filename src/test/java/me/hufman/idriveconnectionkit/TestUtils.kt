package me.hufman.idriveconnectionkit

import org.junit.Test

import org.junit.Assert.*

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
}