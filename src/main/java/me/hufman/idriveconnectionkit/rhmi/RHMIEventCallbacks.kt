package me.hufman.idriveconnectionkit.rhmi


// callback definitions
interface EventCallback {
	fun onHmiEvent(eventId: Int?, args: Map<*, *>?)
}
interface RequestDataCallback {
	fun onRequestData(startIndex: Int, numRows: Int)
}
interface FocusCallback {
	fun onFocus(focused: Boolean)
}
interface VisibleCallback {
	fun onVisible(visible: Boolean)
}

// helpers to create listeners
fun EventCallback(f: (eventId: Int?, args: Map<*, *>?) -> Unit): EventCallback = object : EventCallback {
	override fun onHmiEvent(eventId: Int?, args: Map<*, *>?) {
		f(eventId, args)
	}
}
fun RequestDataCallback(f: (startIndex: Int, numRows: Int) -> Unit): RequestDataCallback = object : RequestDataCallback {
	override fun onRequestData(startIndex: Int, numRows: Int) {
		f(startIndex, numRows)
	}
}
fun FocusCallback(f: (focused: Boolean) -> Unit): FocusCallback = object: FocusCallback {
	override fun onFocus(focused: Boolean) {
		f(focused)
	}
}
fun VisibleCallback(f: (visible: Boolean) -> Unit): VisibleCallback = object: VisibleCallback {
	override fun onVisible(visible: Boolean) {
		f(visible)
	}
}