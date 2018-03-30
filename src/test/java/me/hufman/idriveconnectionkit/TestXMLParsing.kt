package me.hufman.idriveconnectionkit

import de.bmw.idrive.BMWRemoting
import me.hufman.idriveconnectionkit.rhmi.*
import me.hufman.idriveconnectionkit.rhmi.mocking.RHMIApplicationMock
import me.hufman.idriveconnectionkit.xmlutils.XMLUtils
import me.hufman.idriveconnectionkit.xmlutils.getChildNamed
import org.junit.Assert.*
import org.junit.Test
import org.w3c.dom.Node

class TestXMLParsing {
	val xml = "<pluginApps><pluginApp>" +
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
			"<hmiState id=\"46\" textModel=\"5\">" +
			"</hmiState>" +
			"<toolbarHmiState id=\"40\" textModel=\"6\">" +
			"<toolbarComponents>" +
			"<button id=\"41\" action=\"3\" selectAction=\"4\" tooltipModel=\"5\" imageModel=\"4\" />" +
			"</toolbarComponents>" +
			"<components>" +
			"<button id=\"42\" action=\"3\" selectAction=\"4\" tooltipModel=\"5\" model=\"11\" />" +
			"<separator id=\"43\" />" +
			"<label id=\"44\" model=\"6\" />" +
			"<list id=\"4\" model=\"7\" action=\"3\" selectAction=\"4\" />" +
			"<checkbox id=\"46\" model=\"50\" textModel=\"5\" action=\"3\" />" +
			"<gauge id=\"47\" textModel=\"5\" model=\"8\" action=\"3\" changeAction=\"4\" />" +
			"<input id=\"48\" textModel=\"5\" resultModel=\"6\" suggestModel=\"6\" action=\"4\" resultAction=\"3\" suggestAction=\"3\" />" +
			"<image id=\"50\" model=\"62\"/>" +
			"</components>" +
			"</toolbarHmiState>" +
			"<popupHmiState id=\"49\" textModel=\"11\">" +
			"</popupHmiState>" +
			"</hmiStates>" +
			"<entryButton id=\"49\" action=\"4\" model=\"5\" imageModel=\"4\"/>" +
			"</pluginApp></pluginApps>"
	var app = RHMIApplicationMock()
	val root = XMLUtils.loadXML(xml).childNodes.item(0)
	var pluginApp = root.getChildNamed("pluginApp") as Node
	val actions = pluginApp.getChildNamed("actions") as Node
	val models = pluginApp.getChildNamed("models") as Node
	val states = pluginApp.getChildNamed("hmiStates") as Node
	val components = states.getChildNamed("toolbarHmiState")?.getChildNamed("components") as Node

	@Test fun entireDescription() {
		val app = RHMIApplicationConcrete()
		app.loadFromXML(xml)
		assertEquals(5, app.actions.size)
		assertEquals(11, app.models.size)
		assertEquals(3, app.states.size)
		assertEquals(10, app.components.size)

		// all parsed actions
		assertTrue(app.actions[2] is RHMIAction.RAAction)
		assertTrue(app.actions[3] is RHMIAction.CombinedAction)
		assertTrue(app.actions[4] is RHMIAction.RAAction)
		assertTrue(app.actions[5] is RHMIAction.HMIAction)
		assertTrue(app.actions[7] is RHMIAction.LinkAction)

		// all parsed models
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

		// all parsed states
		assertTrue(app.states[46] is RHMIState.PlainState)
		assertTrue(app.states[40] is RHMIState.ToolbarState)
		assertTrue(app.states[49] is RHMIState.PopupState)

		// all parsed components
		assertTrue(app.components[41] is RHMIComponent.ToolbarButton)
		assertTrue(app.components[42] is RHMIComponent.Button)
		assertTrue(app.components[43] is RHMIComponent.Separator)
		assertTrue(app.components[44] is RHMIComponent.Label)
		assertTrue(app.components[4] is RHMIComponent.List)
		assertTrue(app.components[46] is RHMIComponent.Checkbox)
		assertTrue(app.components[47] is RHMIComponent.Gauge)
		assertTrue(app.components[48] is RHMIComponent.Input)

		// check the ordered list
		val toolbarState = app.states[40] as RHMIState.ToolbarState
		assertTrue(toolbarState.toolbarComponentsList[0] is RHMIComponent.ToolbarButton)
		assertTrue(toolbarState.componentsList[0] is RHMIComponent.Button)
		assertTrue(toolbarState.componentsList[1] is RHMIComponent.Separator)
		assertTrue(toolbarState.componentsList[2] is RHMIComponent.Label)
		assertTrue(toolbarState.componentsList[3] is RHMIComponent.List)
		assertTrue(toolbarState.componentsList[4] is RHMIComponent.Checkbox)
		assertTrue(toolbarState.componentsList[5] is RHMIComponent.Gauge)
		assertTrue(toolbarState.componentsList[6] is RHMIComponent.Input)
	}

	@Test fun raAction() {
		val action = RHMIAction.loadFromXML(app, actions.getChildNamed("raAction") as Node)
		assertNotNull(action)
		assertTrue(action is RHMIAction.RAAction)
		assertEquals(2, action?.id)
	}

	@Test fun combinedAction() {
		val action = RHMIAction.loadFromXML(app, actions.getChildNamed("combinedAction") as Node)
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
		val action = RHMIAction.loadFromXML(app, actions.getChildNamed("linkAction") as Node)
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
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("imageIdModel") as Node)
		assertNotNull(model)
		assertTrue(model is RHMIModel.ImageIdModel)
		assertEquals(4, model?.id)
		assertEquals(15, model?.asImageIdModel()?.imageId)
	}

	@Test fun textIdModel() {
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("textIdModel") as Node)
		assertNotNull(model)
		assertTrue(model is RHMIModel.TextIdModel)
		assertEquals(5, model?.id)
		assertEquals(70, model?.asTextIdModel()?.textId)
	}

	@Test fun raBoolModel() {
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("raBoolModel") as Node)
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
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("raDataModel") as Node)
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
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("raImageModel") as Node)
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
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("raIntModel") as Node)
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
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("raListModel") as Node)
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
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("raGaugeModel") as Node)
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
		val model = RHMIModel.loadFromXML(app, models.getChildNamed("formatDataModel") as Node)
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

	@Test fun plainState() {
		val state = RHMIState.loadFromXML(app, states.getChildNamed("hmiState") as Node)
		assertNotNull(state)
		assertTrue(state is RHMIState.PlainState)
		assertEquals(46, state?.id)
		assertEquals(0, state?.components?.size)
		assertEquals(0, state?.componentsList?.size)
		assertEquals(5, state?.textModel)
		assertEquals(5, state?.getTextModel()?.id)

		state?.getTextModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[5])
	}
	@Test fun toolbarState() {
		val state = RHMIState.loadFromXML(app, states.getChildNamed("toolbarHmiState") as Node)
		assertNotNull(state)
		assertTrue(state is RHMIState.ToolbarState)
		assertEquals(40, state?.id)
		assertEquals(1, state?.asToolbarState()?.toolbarComponents?.size)
		assertEquals(1, state?.asToolbarState()?.toolbarComponentsList?.size)
		assertEquals(8, state?.components?.size)
		assertEquals(8, state?.componentsList?.size)
		assertEquals(6, state?.textModel)
		assertEquals(6, state?.getTextModel()?.id)

		state?.getTextModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[6])
	}
	@Test fun popupState() {
		val state = RHMIState.loadFromXML(app, states.getChildNamed("popupHmiState") as Node)
		assertNotNull(state)
		assertTrue(state is RHMIState.PopupState)
		assertEquals(49, state?.id)
		assertEquals(0, state?.components?.size)
		assertEquals(0, state?.componentsList?.size)
		assertEquals(11, state?.textModel)
		assertEquals(11, state?.getTextModel()?.id)

		state?.getTextModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[11])
	}

	@Test fun entryButton() {
		val componentNode = pluginApp.getChildNamed("entryButton") as Node
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

		component?.asEntryButton()?.getImageModel()?.asRaImageModel()?.value = byteArrayOf(10, 12)
		assertArrayEquals(byteArrayOf(10, 12), (app.modelData[4] as BMWRemoting.RHMIResourceData).data)
		component?.asEntryButton()?.getModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[5])
	}

	@Test fun toolbarButton() {
		val componentNode = states.getChildNamed("toolbarHmiState")?.getChildNamed("toolbarComponents")?.getChildNamed("button") as Node
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

		component?.asToolbarButton()?.getImageModel()?.asRaImageModel()?.value = byteArrayOf(10, 12)
		assertArrayEquals(byteArrayOf(10, 12), (app.modelData[4] as BMWRemoting.RHMIResourceData).data)
		component?.asToolbarButton()?.getTooltipModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[5])
	}

	@Test fun button() {
		val component = RHMIComponent.loadFromXML(app, components.getChildNamed("button") as Node)
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

		component?.asButton()?.getModel()?.asRaDataModel()?.value = "Hi"
		assertEquals("Hi", app.modelData[11])
		component?.asButton()?.getTooltipModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[5])
	}

	@Test fun separator() {
		val component = RHMIComponent.loadFromXML(app, components.getChildNamed("separator") as Node)
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Separator)
		assertEquals(43, component?.id)
	}
	@Test fun label() {
		val component = RHMIComponent.loadFromXML(app, components.getChildNamed("label") as Node)
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Label)
		assertEquals(44, component?.id)
		assertEquals(6, component?.asLabel()?.model)
		assertEquals(6, component?.asLabel()?.getModel()?.id)

		component?.asLabel()?.getModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[6])
	}
	@Test fun list() {
		val component = RHMIComponent.loadFromXML(app, components.getChildNamed("list") as Node)
		assertNotNull(component)
		assertTrue(component is RHMIComponent.List)
		assertEquals(4, component?.id)
		assertEquals(7, component?.asList()?.model)
		assertEquals(7, component?.asList()?.getModel()?.id)
		assertEquals(3, component?.asList()?.action)
		assertEquals(3, component?.asList()?.getAction()?.id)
		assertEquals(4, component?.asList()?.selectAction)
		assertEquals(4, component?.asList()?.getSelectAction()?.id)

		val sentList = RHMIModel.RaListModel.RHMIListConcrete(3)
		sentList.addRow(arrayOf(true, 4, "Yes"))
		val expectedList = arrayOf(arrayOf(true, 4, "Yes"))
		component?.asList()?.getModel()?.value = sentList
		assertArrayEquals(expectedList, (app.modelData[7] as BMWRemoting.RHMIDataTable).data)
	}
	@Test fun checkbox() {
		val component = RHMIComponent.loadFromXML(app, components.getChildNamed("checkbox") as Node)
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Checkbox)
		assertEquals(46, component?.id)
		assertEquals(50, component?.asCheckbox()?.model)
		assertEquals(50, component?.asCheckbox()?.getModel()?.id)
		assertEquals(5, component?.asCheckbox()?.textModel)
		assertEquals(5, component?.asCheckbox()?.getTextModel()?.id)
		assertEquals(3, component?.asCheckbox()?.action)
		assertEquals(3, component?.asCheckbox()?.getAction()?.id)

		component?.asCheckbox()?.getModel()?.asRaBoolModel()?.value = true
		assertEquals(true, app.modelData[50])
		component?.asCheckbox()?.getTextModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[5])
	}
	@Test fun gauge() {
		val component = RHMIComponent.loadFromXML(app, components.getChildNamed("gauge") as Node)
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

		component?.asGauge()?.getModel()?.value = 5
		assertEquals(5, app.modelData[8])
		component?.asGauge()?.getTextModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[5])
	}
	@Test fun input() {
		val component = RHMIComponent.loadFromXML(app, components.getChildNamed("input") as Node)
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

		component?.asInput()?.getTextModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[5])
		val sentList = RHMIModel.RaListModel.RHMIListConcrete(3)
		sentList.addRow(arrayOf(true, 4, "Yes"))
		val expectedList = arrayOf(arrayOf(true, 4, "Yes"))
		component?.asInput()?.getSuggestModel()?.value = sentList
		assertArrayEquals(expectedList, (app.modelData[6] as BMWRemoting.RHMIDataTable).data)
	}

	@Test fun image() {
		val component = RHMIComponent.loadFromXML(app, components.getChildNamed("image") as Node)
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Image)
		assertEquals(50, component?.id)
		assertEquals(62, component?.asImage()?.model)
		assertEquals(62, component?.asImage()?.getModel()?.id)

		component?.asImage()?.getModel()?.asRaImageModel()?.value = byteArrayOf(10, 12)
		assertArrayEquals(byteArrayOf(10, 12), (app.modelData[62] as BMWRemoting.RHMIResourceData).data)
	}
}