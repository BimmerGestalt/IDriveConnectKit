package io.bimmergestalt.idriveconnectkit.rhmi

import io.bimmergestalt.idriveconnectkit.Utils.etchAsInt

// callback interfaces
interface RHMIActionCallback {
	fun onActionEvent(args: Map<*, *>?)
}

interface RHMIActionButtonCallback: RHMIActionCallback {
	override fun onActionEvent(args: Map<*, *>?) {
		val invokedBy = etchAsInt(args?.get(43.toByte()))
		onAction(invokedBy)
	}

	fun onAction(invokedBy: Int? = null)
}

interface RHMIActionListCallback: RHMIActionCallback {
	override fun onActionEvent(args: Map<*, *>?) {
		val index = etchAsInt(args?.get(1.toByte()))
		val invokedBy = etchAsInt(args?.get(43.toByte()))
		onAction(index, invokedBy)
	}

	fun onAction(index: Int, invokedBy: Int? = null)
}

interface RHMIActionSpellerCallback: RHMIActionCallback {
	override fun onActionEvent(args: Map<*, *>?) {
		val input = args?.get(8.toByte()) as? String ?: return
		onInput(input)
	}

	fun onInput(input: String)
}

// helpers to create listeners
fun RHMIActionCallback(f: (args: Map<*, *>?) -> Unit): RHMIActionCallback = object : RHMIActionCallback {
	override fun onActionEvent(args: Map<*, *>?) {
		f(args)
	}
}

fun RHMIActionButtonCallback(f: () -> Unit): RHMIActionButtonCallback = object: RHMIActionButtonCallback {
	override fun onAction(invokedBy: Int?) {
		f()
	}
}

fun RHMIActionListCallback(f: (Int) -> Unit): RHMIActionListCallback = object: RHMIActionListCallback {
	override fun onAction(index: Int, invokedBy: Int?) {
		f(index)
	}
}

fun RHMIActionSpellerCallback(f: (String) -> Unit): RHMIActionSpellerCallback = object: RHMIActionSpellerCallback {
	override fun onInput(input: String) {
		f(input)
	}
}