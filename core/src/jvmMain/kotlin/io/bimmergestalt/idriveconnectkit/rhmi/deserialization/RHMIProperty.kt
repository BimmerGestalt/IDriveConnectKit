package io.bimmergestalt.idriveconnectkit.rhmi.deserialization

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIApplication
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIProperty
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttribute
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttributesMap
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildElements
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildNamed
import org.w3c.dom.Node

fun RHMIProperty.Companion.loadFromXML(app: RHMIApplication, componentId: Int, node: Node): RHMIProperty? {
	val attrs = node.getAttributesMap()

	val id = attrs["id"]?.toInt() ?: return null
	val value = attrs["value"]?.toIntOrNull() ?:
	attrs["value"] ?: return null

	val condition = node.getChildNamed("condition")
	val assignmentsNode = condition?.getChildNamed("assignments")
	val assignments = if (assignmentsNode != null) assignmentMap(assignmentsNode) else null
	return when (condition?.getAttribute("conditionType")) {
		"LAYOUTBAG" -> RHMIProperty.LayoutBag(id, value, assignments as Map)
		else -> RHMIProperty.AppProperty(app, componentId, id, value)
	}
}

fun assignmentMap(node: Node): Map<Int, Any> {
	/** Given the assignments node, return a map */
	val map = HashMap<Int, Any>()
	node.getChildElements().filter { it.nodeName == "assignment" }.forEach {
		val attrs = it.getAttributesMap()
		val id = attrs["conditionValue"]?.toIntOrNull()
		val value = attrs["value"]?.toIntOrNull() ?:
		attrs["value"]
		if (id != null && value != null) {
			map[id] = value
		}
	}
	return map
}