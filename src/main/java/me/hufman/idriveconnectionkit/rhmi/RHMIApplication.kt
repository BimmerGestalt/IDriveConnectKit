package me.hufman.idriveconnectionkit.rhmi

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BMWRemotingServer
import me.hufman.IDriveConnectionKit.XMLUtils
import org.w3c.dom.Document
import java.util.HashMap


class RHMIApplication private constructor(val remoteServer: BMWRemotingServer, val rhmiHandle: Int) {

	val models = HashMap<Int, RHMIModel>()

	companion object {
		fun parseUiDescription(remoteServer: BMWRemotingServer, handle: Int, description: ByteArray): RHMIApplication {
			val app = RHMIApplication(remoteServer, handle)
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
		}
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun setModel(modelId: Int, value: Any) {
		this.remoteServer.rhmi_setData(this.rhmiHandle, modelId, value)
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun setProperty(componentId: Int, propertyId: Int, value: Any) {
		val propertyValue = HashMap<Int, Any>()
		propertyValue[0] = value
		this.remoteServer.rhmi_setProperty(rhmiHandle, componentId, propertyId, propertyValue)
	}

	@Throws(BMWRemoting.SecurityException::class, BMWRemoting.IllegalArgumentException::class, BMWRemoting.ServiceException::class)
	fun triggerHMIEvent(eventId: Int, args: Map<Any, Any>) {
		this.remoteServer.rhmi_triggerEvent(rhmiHandle, eventId, args)
	}
}