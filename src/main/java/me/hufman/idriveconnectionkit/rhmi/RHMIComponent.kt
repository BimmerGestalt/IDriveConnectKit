package me.hufman.idriveconnectionkit.rhmi

import me.hufman.idriveconnectionkit.XMLUtils
import org.w3c.dom.Node

abstract class RHMIComponent private constructor(open val app: RHMIApplication, open val id: Int) {

	companion object {
		fun loadFromXML(app: RHMIApplication, node: Node): RHMIComponent? {
			val attrs = XMLUtils.getAttributes(node)

			val id = attrs["id"]?.toInt() ?: return null

			val component = when (node.nodeName) {
				"separator" -> Separator(app, id)
				"image" -> Image(app, id)
				"label" -> Label(app, id)
				"list" -> List(app, id)
				"entryButton" -> EntryButton(app, id)
				"button" -> if (attrs["model"] == null) ToolbarButton(app, id) else Button(app, id)
				"checkbox" -> Checkbox(app, id)
				"gauge" -> Gauge(app, id)
				"input" -> Input(app, id)
				else -> null
			}

			if (component != null) {
				XMLUtils.unmarshalAttributes(component, attrs)
			}
			return component
		}
	}

	class Separator(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id)
	class Image(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The model to show in the label
		var model: Int = 0
		fun getModel(): RHMIModel? {
			return app.models[model]
		}
	}
	class Label(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The model to show in the label
		var model: Int = 0
		fun getModel(): RHMIModel? {
			return app.models[model]
		}
	}
	class List(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The list model to show
		var model: Int = 0
		fun getModel(): RHMIModel.RaListModel? {
			return app.models[model]?.asRaListModel()
		}
		// The action when a user clicks the controller
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}
		// The action when the selection changes
		var selectAction: Int = 0
		fun getSelectAction(): RHMIAction? {
			return app.actions[selectAction]
		}
	}
	class EntryButton(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The text model to show as a label
		var model: Int = 0
		fun getModel(): RHMIModel? {
			return app.models[model]
		}
		// The image model to show as the icon
		var imageModel: Int = 0
		fun getImageModel(): RHMIModel? {
			return app.models[imageModel]
		}
		// The action when a user clicks the controller
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}
	}
	class ToolbarButton(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The text model to show as the popup text
		var tooltipModel: Int = 0
		fun getTooltipModel(): RHMIModel? {
			return app.models[tooltipModel]
		}
		// The image model to show as the icon
		var imageModel: Int = 0
		fun getImageModel(): RHMIModel? {
			return app.models[imageModel]
		}
		// The action when a user clicks the controller
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}
		// The action when the selection changes
		var selectAction: Int = 0
		fun getSelectAction(): RHMIAction? {
			return app.actions[selectAction]
		}
	}
	class Button(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The text model to show
		var model: Int = 0
		fun getModel(): RHMIModel? {
			return app.models[model]
		}
		// The text model to show as the popup text
		var tooltipModel: Int = 0
		fun getTooltipModel(): RHMIModel? {
			return app.models[tooltipModel]
		}
		// The action when a user clicks the controller
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}
		// The action when the selection changes
		var selectAction: Int = 0
		fun getSelectAction(): RHMIAction? {
			return app.actions[selectAction]
		}
	}
	class Checkbox(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The model showing the checked state
		var model: Int = 0
		fun getModel(): RHMIModel? {
			return app.models[model]
		}
		// The text model to show
		var textModel: Int = 0
		fun getTextModel(): RHMIModel? {
			return app.models[textModel]
		}
		// The action when a user clicks the controller
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}
	}
	class Gauge(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The text model to show
		var textModel: Int = 0
		fun getTextModel(): RHMIModel? {
			return app.models[textModel]
		}
		// The gauge model to show the progress
		var model: Int = 0
		fun getModel(): RHMIModel.RaGaugeModel? {
			return app.models[model]?.asRaGaugeModel()
		}
		// The action when a user clicks the controller
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}

		var changeAction: Int = 0
		fun getChangeAction(): RHMIAction? {
			return app.actions[changeAction]
		}
	}
	class Input(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		// The text model to show as the name
		var textModel: Int = 0
		fun getTextModel(): RHMIModel? {
			return app.models[textModel]
		}
		// The text model to show as the name
		var resultModel: Int = 0
		fun getResultModel(): RHMIModel? {
			return app.models[resultModel]
		}
		// The text model to show as the name
		var suggestModel: Int = 0
		fun getSuggestModel(): RHMIModel.RaListModel? {
			return app.models[suggestModel]?.asRaListModel()
		}
		// The action when a user clicks the controller to select a letter
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}

		var resultAction: Int = 0
		fun getResultAction(): RHMIAction? {
			return app.actions[resultAction]
		}
		// The action when a user clicks to select an item from the suggest list
		var suggestAction: Int = 0
		fun getSuggestAction(): RHMIAction? {
			return app.actions[suggestAction]
		}
	}

	class MockComponent(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		override fun asSeparator(): Separator? {
			return Separator(app, id)
		}
		override fun asImage(): Image? {
			return Image(app, id)
		}
		override fun asLabel(): Label? {
			return Label(app, id)
		}
		override fun asList(): List? {
			return List(app, id)
		}
		override fun asEntryButton(): EntryButton? {
			return EntryButton(app, id)
		}
		override fun asToolbarButton(): ToolbarButton? {
			return ToolbarButton(app, id)
		}
		override fun asButton(): Button? {
			return Button(app, id)
		}
		override fun asCheckbox(): Checkbox? {
			return Checkbox(app, id)
		}
		override fun asGauge(): Gauge? {
			return Gauge(app, id)
		}
		override fun asInput(): Input? {
			return Input(app, id)
		}
	}

	open fun asSeparator(): Separator? {
		return this as? Separator
	}
	open fun asImage(): Image? {
		return this as? Image
	}
	open fun asLabel(): Label? {
		return this as? Label
	}
	open fun asList(): List? {
		return this as? List
	}
	open fun asEntryButton(): EntryButton? {
		return this as? EntryButton
	}
	open fun asToolbarButton(): ToolbarButton? {
		return this as? ToolbarButton
	}
	open fun asButton(): Button? {
		return this as? Button
	}
	open fun asCheckbox(): Checkbox? {
		return this as? Checkbox
	}
	open fun asGauge(): Gauge? {
		return this as? Gauge
	}
	open fun asInput(): Input? {
		return this as? Input
	}
}