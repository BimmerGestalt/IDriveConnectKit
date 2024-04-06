package io.bimmergestalt.idriveconnectkit.rhmi

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BMWRemotingServer
import java.io.IOException


class RHMIApplicationEtch(val remoteServer: BMWRemotingServer, val rhmiHandle: Int) : RHMIApplication() {
	/** Represents an application layout that is backed by a Car connection
	 * Most data is not retained, so if you want to read data back out,
	 * use RHMIApplicationConcrete or RHMIApplicationIdempotent
	 * */
	override val models = HashMap<Int, RHMIModel>()
	override val actions = HashMap<Int, RHMIAction>()
	override val events = HashMap<Int, RHMIEvent>()
	override val states = HashMap<Int, RHMIState>()
	override val components = HashMap<Int, RHMIComponent>()

	// remember a little bit of properties and small ints
	val modelData = HashMap<Int, Any?>()
	val propertyData = HashMap<Int, MutableMap<Int, Any?>>()

	@Throws(IOException::class)
	override fun setModel(modelId: Int, value: Any?) {
		if (value is Int || value is BMWRemoting.RHMIResourceIdentifier) {
			modelData[modelId] = value
		} else {
			modelData.remove(modelId)
		}
		if (ignoreUpdates) return
		try {
			this.remoteServer.rhmi_setData(this.rhmiHandle, modelId, value)
		} catch (e: Exception) {
			throw IOException(e)
		}
	}
	override fun getModel(modelId: Int): Any? = modelData[modelId]

	@Throws(IOException::class)
	override fun setProperty(componentId: Int, propertyId: Int, value: Any?) {
		propertyData.getOrPut(componentId){HashMap()}[propertyId] = value
		if (ignoreUpdates) return
		val propertyValue = HashMap<Int, Any?>()
		propertyValue[0] = value
		try {
			this.remoteServer.rhmi_setProperty(rhmiHandle, componentId, propertyId, propertyValue)
		} catch (e: Exception) {
			throw IOException(e)
		}
	}
	override fun getProperty(componentId: Int, propertyId: Int): Any? = propertyData[componentId]?.get(propertyId)

	@Throws(IOException::class)
	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any?>) {
		try {
			this.remoteServer.rhmi_triggerEvent(rhmiHandle, eventId, args)
		} catch (e: Exception) {
			throw IOException(e)
		}
	}
}