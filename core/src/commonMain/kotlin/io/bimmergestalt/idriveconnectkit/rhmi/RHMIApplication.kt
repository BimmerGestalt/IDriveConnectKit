package io.bimmergestalt.idriveconnectkit.rhmi


abstract class RHMIApplication {
	abstract val models: MutableMap<Int, RHMIModel>
	abstract val actions: MutableMap<Int, RHMIAction>
	abstract val events: MutableMap<Int, RHMIEvent>
	abstract val states: MutableMap<Int, RHMIState>
	abstract val components: MutableMap<Int, RHMIComponent>

	var ignoreUpdates = false

	abstract fun setModel(modelId: Int, value: Any?)

	open fun getModel(modelId: Int): Any? = null

	abstract fun setProperty(componentId: Int, propertyId: Int, value: Any?)

	open fun getProperty(componentId: Int, propertyId: Int): Any? = null

	abstract fun triggerHMIEvent(eventId: Int, args: Map<Any, Any?>)
}

class RHMIApplicationConcrete : RHMIApplication() {
	/** Only knows about description elements that are specifically set */
	override val models = HashMap<Int, RHMIModel>()
	override val actions = HashMap<Int, RHMIAction>()
	override val events = HashMap<Int, RHMIEvent>()
	override val states = HashMap<Int, RHMIState>()
	override val components = HashMap<Int, RHMIComponent>()

	val modelData = HashMap<Int, Any?>()
	val propertyData = HashMap<Int, MutableMap<Int, Any?>>().withDefault { HashMap() }
	val triggeredEvents = HashMap<Int, Map<*, *>>()

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
