package me.hufman.idriveconnectionkit.rhmi.mocking

import me.hufman.idriveconnectionkit.rhmi.*
import java.util.HashMap

class RHMIApplicationMock : RHMIApplication {
	/** Automatically instantiates elements of the application layout */
	override val models = MockModelMap(this)
	override val actions = MockActionMap(this)
	override val states = MockStateMap(this)
	override val components = MockComponentMap(this)

	val modelData = HashMap<Int, Any>()

	override fun setModel(modelId: Int, value: Any) {
		modelData[modelId] = value
	}

	override fun setProperty(componentId: Int, propertyId: Int, value: Any) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}

class MockModelMap(val app: RHMIApplication) : HashMap<Int, RHMIModel>() {
	override fun get(key:Int):RHMIModel.MockModel {
		return RHMIModel.MockModel(app, key)
	}
}
class MockActionMap(val app: RHMIApplication) : HashMap<Int, RHMIAction>() {
	override fun get(key:Int):RHMIAction.MockAction {
		return RHMIAction.MockAction(app, key)
	}
}
class MockStateMap(val app: RHMIApplication) : HashMap<Int, RHMIState>() {
	override fun get(key:Int):RHMIState.MockState {
		return RHMIState.MockState(app, key)
	}
}
class MockComponentMap(val app: RHMIApplication) : HashMap<Int, RHMIComponent>() {
	override fun get(key:Int):RHMIComponent.MockComponent {
		return RHMIComponent.MockComponent(app, key)
	}
}