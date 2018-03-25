package me.hufman.idriveconnectionkit.rhmi

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BMWRemotingServer
import me.hufman.idriveconnectionkit.XMLUtils
import org.w3c.dom.Document
import java.util.HashMap


interface RHMIApplication {
	val models: HashMap<Int, RHMIModel>
	val actions: HashMap<Int, RHMIAction>
	val components: HashMap<Int, RHMIComponent>

	fun loadFromXML(description: String) {
		return this.loadFromXML(description.toByteArray())
	}
	fun loadFromXML(description: ByteArray) {
		return this.loadFromXML(XMLUtils.loadXML(description))
	}
	fun loadFromXML(description: Document) {
		XMLUtils.childNodes(description.getElementsByTagName("pluginApp")).forEach {pluginAppNode ->
			XMLUtils.childNodes(XMLUtils.getChildNodeNamed(pluginAppNode, "models")).forEach { modelNode ->
				val model = RHMIModel.loadFromXML(this, modelNode)
				if (model != null) {
					models[model.id] = model
					if (model is RHMIModel.FormatDataModel) {
						model.submodels.forEach {	models[it.id] = it }
					}
				}
			}
			XMLUtils.childNodes(XMLUtils.getChildNodeNamed(pluginAppNode, "actions")).forEach { actionNode ->
				val action = RHMIAction.loadFromXML(this, actionNode)
				if (action != null) {
					actions[action.id] = action
					if (action is RHMIAction.CombinedAction) {
						if (action.raAction != null) actions[action.raAction.id] = action.raAction
						if (action.hmiAction != null) actions[action.hmiAction.id] = action.hmiAction
					}
				}
			}
			XMLUtils.childNodes(XMLUtils.getChildNodeNamed(pluginAppNode, "hmiStates")).forEach { stateNode ->
				XMLUtils.childNodes(XMLUtils.getChildNodeNamed(stateNode, "toolbarComponents")).forEach { componentNode ->
					val component = RHMIComponent.loadFromXML(this, componentNode)
					if (component != null) {
						components[component.id] = component
					}
				}
				XMLUtils.childNodes(XMLUtils.getChildNodeNamed(stateNode, "components")).forEach { componentNode ->
					val component = RHMIComponent.loadFromXML(this, componentNode)
					if (component != null) {
						components[component.id] = component
					}
				}
			}
			val entryButtonNode = XMLUtils.getChildNodeNamed(pluginAppNode, "entryButton")
			if (entryButtonNode != null) {
				val component = RHMIComponent.loadFromXML(this, entryButtonNode)
				if (component != null) {
					components[component.id] = component
				}
			}
		}
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun setModel(modelId: Int, value: Any)

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun setProperty(componentId: Int, propertyId: Int, value: Any)

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun triggerHMIEvent(eventId: Int, args: Map<Any, Any>)
}

class RHMIApplicationConcrete : RHMIApplication {
	/** Only knows about description elements that are specifically set */
	override val models = HashMap<Int, RHMIModel>()
	override val actions = HashMap<Int, RHMIAction>()
	override val components = HashMap<Int, RHMIComponent>()

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

class RHMIApplicationEtch constructor(val remoteServer: BMWRemotingServer, val rhmiHandle: Int) : RHMIApplication {
	override val models = HashMap<Int, RHMIModel>()
	override val actions = HashMap<Int, RHMIAction>()
	override val components = HashMap<Int, RHMIComponent>()

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	override fun setModel(modelId: Int, value: Any) {
		this.remoteServer.rhmi_setData(this.rhmiHandle, modelId, value)
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	override fun setProperty(componentId: Int, propertyId: Int, value: Any) {
		val propertyValue = HashMap<Int, Any>()
		propertyValue[0] = value
		this.remoteServer.rhmi_setProperty(rhmiHandle, componentId, propertyId, propertyValue)
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	override fun triggerHMIEvent(eventId: Int, args: Map<Any, Any>) {
		this.remoteServer.rhmi_triggerEvent(rhmiHandle, eventId, args)
	}
}