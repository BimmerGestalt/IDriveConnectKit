package io.bimmergestalt.idriveconnectkit.rhmi


abstract class RHMIEvent protected constructor(open val app: RHMIApplication, open val id: Int) {

	companion object { }

	fun triggerEvent() {
		triggerEvent(mapOf(0 to null))
	}

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