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

	val modelData = HashMap<Int, Any?>()
	val propertyData = HashMap<Int, HashMap<Int, Any?>>()


	override fun setModel(modelId: Int, value: Any?) {
		modelData[modelId] = value
	}
	override fun getModel(modelId: Int): Any? = modelData[modelId]

	override fun setProperty(componentId: Int, propertyId: Int, value: Any?) {
		if (!propertyData.containsKey(componentId)) {
			propertyData[componentId] = HashMap()
		}
		propertyData[componentId]!!.set(propertyId, value)
	}
	override fun getProperty(componentId: Int, propertyId: Int): Any? = propertyData[componentId]?.get(propertyId)

	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any?>) {
		triggeredEvents[eventId] = args
	}
}