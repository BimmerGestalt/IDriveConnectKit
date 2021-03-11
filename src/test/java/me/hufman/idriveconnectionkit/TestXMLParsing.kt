package me.hufman.idriveconnectionkit

import de.bmw.idrive.BMWRemoting
import me.hufman.idriveconnectionkit.rhmi.*
import me.hufman.idriveconnectionkit.rhmi.mocking.RHMIApplicationMock
import me.hufman.idriveconnectionkit.xmlutils.XMLUtils
import me.hufman.idriveconnectionkit.xmlutils.getChildNamed
import org.junit.Assert.*
import org.junit.Test
import org.w3c.dom.Node
import java.net.URLClassLoader

class TestXMLParsing {
	val xml = this.javaClass.classLoader.getResourceAsStream("ui_layout.xml").bufferedReader().use {
		it.readText()
	}
	var app = RHMIApplicationMock()
	val root = XMLUtils.loadXML(xml).childNodes.item(0)
	var pluginApp = root.getChildNamed("pluginApp") as Node
	val actions = pluginApp.getChildNamed("actions") as Node
	val events = pluginApp.getChildNamed("events") as Node
	val models = pluginApp.getChildNamed("models") as Node
	val states = pluginApp.getChildNamed("hmiStates") as Node
	val components = states.getChildNamed("toolbarHmiState")?.getChildNamed("components") as Node

	@Test fun xmlUtils() {
		val missingChild = XMLUtils.getChildNodeNamed(root, "missing")
		assertNull(missingChild)

		val app = XMLUtils.getChildNodeNamed(root, "pluginApp")
		assertEquals("pluginApp", app?.nodeName)

		val emptyAttrs = XMLUtils.getAttributes(states)
		assertEquals(0, emptyAttrs.size)

		val firstState = XMLUtils.childNodes(states).first()
		assertTrue(XMLUtils.hasAttribute(firstState, "id"))
		assertEquals("46", XMLUtils.getAttribute(firstState, "id"))

		val stateChildren = XMLUtils.childNodes(firstState)
		assertEquals(2, stateChildren.size)
		assertEquals("properties", stateChildren[0].nodeName)
		assertEquals("optionComponents", stateChildren[1].nodeName)
		val stateChildrenNodes = XMLUtils.childNodes(firstState.childNodes)
		assertEquals(2, stateChildrenNodes.size)
		assertEquals("properties", stateChildrenNodes[0].nodeName)
		assertEquals("optionComponents", stateChildrenNodes[1].nodeName)
	}

	@Test fun entireDescription() {
		val app = RHMIApplicationConcrete()
		app.loadFromXML(xml)
		assertEquals(7, app.actions.size)
		assertEquals(8, app.events.size)
		assertEquals(19, app.models.size)
		assertEquals(4, app.states.size)
		assertEquals(13, app.components.size)

		// all parsed actions
		assertTrue(app.actions[2] is RHMIAction.RAAction)
		assertTrue(app.actions[3] is RHMIAction.CombinedAction)
		assertTrue(app.actions[4] is RHMIAction.RAAction)
		assertTrue(app.actions[5] is RHMIAction.HMIAction)
		assertTrue(app.actions[7] is RHMIAction.LinkAction)

		//all parsed events
		assertTrue(app.events[1] is RHMIEvent.PopupEvent)
		assertTrue(app.events[2] is RHMIEvent.ActionEvent)
		assertTrue(app.events[3] is RHMIEvent.ActionEvent)
		assertTrue(app.events[4] is RHMIEvent.NotificationIconEvent)
		assertTrue(app.events[5] is RHMIEvent.PopupEvent)
		assertTrue(app.events[6] is RHMIEvent.FocusEvent)
		assertTrue(app.events[7] is RHMIEvent.MultimediaInfoEvent)
		assertTrue(app.events[8] is RHMIEvent.StatusbarEvent)

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
		assertTrue(app.states[24] is RHMIState.AudioHmiState)

		// all parsed components
		assertTrue(app.components[41] is RHMIComponent.ToolbarButton)
		assertTrue(app.components[42] is RHMIComponent.Button)
		assertTrue(app.components[43] is RHMIComponent.Separator)
		assertTrue(app.components[44] is RHMIComponent.Label)
		assertTrue(app.components[4] is RHMIComponent.List)
		assertTrue(app.components[46] is RHMIComponent.Checkbox)
		assertTrue(app.components[47] is RHMIComponent.Gauge)
		assertTrue(app.components[48] is RHMIComponent.Input)
		assertTrue(app.components[50] is RHMIComponent.Image)
		assertTrue(app.components[51] is RHMIComponent.Button)
		assertTrue(app.components[141] is RHMIComponent.ToolbarButton)
		assertTrue(app.components[49] is RHMIComponent.EntryButton)
		assertTrue(app.components[145] is RHMIComponent.InstrumentCluster)

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
		assertTrue(toolbarState.componentsList[7] is RHMIComponent.Image)
		assertTrue(toolbarState.componentsList[8] is RHMIComponent.Button)

		// check the options components
		val state = app.states[46]!!
		assertEquals(3, state.optionComponentsList.size)
		assertTrue(state.optionComponentsList[0] is RHMIComponent.Separator)
		assertTrue(state.optionComponentsList[1] is RHMIComponent.Label)
		assertTrue(state.optionComponentsList[2] is RHMIComponent.Button)
		assertEquals(76, state.optionComponentsList[2].id)
		assertEquals(61, state.optionComponentsList[2].asButton()?.model)
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

	@Test fun popupEvent() {
		val event = RHMIEvent.loadFromXML(app, events.getChildNamed("popupEvent") as Node)
		assertNotNull(event)
		assertEquals(1, event?.id)
		assertEquals(49, event?.asPopupEvent()?.target)
		assertEquals(10, event?.asPopupEvent()?.priority)
		var target = event?.asPopupEvent()?.getTarget()
		assertEquals(49, target?.id)
	}

	@Test fun actionEvent() {
		val event = RHMIEvent.loadFromXML(app, events.getChildNamed("actionEvent") as Node)
		assertNotNull(event)
		assertEquals(2, event?.id)
		assertEquals(7, event?.asActionEvent()?.action)
		val action = event?.asActionEvent()?.getAction()
		assertEquals(7, action?.id)
	}

	@Test fun notificationIconEvent() {
		val event = RHMIEvent.loadFromXML(app, events.getChildNamed("notificationIconEvent") as Node)
		assertNotNull(event)
		assertEquals(4, event?.id)
		assertEquals(62, event?.asNotificationIconEvent()?.imageIdModel)
		val model = event?.asNotificationIconEvent()?.getImageIdModel()
		assertEquals(62, model?.id)
	}

	@Test fun focusEvent() {
		val event = RHMIEvent.loadFromXML(app, events.getChildNamed("focusEvent") as Node)
		assertNotNull(event)
		assertEquals(6, event?.id)
		assertEquals(6, event?.asFocusEvent()?.targetModel)
		val model = event?.asFocusEvent()?.getTargetModel()
		assertEquals(6, model?.id)
	}

	@Test fun multimediaInfoEvent() {
		val event = RHMIEvent.loadFromXML(app, events.getChildNamed("multimediaInfoEvent") as Node)
		assertNotNull(event)
		assertEquals(7, event?.id)
		assertEquals(6, event?.asMultimediaInfoEvent()?.textModel1)
		val model1 = event?.asMultimediaInfoEvent()?.getTextModel1()
		assertEquals(12, event?.asMultimediaInfoEvent()?.textModel2)
		val model2 = event?.asMultimediaInfoEvent()?.getTextModel2()
		assertEquals(12, model2?.id)
	}

	@Test fun statusbarEvent() {
		val event = RHMIEvent.loadFromXML(app, events.getChildNamed("statusbarEvent") as Node)
		assertNotNull(event)
		assertEquals(8, event?.id)
		assertEquals(12, event?.asStatusbarEvent()?.textModel)
		val model = event?.asStatusbarEvent()?.getTextModel()
		assertEquals(12, model?.id)
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

		assertEquals(1, state?.properties?.size)
		assertEquals("false", state?.properties?.get(4)?.value)
	}
	@Test fun toolbarState() {
		val state = RHMIState.loadFromXML(app, states.getChildNamed("toolbarHmiState") as Node)
		assertNotNull(state)
		assertTrue(state is RHMIState.ToolbarState)
		assertEquals(40, state?.id)
		assertEquals(1, state?.asToolbarState()?.toolbarComponents?.size)
		assertEquals(1, state?.asToolbarState()?.toolbarComponentsList?.size)
		assertEquals(9, state?.components?.size)
		assertEquals(9, state?.componentsList?.size)
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
	@Test fun audioState() {
		val state = RHMIState.loadFromXML(app, states.getChildNamed("audioHmiState") as Node)
		assertNotNull(state)
		assertTrue(state is RHMIState.AudioHmiState)
		val audio = state as RHMIState.AudioHmiState
		assertEquals(24, state?.id)
		assertEquals(0, state?.components?.size)
		assertEquals(0, state?.componentsList?.size)
		assertEquals(44, state?.textModel)
		assertEquals(21, audio.artistAction)
		assertEquals(29, audio.coverAction)
		assertEquals(37, audio.progressAction)
		assertEquals(33, audio.playListAction)
		assertEquals(25, audio.albumAction)
		assertEquals(44, audio.textModel)
		assertEquals(48, audio.alternativeTextModel)
		assertEquals(47, audio.trackTextModel)
		assertEquals(50, audio.playListProgressTextModel)
		assertEquals(49, audio.playListTextModel)
		assertEquals(54, audio.artistImageModel)
		assertEquals(45, audio.artistTextModel)
		assertEquals(55, audio.albumImageModel)
		assertEquals(46, audio.albumTextModel)
		assertEquals(56, audio.coverImageModel)
		assertEquals(62, audio.playbackProgressModel)
		assertEquals(63, audio.downloadProgressModel)
		assertEquals(51, audio.currentTimeModel)
		assertEquals(52, audio.elapsingTimeModel)
		assertEquals(59, audio.playListFocusRowModel)
		assertEquals(57, audio.providerLogoImageModel)
		assertEquals(61, audio.statusBarImageModel)
		assertEquals(58, audio.playListModel)

		audio.getTextModel()?.asRaDataModel()?.value = "textModel"
		assertEquals("textModel", app.modelData[44])
		audio.getAlternativeTextModel()?.asRaDataModel()?.value = "alternativeTextModel"
		assertEquals("alternativeTextModel", app.modelData[48])
		audio.getTrackTextModel()?.asRaDataModel()?.value = "trackTextModel"
		assertEquals("trackTextModel", app.modelData[47])
		audio.getPlayListProgressTextModel()?.asRaDataModel()?.value = "playListProgressTextModel"
		assertEquals("playListProgressTextModel", app.modelData[50])
		audio.getPlayListTextModel()?.asRaDataModel()?.value = "playListTextModel"
		assertEquals("playListTextModel", app.modelData[49])
		audio.getArtistImageModel()?.asRaDataModel()?.value = "artistImageModel"
		assertEquals("artistImageModel", app.modelData[54])
		audio.getArtistTextModel()?.asRaDataModel()?.value = "artistTextModel"
		assertEquals("artistTextModel", app.modelData[45])
		audio.getAlbumImageModel()?.asRaDataModel()?.value = "albumImageModel"
		assertEquals("albumImageModel", app.modelData[55])
		audio.getAlbumTextModel()?.asRaDataModel()?.value = "albumTextModel"
		assertEquals("albumTextModel", app.modelData[46])
		audio.getCoverImageModel()?.asRaDataModel()?.value = "coverImageModel"
		assertEquals("coverImageModel", app.modelData[56])
		audio.getPlaybackProgressModel()?.asRaDataModel()?.value = "playbackProgressModel"
		assertEquals("playbackProgressModel", app.modelData[62])
		audio.getDownloadProgressModel()?.asRaDataModel()?.value = "downloadProgressModel"
		assertEquals("downloadProgressModel", app.modelData[63])
		audio.getCurrentTimeModel()?.asRaDataModel()?.value = "currentTimeModel"
		assertEquals("currentTimeModel", app.modelData[51])
		audio.getElapsingTimeModel()?.asRaDataModel()?.value = "elapsingTimeModel"
		assertEquals("elapsingTimeModel", app.modelData[52])
		audio.getPlayListFocusRowModel()?.asRaDataModel()?.value = "playListFocusRowModel"
		assertEquals("playListFocusRowModel", app.modelData[59])
		audio.getProviderLogoImageModel()?.asRaDataModel()?.value = "providerLogoImageModel"
		assertEquals("providerLogoImageModel", app.modelData[57])
		audio.getStatusBarImageModel()?.asRaDataModel()?.value = "statusBarImageModel"
		assertEquals("statusBarImageModel", app.modelData[61])
		audio.getPlayListModel()?.asRaDataModel()?.value = "playListModel"
		assertEquals("playListModel", app.modelData[58])
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

	@Test fun instrumentCluster() {
		val componentNode = pluginApp.getChildNamed("instrumentCluster") as Node
		val component = RHMIComponent.loadFromXML(app, componentNode)
		assertNotNull(component)
		assertTrue(component is RHMIComponent.InstrumentCluster)
		val ic = component as RHMIComponent.InstrumentCluster
		assertEquals(145, ic.id)
		assertEquals(7, ic.playlistModel)
		assertEquals(7, ic.getPlaylistModel()?.asRaListModel()?.id)
		assertEquals(74, ic.detailsModel)
		assertEquals(74, ic.getDetailsModel()?.asRaListModel()?.id)
		assertEquals(35, ic.useCaseModel)
		assertEquals(35, ic.getUseCaseModel()?.asRaDataModel()?.id)
		assertEquals(64, ic.action)
		assertEquals(64, ic.getAction()?.asRAAction()?.id)
		assertEquals(39, ic.textModel)
		assertEquals(39, ic.getTextModel()?.asRaDataModel()?.id)
		assertEquals(38, ic.additionalTextModel)
		assertEquals(38, ic.getAdditionalTextModel()?.asRaDataModel()?.id)
		assertEquals(65, ic.setTrackAction)
		assertEquals(65, ic.getSetTrackAction()?.asRAAction()?.id)
		assertEquals(36, ic.iSpeechSupport)
		assertEquals(36, ic.getISpeechSupport()?.asRaBoolModel()?.id)
		assertEquals(37, ic.iSpeechText)
		assertEquals(37, ic.getISpeechText()?.asRaIntModel()?.id)
		assertEquals(41, ic.skipForward)
		assertEquals(41, ic.getSkipForward()?.asRaDataModel()?.id)
		assertEquals(40, ic.skipBackward)
		assertEquals(40, ic.getSkipBackward()?.asRaDataModel()?.id)
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
		assertEquals(0, button.imageModel)

		component?.asButton()?.getModel()?.asRaDataModel()?.value = "Hi"
		assertEquals("Hi", app.modelData[11])
		component?.asButton()?.getTooltipModel()?.asRaDataModel()?.value = "Test"
		assertEquals("Test", app.modelData[5])
	}

	@Test fun imageButton() {
		val component = RHMIComponent.loadFromXML(app, XMLUtils.childNodes(components).last {it.nodeName == "button" })
		assertNotNull(component)
		assertTrue(component is RHMIComponent.Button)
		val button = component as RHMIComponent.Button
		assertEquals(51, button.id)
		assertEquals(3, button.action)
		assertEquals(3, button.getAction()?.id)
		assertEquals(4, button.selectAction)
		assertEquals(4, button.getSelectAction()?.id)
		assertEquals(0, button.tooltipModel)
		assertEquals(11, button.model)
		assertEquals(11, button.getModel()?.id)
		assertEquals(4, button.imageModel)

		component?.asButton()?.getModel()?.asRaDataModel()?.value = "Hi"
		assertEquals("Hi", app.modelData[11])
		component?.asButton()?.getImageModel()?.asRaImageModel()?.value = byteArrayOf(10, 12)
		assertArrayEquals(byteArrayOf(10, 12), (app.modelData[4] as BMWRemoting.RHMIResourceData).data)
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

		assertEquals(1, component?.properties?.size)
		assertEquals("100,0,*", component?.properties?.get(RHMIProperty.PropertyId.LIST_COLUMNWIDTH.id)?.value)
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

		assertEquals(1, component?.properties?.size)
		assertEquals("false", component?.properties?.get(1)?.value)
		val propertyBag = component?.properties?.get(1) as RHMIProperty.LayoutBag
		assertEquals("true", propertyBag.get(0))
		assertEquals(1, propertyBag.get(1))
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