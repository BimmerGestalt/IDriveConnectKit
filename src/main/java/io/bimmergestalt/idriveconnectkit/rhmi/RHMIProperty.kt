package io.bimmergestalt.idriveconnectkit.rhmi

import io.bimmergestalt.idriveconnectkit.xmlutils.*
import org.w3c.dom.Node

abstract class RHMIProperty(val id: Int, open var value: Any? = null) {

	enum class PropertyId(val id: Int) {
		ENABLED(1),
		SELECTABLE(2),
		VISIBLE(3),
		VALID(4),
		TOOLBARHMISTATE_PAGING(5),
		LIST_COLUMNWIDTH(6),
		LIST_HASIDCOLUMN(7),
		LABEL_WAITINGANIMATION(8),
		WIDTH(9),
		HEIGHT(10),
		ALIGNMENT(17),
		OFFSET_X(18),
		OFFSET_Y(19),
		POSITION_X(20),
		POSITION_Y(21),
		BOOKMARKABLE(22),
		SPEAKABLE(23),
		HMISTATE_TABLETYPE(24),
		CURSOR_WIDTH(25),
		HMISTATE_TABLELAYOUT(26),
		TOOLBARHMISTATE_PAGING_LIMITED(35),
		SPEEDLOCK(36),
		CUTTYPE(37),
		CHECKED(53),
		UUID(55),
		MODAL(56),
	}

	companion object {
		fun loadFromXML(app: RHMIApplication, componentId: Int, node: Node): RHMIProperty? {
			val attrs = node.getAttributesMap()

			val id = attrs["id"]?.toInt() ?: return null
			val value = attrs["value"]?.toIntOrNull() ?:
			                  attrs["value"] ?: return null

			val condition = node.getChildNamed("condition")
			val assignmentsNode = condition?.getChildNamed("assignments")
			val assignments = if (assignmentsNode != null) assignmentMap(assignmentsNode) else null
			return when (condition?.getAttribute("conditionType")) {
				"LAYOUTBAG" -> LayoutBag(id, value, assignments as Map)
				else -> AppProperty(app, componentId, id, value)
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

	open fun getForLayout(layout: Int): Any? {
		return value
	}

	class SimpleProperty(id: Int, value: Any = 0): RHMIProperty(id, value)

	class LayoutBag(id: Int, value: Any = 0, val values: Map<Int, Any>): RHMIProperty(id, value) {
		override fun getForLayout(layout: Int): Any? {
			return values.getOrElse(layout) { value }
		}
	}

	class AppProperty(val app: RHMIApplication, val componentId: Int, id: Int, private val defaultValue: Any? = null): RHMIProperty(id) {
		override fun getForLayout(layout: Int): Any? = app.getProperty(componentId, id) ?: value
		override var value: Any?
			get() = app.getProperty(componentId, id) ?: defaultValue
			set(value) { app.setProperty(componentId, id, value) }
	}
}
