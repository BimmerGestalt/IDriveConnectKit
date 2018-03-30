package me.hufman.idriveconnectionkit.rhmi

import me.hufman.idriveconnectionkit.xmlutils.XMLUtils
import me.hufman.idriveconnectionkit.xmlutils.getAttributesMap
import me.hufman.idriveconnectionkit.xmlutils.getChildElements
import me.hufman.idriveconnectionkit.xmlutils.getChildNamed
import org.w3c.dom.Node
import java.util.HashMap

abstract class RHMIState private constructor(open val app: RHMIApplication, open val id: Int) {
	val components = HashMap<Int, RHMIComponent>()
	val componentsList = ArrayList<RHMIComponent>()
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
				if (state is ToolbarState) {
					node.getChildNamed("toolbarComponents").getChildElements().forEach { componentNode ->
						val component = RHMIComponent.loadFromXML(app, componentNode)
						if (component != null) {
							state.toolbarComponents[component.id] = component
							state.toolbarComponentsList.add(component)
						}
					}
				}
			}

			return state
		}
	}

	class PlainState(override val app: RHMIApplication, override val id: Int): RHMIState(app, id)
	class ToolbarState(override val app: RHMIApplication, override val id: Int): RHMIState(app, id) {
		val toolbarComponents = HashMap<Int, RHMIComponent>()
		val toolbarComponentsList = ArrayList<RHMIComponent>()
	}
	class PopupState(override val app: RHMIApplication, override val id: Int): RHMIState(app, id)

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
}