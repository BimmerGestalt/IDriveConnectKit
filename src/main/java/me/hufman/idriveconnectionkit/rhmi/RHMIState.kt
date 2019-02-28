package me.hufman.idriveconnectionkit.rhmi

import me.hufman.idriveconnectionkit.xmlutils.XMLUtils
import me.hufman.idriveconnectionkit.xmlutils.getAttributesMap
import me.hufman.idriveconnectionkit.xmlutils.getChildElements
import me.hufman.idriveconnectionkit.xmlutils.getChildNamed
import org.w3c.dom.Node
import java.util.HashMap

abstract class RHMIState private constructor(open val app: RHMIApplication, open val id: Int) {
	val components: MutableMap<Int, RHMIComponent> = HashMap()
	val componentsList: MutableList<RHMIComponent> = ArrayList()
	val properties: MutableMap<Int, RHMIProperty> = HashMap()
	var textModel: Int = 0
	fun getTextModel(): RHMIModel? {
		return app.models[textModel]
	}

	companion object {
		fun loadFromXML(app: RHMIApplication, node: Node): RHMIState? {
			val attrs = node.getAttributesMap()

			val id = attrs["id"]?.toInt() ?: return null

			val state = when (node.nodeName) {
				"hmiState" -> PlainState(app, id)
				"toolbarHmiState" -> ToolbarState(app, id)
				"popupHmiState" -> PopupState(app, id)
				"audioHmiState" -> AudioHmiState(app, id)
				else -> null
			}

			if (state != null) {
				XMLUtils.unmarshalAttributes(state, attrs)

				node.getChildNamed("components").getChildElements().forEach { componentNode ->
					val component = RHMIComponent.loadFromXML(app, componentNode)
					if (component != null) {
						state.components[component.id] = component
						state.componentsList.add(component)
					}
				}

				val propertyNodes = node.getChildNamed("properties")
				if (propertyNodes != null) {
					propertyNodes.getChildElements().filter { it.nodeName == "property" }.forEach {
						val property = RHMIProperty.loadFromXML(it)
						if (property != null)
							state.properties[property.id] = property
					}
				}

				if (state is ToolbarState) {
					node.getChildNamed("toolbarComponents").getChildElements().forEach { componentNode ->
						val component = RHMIComponent.loadFromXML(app, componentNode)
						if (component is RHMIComponent.ToolbarButton) {
							state.toolbarComponents[component.id] = component
							state.toolbarComponentsList.add(component)
						}
					}
				}
			}

			return state
		}
	}

	// any custom event listeners that the client provides
	var eventCallback: EventCallback? = null
	var focusCallback: FocusCallback? = null
	var visibleCallback: VisibleCallback? = null

	// dispatch an event
	fun onHmiEvent(eventId: Int?, args: Map<*, *>?) {
		if (eventCallback != null) {
			eventCallback?.onHmiEvent(eventId, args)
		} else {
			if (eventId == 1 && focusCallback != null) {
				focusCallback?.onFocus(args?.get(4.toByte()) as? Boolean ?: false)
			}
			if (eventId == 11 && visibleCallback != null) {
				visibleCallback?.onVisible(args?.get(23.toByte()) as? Boolean ?: false)
			}
		}
	}

	class PlainState(override val app: RHMIApplication, override val id: Int): RHMIState(app, id)
	open class ToolbarState(override val app: RHMIApplication, override val id: Int): RHMIState(app, id) {
		val toolbarComponents = HashMap<Int, RHMIComponent.ToolbarButton>()
		val toolbarComponentsList = ArrayList<RHMIComponent.ToolbarButton>()
	}
	class PopupState(override val app: RHMIApplication, override val id: Int): RHMIState(app, id)
	class AudioHmiState(override val app: RHMIApplication, override val id: Int): ToolbarState(app, id) {

		var artistAction: Int = 0
		fun getArtistAction(): RHMIAction? {
			return app.actions[artistAction]
		}

		var coverAction: Int = 0
		fun getCoverAction(): RHMIAction? {
			return app.actions[coverAction]
		}

		var progressAction: Int = 0
		fun getProgressAction(): RHMIAction? {
			return app.actions[progressAction]
		}

		var playListAction: Int = 0
		fun getPlayListAction(): RHMIAction? {
			return app.actions[playListAction]
		}

		var albumAction: Int = 0
		fun getAlbumAction(): RHMIAction? {
			return app.actions[albumAction]
		}

		var alternativeTextModel: Int = 0
		fun getAlternativeTextModel(): RHMIModel? {
			return app.models[alternativeTextModel]
		}

		var trackTextModel: Int = 0
		fun getTrackTextModel(): RHMIModel? {
			return app.models[trackTextModel]
		}

		var playListProgressTextModel: Int = 0
		fun getPlayListProgressTextModel(): RHMIModel? {
			return app.models[playListProgressTextModel]
		}

		var playListTextModel: Int = 0
		fun getPlayListTextModel(): RHMIModel? {
			return app.models[playListTextModel]
		}

		var artistImageModel: Int = 0
		fun getArtistImageModel(): RHMIModel? {
			return app.models[artistImageModel]
		}

		var artistTextModel: Int = 0
		fun getArtistTextModel(): RHMIModel? {
			return app.models[artistTextModel]
		}

		var albumImageModel: Int = 0
		fun getAlbumImageModel(): RHMIModel? {
			return app.models[albumImageModel]
		}

		var albumTextModel: Int = 0
		fun getAlbumTextModel(): RHMIModel? {
			return app.models[albumTextModel]
		}

		var coverImageModel: Int = 0
		fun getCoverImageModel(): RHMIModel? {
			return app.models[coverImageModel]
		}

		var playbackProgressModel: Int = 0
		fun getPlaybackProgressModel(): RHMIModel? {
			return app.models[playbackProgressModel]
		}

		var downloadProgressModel: Int = 0
		fun getDownloadProgressModel(): RHMIModel? {
			return app.models[downloadProgressModel]
		}

		var currentTimeModel: Int = 0
		fun getCurrentTimeModel(): RHMIModel? {
			return app.models[currentTimeModel]
		}

		var elapsingTimeModel: Int = 0
		fun getElapsingTimeModel(): RHMIModel? {
			return app.models[elapsingTimeModel]
		}

		var playListFocusRowModel: Int = 0
		fun getPlayListFocusRowModel(): RHMIModel? {
			return app.models[playListFocusRowModel]
		}

		var providerLogoImageModel: Int = 0
		fun getProviderLogoImageModel(): RHMIModel? {
			return app.models[providerLogoImageModel]
		}

		var statusBarImageModel: Int = 0
		fun getStatusBarImageModel(): RHMIModel? {
			return app.models[statusBarImageModel]
		}

		var playListModel: Int = 0
		fun getPlayListModel(): RHMIModel? {
			return app.models[playListModel]
		}
	}

	class MockState(override val app: RHMIApplication, override val id: Int): RHMIState(app, id) {
		override fun asPlainState(): PlainState {
			return PlainState(app, id)
		}
		override fun asPopupState(): PopupState {
			return PopupState(app, id)
		}
		override fun asToolbarState(): ToolbarState {
			return ToolbarState(app, id)
		}
		override fun asAudioState(): AudioHmiState {
			return AudioHmiState(app, id)
		}
	}

	open fun asPlainState(): PlainState? {
		return this as? PlainState
	}
	open fun asToolbarState(): ToolbarState? {
		return this as? ToolbarState
	}
	open fun asPopupState(): PopupState? {
		return this as? PopupState
	}
	open fun asAudioState(): AudioHmiState? {
		return this as? AudioHmiState
	}
}