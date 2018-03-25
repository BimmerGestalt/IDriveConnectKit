package me.hufman.idriveconnectionkit

import junit.framework.TestCase.*
import me.hufman.idriveconnectionkit.XMLUtils
import me.hufman.idriveconnectionkit.rhmi.RHMIAction
import me.hufman.idriveconnectionkit.rhmi.RHMIComponent
import me.hufman.idriveconnectionkit.rhmi.RHMIModel
import me.hufman.idriveconnectionkit.rhmi.mocking.RHMIApplicationMock
import org.junit.Test

class TestRHMIApplication {
	@Test fun mockModels() {
		val app = RHMIApplicationMock()
		val model = app.models[10]
		assertTrue(model is RHMIModel)
		assertTrue(model is RHMIModel.MockModel)
		assertEquals(10, model.id)
		val imageModel = model.asImageIdModel()
		assertTrue(imageModel is RHMIModel.ImageIdModel)
		assertEquals(10, imageModel.id)
		assertEquals(0, imageModel.imageId)
		val intModel = model.asRaIntModel()
		assertTrue(intModel is RHMIModel.RaIntModel)
		assertEquals(0, intModel.value)
		assertEquals(null, app.modelData[model.id])
		intModel.value = 50
		assertEquals(50, app.modelData[model.id])
	}

	@Test fun mockActions() {
		val app = RHMIApplicationMock()
		val hmiAction = RHMIAction.HMIAction(app, 2)
		hmiAction.targetModel = 5
		assertNotNull(hmiAction.getTargetModel())
		assertEquals(5, hmiAction.getTargetModel()?.id)
		val combinedAction = RHMIAction.CombinedAction(app, 3, RHMIAction.RAAction(app, 2), hmiAction)
		assertEquals(5, combinedAction.hmiAction?.getTargetModel()?.id)
	}

	@Test fun mockComponents() {
		val app = RHMIApplicationMock()
		val component = app.components[10]
		val button = component.asButton()
		assertTrue(button is RHMIComponent.Button)
		assertEquals(10, button.id)
		button.tooltipModel = 51
		assertEquals(51, button.getTooltipModel()?.id)
	}
}