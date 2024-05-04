package io.bimmergestalt.idriveconnectkit.rhmi

import de.bmw.idrive.BMWRemoting

/**
 * These models send to the underlying RHMIApplication
 * and so are stored in jvmMain
 */
class RHMIModelLive {
	open class TextIdModel(val app: RHMIApplication, id: Int): RHMIModel.TextIdModel(id) {
		override var textId: Int
			get() = (app.getModel(id) as? BMWRemoting.RHMIResourceIdentifier)?.id ?: 0
			set(value) {
				val resource = BMWRemoting.RHMIResourceIdentifier(BMWRemoting.RHMIResourceType.TEXTID, value)
				app.setModel(id, resource)
			}
	}
	open class ImageIdModel(val app: RHMIApplication, id: Int): RHMIModel.ImageIdModel(id) {
		override var imageId: Int
			get() = (app.getModel(id) as? BMWRemoting.RHMIResourceIdentifier)?.id ?: 0
			set(value) {
				val resource = BMWRemoting.RHMIResourceIdentifier(BMWRemoting.RHMIResourceType.IMAGEID, value)
				app.setModel(id, resource)
			}
	}

	open class RaBoolModel(val app: RHMIApplication, id: Int): RHMIModel.RaBoolModel(id) {
		override var value: Boolean
			get() = app.getModel(id) as? Boolean ?: false
			set(value) = app.setModel(id, value)
	}
	open class RaDataModel(val app: RHMIApplication, id: Int): RHMIModel.RaDataModel(id) {
		override var value: String
			get() = app.getModel(id) as? String ?: ""
			set(value) = app.setModel(id, value)
	}
	open class RaIntModel(val app: RHMIApplication, id: Int): RHMIModel.RaIntModel(id) {
		override var value: Int
			get() = app.getModel(id) as? Int ?: 0
			set(value) = app.setModel(id, value)
	}
	open class RaGaugeModel(val app: RHMIApplication, id: Int): RHMIModel.RaGaugeModel(id) {
		override var value: Int
			get() = app.getModel(id) as? Int ?: 0
			set(value) = app.setModel(id, value)

	}

	open class FormatDataModel(app: RHMIApplication, id: Int, submodels: List<RHMIModel>): RHMIModel.FormatDataModel(id, submodels) {
	}

	class RaImageModel(val app: RHMIApplication, id: Int): RHMIModel.RaImageModel(id) {
		override var value: ByteArray?
			get() = (app.getModel(id) as? BMWRemoting.RHMIResourceData)?.data
			set(value) {
				if (value != null) {
					val data = BMWRemoting.RHMIResourceData(BMWRemoting.RHMIResourceType.IMAGEDATA, value)
					app.setModel(this.id, data)
				}
			}
	}

	class RaListModel(val app: RHMIApplication, id: Int): RHMIModel.RaListModel(id) {
		override var value: RHMIList?
			get() = _getValue()
			set(value) {
				if (value != null) {
					setValue(value, value.startIndex, value.height, value.endIndex)
				}
			}

		fun setValue(data: RHMIList, startIndex: Int, numRows: Int, totalRows: Int) {
			val table = BMWRemoting.RHMIDataTable(data.getWindow(startIndex, numRows), false, startIndex, numRows, totalRows, 0, data.width, data.width)
			app.setModel(this.id, table)
		}
		private fun _getValue(): RHMIList? {
			return (app.getModel(id) as? BMWRemoting.RHMIDataTable)?.let {
				RHMIListConcrete(it.numColumns, startIndex = it.fromRow, endIndex = it.totalRows).apply {
					addAll(it.data.toList())
				}
			}
		}
	}
}