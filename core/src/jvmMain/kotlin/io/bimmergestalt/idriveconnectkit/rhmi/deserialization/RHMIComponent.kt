package io.bimmergestalt.idriveconnectkit.rhmi.deserialization

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIApplication
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIComponent
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIProperty
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttributesMap
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildElements
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildNamed
import org.w3c.dom.Node

fun RHMIComponent.Companion.loadFromXML(app: RHMIApplication, node: Node): RHMIComponent? {
	val attrs = node.getAttributesMap()

	val id = attrs["id"]?.toInt() ?: return null

	val component = when (node.nodeName) {
		"separator" -> RHMIComponent.Separator(app, id)
		"image" -> RHMIComponent.Image(app, id)
		"label" -> RHMIComponent.Label(app, id)
		"list" -> RHMIComponent.List(app, id)
		"entryButton" -> RHMIComponent.EntryButton(app, id)
		"instrumentCluster" -> RHMIComponent.InstrumentCluster(app, id)
		"button" -> if (attrs["model"] == null) RHMIComponent.ToolbarButton(
			app,
			id
		) else RHMIComponent.Button(app, id)
		"checkbox" -> RHMIComponent.Checkbox(app, id)
		"gauge" -> RHMIComponent.Gauge(app, id)
		"input" -> RHMIComponent.Input(app, id)
		"calendarDay" -> RHMIComponent.CalendarDay(app, id)
		else -> null
	}

	if (component != null) {
		XMLUtils.unmarshalAttributes(component, attrs)

		val propertyNodes = node.getChildNamed("properties")
		if (propertyNodes != null) {
			propertyNodes.getChildElements().filter { it.nodeName == "property" }.forEach {
				val property = RHMIProperty.loadFromXML(app, component.id, it)
				if (property != null)
					component.properties[property.id] = property
			}
		}
	}
	return component
}