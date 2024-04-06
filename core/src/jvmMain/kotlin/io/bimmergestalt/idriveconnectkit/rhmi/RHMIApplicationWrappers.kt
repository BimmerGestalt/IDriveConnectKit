package io.bimmergestalt.idriveconnectkit.rhmi

import de.bmw.idrive.BMWRemoting.RHMIResourceIdentifier

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

	internal val sentData = HashMap<Int, Any?>()
	internal val sentProperties = HashMap<Int, MutableMap<Int, Any?>>().withDefault { HashMap() }

	override fun setModel(modelId: Int, value: Any?) {
		val model = models[modelId]
		val previouslySent = sentData.containsKey(modelId)
		val identical = when(model) {
			is RHMIModel.RaIntModel -> model.value == value
			is RHMIModel.RaGaugeModel -> model.value == value
			is RHMIModel.RaDataModel -> model.value == value
			is RHMIModel.RaBoolModel -> model.value == value
			is RHMIModel.TextIdModel -> model.textId == (value as? RHMIResourceIdentifier)?.id
			is RHMIModel.ImageIdModel -> model.imageId == (value as? RHMIResourceIdentifier)?.id
			else -> false
		}
		if (!previouslySent || !identical) {
			app.setModel(modelId, value)
		}
		val saved = when(model) {
			is RHMIModel.RaIntModel -> true
			is RHMIModel.RaGaugeModel -> true
			is RHMIModel.RaDataModel -> true
			is RHMIModel.RaBoolModel -> true
			is RHMIModel.TextIdModel -> true
			is RHMIModel.ImageIdModel -> true
			else -> false
		}
		if (saved) {
			sentData[modelId] = value
		}
	}

	override fun getModel(modelId: Int): Any? =
		sentData[modelId] ?: app.getModel(modelId)

	override fun setProperty(componentId: Int, propertyId: Int, value: Any?) {
		if (getProperty(componentId, propertyId) != value) {
			app.setProperty(componentId, propertyId, value)
			sentProperties.getOrPut(componentId) { HashMap() }[propertyId] = value
		}
	}

	override fun getProperty(componentId: Int, propertyId: Int): Any? =
		sentProperties[componentId]?.get(propertyId) ?: app.getProperty(componentId, propertyId)

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
	fun <R> runSynchronized(task: () -> R): R {
		synchronized(lock ?: this) {
			return task()
		}
	}

	override fun setModel(modelId: Int, value: Any?) = runSynchronized {
		app.setModel(modelId, value)
	}

	override fun getModel(modelId: Int): Any? = runSynchronized {
		app.getModel(modelId)
	}

	override fun setProperty(componentId: Int, propertyId: Int, value: Any?) = runSynchronized {
		app.setProperty(componentId, propertyId, value)
	}

	override fun getProperty(componentId: Int, propertyId: Int): Any? = runSynchronized {
		app.getProperty(componentId, propertyId)
	}

	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any?>) = runSynchronized {
		app.triggerHMIEvent(eventId, args)
	}

	override fun unwrap(): RHMIApplication {
		return (app as? RHMIApplicationWrapper)?.unwrap() ?: app
	}
}