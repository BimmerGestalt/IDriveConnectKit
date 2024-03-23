package io.bimmergestalt.idriveconnectkit

object Utils {
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
}

fun <K, V> MutableMap<K, V>.withRealDefault(defaultValue: (key: K) -> V): MutableMap<K, V> = MutableMapWithRealDefault(this, defaultValue)

class MutableMapWithRealDefault<K, V>(public val map: MutableMap<K, V>, private val default: (key: K) -> V) : MutableMap<K, V> {
	override fun equals(other: Any?): Boolean = map.equals(other)
	override fun hashCode(): Int = map.hashCode()
	override fun toString(): String = map.toString()
	override val size: Int get() = map.size
	override fun isEmpty(): Boolean = map.isEmpty()
	override fun containsKey(key: K): Boolean = map.containsKey(key)
	override fun containsValue(value: @UnsafeVariance V): Boolean = map.containsValue(value)
	override fun get(key: K): V? = map[key] ?: default(key)
	override val keys: MutableSet<K> get() = map.keys
	override val values: MutableCollection<V> get() = map.values
	override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() = map.entries

	override fun put(key: K, value: V): V? = map.put(key, value)
	override fun remove(key: K): V? = map.remove(key)
	override fun putAll(from: Map<out K, V>) = map.putAll(from)
	override fun clear() = map.clear()
}