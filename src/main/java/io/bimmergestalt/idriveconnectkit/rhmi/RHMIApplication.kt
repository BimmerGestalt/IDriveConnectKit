package io.bimmergestalt.idriveconnectkit.rhmi

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BMWRemotingServer
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttribute
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildElements
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildNamed
import org.w3c.dom.Document


abstract class RHMIApplication {
	abstract val models: MutableMap<Int, RHMIModel>
	abstract val actions: MutableMap<Int, RHMIAction>
	abstract val events: MutableMap<Int, RHMIEvent>
	abstract val states: MutableMap<Int, RHMIState>
	abstract val components: MutableMap<Int, RHMIComponent>

	var ignoreUpdates = false

	fun loadFromXML(description: String) {
		return this.loadFromXML(description.toByteArray())
	}
	fun loadFromXML(description: ByteArray) {
		return this.loadFromXML(XMLUtils.loadXML(description))
	}
	fun loadFromXML(description: Document) {
		ignoreUpdates = true
		description.getChildNamed("pluginApps").getChildElements().forEach { pluginAppNode ->
			pluginAppNode.getChildNamed("models").getChildElements().forEach { modelNode ->
				val model = RHMIModel.loadFromXML(this, modelNode)
				if (model != null) {
					models[model.id] = model
					if (model is RHMIModel.FormatDataModel) {
						model.submodels.forEach {	models[it.id] = it }
					}
				}
			}
			pluginAppNode.getChildNamed("actions").getChildElements().forEach { actionNode ->
				val action = RHMIAction.loadFromXML(this, actionNode)
				if (action != null) {
					actions[action.id] = action
					if (action is RHMIAction.CombinedAction) {
						if (action.raAction != null) actions[action.raAction.id] = action.raAction
						if (action.hmiAction != null) actions[action.hmiAction.id] = action.hmiAction
					}
				}
			}
			pluginAppNode.getChildNamed("events").getChildElements().forEach { actionNode ->
				val event = RHMIEvent.loadFromXML(this, actionNode)
				if (event != null) {
					events[event.id] = event
				}
			}
			pluginAppNode.getChildNamed("hmiStates").getChildElements().forEach { stateNode ->
				val state = RHMIState.loadFromXML(this, stateNode)
				if (state != null) {
					states[state.id] = state
					components.putAll(state.components)
					if (state is RHMIState.ToolbarState) {
						components.putAll(state.toolbarComponents)
					}
				}
			}
			val entryButtonNode = pluginAppNode.getChildNamed("entryButton")
			if (entryButtonNode != null) {
				val component = RHMIComponent.loadFromXML(this, entryButtonNode)
				if (component is RHMIComponent.EntryButton) {
					pluginAppNode.getAttribute("applicationType")?.also {
						component.applicationType = it
					}
					pluginAppNode.getAttribute("applicationWeight")?.toIntOrNull()?.also {
						component.applicationWeight = it
					}
					components[component.id] = component
				}
			}
			val instrumentClusterNode = pluginAppNode.getChildNamed("instrumentCluster")
			if (instrumentClusterNode != null) {
				val component = RHMIComponent.loadFromXML(this, instrumentClusterNode)
				if (component != null) {
					components[component.id] = component
				}
			}
		}
		ignoreUpdates = false
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	abstract fun setModel(modelId: Int, value: Any?)

	open fun getModel(modelId: Int): Any? = null

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	abstract fun setProperty(componentId: Int, propertyId: Int, value: Any?)

	open fun getProperty(componentId: Int, propertyId: Int): Any? = null

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
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

class RHMIApplicationEtch constructor(val remoteServer: BMWRemotingServer, val rhmiHandle: Int) : RHMIApplication() {
	/** Represents an application layout that is backed by a Car connection */
	override val models = HashMap<Int, RHMIModel>()
	override val actions = HashMap<Int, RHMIAction>()
	override val events = HashMap<Int, RHMIEvent>()
	override val states = HashMap<Int, RHMIState>()
	override val components = HashMap<Int, RHMIComponent>()

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	override fun setModel(modelId: Int, value: Any?) {
		if (ignoreUpdates) return
		this.remoteServer.rhmi_setData(this.rhmiHandle, modelId, value)
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	override fun setProperty(componentId: Int, propertyId: Int, value: Any?) {
		if (ignoreUpdates) return
		val propertyValue = HashMap<Int, Any?>()
		propertyValue[0] = value
		this.remoteServer.rhmi_setProperty(rhmiHandle, componentId, propertyId, propertyValue)
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any?>) {
		this.remoteServer.rhmi_triggerEvent(rhmiHandle, eventId, args)
	}
}