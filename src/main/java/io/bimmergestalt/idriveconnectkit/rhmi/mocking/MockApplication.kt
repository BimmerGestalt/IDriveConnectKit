package io.bimmergestalt.idriveconnectkit.rhmi.mocking

import io.bimmergestalt.idriveconnectkit.rhmi.*

class RHMIApplicationMock : RHMIApplication() {
	/** Automatically instantiates elements of the application layout */
	override val models = MockModelMap(this)
	override val actions = MockActionMap(this)
	override val events = MockEventMap(this)
	override val states = MockStateMap(this)
	override val components = MockComponentMap(this)
	val triggeredEvents = HashMap<Int, Map<*, *>>()

	val modelData = HashMap<Int, Any>()
	val propertyData = HashMap<Int, HashMap<Int, Any?>>()


	override fun setModel(modelId: Int, value: Any) {
		modelData[modelId] = value
	}

	override fun setProperty(componentId: Int, propertyId: Int, value: Any?) {
		if (!propertyData.containsKey(componentId)) {
			propertyData[componentId] = HashMap()
		}
		propertyData[componentId]!!.set(propertyId, value)
	}

	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any?>) {
		triggeredEvents[eventId] = args
	}
}

class MockModelMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIModel>() {
	override fun get(key:Int):RHMIModel {
		return super.get(key) ?: RHMIModel.MockModel(app, key)
	}

	inline fun <reified R: RHMIModel> computeIfWrongType(id: Int, compute: (Int) -> R): R {
		val existing = app.models[id]
		return if (existing is R) { existing }
		else {
			compute(id).also {
				app.models[id] = it
			}
		}
	}
}
class MockActionMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIAction>() {
	override fun get(key:Int):RHMIAction {
		return super.get(key) ?: RHMIAction.MockAction(app, key)
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
class MockEventMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIEvent>() {
	override fun get(key:Int):RHMIEvent {
		return super.get(key) ?: RHMIEvent.MockEvent(app, key)
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
class MockStateMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIState>() {
	override fun get(key:Int):RHMIState {
		return super.get(key) ?: RHMIState.MockState(app, key)
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
class MockComponentMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIComponent>() {
	override fun get(key:Int):RHMIComponent {
		return super.get(key) ?: RHMIComponent.MockComponent(app, key)
	}

	inline fun <reified R: RHMIComponent> computeIfWrongType(id: Int, compute: (Int) -> R): R {
		val existing = app.components[id]
		return if (existing is R) { existing }
		else {
			compute(id).also {
				app.components[id] = it
			}
		}
	}
}