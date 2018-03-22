package me.hufman.idriveconnectionkit.rhmi.mocking

import me.hufman.idriveconnectionkit.rhmi.RHMIApplication
import me.hufman.idriveconnectionkit.rhmi.RHMIModel
import java.util.HashMap

class RHMIApplicationMock : RHMIApplication {
	override val models = MockModelMap(this)

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