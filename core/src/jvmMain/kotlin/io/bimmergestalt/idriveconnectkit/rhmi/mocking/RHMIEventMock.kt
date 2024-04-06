package io.bimmergestalt.idriveconnectkit.rhmi.mocking

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIEvent

class MockEventMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIEvent>() {
	override fun get(key:Int):RHMIEvent {
		return super.get(key) ?: RHMIEventMock(app, key)
	}

	inline fun <reified R: RHMIEvent> computeIfWrongType(id: Int, compute: (Int) -> R): R {
		val existing = app.events[id]
		return if (existing is R) { existing }
		else {
			compute(id).also {
				app.events[id] = it
			}
		}
	}
}

class RHMIEventMock(override val app: RHMIApplicationMock, override val id: Int): RHMIEvent(app, id) {
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