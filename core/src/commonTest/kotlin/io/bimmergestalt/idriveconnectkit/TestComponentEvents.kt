package io.bimmergestalt.idriveconnectkit

import io.bimmergestalt.idriveconnectkit.rhmi.*
import io.bimmergestalt.idriveconnectkit.rhmi.mocking.RHMIApplicationMock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MockEventCallback: EventCallback {
	var callCount = 0
	override fun onHmiEvent(eventId: Int?, args: Map<*, *>?) {
		callCount ++
	}
}
class MockRequestDataCallback: RequestDataCallback {
	val dataRequests = ArrayList<Pair<Int, Int>>()
	override fun onRequestData(startIndex: Int, numRows: Int) {
		dataRequests.add(startIndex to numRows)
	}
}
class MockFocusCallback: FocusCallback {
	var focused: Boolean? = null
	override fun onFocus(focused: Boolean) {
		this.focused = focused
	}
}
class MockVisibleCallback: VisibleCallback {
	var visible: Boolean? = null
	override fun onVisible(visible: Boolean) {
		this.visible = visible
	}
}

class TestComponentEvents {
	val app = RHMIApplicationMock()
	@Test fun testEventCallback() {
		val eventCallback = MockEventCallback()
		val button = RHMIComponent.Button(app, 2)
		button.eventCallback = eventCallback
		button.onHmiEvent(99, mapOf<String, String>())
		assertEquals(1, eventCallback.callCount)
	}

	@Test fun testRequestDataCallback() {
		val requestDataCallback = MockRequestDataCallback()
		val list = RHMIComponent.List(app, 3)
		list.requestDataCallback = requestDataCallback
		list.onHmiEvent(2, mapOf(5.toByte() to 5.toByte(),
				6.toByte() to 10.toByte()))
		assertEquals(1, requestDataCallback.dataRequests.size)
		assertEquals(5 to 10, requestDataCallback.dataRequests[0])

		// assert it doesn't get called if we have a generic handler
		requestDataCallback.dataRequests.clear()
		list.eventCallback = MockEventCallback()
		list.onHmiEvent(2, mapOf(5.toByte() to 5.toByte(),
				6.toByte() to 10.toByte()))
		assertEquals(0, requestDataCallback.dataRequests.size)
	}

	@Test fun testFocusCallback() {
		val focusCallback = MockFocusCallback()
		val list = RHMIComponent.List(app, 3)
		list.focusCallback = focusCallback
		list.onHmiEvent(1, mapOf(4.toByte() to true))
		assertEquals(true, focusCallback.focused)
		list.onHmiEvent(1, mapOf(4.toByte() to false))
		assertEquals(false, focusCallback.focused)

		// assert it doesn't get called if we have a generic handler
		focusCallback.focused = null
		list.eventCallback = MockEventCallback()
		list.onHmiEvent(1, mapOf(4.toByte() to true))
		assertNull(focusCallback.focused)
	}

	@Test fun testVisibleCallback() {
		val visibleCallback = MockVisibleCallback()
		val list = RHMIComponent.List(app, 3)
		list.visibleCallback = visibleCallback
		list.onHmiEvent(11, mapOf(23.toByte() to true))
		assertEquals(true, visibleCallback.visible)
		list.onHmiEvent(11, mapOf(23.toByte() to false))
		assertEquals(false, visibleCallback.visible)

		// assert it doesn't get called if we have a generic handler
		visibleCallback.visible = null
		list.eventCallback = MockEventCallback()
		list.onHmiEvent(11, mapOf(23.toByte() to true))
		assertNull(visibleCallback.visible)
	}
}

class TestStateEvents {
	val app = RHMIApplicationMock()

	@Test fun testEventCallback() {
		val eventCallback = MockEventCallback()
		val state = RHMIState.PlainState(app, 2)
		state.eventCallback = eventCallback
		state.onHmiEvent(99, mapOf<String, String>())
		assertEquals(1, eventCallback.callCount)
	}

	@Test fun testFocusCallback() {
		val focusCallback = MockFocusCallback()
		val state = RHMIState.PlainState(app, 3)
		state.focusCallback = focusCallback
		state.onHmiEvent(1, mapOf(4.toByte() to true))
		assertEquals(true, focusCallback.focused)
		state.onHmiEvent(1, mapOf(4.toByte() to false))
		assertEquals(false, focusCallback.focused)

		// assert it doesn't get called if we have a generic handler
		focusCallback.focused = null
		state.eventCallback = MockEventCallback()
		state.onHmiEvent(1, mapOf(4.toByte() to true))
		assertNull(focusCallback.focused)
	}

	@Test fun testVisibleCallback() {
		val visibleCallback = MockVisibleCallback()
		val state = RHMIState.PlainState(app, 3)
		state.visibleCallback = visibleCallback
		state.onHmiEvent(11, mapOf(23.toByte() to true))
		assertEquals(true, visibleCallback.visible)
		state.onHmiEvent(11, mapOf(23.toByte() to false))
		assertEquals(false, visibleCallback.visible)

		// assert it doesn't get called if we have a generic handler
		visibleCallback.visible = null
		state.eventCallback = MockEventCallback()
		state.onHmiEvent(11, mapOf(23.toByte() to true))
		assertNull(visibleCallback.visible)
	}
}