package io.bimmergestalt.idriveconnectkit.rhmi.mocking

import io.bimmergestalt.idriveconnectkit.rhmi.RHMIComponent

class MockComponentMap(val app: RHMIApplicationMock) : HashMap<Int, RHMIComponent>() {
	override fun get(key:Int):RHMIComponent {
		return super.get(key) ?: RHMIComponentMock(app, key)
	}

	inline fun <reified R: RHMIComponent> computeIfWrongType(id: Int, compute: (Int) -> R): R {
		val existing = app.components[id]
		return if (existing is R) { existing }
		else {
			compute(id).also {
				app.components[id] = it
			}
		}
	}
}

class RHMIComponentMock(override val app: RHMIApplicationMock, override val id: Int): RHMIComponent(app, id) {
	override fun asSeparator(): Separator {
		return app.components.computeIfWrongType(id) {
			Separator(app, id)
		}
	}
	override fun asImage(): Image {
		return app.components.computeIfWrongType(id) {
			Image(app, id)
		}
	}
	override fun asLabel(): Label {
		return app.components.computeIfWrongType(id) {
			Label(app, id)
		}
	}
	override fun asList(): List {
		return app.components.computeIfWrongType(id) {
			List(app, id)
		}
	}
	override fun asEntryButton(): EntryButton {
		return app.components.computeIfWrongType(id) {
			EntryButton(app, id)
		}
	}
	override fun asInstrumentCluster(): InstrumentCluster {
		return app.components.computeIfWrongType(id) {
			InstrumentCluster(app, id)
		}
	}
	override fun asToolbarButton(): ToolbarButton {
		return app.components.computeIfWrongType(id) {
			ToolbarButton(app, id)
		}
	}
	override fun asButton(): Button {
		return app.components.computeIfWrongType(id) {
			Button(app, id)
		}
	}
	override fun asCheckbox(): Checkbox {
		return app.components.computeIfWrongType(id) {
			Checkbox(app, id)
		}
	}
	override fun asGauge(): Gauge {
		return app.components.computeIfWrongType(id) {
			Gauge(app, id)
		}
	}
	override fun asInput(): Input {
		return app.components.computeIfWrongType(id) {
			Input(app, id)
		}
	}
	override fun asCalendarDay(): CalendarDay {
		return app.components.computeIfWrongType(id) {
			CalendarDay(app, id)
		}
	}
}
