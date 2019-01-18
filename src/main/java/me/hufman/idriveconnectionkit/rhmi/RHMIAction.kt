package me.hufman.idriveconnectionkit.rhmi

import me.hufman.idriveconnectionkit.xmlutils.XMLUtils
import me.hufman.idriveconnectionkit.xmlutils.getAttributesMap
import me.hufman.idriveconnectionkit.xmlutils.getChildElements
import me.hufman.idriveconnectionkit.xmlutils.getChildNamed
import org.w3c.dom.Node


abstract class RHMIAction private constructor(open val app: RHMIApplication, open val id: Int) {

	companion object {
		fun loadFromXML(app: RHMIApplication, node: Node): RHMIAction? {
			val attrs = node.getAttributesMap()

			val id = attrs["id"]?.toInt() ?: return null

			if (node.nodeName == "combinedAction") {
				val subactions = node.getChildNamed("actions").getChildElements().mapNotNull { subactionNode ->
					loadFromXML(app, subactionNode)
				}
				val raAction = subactions.firstOrNull { it is RAAction } as RAAction?
				val hmiAction = subactions.firstOrNull { it is HMIAction } as HMIAction?
				val action = CombinedAction(app, id, raAction, hmiAction)
				XMLUtils.unmarshalAttributes(action, attrs)
				return action
			}

			val action = when (node.nodeName) {
				"hmiAction" -> HMIAction(app, id)
				"raAction" -> RAAction(app, id)
				"linkAction" -> LinkAction(app, id)
				else -> null
			}

			if (action != null) {
				XMLUtils.unmarshalAttributes(action, attrs)
			}
			return action
		}
	}

	interface RHMIActionCallback {
		fun onActionEvent(args: Map<*, *>?)
	}

	class CombinedAction(override val app: RHMIApplication, override val id: Int, val raAction: RAAction?, val hmiAction: HMIAction?): RHMIAction(app, id) {
		var sync: String = ""
		var actionType: String = ""

		var onActionEvent: RHMIActionCallback?
			set(value) { raAction?.rhmiActionCallback = value }
			get() = raAction?.rhmiActionCallback

		override fun asRAAction(): RAAction? {
			return raAction
		}
		override fun asHMIAction(): HMIAction? {
			return hmiAction
		}
	}
	class HMIAction(override val app: RHMIApplication, override val id: Int): RHMIAction(app, id) {
		var targetModel: Int = 0
		fun getTargetModel(): RHMIModel? {
			return app.models[targetModel]
		}
		var target: Int = 0
		fun getTargetState(): RHMIState? {
			if (target > 0) {
				return app.states[target]
			} else if (targetModel > 0){
				val modelValue = getTargetModel()?.asRaIntModel()?.value ?:
						getTargetModel()?.asRaDataModel()?.value?.toInt()
				if (modelValue != null) {
					return app.states[modelValue]
				}
			}
			return null
		}
	}
	class RAAction(override val app: RHMIApplication, override val id: Int): RHMIAction(app, id) {
		var rhmiActionCallback: RHMIActionCallback? = null
	}
	class LinkAction(override val app: RHMIApplication, override val id: Int): RHMIAction(app, id) {
		var actionType: String = ""
		var linkModel: Int = 0
		fun getLinkModel(): RHMIModel? {
			return app.models[linkModel]
		}
	}

	class MockAction(override val app: RHMIApplication, override val id: Int): RHMIAction(app, id) // no extra attrs

	open fun asCombinedAction(): CombinedAction? {
		return this as? CombinedAction
	}
	open fun asHMIAction(): HMIAction? {
		return this as? HMIAction
	}
	open fun asRAAction(): RAAction? {
		return this as? RAAction
	}
	open fun asLinkAction(): LinkAction? {
		return this as? LinkAction
	}
}