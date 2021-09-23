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
		backing.modelData.remove(1)

		// repeated setting should not send to the backing connection
		subject.models[1]?.asRaDataModel()?.value = "2"
		assertEquals(null, backing.modelData[1])
		// but changing the value should write to the backing connection
		subject.models[1]?.asRaDataModel()?.value = "3"
		assertEquals("3", backing.modelData[1])

		// RaIntModel
		subject.models[2]?.asRaIntModel()?.value = 2
		assertEquals(2, backing.modelData[2])
		backing.modelData.remove(2)

		// repeated setting should not send to the backing connection
		subject.models[2]?.asRaIntModel()?.value = 2
		assertEquals(null, backing.modelData[2])
		// but changing the value should write to the backing connection
		subject.models[2]?.asRaIntModel()?.value = 3
		assertEquals(3, backing.modelData[2])

		// RaGaugeModel
		subject.models[3]?.asRaGaugeModel()?.value = 2
		assertEquals(2, backing.modelData[3])
		backing.modelData.remove(3)

		// repeated setting should not send to the backing connection
		subject.models[3]?.asRaGaugeModel()?.value = 2
		assertEquals(null, backing.modelData[3])
		// but changing the value should write to the backing connection
		subject.models[3]?.asRaGaugeModel()?.value = 3
		assertEquals(3, backing.modelData[3])

		// RaBoolModel
		subject.models[4]?.asRaBoolModel()?.value = true
		assertEquals(true, backing.modelData[4])
		backing.modelData.remove(4)

		// repeated setting should not send to the backing connection
		subject.models[4]?.asRaBoolModel()?.value = true
		assertEquals(null, backing.modelData[4])
		// but changing the value should write to the backing connection
		subject.models[4]?.asRaBoolModel()?.value = false
		assertEquals(false, backing.modelData[4])

		// TextIdModel
		subject.models[5]?.asTextIdModel()?.textId = 1
		assertEquals(1, (backing.modelData[5] as BMWRemoting.RHMIResourceIdentifier).id)
		backing.modelData.remove(5)

		// repeated setting should not send to the backing connection
		subject.models[5]?.asTextIdModel()?.textId = 1
		assertEquals(null, backing.modelData[5])
		// but changing the value should write to the backing connection
		subject.models[5]?.asTextIdModel()?.textId = 5
		assertEquals(5, (backing.modelData[5] as BMWRemoting.RHMIResourceIdentifier).id)

		// ImageIdModel
		subject.models[6]?.asImageIdModel()?.imageId = 3
		assertEquals(3, (backing.modelData[6] as BMWRemoting.RHMIResourceIdentifier).id)
		backing.modelData.remove(6)

		// repeated setting should not send to the backing connection
		subject.models[6]?.asImageIdModel()?.imageId = 3
		assertEquals(null, backing.modelData[6])
		// but changing the value should write to the backing connection
		subject.models[6]?.asImageIdModel()?.imageId = 6
		assertEquals(6, (backing.modelData[6] as BMWRemoting.RHMIResourceIdentifier).id)
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
		backing.propertyData.remove(10)

		// repeated setting should not send to the backing connection
		subject.components[10]?.asLabel()?.setVisible(true)
		assertEquals(null, backing.propertyData[10])
		// but changing the value should write to the backing connection
		subject.components[10]?.asLabel()?.setVisible(false)
		assertEquals(false, backing.propertyData[10]!![RHMIProperty.PropertyId.VISIBLE.id])
	}

	@Test
	fun triggerEvent() {
		subject.triggerHMIEvent(2, mapOf(1 to 2))
		assertEquals(mapOf(1 to 2), backing.triggeredEvents[2])
	}
}