package io.bimmergestalt.idriveconnectkit.rhmi.deserialization

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIAction
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIApplication
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttributesMap
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildElements
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildNamed
import org.w3c.dom.Node

fun RHMIAction.Companion.loadFromXML(app: RHMIApplication, node: Node): RHMIAction? {
	val attrs = node.getAttributesMap()

	val id = attrs["id"]?.toInt() ?: return null

	if (node.nodeName == "combinedAction") {
		val subactions = node.getChildNamed("actions").getChildElements().mapNotNull { subactionNode ->
			loadFromXML(app, subactionNode)
		}
		val raAction = subactions.firstOrNull { it is RHMIAction.RAAction } as RHMIAction.RAAction?
		val hmiAction = subactions.firstOrNull { it is RHMIAction.HMIAction } as RHMIAction.HMIAction?
		val action = RHMIAction.CombinedAction(app, id, raAction, hmiAction)
		XMLUtils.unmarshalAttributes(action, attrs)
		return action
	}

	val action = when (node.nodeName) {
		"hmiAction" -> RHMIAction.HMIAction(app, id)
		"raAction" -> RHMIAction.RAAction(app, id)
		"linkAction" -> RHMIAction.LinkAction(app, id)
		else -> null
	}

	if (action != null) {
		XMLUtils.unmarshalAttributes(action, attrs)
	}
	return action
}