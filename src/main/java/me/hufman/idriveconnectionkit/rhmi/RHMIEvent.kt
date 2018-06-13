package me.hufman.idriveconnectionkit.rhmi

import de.bmw.idrive.BMWRemoting
import me.hufman.idriveconnectionkit.xmlutils.XMLUtils
import me.hufman.idriveconnectionkit.xmlutils.getAttributesMap
import org.w3c.dom.Node

abstract class RHMIEvent private constructor(open val app: RHMIApplication, open val id: Int) {

	companion object {
		fun loadFromXML(app: RHMIApplication, node: Node): RHMIEvent? {
			val attrs = node.getAttributesMap()

			val id = attrs["id"]?.toInt() ?: return null

			val event = when (node.nodeName) {
				"popupEvent" -> PopupEvent(app, id)
				"actionEvent" -> ActionEvent(app, id)
				"notificationIconEvent" -> NotificationIconEvent(app, id)
				"focusEvent" -> FocusEvent(app, id)
				"multimediaInfoEvent" -> MultimediaInfoEvent(app, id)
				"statusbarEvent" -> StatusbarEvent(app, id)
				else -> null
			}

			if (event != null) {
				XMLUtils.unmarshalAttributes(event, attrs)
			}
			return event
		}
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun triggerEvent() {
		triggerEvent(mapOf(0 to null))
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun triggerEvent(args: Map<Any, Any?>) {
		app.triggerHMIEvent(id, args)
	}

	class PopupEvent(override val app: RHMIApplication, override val id: Int): RHMIEvent(app, id) {
		var target: Int = 0
		fun getTarget(): RHMIState? {
			return app.states[target]
		}

		var priority: Int = 0
	}

	class ActionEvent(override val app: RHMIApplication, override val id: Int): RHMIEvent(app, id) {
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}
	}

	class NotificationIconEvent(override val app: RHMIApplication, override val id: Int): RHMIEvent(app, id) {
		var imageIdModel: Int = 0
		fun getImageIdModel(): RHMIModel? {
			return app.models[imageIdModel]
		}
	}

	class FocusEvent(override val app: RHMIApplication, override val id: Int): RHMIEvent(app, id) {
		var targetModel: Int = 0
		fun getTargetModel(): RHMIModel? {
			return app.models[targetModel]
		}
	}

	class MultimediaInfoEvent(override val app: RHMIApplication, override val id: Int): RHMIEvent(app, id) {
		var textModel1: Int = 0
		fun getTextModel1(): RHMIModel? {
			return app.models[textModel1]
		}

		var textModel2: Int = 0
		fun getTextModel2(): RHMIModel? {
			return app.models[textModel2]
		}
	}

	class StatusbarEvent(override val app: RHMIApplication, override val id: Int): RHMIEvent(app, id) {
		var textModel: Int = 0
		fun getTextModel(): RHMIModel? {
			return app.models[textModel]
		}
	}

	class MockEvent(override val app: RHMIApplication, override val id: Int): RHMIEvent(app, id)

	open fun asPopupEvent(): PopupEvent? {
		return this as? PopupEvent
	}
	open fun asActionEvent(): ActionEvent? {
		return this as? ActionEvent
	}
	open fun asNotificationIconEvent(): NotificationIconEvent? {
		return this as? NotificationIconEvent
	}
	open fun asFocusEvent(): FocusEvent? {
		return this as? FocusEvent
	}
	open fun asMultimediaInfoEvent(): MultimediaInfoEvent? {
		return this as? MultimediaInfoEvent
	}
	open fun asStatusbarEvent(): StatusbarEvent? {
		return this as? StatusbarEvent
	}
}