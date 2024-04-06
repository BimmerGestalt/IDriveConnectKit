package io.bimmergestalt.idriveconnectkit.rhmi.deserialization

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIApplication
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIModel
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIModelLive
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttributesMap
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildElements
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildNamed
import org.w3c.dom.Node

fun RHMIModel.Companion.loadFromXML(app: RHMIApplication, node: Node): RHMIModel? {
	val attrs = node.getAttributesMap()

	val id = attrs["id"]?.toInt() ?: return null

	if (node.nodeName == "formatDataModel") {
		val submodels = node.getChildNamed("models").getChildElements().mapNotNull { submodelNode ->
			loadFromXML(app, submodelNode)
		}
		val model = RHMIModelLive.FormatDataModel(app, id, submodels)
		XMLUtils.unmarshalAttributes(model, attrs)
		return model
	}

	val model = when(node.nodeName) {
		"textIdModel" -> RHMIModelLive.TextIdModel(app, id)
		"imageIdModel" -> RHMIModelLive.ImageIdModel(app, id)
		"raBoolModel" -> RHMIModelLive.RaBoolModel(app, id)
		"raDataModel" -> RHMIModelLive.RaDataModel(app, id)
		"raGaugeModel" -> RHMIModelLive.RaGaugeModel(app, id)
		"raImageModel" -> RHMIModelLive.RaImageModel(app, id)
		"raIntModel" -> RHMIModelLive.RaIntModel(app, id)
		"raListModel" -> RHMIModelLive.RaListModel(app, id)
		else -> null
	}

	if (model != null) {
		XMLUtils.unmarshalAttributes(model, attrs)
	}
	return model
}