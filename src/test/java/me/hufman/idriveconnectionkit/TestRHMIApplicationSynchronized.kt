package me.hufman.idriveconnectionkit

import me.hufman.idriveconnectionkit.rhmi.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Test that the RHMIApplicationSynchronized wrapper successfully blocks
 * simultaneous threads
 *
 * The Thread runs first, sleeps a bit, and then sets its data
 * The main thread is bocked, and then sets its data afterwards
 */
class TestRHMIApplicationSynchronized {

	val backing = RHMIApplicationConcrete()
	val subject = RHMIApplicationSynchronized(backing)

	@Before
	fun setUp() {
		backing.models[0] = RHMIModel.RaDataModel(backing, 0)
		subject.models[1] = RHMIModel.RaDataModel(subject, 1)
		subject.components[10] = RHMIComponent.Label(subject, 10)
	}

	@Test
	fun unwrap() {
		assertEquals(backing, subject.unwrap())
	}

	@Test
	fun nullTest() {
		val model = backing.models[0] as RHMIModel.RaDataModel
		val thread = Thread {
			Thread.sleep(1000)
			model.value = "thread"
		}
		thread.start()
		Thread.sleep(100)
		model.value = "main"    // won't block

		assertEquals("main", model.value)

		thread.join(5000)
		assertEquals("thread", model.value)
	}

	@Test
	fun setModel() {
		val model = subject.models[1] as RHMIModel.RaDataModel
		val thread = Thread {
			subject.runSynchronized {
				Thread.sleep(1000)
				model.value = "thread"
			}
		}
		thread.start()
		Thread.sleep(100)
		model.value = "main"    // should block

		assertEquals("main", model.value)
		thread.join(5000)
		assertEquals("main", model.value)
	}

	@Test
	fun setProperty() {
		val component = subject.components[10] as RHMIComponent.Label
		val thread = Thread {
			subject.runSynchronized {
				Thread.sleep(1000)
				component.setVisible(true)
			}
		}
		thread.start()
		Thread.sleep(100)
		component.setVisible(false)    // should block

		assertEquals(false, component.properties[RHMIProperty.PropertyId.VISIBLE.id]?.value)
		thread.join(5000)
		assertEquals(false, component.properties[RHMIProperty.PropertyId.VISIBLE.id]?.value)
	}

	@Test
	fun triggerEvent() {
		val thread = Thread {
			subject.runSynchronized {
				Thread.sleep(1000)
				subject.triggerHMIEvent(1, mapOf(0 to "thread"))
			}
		}
		thread.start()
		Thread.sleep(100)
		subject.triggerHMIEvent(1, mapOf(0 to "main"))

		assertEquals("main", backing.triggeredEvents[1]!![0])
		thread.join(5000)
		assertEquals("main", backing.triggeredEvents[1]!![0])
	}
}