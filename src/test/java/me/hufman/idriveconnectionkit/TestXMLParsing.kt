package me.hufman.idriveconnectionkit

import de.bmw.idrive.BMWRemoting
import me.hufman.idriveconnectionkit.XMLUtils
import me.hufman.idriveconnectionkit.rhmi.RHMIModel
import me.hufman.idriveconnectionkit.rhmi.mocking.RHMIApplicationMock
import org.junit.Assert.*
import org.junit.Test

class TestXMLParsing {
	val xml = "<models>" +
			"<imageIdModel id=\"4\" imageId=\"15\"/>" +
			"<textIdModel id=\"5\" textId=\"70\"/>" +
			"<raBoolModel id=\"50\"/>" +
			"<raDataModel id=\"6\"/>" +
			"<raImageModel id=\"62\"/>" +
			"<raIntModel id=\"60\" value=\"0\"/>" +
			"<raListModel id=\"7\" modelType=\"EntICPlaylist\"/>" +
			"<raGaugeModel id=\"8\" modelType=\"Progress\" min=\"0\" max=\"100\" value=\"0\" increment=\"1\"/>" +
			"<formatDataModel id=\"10\" formatString=\"%0%1\">\n" +
			"    <models>\n" +
			"        <textIdModel id=\"11\"/>\n" +
			"        <raDataModel id=\"12\"/>\n" +
			"    </models>\n" +
			"</formatDataModel>" +
			"</models>"
	var app = RHMIApplicationMock()
	val root = XMLUtils.loadXML(xml).childNodes.item(0)

	fun setUpBeforeClass() {
		assertNotNull(root)
	}

	@Test fun imageIdModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(0))
		assertNotNull(model)
		assertTrue(model is RHMIModel.ImageIdModel)
		assertEquals(4, model?.id)
		assertEquals(15, model?.asImageIdModel()?.imageId)
	}

	@Test fun textIdModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(1))
		assertNotNull(model)
		assertTrue(model is RHMIModel.TextIdModel)
		assertEquals(5, model?.id)
		assertEquals(70, model?.asTextIdModel()?.textId)
	}

	@Test fun raBoolModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(2))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaBoolModel)
		assertEquals(50, model?.id)
		assertEquals(false, model?.asRaBoolModel()?.value)
		assertEquals(null, app.modelData[model?.id])
		model?.asRaBoolModel()?.value = true
		assertEquals(true, model?.asRaBoolModel()?.value)
		assertEquals(true, app.modelData[model?.id])
	}
	@Test fun raDataModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(3))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaDataModel)
		assertEquals(null, app.modelData[model?.id])
		assertEquals(6, model?.id)
		assertEquals("", model?.asRaDataModel()?.value)
		model?.asRaDataModel()?.value = "hi"
		assertEquals("hi", model?.asRaDataModel()?.value)
		assertEquals("hi", app.modelData[model?.id])
	}

	@Test fun raImageModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(4))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaImageModel)
		assertEquals(null, app.modelData[model?.id])
		assertEquals(62, model?.id)
		assertEquals(null, model?.asRaImageModel()?.value)
		model?.asRaImageModel()?.value = "Hi".toByteArray()
		assertEquals(null, model?.asRaImageModel()?.value)
		assertTrue(app.modelData[model?.id] is BMWRemoting.RHMIResourceData)
		val expectedImageData = BMWRemoting.RHMIResourceData(BMWRemoting.RHMIResourceType.IMAGEDATA, "Hi".toByteArray())
		assertEquals(expectedImageData.type, (app.modelData[model?.id] as BMWRemoting.RHMIResourceData).type)
		assertArrayEquals(expectedImageData.data, (app.modelData[model?.id] as BMWRemoting.RHMIResourceData).data)
	}

	@Test fun raIntModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(5))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaIntModel)
		assertEquals(null, app.modelData[model?.id])
		assertEquals(60, model?.id)
		assertEquals(0, model?.asRaIntModel()?.value)
		model?.asRaIntModel()?.value = 5
		assertEquals(5, model?.asRaIntModel()?.value)
		assertEquals(5, app.modelData[model?.id])
	}

	@Test fun raListModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(6))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaListModel)
		assertEquals(null, app.modelData[model?.id])
		assertEquals(7, model?.id)
		assertEquals("EntICPlaylist", model?.asRaListModel()?.modelType)
		assertEquals(null, model?.asRaListModel()?.value)
		val sentList = RHMIModel.RaListModel.RHMIListConcrete(3)
		sentList.addRow(arrayOf(true, 4, "Yes"))
		val expectedList = arrayOf(arrayOf(true, 4, "Yes"))
		model?.asRaListModel()?.value = sentList
		assertEquals(null, model?.asRaListModel()?.value)
		assertArrayEquals(expectedList, (app.modelData[model?.id] as BMWRemoting.RHMIDataTable).data)
	}

	@Test fun raGaugeModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(7))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaGaugeModel)
		assertEquals(null, app.modelData[model?.id])
		assertEquals(8, model?.id)
		assertEquals("Progress", model?.asRaGaugeModel()?.modelType)
		assertEquals(0, model?.asRaGaugeModel()?.min)
		assertEquals(100, model?.asRaGaugeModel()?.max)
		assertEquals(0, model?.asRaGaugeModel()?.value)
		assertEquals(1, model?.asRaGaugeModel()?.increment)
		model?.asRaGaugeModel()?.value = 5
		assertEquals(5, model?.asRaGaugeModel()?.value)
		assertEquals(5, app.modelData[model?.id])
	}

	@Test fun formatDataModel() {
		val model = RHMIModel.loadFromXML(app, root.childNodes.item(8))
		assertNotNull(model)
		assertTrue(model is RHMIModel.FormatDataModel)
		assertEquals(10, model?.id)
		assertEquals("%0%1", model?.asFormatDataModel()?.formatString)
		assertEquals(2, model?.asFormatDataModel()?.submodels?.size)
		val submodels = model!!.asFormatDataModel()!!.submodels
		assertTrue(submodels[0] is RHMIModel.TextIdModel)
		assertEquals(11, submodels[0].id)
		assertTrue(submodels[1] is RHMIModel.RaDataModel)
		assertEquals(12, submodels[1].id)
	}
}