package me.hufman.idriveconnectionkit.rhmi

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BMWRemotingServer
import me.hufman.idriveconnectionkit.XMLUtils
import org.w3c.dom.Document
import java.util.HashMap


interface RHMIApplication {
	val models: HashMap<Int, RHMIModel>
	val actions: HashMap<Int, RHMIAction>

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun setModel(modelId: Int, value: Any)

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun setProperty(componentId: Int, propertyId: Int, value: Any)

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun triggerHMIEvent(eventId: Int, args: Map<Any, Any>)
}

class RHMIApplicationEtch private constructor(val remoteServer: BMWRemotingServer, val rhmiHandle: Int) : RHMIApplication {

	override val models = HashMap<Int, RHMIModel>()
	override val actions = HashMap<Int, RHMIAction>()

	companion object {
		fun parseUiDescription(remoteServer: BMWRemotingServer, handle: Int, description: ByteArray): RHMIApplication {
			val app = RHMIApplicationEtch(remoteServer, handle)
			app.loadFromXML(XMLUtils.loadXML(description))
			return app
		}
	}

	private fun loadFromXML(description: Document) {
		XMLUtils.childNodes(description.getElementsByTagName("pluginApp")).forEach {
			XMLUtils.childNodes(XMLUtils.getChildNodeNamed(it, "models")).forEach {
				val model = RHMIModel.loadFromXML(this, it)
				if (model != null)
					models[model.id] = model
			}
			XMLUtils.childNodes(XMLUtils.getChildNodeNamed(it, "actions")).forEach {
				val action = RHMIAction.loadFromXML(this, it)
				if (action != null)
					actions[action.id] = action
			}
		}
	}

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