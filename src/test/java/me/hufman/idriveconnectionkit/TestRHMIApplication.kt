package me.hufman.idriveconnectionkit

import junit.framework.TestCase.*
import me.hufman.idriveconnectionkit.XMLUtils
import me.hufman.idriveconnectionkit.rhmi.RHMIModel
import me.hufman.idriveconnectionkit.rhmi.mocking.RHMIApplicationMock
import org.junit.Test

class TestRHMIApplication {
	@Test
	fun mockModels() {
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
}