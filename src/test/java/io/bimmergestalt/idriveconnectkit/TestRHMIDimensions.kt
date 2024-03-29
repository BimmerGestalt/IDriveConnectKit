package io.bimmergestalt.idriveconnectkit

import org.junit.Assert
import org.junit.Test

class TestRHMIDimensions {

	@Test
	fun testDimensionsFactory() {
		assert(RHMIDimensions.create(mapOf("hmi.display-width" to "1280", "hmi.display-height" to "480"))
				is GenericRHMIDimensions)
		assert(RHMIDimensions.create(mapOf("hmi.display-width" to "1280", "hmi.display-height" to "480",
				"hmi.type" to "BMW ID4"))
				is BMW4RHMIDimensions)
		assert(RHMIDimensions.create(mapOf("hmi.display-width" to "1280", "hmi.display-height" to "480",
			"hmi.type" to "BMW ID5"))
				is GenericRHMIDimensions)
		assert(RHMIDimensions.create(mapOf("hmi.display-width" to "1440", "hmi.display-height" to "540",
			"hmi.type" to "BMW ID5"))
				is BMW5XLRHMIDimensions)
		assert(RHMIDimensions.create(mapOf("hmi.display-width" to "1280", "hmi.display-height" to "480",
			"hmi.type" to "MINI ID4"))
				is MiniDimensions)
		assert(RHMIDimensions.create(mapOf("hmi.display-width" to "1280", "hmi.display-height" to "480",
			"hmi.type" to "MINI ID5", "a4axl" to "false"))
				is MiniDimensions)
		assert(RHMIDimensions.create(mapOf("hmi.display-width" to "1280", "hmi.display-height" to "480",
			"hmi.type" to "MINI ID5", "a4axl" to "true"))
				is Mini5XLDimensions)
	}

	@Test
	fun testSidebarDimensions() {
		var isWidescreen = true
		val base = GenericRHMIDimensions(1280, 480)
		val sidebar = SidebarRHMIDimensions(base) {isWidescreen}
		Assert.assertEquals(1141, sidebar.appWidth)

		isWidescreen = false
		Assert.assertEquals(673, sidebar.appWidth)

		// Mini5XL has these sidebar dimensions
		val mini = Mini5XLDimensions()
		val miniSidebar = SidebarRHMIDimensions(mini) {isWidescreen}
		isWidescreen = true
		Assert.assertEquals(1267, miniSidebar.appWidth)

		isWidescreen = false
		Assert.assertEquals(532, miniSidebar.marginRight)
		Assert.assertEquals(908, miniSidebar.visibleWidth)
	}

}