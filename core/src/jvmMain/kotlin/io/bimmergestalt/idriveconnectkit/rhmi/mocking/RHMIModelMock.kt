package io.bimmergestalt.idriveconnectkit.rhmi.mocking

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIModel
import io.bimmergestalt.idriveconnectkit.rhmi.RHMIModelLive

class MockModelMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIModel>() {
	override fun get(key:Int):RHMIModel {
		return super.get(key) ?: RHMIModelMock(app, key)
	}

	inline fun <reified R: RHMIModel> computeIfWrongType(id: Int, compute: (Int) -> R): R {
		val existing = app.models[id]
		return if (existing is R) { existing }
		else {
			compute(id).also {
				app.models[id] = it
			}
		}
	}
}

open class RHMIModelMock(val app: RHMIApplicationMock, id: Int): RHMIModel(id) {
	override fun asFormatDataModel(): FormatDataModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.FormatDataModel(app, id, ArrayList())
		}
	}

	override fun asImageIdModel(): ImageIdModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.ImageIdModel(app, id)
		}
	}

	override fun asRaBoolModel(): RaBoolModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.RaBoolModel(app, id)
		}
	}

	override fun asRaDataModel(): RaDataModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.RaDataModel(app, id)
		}
	}

	override fun asRaGaugeModel(): RaGaugeModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.RaGaugeModel(app, id)
		}
	}

	override fun asRaImageModel(): RaImageModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.RaImageModel(app, id)
		}
	}

	override fun asRaIntModel(): RaIntModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.RaIntModel(app, id)
		}
	}

	override fun asRaListModel(): RaListModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.RaListModel(app, id)
		}
	}

	override fun asTextIdModel(): TextIdModel {
		return app.models.computeIfWrongType(id) {
			RHMIModelLive.TextIdModel(app, id)
		}
	}
}