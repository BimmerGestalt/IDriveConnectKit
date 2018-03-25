package me.hufman.idriveconnectionkit

import de.bmw.idrive.BMWRemoting
import me.hufman.idriveconnectionkit.rhmi.RHMIAction
import me.hufman.idriveconnectionkit.rhmi.RHMIApplicationConcrete
import me.hufman.idriveconnectionkit.rhmi.RHMIComponent
import me.hufman.idriveconnectionkit.rhmi.RHMIModel
import me.hufman.idriveconnectionkit.rhmi.mocking.RHMIApplicationMock
import org.junit.Assert.*
import org.junit.Test
import java.text.Normalizer

class TestXMLParsing {
	val xml = "<pluginApp>" +
			"<actions>" +
			"<raAction id=\"2\"/>" +
			"<combinedAction id=\"3\" sync=\"true\" actionType=\"spellWord\">" +
			"    <actions>" +
			"        <raAction id=\"4\"/>" +
			"        <hmiAction id=\"5\" targetModel=\"6\"/>" +
			"    </actions>" +
			"</combinedAction>" +
			"<linkAction id=\"7\" actionType=\"call\" linkModel=\"12\"/>" +
			"</actions>" +
			"<models>" +
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
			"</models>" +
			"<hmiStates>" +
			"<hmiState id=\"40\">" +
			"<toolbarComponents>" +
			"<button id=\"41\" action=\"3\" selectAction=\"4\" tooltipModel=\"5\" imageModel=\"4\" />" +
			"</toolbarComponents>" +
			"<components>" +
			"<button id=\"42\" action=\"3\" selectAction=\"4\" tooltipModel=\"5\" model=\"11\" />" +
			"<separator id=\"43\" />" +
			"<label id=\"44\" model=\"6\" />" +
			"<list id=\"45\" model=\"7\" action=\"3\" selectAction=\"4\" />" +
			"<checkbox id=\"46\" model=\"50\" textModel=\"5\" action=\"3\" />" +
			"<gauge id=\"47\" textModel=\"5\" model=\"8\" action=\"3\" changeAction=\"4\" />" +
			"<input id=\"48\" textModel=\"5\" resultModel=\"6\" suggestModel=\"6\" action=\"4\" resultAction=\"3\" suggestAction=\"3\" />" +
			"</components>" +
			"</hmiState>" +
			"</hmiStates>" +
			"<entryButton id=\"49\" action=\"4\" model=\"5\" imageModel=\"4\"/>" +
			"</pluginApp>"
	var app = RHMIApplicationMock()
	val root = XMLUtils.loadXML(xml).childNodes.item(0)
	val actions = root.childNodes.item(0)
	val models = root.childNodes.item(1)
	val components = root.childNodes.item(2).childNodes.item(0).childNodes.item(1)

	@Test fun entireDescription() {
		val app = RHMIApplicationConcrete()
		app.loadFromXML(xml)
		assertEquals(5, app.actions.size)
		assertEquals(11, app.models.size)
		assertEquals(9, app.components.size)

		assertTrue(app.actions[2] is RHMIAction.RAAction)
		assertTrue(app.actions[3] is RHMIAction.CombinedAction)
		assertTrue(app.actions[4] is RHMIAction.RAAction)
		assertTrue(app.actions[5] is RHMIAction.HMIAction)
		assertTrue(app.actions[7] is RHMIAction.LinkAction)

		assertTrue(app.models[4] is RHMIModel.ImageIdModel)
		assertTrue(app.models[5] is RHMIModel.TextIdModel)
		assertTrue(app.models[50] is RHMIModel.RaBoolModel)
		assertTrue(app.models[6] is RHMIModel.RaDataModel)
		assertTrue(app.models[62] is RHMIModel.RaImageModel)
		assertTrue(app.models[60] is RHMIModel.RaIntModel)
		assertTrue(app.models[7] is RHMIModel.RaListModel)
		assertTrue(app.models[8] is RHMIModel.RaGaugeModel)
		assertTrue(app.models[10] is RHMIModel.FormatDataModel)
		assertTrue(app.models[11] is RHMIModel.TextIdModel)
		assertTrue(app.models[12] is RHMIModel.RaDataModel)

		assertTrue(app.components[41] is RHMIComponent.ToolbarButton)
		assertTrue(app.components[42] is RHMIComponent.Button)
		assertTrue(app.components[43] is RHMIComponent.Separator)
		assertTrue(app.components[44] is RHMIComponent.Label)
		assertTrue(app.components[45] is RHMIComponent.List)
		assertTrue(app.components[46] is RHMIComponent.Checkbox)
		assertTrue(app.components[47] is RHMIComponent.Gauge)
		assertTrue(app.components[48] is RHMIComponent.Input)
	}

	@Test fun raAction() {
		val action = RHMIAction.loadFromXML(app, actions.childNodes.item(0))
		assertNotNull(action)
		assertTrue(action is RHMIAction.RAAction)
		assertEquals(2, action?.id)
	}

	@Test fun combinedAction() {
		val action = RHMIAction.loadFromXML(app, actions.childNodes.item(1))
		assertNotNull(action)
		assertTrue(action is RHMIAction.CombinedAction)
		val combinedAction = action as RHMIAction.CombinedAction
		assertEquals(3, action?.id)
		assertEquals("spellWord", combinedAction.actionType)
		assertNotNull(combinedAction.raAction)
		assertTrue(combinedAction.raAction is RHMIAction.RAAction)
		assertEquals(4, combinedAction.raAction?.id)
		assertNotNull(combinedAction.hmiAction)
		assertTrue(combinedAction.hmiAction is RHMIAction.HMIAction)
		assertEquals(5, combinedAction.hmiAction?.id)
		assertEquals(6, combinedAction.hmiAction?.targetModel)
		assertNotNull(combinedAction.hmiAction?.getTargetModel())
		assertEquals(6, combinedAction.hmiAction?.getTargetModel()?.id)
	}

	@Test fun linkAction() {
		val action = RHMIAction.loadFromXML(app, actions.childNodes.item(2))
		assertNotNull(action)
		assertTrue(action is RHMIAction.LinkAction)
		assertEquals(7, action?.id)
		val linkAction = action as RHMIAction.LinkAction
		assertEquals("call", linkAction.actionType)
		assertEquals(12, linkAction.linkModel)
		assertNotNull(linkAction.getLinkModel())
		assertEquals(12, linkAction.getLinkModel()?.id)
	}
	@Test fun imageIdModel() {
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(0))
		assertNotNull(model)
		assertTrue(model is RHMIModel.ImageIdModel)
		assertEquals(4, model?.id)
		assertEquals(15, model?.asImageIdModel()?.imageId)
	}

	@Test fun textIdModel() {
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(1))
		assertNotNull(model)
		assertTrue(model is RHMIModel.TextIdModel)
		assertEquals(5, model?.id)
		assertEquals(70, model?.asTextIdModel()?.textId)
	}

	@Test fun raBoolModel() {
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(2))
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
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(3))
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
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(4))
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
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(5))
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
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(6))
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
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(7))
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
		val model = RHMIModel.loadFromXML(app, models.childNodes.item(8))
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

	@Test fun entryButton() {
		val componentNode = root.childNodes.item(3)
		val component = RHMIComponent.loadFromXML(app, componentNode)
		assertNotNull(component)
		assertTrue(component is RHMIComponent.EntryButton)
		val button = component as RHMIComponent.EntryButton
		assertEquals(49, button.id)
		assertEquals(4, button.action)
		assertEquals(4, button.getAction()?.id)
		assertEquals(5, button.model)
		assertEquals(5, button.getModel()?.id)
		assertEquals(4, button.imageModel)
		assertEquals(4, button.getImageModel()?.id)
	}

	@Test fun toolbarButton() {
		val componentNode = root.childNodes.item(2).childNodes.item(0).childNodes.item(0).childNodes.item(0)
		val component = RHMIComponent.loadFromXML(app, componentNode)
		assertNotNull(component)
		assertTrue(component is RHMIComponent.ToolbarButton)
		val button = component as RHMIComponent.ToolbarButton
		assertEquals(41, button.id)
		assertEquals(3, button.action)
		assertEquals(3, button.getAction()?.id)
		assertEquals(4, button.selectAction)
		assertEquals(4, button.getSelectAction()?.id)
		assertEquals(5, button.tooltipModel)
		assertEquals(5, button.getTooltipModel()?.id)
		assertEquals(4, button.imageModel)
		assertEquals(4, button.getImageModel()?.id)
	}

	@Test fun button() {
		val component = RHMIComponent.loadFromXML(app, components.childNodes.item(0))
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Button)
		val button = component as RHMIComponent.Button
		assertEquals(42, button.id)
		assertEquals(3, button.action)
		assertEquals(3, button.getAction()?.id)
		assertEquals(4, button.selectAction)
		assertEquals(4, button.getSelectAction()?.id)
		assertEquals(5, button.tooltipModel)
		assertEquals(5, button.getTooltipModel()?.id)
		assertEquals(11, button.model)
		assertEquals(11, button.getModel()?.id)
	}

	@Test fun separator() {
		val component = RHMIComponent.loadFromXML(app, components.childNodes.item(1))
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Separator)
		assertEquals(43, component?.id)
	}
	@Test fun label() {
		val component = RHMIComponent.loadFromXML(app, components.childNodes.item(2))
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Label)
		assertEquals(44, component?.id)
		assertEquals(6, component?.asLabel()?.model)
		assertEquals(6, component?.asLabel()?.getModel()?.id)
	}
	@Test fun list() {
		val component = RHMIComponent.loadFromXML(app, components.childNodes.item(3))
		assertNotNull(component)
		assertTrue(component is RHMIComponent.List)
		assertEquals(45, component?.id)
		assertEquals(7, component?.asList()?.model)
		assertEquals(7, component?.asList()?.getModel()?.id)
		assertEquals(3, component?.asList()?.action)
		assertEquals(3, component?.asList()?.getAction()?.id)
		assertEquals(4, component?.asList()?.selectAction)
		assertEquals(4, component?.asList()?.getSelectAction()?.id)
	}
	@Test fun checkbox() {
		val component = RHMIComponent.loadFromXML(app, components.childNodes.item(4))
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Checkbox)
		assertEquals(46, component?.id)
		assertEquals(50, component?.asCheckbox()?.model)
		assertEquals(50, component?.asCheckbox()?.getModel()?.id)
		assertEquals(5, component?.asCheckbox()?.textModel)
		assertEquals(5, component?.asCheckbox()?.getTextModel()?.id)
		assertEquals(3, component?.asCheckbox()?.action)
		assertEquals(3, component?.asCheckbox()?.getAction()?.id)
	}
	@Test fun gauge() {
		val component = RHMIComponent.loadFromXML(app, components.childNodes.item(5))
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Gauge)
		assertEquals(47, component?.id)
		assertEquals(8, component?.asGauge()?.model)
		assertEquals(8, component?.asGauge()?.getModel()?.id)
		assertEquals(5, component?.asGauge()?.textModel)
		assertEquals(5, component?.asGauge()?.getTextModel()?.id)
		assertEquals(3, component?.asGauge()?.action)
		assertEquals(3, component?.asGauge()?.getAction()?.id)
		assertEquals(4, component?.asGauge()?.changeAction)
		assertEquals(4, component?.asGauge()?.getChangeAction()?.id)
	}
	@Test fun input() {
		val component = RHMIComponent.loadFromXML(app, components.childNodes.item(6))
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Input)
		assertEquals(48, component?.id)
		assertEquals(5, component?.asInput()?.textModel)
		assertEquals(5, component?.asInput()?.getTextModel()?.id)
		assertEquals(6, component?.asInput()?.resultModel)
		assertEquals(6, component?.asInput()?.getResultModel()?.id)
		assertEquals(6, component?.asInput()?.suggestModel)
		assertEquals(6, component?.asInput()?.getSuggestModel()?.id)
		assertEquals(4, component?.asInput()?.action)
		assertEquals(4, component?.asInput()?.getAction()?.id)
		assertEquals(3, component?.asInput()?.resultAction)
		assertEquals(3, component?.asInput()?.getResultAction()?.id)
		assertEquals(3, component?.asInput()?.suggestAction)
		assertEquals(3, component?.asInput()?.getSuggestAction()?.id)
	}

}