package me.hufman.idriveconnectionkit

import junit.framework.TestCase.*
import me.hufman.idriveconnectionkit.rhmi.*
import me.hufman.idriveconnectionkit.rhmi.mocking.RHMIApplicationMock
import org.junit.Test

// Allow nested nullable access
operator fun <K, V> Map<K, V>?.get(key: K) = this?.get(key)

class TestRHMIApplication {
	@Test fun mockStates() {
		val app = RHMIApplicationMock()
		val state = app.states[10]
		assertTrue(state is RHMIState.MockState)
		assertEquals(10, state.id)

		state.textModel = 12
		assertNotNull(state.getTextModel())
		assertEquals(12, state.getTextModel()?.id)

		assertEquals(null, app.propertyData[state.id][24])
		state.setProperty(24, 3)
		assertEquals(3, app.propertyData[state.id][24])

		// test that it was memoized
		state.asAudioState()?.trackTextModel = 50
		assertTrue(app.states[10] is RHMIState.AudioHmiState)
		assertEquals(50, app.states[10].asAudioState()?.trackTextModel)
	}

	@Test fun mockModels() {
		val app = RHMIApplicationMock()
		val model = app.models[10]
		assertTrue(model is RHMIModel.MockModel)
		assertEquals(10, model.id)
		val imageModel = model.asImageIdModel() as RHMIModel.ImageIdModel
		assertEquals(10, imageModel.id)
		assertEquals(0, imageModel.imageId)
		assertTrue(app.models[10] is RHMIModel.ImageIdModel)
		val intModel = model.asRaIntModel() as RHMIModel.RaIntModel
		assertEquals(0, intModel.value)
		assertEquals(null, app.modelData[model.id])
		intModel.value = 50
		assertEquals(50, app.modelData[model.id])

		// test that it was memoized
		assertTrue(app.models[10] is RHMIModel.RaIntModel)
		assertEquals(50, app.models[10].asRaIntModel()?.value)
	}

	@Test fun mockActions() {
		val app = RHMIApplicationMock()
		val hmiAction = app.actions[2].asHMIAction() as RHMIAction.HMIAction
		hmiAction.targetModel = 5
		assertNotNull(hmiAction.getTargetModel())
		assertEquals(5, hmiAction.getTargetModel()?.id)
		val combinedAction = RHMIAction.CombinedAction(app, 3, RHMIAction.RAAction(app, 2), hmiAction)
		assertEquals(5, combinedAction.hmiAction?.getTargetModel()?.id)

		// test that it was memoized
		assertTrue(app.actions[2] is RHMIAction.HMIAction)
		assertEquals(5, app.actions[2].asHMIAction()?.targetModel)
	}

	@Test fun mockComponents() {
		val app = RHMIApplicationMock()
		val component = app.components[10]
		val button = component.asButton() as RHMIComponent.Button
		assertEquals(10, button.id)
		button.tooltipModel = 51
		assertEquals(51, button.getTooltipModel()?.id)

		assertEquals(null, app.propertyData[button.id][RHMIProperty.PropertyId.VISIBLE.id])
		button.setVisible(true)
		assertEquals(true, app.propertyData[button.id][RHMIProperty.PropertyId.VISIBLE.id])

		button.setEnabled(false)
		assertEquals(false, app.propertyData[button.id][RHMIProperty.PropertyId.ENABLED.id])

		button.setSelectable(false)
		assertEquals(false, app.propertyData[button.id][RHMIProperty.PropertyId.SELECTABLE.id])

		button.setProperty(RHMIProperty.PropertyId.LIST_COLUMNWIDTH, "0,100,*")
		assertEquals("0,100,*", app.propertyData[button.id][RHMIProperty.PropertyId.LIST_COLUMNWIDTH.id])

		// test that it was memoized
		assertTrue(app.components[10] is RHMIComponent.Button)
		assertEquals(51, app.components[10].asButton()?.tooltipModel)
	}

	@Test fun mockEvents() {
		val app = RHMIApplicationMock()
		val event = app.events[6].asFocusEvent() as RHMIEvent.FocusEvent
		event.targetModel = 10
		event.triggerEvent(mapOf(0 to 20))
		assertEquals(1, app.triggeredEvents.size)
		assertEquals(mapOf(0 to 20), app.triggeredEvents[6])

		// test that it was memoized
		assertTrue(app.events[6] is RHMIEvent.FocusEvent)
		assertEquals(10, app.events[6].asFocusEvent()?.targetModel)
	}
}