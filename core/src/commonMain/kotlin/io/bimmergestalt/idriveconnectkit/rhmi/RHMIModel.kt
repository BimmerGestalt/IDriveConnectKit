package io.bimmergestalt.idriveconnectkit.rhmi

import kotlin.math.max
import kotlin.math.min


/**
 * These base classes just store their data locally
 */
abstract class RHMIModel protected constructor(val id: Int) {

	companion object {}

	open class TextIdModel(id: Int): RHMIModel(id) {
		open var textId: Int = 0
	}
	open class ImageIdModel(id: Int): RHMIModel(id) {
		open var imageId: Int = 0
	}
	open class RaBoolModel(id: Int): RHMIModel(id) {
		open var value: Boolean = false
	}
	open class RaDataModel(id: Int): RHMIModel(id) {
		var modelType: String = ""
		open var value: String = ""
	}
	open class RaIntModel(id: Int): RHMIModel(id) {
		open var value: Int = 0
	}

	open class RaGaugeModel(id: Int): RaIntModel(id) {
		var modelType: String = ""
		var min: Int = 0
		var max: Int = 100
		var increment: Int = 1

		// inherits value from RaIntModel
	}

	open class FormatDataModel(id: Int, val submodels: List<RHMIModel>): RHMIModel(id) {
		var formatString: String = ""
	}

	open class RaImageModel(id: Int): RHMIModel(id) {
		open var value: ByteArray? = null
	}

	open class RaListModel(id: Int): RHMIModel(id) {
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
			 * Return the number of actual rows in this RHMIList
			 */
			abstract val height: Int

			fun getWindowList(startIndex: Int, numRows: Int): RHMIList {
				return RHMIListPartial(this, startIndex, startIndex + numRows)
			}

			fun getWindow(startIndex: Int, numRows: Int): Array<Array<Any>> {
				val lastIndex = min(startIndex + numRows, endIndex)
				val actualNumRows = max(0, lastIndex - startIndex)
				return Array(actualNumRows) { index -> this[startIndex + index] ?: emptyArray() }
			}
		}

		/**
		 * An RHMIList that provides a smaller view into another RHMIList
		 * to create partial updates
		 */
		class RHMIListPartial(val base: RHMIList, override var startIndex: Int = 0, endIndex: Int = 0): RHMIList(base.width) {
			override val width: Int = base.width

			private val desiredEndIndex = endIndex
			override val endIndex: Int
				get() = min(desiredEndIndex, base.endIndex)
			override val height: Int
				get() = base.height

			override fun get(index: Int): Array<Any>? = base[index]
		}

		/**
		 * An RHMIList that has a concrete storage, which can be added and set
		 * A starting index can optionally be set, to create partial updates without materializing the entire list
		 */
		class RHMIListConcrete(override var width: Int, override var startIndex: Int = 0, endIndex: Int = 0): RHMIList(width) {
			private var realData = ArrayList<Array<Any>>()
			override fun get(index: Int): Array<Any> {
				return realData.getOrNull(index - startIndex) ?: emptyArray()
			}

			private val forcedEndIndex = endIndex
			override val endIndex: Int
				get() = max(forcedEndIndex, startIndex + realData.size)
			override val height: Int
				get() = endIndex

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

		/**
		 * An RHMIList that wraps an existing List object, by default with toString() for each item
		 */
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
		open var value: RHMIList? = null
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