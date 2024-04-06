package io.bimmergestalt.idriveconnectkit.rhmi.deserialization

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIAction
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIApplication
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIComponent
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIEvent
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIModel
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIState
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttribute
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildElements
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildNamed
import org.w3c.dom.Document

fun RHMIApplication.loadFromXML(description: String) {
	this.loadFromXML(description.toByteArray())
}

fun RHMIApplication.loadFromXML(description: ByteArray) {
	this.loadFromXML(XMLUtils.loadXML(description))
}

fun RHMIApplication.loadFromXML(description: Document) {
	ignoreUpdates = true
	description.getChildNamed("pluginApps").getChildElements().forEach { pluginAppNode ->
		pluginAppNode.getChildNamed("models").getChildElements().forEach { modelNode ->
			val model = RHMIModel.loadFromXML(this, modelNode)
			if (model != null) {
				models[model.id] = model
				if (model is RHMIModel.FormatDataModel) {
					model.submodels.forEach { models[it.id] = it }
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