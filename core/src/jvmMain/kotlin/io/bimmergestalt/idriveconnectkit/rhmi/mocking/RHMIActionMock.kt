package io.bimmergestalt.idriveconnectkit.rhmi.mocking

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIAction

class MockActionMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIAction>() {
	override fun get(key:Int):RHMIAction {
		return super.get(key) ?: RHMIActionMock(app, key)
	}

	inline fun <reified R: RHMIAction> computeIfWrongType(id: Int, compute: (Int) -> R): R {
		val existing = app.actions[id]
		return if (existing is R) { existing }
		else {
			compute(id).also {
				app.actions[id] = it
			}
		}
	}
}

class RHMIActionMock(override val app: RHMIApplicationMock, override val id: Int): RHMIAction(app, id) {
	override fun asCombinedAction(): CombinedAction {
		return app.actions.computeIfWrongType(id) {
			CombinedAction(app, id, RAAction(app, id), HMIAction(app, id))
		}
	}

	override fun asHMIAction(): HMIAction {
		return app.actions.computeIfWrongType(id) {
			HMIAction(app, id)
		}
	}

	override fun asRAAction(): RAAction {
		return app.actions.computeIfWrongType(id) {
			RAAction(app, id)
		}
	}

	override fun asLinkAction(): LinkAction {
		return app.actions.computeIfWrongType(id) {
			LinkAction(app, id)
		}
	}
}