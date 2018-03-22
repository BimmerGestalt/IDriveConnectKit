package me.hufman.idriveconnectionkit

import junit.framework.TestCase.*
import me.hufman.idriveconnectionkit.XMLUtils
import me.hufman.idriveconnectionkit.rhmi.RHMIModel
import me.hufman.idriveconnectionkit.rhmi.mocking.RHMIApplicationMock
import org.junit.Test

class TestXMLParsing {
	@Test
	fun models() {
		var xml = "<models>" +
		          "<imageIdModel id=\"4\" imageId=\"15\"/>" +
		          "<textIdModel id=\"5\" textId=\"70\"/>" +
		          "<raBoolModel id=\"50\"/>" +
		          "<raDataModel id=\"6\"/>" +
		          "<raImageModel id=\"62\"/>" +
		          "<raIntModel id=\"60\" value=\"2\"/>" +
		          "<raListModel id=\"7\" modelType=\"EntICPlaylist\"/>" +
		          "<raGaugeModel id=\"8\" modelType=\"Progress\" min=\"0\" max=\"100\" value=\"0\" increment=\"1\"/>" +
		          "<formatDataModel id=\"10\" formatString=\"%0%1\">\n" +
		          "    <models>\n" +
		          "        <textIdModel id=\"11\"/>\n" +
		          "        <raDataModel id=\"12\"/>\n" +
		          "    </models>\n" +
		          "</formatDataModel>" +
		          "</models>"

		val root = XMLUtils.loadXML(xml).childNodes.item(0)
		assertNotNull(root)

		var model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(0))
		assertNotNull(model)
		assertTrue(model is RHMIModel.ImageIdModel)
		assertEquals(4, model?.id)
		assertEquals(15, model?.asImageIdModel()?.imageId)

		model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(1))
		assertNotNull(model)
		assertTrue(model is RHMIModel.TextIdModel)
		assertEquals(5, model?.id)
		assertEquals(70, model?.asTextIdModel()?.textId)

		model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(2))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaBoolModel)
		assertEquals(50, model?.id)
		assertEquals(false, model?.asRaBoolModel()?.value)

		model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(3))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaDataModel)
		assertEquals(6, model?.id)
		assertEquals("", model?.asRaDataModel()?.value)

		model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(4))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaImageModel)
		assertEquals(62, model?.id)

		model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(5))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaIntModel)
		assertEquals(60, model?.id)
		assertEquals(2, model?.asRaIntModel()?.value)

		model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(6))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaListModel)
		assertEquals(7, model?.id)
		assertEquals("EntICPlaylist", model?.asRaListModel()?.modelType)

		model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(7))
		assertNotNull(model)
		assertTrue(model is RHMIModel.RaGaugeModel)
		assertEquals(8, model?.id)
		assertEquals("Progress", model?.asRaGaugeModel()?.modelType)
		assertEquals(0, model?.asRaGaugeModel()?.min)
		assertEquals(100, model?.asRaGaugeModel()?.max)
		assertEquals(0, model?.asRaGaugeModel()?.value)
		assertEquals(1, model?.asRaGaugeModel()?.increment)

		model = RHMIModel.loadFromXML(RHMIApplicationMock(), root.childNodes.item(8))
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