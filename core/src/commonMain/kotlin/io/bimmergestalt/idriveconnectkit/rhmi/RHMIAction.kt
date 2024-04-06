package io.bimmergestalt.idriveconnectkit.rhmi


abstract class RHMIAction protected constructor(open val app: RHMIApplication, open val id: Int) {

	companion object { }

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