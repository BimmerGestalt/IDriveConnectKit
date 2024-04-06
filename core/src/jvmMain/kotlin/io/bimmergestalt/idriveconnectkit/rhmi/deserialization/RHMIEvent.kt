package io.bimmergestalt.idriveconnectkit.rhmi.deserialization

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIApplication
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIEvent
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttributesMap
import org.w3c.dom.Node


fun RHMIEvent.Companion.loadFromXML(app: RHMIApplication, node: Node): RHMIEvent? {
	val attrs = node.getAttributesMap()

	val id = attrs["id"]?.toInt() ?: return null

	val event = when (node.nodeName) {
		"popupEvent" -> RHMIEvent.PopupEvent(app, id)
		"actionEvent" -> RHMIEvent.ActionEvent(app, id)
		"notificationEvent" -> RHMIEvent.NotificationEvent(app, id)
		"notificationIconEvent" -> RHMIEvent.NotificationIconEvent(app, id)
		"focusEvent" -> RHMIEvent.FocusEvent(app, id)
		"multimediaInfoEvent" -> RHMIEvent.MultimediaInfoEvent(app, id)
		"statusbarEvent" -> RHMIEvent.StatusbarEvent(app, id)
		else -> null
	}

	if (event != null) {
		XMLUtils.unmarshalAttributes(event, attrs)
	}
	return event
}