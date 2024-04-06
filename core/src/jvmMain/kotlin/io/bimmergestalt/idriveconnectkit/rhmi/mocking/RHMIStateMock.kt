package io.bimmergestalt.idriveconnectkit.rhmi.mocking

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIState


class MockStateMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIState>() {
	override fun get(key:Int):RHMIState {
		return super.get(key) ?: RHMIStateMock(app, key)
	}

	inline fun <reified R: RHMIState> computeIfWrongType(id: Int, compute: (Int) -> R): R {
		val existing = app.states[id]
		return if (existing is R) { existing }
		else {
			compute(id).also {
				app.states[id] = it
			}
		}
	}
}

class RHMIStateMock(override val app: RHMIApplicationMock, override val id: Int): RHMIState(app, id) {
	override fun asPlainState(): PlainState {
		return app.states.computeIfWrongType(id) {
			PlainState(app, id)
		}
	}
	override fun asPopupState(): PopupState {
		return app.states.computeIfWrongType(id) {
			PopupState(app, id)
		}
	}
	override fun asToolbarState(): ToolbarState {
		return app.states.computeIfWrongType(id) {
			ToolbarState(app, id)
		}
	}
	override fun asAudioState(): AudioHmiState {
		return app.states.computeIfWrongType(id) {
			AudioHmiState(app, id)
		}
	}
	override fun asCalendarMonthState(): CalendarMonthState {
		return app.states.computeIfWrongType(id) {
			CalendarMonthState(app, id)
		}
	}
	override fun asCalendarState(): CalendarState {
		return app.states.computeIfWrongType(id) {
			CalendarState(app, id)
		}
	}
}
