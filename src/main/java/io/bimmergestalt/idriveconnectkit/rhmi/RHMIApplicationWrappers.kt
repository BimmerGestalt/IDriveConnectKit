package io.bimmergestalt.idriveconnectkit.rhmi

import de.bmw.idrive.BMWRemoting

interface RHMIApplicationWrapper {
	fun unwrap(): RHMIApplication
}

/**
 * A wrapper RHMIApplication that skips sending data if the same value was previously set
 */
class RHMIApplicationIdempotent(val app: RHMIApplication): RHMIApplication(), RHMIApplicationWrapper {
	override val models = app.models
	override val actions = app.actions
	override val events = app.events
	override val states = app.states
	override val components = app.components

	override fun setModel(modelId: Int, value: Any) {
		val model = models[modelId]
		val identical = when(model) {
			is RHMIModel.RaIntModel -> model.value == value
			is RHMIModel.RaDataModel -> model.value == value
			is RHMIModel.RaBoolModel -> model.value == value
			is RHMIModel.TextIdModel -> model.textId == (value as? BMWRemoting.RHMIResourceIdentifier)?.id
			is RHMIModel.ImageIdModel -> model.imageId == (value as? BMWRemoting.RHMIResourceIdentifier)?.id
			else -> false
		}
		if (!identical) {
			app.setModel(modelId, value)
		}
	}

	override fun setProperty(componentId: Int, propertyId: Int, value: Any?) {
		if (components[componentId]?.properties?.get(propertyId)?.value != value)
			app.setProperty(componentId, propertyId, value)
	}

	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any?>) {
		app.triggerHMIEvent(eventId, args)
	}

	override fun unwrap(): RHMIApplication {
		return (app as? RHMIApplicationWrapper)?.unwrap() ?: app
	}
}

/**
 * A wrapper RHMIApplication that ensures only a single thread can send to the car at a time
 * Pass in a lock object to synchronize around, or null to synchronize on itself
 */
class RHMIApplicationSynchronized(val app: RHMIApplication, private val lock: Any?): RHMIApplication(), RHMIApplicationWrapper {
	override val models = app.models
	override val actions = app.actions
	override val events = app.events
	override val states = app.states
	override val components = app.components

	/** Run some code in the same lock as this RHMI Application */
	fun runSynchronized(task: () -> Unit) {
		synchronized(lock ?: this) {
			task()
		}
	}

	override fun setModel(modelId: Int, value: Any) = runSynchronized {
		app.setModel(modelId, value)
	}

	override fun setProperty(componentId: Int, propertyId: Int, value: Any?) = runSynchronized {
		app.setProperty(componentId, propertyId, value)
	}

	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any?>) = runSynchronized {
		app.triggerHMIEvent(eventId, args)
	}

	override fun unwrap(): RHMIApplication {
		return (app as? RHMIApplicationWrapper)?.unwrap() ?: app
	}
}