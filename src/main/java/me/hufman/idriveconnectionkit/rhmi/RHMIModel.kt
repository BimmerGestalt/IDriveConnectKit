package me.hufman.idriveconnectionkit.rhmi

import de.bmw.idrive.BMWRemoting
import me.hufman.idriveconnectionkit.xmlutils.XMLUtils
import me.hufman.idriveconnectionkit.xmlutils.getAttributesMap
import me.hufman.idriveconnectionkit.xmlutils.getChildElements
import me.hufman.idriveconnectionkit.xmlutils.getChildNamed
import org.w3c.dom.Node


abstract class RHMIModel private constructor(open val app: RHMIApplication, open val id: Int) {

	companion object {
		fun loadFromXML(app: RHMIApplication, node: Node): RHMIModel? {
			val attrs = node.getAttributesMap()

			val id = attrs["id"]?.toInt() ?: return null

			if (node.nodeName == "formatDataModel") {
				val submodels = node.getChildNamed("models").getChildElements().mapNotNull { submodelNode ->
					loadFromXML(app, submodelNode)
				}
				val model = FormatDataModel(app, id, submodels)
				XMLUtils.unmarshalAttributes(model, attrs)
				return model
			}

			val model = when(node.nodeName) {
				"textIdModel" -> TextIdModel(app, id)
				"imageIdModel" -> ImageIdModel(app, id)
				"raBoolModel" -> RaBoolModel(app, id)
				"raDataModel" -> RaDataModel(app, id)
				"raGaugeModel" -> RaGaugeModel(app, id)
				"raImageModel" -> RaImageModel(app, id)
				"raIntModel" -> RaIntModel(app, id)
				"raListModel" -> RaListModel(app, id)
				else -> null
			}

			if (model != null) {
				XMLUtils.unmarshalAttributes(model, attrs)
			}
			return model
		}
	}

	open class TextIdModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var textId: Int = 0
			set(value) {
				val resource = BMWRemoting.RHMIResourceIdentifier(BMWRemoting.RHMIResourceType.TEXTID, value)
				app.setModel(id, resource)
				field = value
			}
	}
	open class ImageIdModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var imageId: Int = 0
			set(value) {
				val resource = BMWRemoting.RHMIResourceIdentifier(BMWRemoting.RHMIResourceType.IMAGEID, value)
				app.setModel(id, resource)
				field = value
			}
	}
	class RaBoolModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var value: Boolean = false
			set(value) { app.setModel(id, value); field = value }
	}
	class RaDataModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var modelType: String = ""
		var value: String = ""
			set(value) { app.setModel(id, value); field = value }
	}
	open class RaIntModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var value: Int = 0
			set(value) { app.setModel(id, value); field = value }
	}
	class RaGaugeModel(override val app: RHMIApplication, override val id: Int): RaIntModel(app, id) {
		var modelType: String = ""
		var min: Int = 0
		var max: Int = 100
		var increment: Int = 1
		// has a value from RaIntModel
	}

	class FormatDataModel(override val app: RHMIApplication, override val id: Int, val submodels: List<RHMIModel>): RHMIModel(app, id) {
		var formatString: String = ""
	}

	class RaImageModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var value: ByteArray?
			get() = null
			set(value) {
				if (value != null) {
					val data = BMWRemoting.RHMIResourceData(BMWRemoting.RHMIResourceType.IMAGEDATA, value)
					app.setModel(this.id, data)
				}
			}

	}

	class RaListModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var modelType: String = ""
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

		var value: RHMIList?
			get() = null
			set(value) {
				if (value != null) {
					setValue(value, 0, value.height, value.height)
				}
			}

		fun setValue(data: RaListModel.RHMIList, startIndex: Int, numRows: Int, totalRows: Int) {
			val table = BMWRemoting.RHMIDataTable(data.getWindow(startIndex, numRows), false, startIndex, numRows, totalRows, 0, data.width, data.width)
			app.setModel(this.id, table)
		}
	}

	open class MockModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		override fun asFormatDataModel(): FormatDataModel {
			return FormatDataModel(app, id, ArrayList())
		}

		override fun asImageIdModel(): ImageIdModel {
			return ImageIdModel(app, id)
		}

		override fun asRaBoolModel(): RaBoolModel {
			return RaBoolModel(app, id)
		}

		override fun asRaDataModel(): RaDataModel {
			return RaDataModel(app, id)
		}

		override fun asRaGaugeModel(): RaGaugeModel {
			return RaGaugeModel(app, id)
		}

		override fun asRaImageModel(): RaImageModel {
			return RaImageModel(app, id)
		}

		override fun asRaIntModel(): RaIntModel {
			return RaIntModel(app, id)
		}

		override fun asRaListModel(): RaListModel {
			return RaListModel(app, id)
		}

		override fun asTextIdModel(): TextIdModel {
			return TextIdModel(app, id)
		}
	}

	open fun asFormatDataModel(): FormatDataModel? {
		return this as? FormatDataModel
	}

	open fun asImageIdModel(): ImageIdModel? {
		return this as? ImageIdModel
	}

	open fun asRaBoolModel(): RaBoolModel? {
		return this as? RaBoolModel
	}

	open fun asRaDataModel(): RaDataModel? {
		return this as? RaDataModel
	}

	open fun asRaGaugeModel(): RaGaugeModel? {
		return this as? RaGaugeModel
	}

	open fun asRaImageModel(): RaImageModel? {
		return this as? RaImageModel
	}

	open fun asRaIntModel(): RaIntModel? {
		return this as? RaIntModel
	}

	open fun asRaListModel(): RaListModel? {
		return this as? RaListModel
	}

	open fun asTextIdModel(): TextIdModel? {
		return this as? TextIdModel
	}
}