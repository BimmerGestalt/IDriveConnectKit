package me.hufman.idriveconnectionkit.rhmi

import de.bmw.idrive.BMWRemoting
import me.hufman.IDriveConnectionKit.XMLUtils
import org.w3c.dom.Node
import kotlin.reflect.KProperty

private class ModelAttributeAny(val default:Any) {
	operator fun getValue(thisRef: RHMIModel, property: KProperty<*>): Any {
		val resultStr = thisRef.attributes[property.name]
		return resultStr?.toInt() ?: default
	}
	operator fun setValue(thisRef: RHMIModel, property: KProperty<*>, value: Any) {
		thisRef.app.setModel(thisRef.id, value)
	}
}
private class ModelAttributeString(val default:String = "") {
	operator fun getValue(thisRef: RHMIModel, property: KProperty<*>): String {
		val resultStr = thisRef.attributes[property.name]
		return resultStr ?: default
	}
	operator fun setValue(thisRef: RHMIModel, property: KProperty<*>, value: String) {
		thisRef.app.setModel(thisRef.id, value)
	}
}
private class ModelAttributeBoolean(val default:Boolean = false) {
	operator fun getValue(thisRef: RHMIModel, property: KProperty<*>): Boolean {
		val resultStr = thisRef.attributes[property.name]
		return resultStr?.toBoolean() ?: default
	}
	operator fun setValue(thisRef: RHMIModel, property: KProperty<*>, value: Boolean) {
		thisRef.app.setModel(thisRef.id, value)
	}
}
private class ModelAttributeInt(val default:Int = 0) {
	operator fun getValue(thisRef: RHMIModel, property: KProperty<*>): Int {
		val resultStr = thisRef.attributes[property.name]
		return resultStr?.toInt() ?: default
	}
	operator fun setValue(thisRef: RHMIModel, property: KProperty<*>, value: Int) {
		thisRef.app.setModel(thisRef.id, value)
	}
}


abstract class RHMIModel private constructor(open val app: RHMIApplication, open val attributes: Map<String, String>) {
	val id by ModelAttributeInt(0)

	companion object {
		fun loadFromXML(app: RHMIApplication, node: Node): RHMIModel? {
			val attrs = XMLUtils.getAttributes(node)
			return when(node.nodeName) {
				"textIdModel" -> TextIdModel(app, attrs)
				"imageIdModel" -> ImageIdModel(app, attrs)
				"raBoolModel" -> RaBoolModel(app, attrs)
				"raDataModel" -> RaDataModel(app, attrs)
				"raGaugeModel" -> RaGaugeModel(app, attrs)
				"raIntModel" -> RaIntModel(app, attrs)
				"raListModel" -> RaListModel(app, attrs)
				else -> null
			}
		}
	}

	open class TextIdModel(override val app: RHMIApplication, override val attributes: Map<String, String>): RHMIModel(app, attributes) {
		val textId by ModelAttributeInt()
	}
	open class ImageIdModel(override val app: RHMIApplication, override val attributes: Map<String, String>): RHMIModel(app, attributes) {
		val imageId by ModelAttributeInt()
	}
	abstract class ValueModel(override val app: RHMIApplication, override val attributes: Map<String, String>): RHMIModel(app, attributes) {
		open val value by ModelAttributeAny("0")
	}
	class RaBoolModel(override val app: RHMIApplication, override val attributes: Map<String, String>): ValueModel(app, attributes) {
		override val value by ModelAttributeBoolean()
	}
	class RaDataModel(override val app: RHMIApplication, override val attributes: Map<String, String>): ValueModel(app, attributes) {
		val modelType by ModelAttributeString()
		override val value by ModelAttributeAny("")
	}
	open class RaIntModel(override val app: RHMIApplication, override val attributes: Map<String, String>): ValueModel(app, attributes) {
		override val value by ModelAttributeInt()
	}
	class RaGaugeModel(override val app: RHMIApplication, override val attributes: Map<String, String>): RaIntModel(app, attributes) {
		val modelType by ModelAttributeString()
		val min by ModelAttributeInt(0)
		val max by ModelAttributeInt(100)
		val increment by ModelAttributeInt(1)
	}

	class RaListModel(override val app: RHMIApplication, override val attributes: Map<String, String>): RHMIModel(app, attributes) {
		val modelType by ModelAttributeString()
		abstract class RHMIList(open val width: Int) {
			abstract operator fun get(index: Int): Array<Any>
			abstract var height: Int
			fun getWindow(startIndex: Int, numRows: Int): Array<Array<Any>> {
				var actualNumRows = numRows
				if (startIndex + numRows > height) {
					actualNumRows = height - startIndex
				}
				return Array(actualNumRows) { index -> this[startIndex + index]}
			}
		}
		class RHMIListConcrete(override var width: Int): RHMIList(width) {
			val realData = ArrayList<Array<Any>>()
			override fun get(index: Int): Array<Any> {
				return realData[index]
			}

			override var height: Int = 0
				get() = realData.size
			fun clear() {
				realData.clear()
			}
			fun addRow(row: Array<Any>) {
				realData.add(row)
			}
			operator fun set(index: Int, row: Array<Any>) {
				realData[index] = row
			}
		}
		fun setValue(data: RHMIList) {
			setValue(data, 0, data.height, data.height)
		}
		fun setValue(data: RHMIList, startIndex: Int, numRows: Int, totalRows: Int) {
			val table = BMWRemoting.RHMIDataTable(data.getWindow(startIndex, numRows), false, startIndex, numRows, totalRows, 0, data.width, data.width)
			app.setModel(this.id, table)
		}
	}
}