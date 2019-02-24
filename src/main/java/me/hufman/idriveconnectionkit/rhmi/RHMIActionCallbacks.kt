package me.hufman.idriveconnectionkit.rhmi

import me.hufman.idriveconnectionkit.etchAsInt

// callback interfaces
interface RHMIActionCallback {
	fun onActionEvent(args: Map<*, *>?)
}

interface RHMIActionButtonCallback: RHMIActionCallback {
	override fun onActionEvent(args: Map<*, *>?) {
		onAction()
	}

	fun onAction()
}

interface RHMIActionListCallback: RHMIActionCallback {
	override fun onActionEvent(args: Map<*, *>?) {
		val index = etchAsInt(args?.get(1.toByte()))
		onAction(index)
	}

	fun onAction(index: Int)
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
	override fun onAction() {
		f()
	}
}

fun RHMIActionListCallback(f: (Int) -> Unit): RHMIActionListCallback = object: RHMIActionListCallback {
	override fun onAction(index: Int) {
		f(index)
	}
}

fun RHMIActionSpellerCallback(f: (String) -> Unit): RHMIActionSpellerCallback = object: RHMIActionSpellerCallback {
	override fun onInput(input: String) {
		f(input)
	}
}