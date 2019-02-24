package me.hufman.idriveconnectionkit.rhmi

import me.hufman.idriveconnectionkit.xmlutils.*
import org.w3c.dom.Node

abstract class RHMIProperty(val id: Int, var value: Any = 0) {

	enum class PropertyId(val id: Int) {
		ENABLED(1),
		SELECTABLE(2),
		VISIBLE(3),
		VALID(4),
		LIST_COLUMNWIDTH(6),
		WIDTH(9),
		HEIGHT(10),
		POSITION_X(20),
		POSITION_Y(21)
	}

	companion object {
		fun loadFromXML(node: Node): RHMIProperty? {
			val attrs = node.getAttributesMap()

			val id = attrs["id"]?.toInt() ?: return null
			val value = attrs["value"]?.toIntOrNull() ?:
			                  attrs["value"] ?: return null

			val condition = node.getChildNamed("condition")
			val assignmentsNode = condition?.getChildNamed("assignments")
			val assignments = if (assignmentsNode != null) assignmentMap(assignmentsNode) else null
			return when (condition?.getAttribute("conditionType")) {
				"LAYOUTBAG" -> LayoutBag(id, value, assignments as Map)
				else -> SimpleProperty(id, value)
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
	}

	class SimpleProperty(id: Int, value: Any = 0): RHMIProperty(id, value)

	class LayoutBag(id: Int, value: Any = 0, val values: Map<Int, Any>): RHMIProperty(id, value) {
		fun get(layout: Int): Any {
			return values.getOrElse(layout) { value }
		}
	}
}
