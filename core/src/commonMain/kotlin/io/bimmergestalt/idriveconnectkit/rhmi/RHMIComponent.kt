package io.bimmergestalt.idriveconnectkit.rhmi

import io.bimmergestalt.idriveconnectkit.Utils.etchAsInt
import io.bimmergestalt.idriveconnectkit.withRealDefault


abstract class RHMIComponent protected constructor(open val app: RHMIApplication, open val id: Int) {

	val properties: MutableMap<Int, RHMIProperty> = HashMap<Int, RHMIProperty>().withRealDefault { propertyId ->
		// look up from the app storage if the property wasn't loaded from xml
		RHMIProperty.AppProperty(app, id, propertyId)
	}

	companion object { }

	fun setProperty(property: RHMIProperty.PropertyId, value: Any) {
		this.setProperty(property.id, value)
	}
	fun setProperty(propertyId: Int, value: Any) {
		app.setProperty(id, propertyId, value)
		// allow component.property[id] to read from the app's setProperty
		properties[propertyId] = RHMIProperty.AppProperty(app, id, propertyId, value)
	}
	fun setVisible(visible: Boolean) {
		this.setProperty(RHMIProperty.PropertyId.VISIBLE, visible)
	}
	fun setSelectable(selectable: Boolean) {
		this.setProperty(RHMIProperty.PropertyId.SELECTABLE, selectable)
	}
	fun setEnabled(enabled: Boolean) {
		this.setProperty(RHMIProperty.PropertyId.ENABLED, enabled)
	}

	// any custom event listeners that the client provides
	var eventCallback: EventCallback? = null
	var requestDataCallback: RequestDataCallback? = null
	var focusCallback: FocusCallback? = null
	var visibleCallback: VisibleCallback? = null

	// dispatch an event
	fun onHmiEvent(eventId: Int?, args: Map<*, *>?) {
		if (eventCallback != null) {
			eventCallback?.onHmiEvent(eventId, args)
		} else {
			if (eventId == 1 && focusCallback != null) {
				focusCallback?.onFocus(args?.get(4.toByte()) as? Boolean ?: false)
			}
			if (eventId == 2 && requestDataCallback != null) {
				requestDataCallback?.onRequestData(
						etchAsInt(args?.get(5.toByte()), 0),
						etchAsInt(args?.get(6.toByte()), 20)
				)
			}
			if (eventId == 11 && visibleCallback != null) {
				visibleCallback?.onVisible(args?.get(23.toByte()) as? Boolean ?: false)
			}
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
		// What category to put the entrybutton
		var applicationType = "OnlineServices"
		// How to sort the button
		var applicationWeight = 500
	}
	class InstrumentCluster(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		var textModel: Int = 0
		fun getTextModel(): RHMIModel? {
			return app.models[textModel]
		}

		var additionalTextModel: Int = 0
		fun getAdditionalTextModel(): RHMIModel? {
			return app.models[additionalTextModel]
		}

		var useCaseModel: Int = 0
		fun getUseCaseModel(): RHMIModel? {
			return app.models[useCaseModel]
		}

		var detailsModel: Int = 0
		fun getDetailsModel(): RHMIModel? {
			return app.models[detailsModel]
		}

		var playlistModel: Int = 0
		fun getPlaylistModel(): RHMIModel? {
			return app.models[playlistModel]
		}

		var iSpeechSupport: Int = 0
		fun getISpeechSupport(): RHMIModel? {
			return app.models[iSpeechSupport]
		}

		var iSpeechText: Int = 0
		fun getISpeechText(): RHMIModel? {
			return app.models[iSpeechText]
		}

		var skipForward: Int = 0
		fun getSkipForward(): RHMIModel? {
			return app.models[skipForward]
		}

		var skipBackward: Int = 0
		fun getSkipBackward(): RHMIModel? {
			return app.models[skipBackward]
		}

		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}

		var setTrackAction: Int = 0
		fun getSetTrackAction(): RHMIAction? {
			return app.actions[setTrackAction]
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
		// Some buttons can have an icon, usually an ImageIdModel
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
	class CalendarDay(override val app: RHMIApplication, override val id: Int): RHMIComponent(app, id) {
		var action: Int = 0
		fun getAction(): RHMIAction? {
			return app.actions[action]
		}
		var dateModel: Int = 0
		fun getDateModel(): RHMIModel? {
			return app.models[dateModel]
		}
		var appointmentListModel: Int = 0
		fun getAppointmentListModel(): RHMIModel? {
			return app.models[appointmentListModel]
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
	open fun asInstrumentCluster(): InstrumentCluster? {
		return this as? InstrumentCluster
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
	open fun asCalendarDay(): CalendarDay? {
		return this as? CalendarDay
	}
}