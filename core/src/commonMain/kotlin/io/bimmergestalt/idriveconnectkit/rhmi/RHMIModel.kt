package io.bimmergestalt.idriveconnectkit.rhmi

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BMWRemoting.RHMIDataTable
import de.bmw.idrive.BMWRemoting.RHMIResourceData
import io.bimmergestalt.idriveconnectkit.rhmi.mocking.RHMIApplicationMock
import io.bimmergestalt.idriveconnectkit.xmlutils.XMLUtils
import io.bimmergestalt.idriveconnectkit.xmlutils.getAttributesMap
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildElements
import io.bimmergestalt.idriveconnectkit.xmlutils.getChildNamed
import org.w3c.dom.Node
import kotlin.math.max
import kotlin.math.min


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
		var textId: Int
			get() = (app.getModel(id) as? BMWRemoting.RHMIResourceIdentifier)?.id ?: 0
			set(value) {
				val resource = BMWRemoting.RHMIResourceIdentifier(BMWRemoting.RHMIResourceType.TEXTID, value)
				app.setModel(id, resource)
			}
	}
	open class ImageIdModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var imageId: Int
			get() = (app.getModel(id) as? BMWRemoting.RHMIResourceIdentifier)?.id ?: 0
			set(value) {
				val resource = BMWRemoting.RHMIResourceIdentifier(BMWRemoting.RHMIResourceType.IMAGEID, value)
				app.setModel(id, resource)
			}
	}
	class RaBoolModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var value: Boolean
			get() = app.getModel(id) as? Boolean ?: false
			set(value) = app.setModel(id, value)
	}
	class RaDataModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var modelType: String = ""
		var value: String
			get() = app.getModel(id) as? String ?: ""
			set(value) = app.setModel(id, value)
	}
	open class RaIntModel(override val app: RHMIApplication, override val id: Int): RHMIModel(app, id) {
		var value: Int
			get() = app.getModel(id) as? Int ?: 0
			set(value) = app.setModel(id, value)
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
			get() = (app.getModel(id) as? RHMIResourceData)?.data
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
			/**
			 * Return the data at this row, or null if it doesn't exist
			 */
			abstract operator fun get(index: Int): Array<Any>?

			/**
			 * Return the table index of the first row of this RHMIList
			 */
			abstract val startIndex: Int

			/**
			 * Return the table index after the last row of this RHMIList
			 */
			abstract val endIndex: Int

			/**
			 * Return the number of actual rows in this RHMList
			 */
			abstract val height: Int

			fun getWindow(startIndex: Int, numRows: Int): Array<Array<Any>> {
				val lastIndex = min(startIndex + numRows, endIndex)
				val actualNumRows = max(0, lastIndex - startIndex)
				return Array(actualNumRows) { index -> this[startIndex + index] ?: emptyArray() }
			}
		}
		class RHMIListConcrete(override var width: Int, override var startIndex: Int = 0, endIndex: Int = 0): RHMIList(width) {
			private var realData = ArrayList<Array<Any>>()
			override fun get(index: Int): Array<Any> {
				return realData.getOrNull(index - startIndex) ?: emptyArray()
			}

			private val forcedEndIndex = endIndex
			override val endIndex: Int
				get() = max(forcedEndIndex, startIndex + realData.size)
			override val height: Int
				get() = realData.size

			fun clear() {
				realData.clear()
			}
			private fun resize(index: Int) {
				if (index < startIndex) {
					val newData = ArrayList<Array<Any>>()
					newData.addAll((index until startIndex).map { emptyArray() })
					newData.addAll(realData)
					realData = newData
					startIndex = index
				}
				if (index >= endIndex) {
					realData.addAll((endIndex until index).map { emptyArray() })
				}
			}
			fun addRow(row: Array<Any>) {
				realData.add(row)
			}
			fun addAll(data: Iterable<Array<Any>>) {
				realData.addAll(data)
			}
			operator fun set(index: Int, row: Array<Any>) {
				resize(index)
				realData[index - startIndex] = row
			}
		}
		open class RHMIListAdapter<T>(width: Int, val realData: List<T>) : RHMIModel.RaListModel.RHMIList(width) {
			override val startIndex: Int = 0
			override val endIndex: Int
				get() = realData.size
			override val height: Int
				get() = realData.size

			override fun get(index: Int): Array<Any> {
				return convertRow(index, realData[index])
			}

			open fun convertRow(index: Int, item: T): Array<Any> {
				return Array(width) { i ->
					if (i == width - 1) {
						item.toString()
					} else {
						""
					}
				}
			}
		}
		var value: RHMIList?
			get() = _getValue()
			set(value) {
				if (value != null) {
					setValue(value, value.startIndex, value.height, value.endIndex)
				}
			}

		fun setValue(data: RaListModel.RHMIList, startIndex: Int, numRows: Int, totalRows: Int) {
			val table = BMWRemoting.RHMIDataTable(data.getWindow(startIndex, numRows), false, startIndex, numRows, totalRows, 0, data.width, data.width)
			app.setModel(this.id, table)
		}
		private fun _getValue(): RHMIList? {
			return (app.getModel(id) as? RHMIDataTable)?.let {
				RHMIListConcrete(it.numColumns, startIndex = it.fromRow, endIndex = it.totalRows).apply {
					addAll(it.data.toList())
				}
			}
		}
	}

	open class MockModel(override val app: RHMIApplicationMock, override val id: Int): RHMIModel(app, id) {
		override fun asFormatDataModel(): FormatDataModel {
			return app.models.computeIfWrongType(id) {
				FormatDataModel(app, id, ArrayList())
			}
		}

		override fun asImageIdModel(): ImageIdModel {
			return app.models.computeIfWrongType(id) {
				ImageIdModel(app, id)
			}
		}

		override fun asRaBoolModel(): RaBoolModel {
			return app.models.computeIfWrongType(id) {
				RaBoolModel(app, id)
			}
		}

		override fun asRaDataModel(): RaDataModel {
			return app.models.computeIfWrongType(id) {
				RaDataModel(app, id)
			}
		}

		override fun asRaGaugeModel(): RaGaugeModel {
			return app.models.computeIfWrongType(id) {
				RaGaugeModel(app, id)
			}
		}

		override fun asRaImageModel(): RaImageModel {
			return app.models.computeIfWrongType(id) {
				RaImageModel(app, id)
			}
		}

		override fun asRaIntModel(): RaIntModel {
			return app.models.computeIfWrongType(id) {
				RaIntModel(app, id)
			}
		}

		override fun asRaListModel(): RaListModel {
			return app.models.computeIfWrongType(id) {
				RaListModel(app, id)
			}
		}

		override fun asTextIdModel(): TextIdModel {
			return app.models.computeIfWrongType(id) {
				TextIdModel(app, id)
			}
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