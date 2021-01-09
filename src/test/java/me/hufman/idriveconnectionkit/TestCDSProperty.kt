package me.hufman.idriveconnectionkit

import org.junit.Assert.assertEquals
import org.junit.Test

class TestCDSProperty {
	@Test
	fun testCdsProperty() {
		val property = CDSProperty.VEHICLE_VIN
		assertEquals(property, CDSProperty.fromIdent(property.ident))
		assertEquals(property, CDSProperty.fromIdent(property.ident.toString()))
		assertEquals(null, CDSProperty.fromIdent(null as Int?))
		assertEquals(null, CDSProperty.fromIdent(null as String?))

		// initializes the CDS accessors for code coverage
		assertEquals(CDSProperty.API_CARCLOUD, CDS.API.CARCLOUD)
		assertEquals(CDSProperty.CLIMATE_DRIVERSETTINGS, CDS.CLIMATE.DRIVERSETTINGS)
		assertEquals(CDSProperty.COMMUNICATION_CURRENTCALLINFO, CDS.COMMUNICATION.CURRENTCALLINFO)
		assertEquals(CDSProperty.CONTROLS_TURNSIGNAL, CDS.CONTROLS.TURNSIGNAL)
		assertEquals(CDSProperty.DRIVING_GEAR, CDS.DRIVING.GEAR)
		assertEquals(CDSProperty.ENGINE_INFO, CDS.ENGINE.INFO)
		assertEquals(CDSProperty.ENTERTAINMENT_MULTIMEDIA, CDS.ENTERTAINMENT.MULTIMEDIA)
		assertEquals(CDSProperty.HMI_TTS, CDS.HMI.TTS)
		assertEquals(CDSProperty.NAVIGATION_NEXTDESTINATION, CDS.NAVIGATION.NEXTDESTINATION)
		assertEquals(CDSProperty.SENSORS_BATTERY, CDS.SENSORS.BATTERY)
		assertEquals(CDSProperty.VEHICLE_VIN, CDS.VEHICLE.VIN)
	}
}