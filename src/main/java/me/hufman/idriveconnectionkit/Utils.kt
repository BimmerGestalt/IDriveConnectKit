package me.hufman.idriveconnectionkit

fun etchAsInt(obj: Any?, default: Int = 0): Int {
	/** Etch likes to shrink numbers to the smallest type that will fit
	 * But JVM wants the number types to match in various places
	 */
	return when (obj) {
		is Byte -> obj.toInt()
		is Short -> obj.toInt()
		is Int -> obj
		else -> default
	}
}