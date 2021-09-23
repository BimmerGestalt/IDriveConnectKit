package io.bimmergestalt.idriveconnectkit.rhmi

import de.bmw.idrive.BMWRemoting
import io.bimmergestalt.idriveconnectkit.rhmi.mocking.RHMIApplicationMock
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttributesMap
import org.w3c.dom.Node

abstract class RHMIEvent private constructor(open val app: RHMIApplication, open val id: Int) {

	companion object {
		fun loadFromXML(app: RHMIApplication, node: Node): RHMIEvent? {
			val attrs = node.getAttributesMap()

			val id = attrs["id"]?.toInt() ?: return null

			val event = when (node.nodeName) {
				"popupEvent" -> PopupEvent(app, id)
				"actionEvent" -> ActionEvent(app, id)
				"notificationEvent" -> NotificationEvent(app, id)
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

	class NotificationEvent(override val app: RHMIApplication, override val id: Int): RHMIEvent(app, id) {
		var imageModel: Int = 0
		fun getImageModel(): RHMIModel? {
			return app.models[imageModel]
		}

		var titleTextModel: Int = 0
		fun getTitleTextModel(): RHMIModel? {
			return app.models[titleTextModel]
		}

		var notificationTextModel: Int = 0
		fun getNotificationTextModel(): RHMIModel? {
			return app.models[notificationTextModel]
		}

		var notificationPriority: Int = 0
		fun getNotificationPriority(): RHMIModel? {
			return app.models[notificationPriority]
		}

		var indexId: Int = 0
		fun getIndexId(): RHMIModel? {
			return app.models[indexId]
		}

		var categoryTextModel: Int = 0
		fun getCategoryTextModel(): RHMIModel? {
			return app.models[categoryTextModel]
		}

		var optionTextId: Int = 0

		var actionId: Int = 0
		fun getActionId(): RHMIAction? {
			return app.actions[actionId]
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

	class MockEvent(override val app: RHMIApplicationMock, override val id: Int): RHMIEvent(app, id) {
		override fun asPopupEvent(): PopupEvent {
			return app.events.computeIfWrongType(id) {
				PopupEvent(app, id)
			}
		}
		override fun asActionEvent(): ActionEvent {
			return app.events.computeIfWrongType(id) {
				ActionEvent(app, id)
			}
		}
		override fun asNotificationIconEvent(): NotificationIconEvent {
			return app.events.computeIfWrongType(id) {
				NotificationIconEvent(app, id)
			}
		}
		override fun asFocusEvent(): FocusEvent {
			return app.events.computeIfWrongType(id) {
				FocusEvent(app, id)
			}
		}
		override fun asMultimediaInfoEvent(): MultimediaInfoEvent {
			return app.events.computeIfWrongType(id) {
				MultimediaInfoEvent(app, id)
			}
		}
		override fun asStatusbarEvent(): StatusbarEvent {
			return app.events.computeIfWrongType(id) {
				StatusbarEvent(app, id)
			}
		}
	}

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