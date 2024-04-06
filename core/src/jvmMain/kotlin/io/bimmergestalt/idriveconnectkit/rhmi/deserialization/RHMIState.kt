package io.bimmergestalt.idriveconnectkit.rhmi.deserialization

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIApplication
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIComponent
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIProperty
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIState
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttributesMap
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildElements
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildNamed
import org.w3c.dom.Node


fun RHMIState.Companion.loadFromXML(app: RHMIApplication, node: Node): RHMIState? {
	val attrs = node.getAttributesMap()

	val id = attrs["id"]?.toInt() ?: return null

	val state = when (node.nodeName) {
		"hmiState" -> RHMIState.PlainState(app, id)
		"toolbarHmiState" -> RHMIState.ToolbarState(app, id)
		"popupHmiState" -> RHMIState.PopupState(app, id)
		"audioHmiState" -> RHMIState.AudioHmiState(app, id)
		"calendarMonthHmiState" -> RHMIState.CalendarMonthState(app, id)
		"calendarHmiState" -> RHMIState.CalendarState(app, id)
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
				val property = RHMIProperty.loadFromXML(app, state.id, it)
				if (property != null)
					state.properties[property.id] = property
			}
		}

		if (state is RHMIState.ToolbarState) {
			node.getChildNamed("toolbarComponents").getChildElements().forEach { componentNode ->
				val component = RHMIComponent.loadFromXML(app, componentNode)
				if (component is RHMIComponent.ToolbarButton) {
					state.toolbarComponents[component.id] = component
					state.toolbarComponentsList.add(component)
				}
			}
		}

		val optionComponents = node.getChildNamed("optionComponents")
		if (optionComponents != null) {
			optionComponents.getChildElements().forEach { optionComponentNode ->
				val component = RHMIComponent.loadFromXML(app, optionComponentNode)
				if (component != null) {
					state.optionComponentsList.add(component)
				}
			}
		}
	}

	return state
}