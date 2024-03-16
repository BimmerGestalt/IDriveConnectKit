package io.bimmergestalt.idriveconnectkit

import de.bmw.idrive.BMWRemoting
import io.bimmergestalt.idriveconnectkit.rhmi.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class TestRHMIApplicationIdempotent {
	val backing = RHMIApplicationConcrete()
	val subject = RHMIApplicationIdempotent(backing)

	@Before
	fun setUp() {
		subject.models[1] = RHMIModel.RaDataModel(subject, 1)
		subject.models[2] = RHMIModel.RaIntModel(subject, 2)
		subject.models[3] = RHMIModel.RaGaugeModel(subject, 3)
		subject.models[4] = RHMIModel.RaBoolModel(subject, 4)
		subject.models[5] = RHMIModel.TextIdModel(subject, 5)
		subject.models[6] = RHMIModel.ImageIdModel(subject, 6)
		subject.models[7] = RHMIModel.RaImageModel(subject, 7)
		subject.models[8] = RHMIModel.RaListModel(subject, 8)

		subject.components[10] = RHMIComponent.Label(subject, 10)
	}

	@Test
	fun unwrap() {
		assertEquals(backing, subject.unwrap())
	}

	@Test
	fun setModel() {
		// RaDataModel
		subject.models[1]?.asRaDataModel()?.value = "2"
		assertEquals("2", backing.modelData[1])
		assertEquals("2", subject.models[1]?.asRaDataModel()?.value)
		backing.modelData.remove(1)

		// repeated setting should not send to the backing connection
		subject.models[1]?.asRaDataModel()?.value = "2"
		assertEquals(null, backing.modelData[1])
		assertEquals("2", subject.models[1]?.asRaDataModel()?.value)
		// but changing the value should write to the backing connection
		subject.models[1]?.asRaDataModel()?.value = "3"
		assertEquals("3", backing.modelData[1])
		assertEquals("3", subject.models[1]?.asRaDataModel()?.value)

		// idempotent should read through to backing
		subject.sentData.remove(1)
		assertEquals("3", subject.models[1]?.asRaDataModel()?.value)    // read from backing if cleared from Idempotent

		// RaIntModel
		subject.models[2]?.asRaIntModel()?.value = 2
		assertEquals(2, backing.modelData[2])
		assertEquals(2, subject.models[2]?.asRaIntModel()?.value)
		backing.modelData.remove(2)

		// repeated setting should not send to the backing connection
		subject.models[2]?.asRaIntModel()?.value = 2
		assertEquals(null, backing.modelData[2])
		assertEquals(2, subject.models[2]?.asRaIntModel()?.value)
		// but changing the value should write to the backing connection
		subject.models[2]?.asRaIntModel()?.value = 3
		assertEquals(3, backing.modelData[2])
		assertEquals(3, subject.models[2]?.asRaIntModel()?.value)

		// RaGaugeModel
		subject.models[3]?.asRaGaugeModel()?.value = 2
		assertEquals(2, subject.models[3]?.asRaGaugeModel()?.value)
		assertEquals(2, backing.modelData[3])
		backing.modelData.remove(3)

		// repeated setting should not send to the backing connection
		subject.models[3]?.asRaGaugeModel()?.value = 2
		assertEquals(null, backing.modelData[3])
		assertEquals(2, subject.models[3]?.asRaGaugeModel()?.value)
		// but changing the value should write to the backing connection
		subject.models[3]?.asRaGaugeModel()?.value = 3
		assertEquals(3, backing.modelData[3])
		assertEquals(3, subject.models[3]?.asRaGaugeModel()?.value)

		// RaBoolModel
		subject.models[4]?.asRaBoolModel()?.value = true
		assertEquals(true, backing.modelData[4])
		assertEquals(true, subject.models[4]?.asRaBoolModel()?.value)
		backing.modelData.remove(4)

		// repeated setting should not send to the backing connection
		subject.models[4]?.asRaBoolModel()?.value = true
		assertEquals(null, backing.modelData[4])
		assertEquals(true, subject.models[4]?.asRaBoolModel()?.value)
		// but changing the value should write to the backing connection
		subject.models[4]?.asRaBoolModel()?.value = false
		assertEquals(false, backing.modelData[4])
		assertEquals(false, subject.models[4]?.asRaBoolModel()?.value)

		// TextIdModel
		subject.models[5]?.asTextIdModel()?.textId = 1
		assertEquals(1, (backing.modelData[5] as BMWRemoting.RHMIResourceIdentifier).id)
		assertEquals(1, subject.models[5]?.asTextIdModel()?.textId)
		backing.modelData.remove(5)

		// repeated setting should not send to the backing connection
		subject.models[5]?.asTextIdModel()?.textId = 1
		assertEquals(null, backing.modelData[5])
		assertEquals(1, subject.models[5]?.asTextIdModel()?.textId)
		// but changing the value should write to the backing connection
		subject.models[5]?.asTextIdModel()?.textId = 5
		assertEquals(5, (backing.modelData[5] as BMWRemoting.RHMIResourceIdentifier).id)
		assertEquals(5, subject.models[5]?.asTextIdModel()?.textId)

		// ImageIdModel
		subject.models[6]?.asImageIdModel()?.imageId = 3
		assertEquals(3, (backing.modelData[6] as BMWRemoting.RHMIResourceIdentifier).id)
		assertEquals(3, subject.models[6]?.asImageIdModel()?.imageId)
		backing.modelData.remove(6)

		// repeated setting should not send to the backing connection
		subject.models[6]?.asImageIdModel()?.imageId = 3
		assertEquals(null, backing.modelData[6])
		assertEquals(3, subject.models[6]?.asImageIdModel()?.imageId)
		// but changing the value should write to the backing connection
		subject.models[6]?.asImageIdModel()?.imageId = 6
		assertEquals(6, (backing.modelData[6] as BMWRemoting.RHMIResourceIdentifier).id)
		assertEquals(6, subject.models[6]?.asImageIdModel()?.imageId)
	}

	@Test
	fun setUncachedModel() {
		// RaImageModel
		subject.models[7]?.asRaImageModel()?.value = ByteArray(10)
		assertNotEquals(null, (backing.modelData[7] as BMWRemoting.RHMIResourceData).data)
		backing.modelData.remove(7)

		// repeated setting should send to the backing connection
		subject.models[7]?.asRaImageModel()?.value = ByteArray(10)
		assertNotEquals(null, (backing.modelData[7] as BMWRemoting.RHMIResourceData).data)

		// RaListModel
		subject.models[8]?.asRaListModel()?.value = RHMIModel.RaListModel.RHMIListConcrete(3)
		assertNotEquals(null, (backing.modelData[8] as BMWRemoting.RHMIDataTable).data)
		backing.modelData.remove(8)

		// repeated setting should send to the backing connection
		subject.models[8]?.asRaListModel()?.value = RHMIModel.RaListModel.RHMIListConcrete(3)
		assertNotEquals(null, (backing.modelData[8] as BMWRemoting.RHMIDataTable).data)
	}

	@Test
	fun setProperty() {
		subject.components[10]?.asLabel()?.setVisible(true)
		assertEquals(true, backing.propertyData[10]!![RHMIProperty.PropertyId.VISIBLE.id])
		assertEquals(true, subject.components[10]?.asLabel()?.properties?.get(RHMIProperty.PropertyId.VISIBLE.id)?.value)
		backing.propertyData.remove(10)

		// repeated setting should not send to the backing connection
		subject.components[10]?.asLabel()?.setVisible(true)
		assertEquals(null, backing.propertyData[10])
		assertEquals(true, subject.components[10]?.asLabel()?.properties?.get(RHMIProperty.PropertyId.VISIBLE.id)?.value)
		// but changing the value should write to the backing connection
		subject.components[10]?.asLabel()?.setVisible(false)
		assertEquals(false, backing.propertyData[10]!![RHMIProperty.PropertyId.VISIBLE.id])
		assertEquals(false, subject.components[10]?.asLabel()?.properties?.get(RHMIProperty.PropertyId.VISIBLE.id)?.value)

		// idempotent should read through backing
		subject.sentProperties.remove(10)
		assertEquals(false, subject.components[10]?.asLabel()?.properties?.get(RHMIProperty.PropertyId.VISIBLE.id)?.value)
	}

	@Test
	fun triggerEvent() {
		subject.triggerHMIEvent(2, mapOf(1 to 2))
		assertEquals(mapOf(1 to 2), backing.triggeredEvents[2])
	}
}