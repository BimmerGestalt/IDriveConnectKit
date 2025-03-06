package io.bimmergestalt.idriveconnectkit


/**
 * The list of all known CDS Properties
 */
class CDSProperty private constructor(val ident: Int, val propertyName: String) {
	companion object {
		private val byId: MutableMap<Int, CDSProperty> = HashMap()
		private val byName: MutableMap<String, CDSProperty> = HashMap()

		val REPLAYING = CDSProperty(0, "replaying")
		val CLIMATE_ACCOMPRESSOR = CDSProperty(1, "climate.ACCompressor")
		val CLIMATE_ACMODE = CDSProperty(3, "climate.ACMode")
		val CLIMATE_ACSYSTEMTEMPERATURES = CDSProperty(4, "climate.ACSystemTemperatures")
		val CLIMATE_DRIVERSETTINGS = CDSProperty(5, "climate.driverSettings")
		val CLIMATE_PASSENGERSETTINGS = CDSProperty(6, "climate.passengerSettings")
		val CLIMATE_RESIDUALHEAT = CDSProperty(7, "climate.residualHeat")
		val CLIMATE_SEATHEATDRIVER = CDSProperty(8, "climate.seatHeatDriver")
		val CLIMATE_SEATHEATPASSENGER = CDSProperty(9, "climate.seatHeatPassenger")
		val COMMUNICATION_CURRENTCALLINFO = CDSProperty(10, "communication.currentCallInfo")
		val COMMUNICATION_LASTCALLINFO = CDSProperty(11, "communication.lastCallInfo")
		val CONTROLS_CONVERTIBLETOP = CDSProperty(12, "controls.convertibleTop")
		val CONTROLS_CRUISECONTROL = CDSProperty(13, "controls.cruiseControl")
		val CONTROLS_DEFROSTREAR = CDSProperty(15, "controls.defrostRear")
		val CONTROLS_HEADLIGHTS = CDSProperty(16, "controls.headlights")
		val CONTROLS_LIGHTS = CDSProperty(18, "controls.lights")
		val CONTROLS_STARTSTOPSTATUS = CDSProperty(20, "controls.startStopStatus")
		val CONTROLS_SUNROOF = CDSProperty(21, "controls.sunroof")
		val CONTROLS_TURNSIGNAL = CDSProperty(22, "controls.turnSignal")
		val CONTROLS_WINDOWDRIVERFRONT = CDSProperty(23, "controls.windowDriverFront")
		val CONTROLS_WINDOWDRIVERREAR = CDSProperty(24, "controls.windowDriverRear")
		val CONTROLS_WINDOWPASSENGERFRONT = CDSProperty(25, "controls.windowPassengerFront")
		val CONTROLS_WINDOWPASSENGERREAR = CDSProperty(26, "controls.windowPassengerRear")
		val CONTROLS_WINDSHIELDWIPER = CDSProperty(27, "controls.windshieldWiper")
		val DRIVING_ACCELERATION = CDSProperty(28, "driving.acceleration")
		val DRIVING_ACCELERATORPEDAL = CDSProperty(29, "driving.acceleratorPedal")
		val DRIVING_AVERAGECONSUMPTION = CDSProperty(30, "driving.averageConsumption")
		val DRIVING_AVERAGESPEED = CDSProperty(31, "driving.averageSpeed")
		val DRIVING_BRAKECONTACT = CDSProperty(32, "driving.brakeContact")
		val DRIVING_CLUTCHPEDAL = CDSProperty(34, "driving.clutchPedal")
		val DRIVING_DSCACTIVE = CDSProperty(35, "driving.DSCActive")
		val DRIVING_ECOTIP = CDSProperty(36, "driving.ecoTip")
		val DRIVING_GEAR = CDSProperty(37, "driving.gear")
		val DRIVING_KEYPOSITION = CDSProperty(38, "driving.keyPosition")
		val DRIVING_ODOMETER = CDSProperty(39, "driving.odometer")
		val DRIVING_PARKINGBRAKE = CDSProperty(40, "driving.parkingBrake")
		val DRIVING_SHIFTINDICATOR = CDSProperty(41, "driving.shiftIndicator")
		val DRIVING_SPEEDACTUAL = CDSProperty(42, "driving.speedActual")
		val DRIVING_SPEEDDISPLAYED = CDSProperty(43, "driving.speedDisplayed")
		val DRIVING_STEERINGWHEEL = CDSProperty(44, "driving.steeringWheel")
		val DRIVING_MODE = CDSProperty(45, "driving.mode")
		val ENGINE_CONSUMPTION = CDSProperty(46, "engine.consumption")
		val ENGINE_INFO = CDSProperty(47, "engine.info")
		val ENGINE_RPMSPEED = CDSProperty(50, "engine.RPMSpeed")
		val ENGINE_STATUS = CDSProperty(51, "engine.status")
		val ENGINE_TEMPERATURE = CDSProperty(52, "engine.temperature")
		val ENGINE_TORQUE = CDSProperty(53, "engine.torque")
		val ENTERTAINMENT_MULTIMEDIA = CDSProperty(54, "entertainment.multimedia")
		val ENTERTAINMENT_RADIOFREQUENCY = CDSProperty(55, "entertainment.radioFrequency")
		val ENTERTAINMENT_RADIOSTATION = CDSProperty(56, "entertainment.radioStation")
		val NAVIGATION_CURRENTPOSITIONDETAILEDINFO = CDSProperty(57, "navigation.currentPositionDetailedInfo")
		val NAVIGATION_FINALDESTINATION = CDSProperty(59, "navigation.finalDestination")
		val NAVIGATION_FINALDESTINATIONDETAILEDINFO = CDSProperty(60, "navigation.finalDestinationDetailedInfo")
		val NAVIGATION_GPSEXTENDEDINFO = CDSProperty(61, "navigation.GPSExtendedInfo")
		val NAVIGATION_GPSPOSITION = CDSProperty(62, "navigation.GPSPosition")
		val NAVIGATION_GUIDANCESTATUS = CDSProperty(63, "navigation.guidanceStatus")
		val NAVIGATION_INFOTONEXTDESTINATION = CDSProperty(65, "navigation.infoToNextDestination")
		val NAVIGATION_NEXTDESTINATION = CDSProperty(66, "navigation.nextDestination")
		val NAVIGATION_NEXTDESTINATIONDETAILEDINFO = CDSProperty(67, "navigation.nextDestinationDetailedInfo")
		val SENSORS_BATTERY = CDSProperty(68, "sensors.battery")
		val SENSORS_DOORS = CDSProperty(70, "sensors.doors")
		val SENSORS_FUEL = CDSProperty(71, "sensors.fuel")
		val SENSORS_PDCRANGEFRONT = CDSProperty(72, "sensors.PDCRangeFront")
		val SENSORS_PDCRANGEREAR = CDSProperty(73, "sensors.PDCRangeRear")
		val SENSORS_PDCSTATUS = CDSProperty(74, "sensors.PDCStatus")
		val SENSORS_SEATOCCUPIEDPASSENGER = CDSProperty(76, "sensors.seatOccupiedPassenger")
		val SENSORS_SEATBELT = CDSProperty(77, "sensors.seatbelt")
		val SENSORS_TEMPERATUREEXTERIOR = CDSProperty(78, "sensors.temperatureExterior")
		val SENSORS_TEMPERATUREINTERIOR = CDSProperty(79, "sensors.temperatureInterior")
		val SENSORS_TRUNK = CDSProperty(81, "sensors.trunk")
		val VEHICLE_COUNTRY = CDSProperty(82, "vehicle.country")
		val VEHICLE_LANGUAGE = CDSProperty(83, "vehicle.language")
		val VEHICLE_TYPE = CDSProperty(84, "vehicle.type")
		val VEHICLE_UNITSPEED = CDSProperty(85, "vehicle.unitSpeed")
		val VEHICLE_UNITS = CDSProperty(86, "vehicle.units")
		val VEHICLE_VIN = CDSProperty(87, "vehicle.VIN")
		val ENGINE_RANGECALC = CDSProperty(88, "engine.rangeCalc")
		val ENGINE_ELECTRICVEHICLEMODE = CDSProperty(89, "engine.electricVehicleMode")
		val DRIVING_SOCHOLDSTATE = CDSProperty(90, "driving.SOCHoldState")
		val DRIVING_ELECTRICALPOWERDISTRIBUTION = CDSProperty(91, "driving.electricalPowerDistribution")
		val DRIVING_DISPLAYRANGEELECTRICVEHICLE = CDSProperty(92, "driving.displayRangeElectricVehicle")
		val SENSORS_SOCBATTERYHYBRID = CDSProperty(93, "sensors.SOCBatteryHybrid")
		val SENSORS_BATTERYTEMP = CDSProperty(94, "sensors.batteryTemp")
		val HMI_IDRIVE = CDSProperty(95, "hmi.iDrive")
		val DRIVING_ECORANGEWON = CDSProperty(96, "driving.ecoRangeWon")
		val CLIMATE_AIRCONDITIONERCOMPRESSOR = CDSProperty(97, "climate.airConditionerCompressor")
		val CONTROLS_STARTSTOPLEDS = CDSProperty(98, "controls.startStopLEDs")
		val DRIVING_ECORANGE = CDSProperty(99, "driving.ecoRange")
		val DRIVING_FDRCONTROL = CDSProperty(100, "driving.FDRControl")
		val DRIVING_KEYNUMBER = CDSProperty(101, "driving.keyNumber")
		val NAVIGATION_INFOTOFINALDESTINATION = CDSProperty(102, "navigation.infoToFinalDestination")
		val NAVIGATION_UNITS = CDSProperty(103, "navigation.units")
		val SENSORS_LID = CDSProperty(104, "sensors.lid")
		val SENSORS_SEATOCCUPIEDDRIVER = CDSProperty(105, "sensors.seatOccupiedDriver")
		val SENSORS_SEATOCCUPIEDREARLEFT = CDSProperty(106, "sensors.seatOccupiedRearLeft")
		val SENSORS_SEATOCCUPIEDREARRIGHT = CDSProperty(107, "sensors.seatOccupiedRearRight")
		val VEHICLE_SYSTEMTIME = CDSProperty(108, "vehicle.systemTime")
		val VEHICLE_TIME = CDSProperty(109, "vehicle.time")
		val DRIVING_DRIVINGSTYLE = CDSProperty(110, "driving.drivingStyle")
		val NAVIGATION_ROUTEELAPSEDINFO = CDSProperty(112, "navigation.routeElapsedInfo")
		val HMI_TTS = CDSProperty(113, "hmi.tts")
		val HMI_GRAPHICALCONTEXT = CDSProperty(114, "hmi.graphicalContext")
		val SENSORS_PDCRANGEFRONT2 = CDSProperty(115, "sensors.PDCRangeFront2")
		val SENSORS_PDCRANGEREAR2 = CDSProperty(116, "sensors.PDCRangeRear2")
		val CDS_APIREGISTRY = CDSProperty(117, "cds.apiRegistry")
		val API_CARCLOUD = CDSProperty(118, "api.carcloud")
		val API_STARTJSAPP = CDSProperty(119, "api.startJSApp")

		fun fromIdent(ident: Int?): CDSProperty? {
			return byId[ident]
		}
		fun fromIdent(ident: String?): CDSProperty? {
			return byId[ident?.toIntOrNull()]
		}

		/**
		 * When creating custom CDSProperties, I think the car wants the ident to stay stable
		 * So this function helps return the same CDSProperty for the same given propertyName
		 */
		fun forName(name: String, startingIdent: Int = 1000): CDSProperty {
			val existing = byName[name]
			if (existing != null) {
				return existing
			}
			var ident = startingIdent
			while (byId.containsKey(ident)) {
				ident++
			}
			return CDSProperty(ident, name)
		}
	}

	init {
		val existingById = byId[ident]
		if (existingById != null && existingById.propertyName != this.propertyName) {
			throw IllegalArgumentException("Duplicate ident $ident")
		}
		byId[ident] = this

		val existingByName = byName[propertyName]
		if (existingByName != null && existingByName.ident != this.ident) {
			throw IllegalArgumentException("Duplicate propertyName $propertyName")
		}
		byName[propertyName] = this
	}
}

/**
 * An organized tree of CDS properties
 */
object CDS {
	object API {
		val CARCLOUD = CDSProperty.API_CARCLOUD
		val STARTJSAPP = CDSProperty.API_STARTJSAPP
		val APIREGISTRY = CDSProperty.CDS_APIREGISTRY
	}
	object CLIMATE {
		val ACCOMPRESSOR = CDSProperty.CLIMATE_ACCOMPRESSOR
		val ACMODE = CDSProperty.CLIMATE_ACMODE
		val ACSYSTEMTEMPERATURES = CDSProperty.CLIMATE_ACSYSTEMTEMPERATURES
		val AIRCONDITIONERCOMPRESSOR = CDSProperty.CLIMATE_AIRCONDITIONERCOMPRESSOR
		val DRIVERSETTINGS = CDSProperty.CLIMATE_DRIVERSETTINGS
		val PASSENGERSETTINGS = CDSProperty.CLIMATE_PASSENGERSETTINGS
		val RESIDUALHEAT = CDSProperty.CLIMATE_RESIDUALHEAT
		val SEATHEATDRIVER = CDSProperty.CLIMATE_SEATHEATDRIVER
		val SEATHEATPASSENGER = CDSProperty.CLIMATE_SEATHEATPASSENGER
	}
	object COMMUNICATION {
		val CURRENTCALLINFO = CDSProperty.COMMUNICATION_CURRENTCALLINFO
		val LASTCALLINFO = CDSProperty.COMMUNICATION_LASTCALLINFO
	}
	object CONTROLS {
		val CONVERTIBLETOP = CDSProperty.CONTROLS_CONVERTIBLETOP
		val CRUISECONTROL = CDSProperty.CONTROLS_CRUISECONTROL
		val DEFROSTREAR = CDSProperty.CONTROLS_DEFROSTREAR
		val HEADLIGHTS = CDSProperty.CONTROLS_HEADLIGHTS
		val LIGHTS = CDSProperty.CONTROLS_LIGHTS
		val STARTSTOPLEDS = CDSProperty.CONTROLS_STARTSTOPLEDS
		val STARTSTOPSTATUS = CDSProperty.CONTROLS_STARTSTOPSTATUS
		val SUNROOF = CDSProperty.CONTROLS_SUNROOF
		val TURNSIGNAL = CDSProperty.CONTROLS_TURNSIGNAL
		val WINDOWDRIVERFRONT = CDSProperty.CONTROLS_WINDOWDRIVERFRONT
		val WINDOWDRIVERREAR = CDSProperty.CONTROLS_WINDOWDRIVERREAR
		val WINDOWPASSENGERFRONT = CDSProperty.CONTROLS_WINDOWPASSENGERFRONT
		val WINDOWPASSENGERREAR = CDSProperty.CONTROLS_WINDOWPASSENGERREAR
		val WINDSHIELDWIPER = CDSProperty.CONTROLS_WINDSHIELDWIPER
	}
	object DRIVING {
		val ACCELERATION = CDSProperty.DRIVING_ACCELERATION
		val ACCELERATORPEDAL = CDSProperty.DRIVING_ACCELERATORPEDAL
		val AVERAGECONSUMPTION = CDSProperty.DRIVING_AVERAGECONSUMPTION
		val AVERAGESPEED = CDSProperty.DRIVING_AVERAGESPEED
		val BRAKECONTACT = CDSProperty.DRIVING_BRAKECONTACT
		val CLUTCHPEDAL = CDSProperty.DRIVING_CLUTCHPEDAL
		val DISPLAYRANGEELECTRICVEHICLE = CDSProperty.DRIVING_DISPLAYRANGEELECTRICVEHICLE
		val DRIVINGSTYLE = CDSProperty.DRIVING_DRIVINGSTYLE
		val DSCACTIVE = CDSProperty.DRIVING_DSCACTIVE
		val ECORANGE = CDSProperty.DRIVING_ECORANGE
		val ECORANGEWON = CDSProperty.DRIVING_ECORANGEWON
		val ECOTIP = CDSProperty.DRIVING_ECOTIP
		val ELECTRICALPOWERDISTRIBUTION = CDSProperty.DRIVING_ELECTRICALPOWERDISTRIBUTION
		val FDRCONTROL = CDSProperty.DRIVING_FDRCONTROL
		val GEAR = CDSProperty.DRIVING_GEAR
		val KEYNUMBER = CDSProperty.DRIVING_KEYNUMBER
		val KEYPOSITION = CDSProperty.DRIVING_KEYPOSITION
		val MODE = CDSProperty.DRIVING_MODE
		val ODOMETER = CDSProperty.DRIVING_ODOMETER
		val PARKINGBRAKE = CDSProperty.DRIVING_PARKINGBRAKE
		val SHIFTINDICATOR = CDSProperty.DRIVING_SHIFTINDICATOR
		val SOCHOLDSTATE = CDSProperty.DRIVING_SOCHOLDSTATE
		val SPEEDACTUAL = CDSProperty.DRIVING_SPEEDACTUAL
		val SPEEDDISPLAYED = CDSProperty.DRIVING_SPEEDDISPLAYED
		val STEERINGWHEEL = CDSProperty.DRIVING_STEERINGWHEEL
	}
	object ENGINE {
		val CONSUMPTION = CDSProperty.ENGINE_CONSUMPTION
		val ELECTRICVEHICLEMODE = CDSProperty.ENGINE_ELECTRICVEHICLEMODE
		val INFO = CDSProperty.ENGINE_INFO
		val RANGECALC = CDSProperty.ENGINE_RANGECALC
		val RPMSPEED = CDSProperty.ENGINE_RPMSPEED
		val STATUS = CDSProperty.ENGINE_STATUS
		val TEMPERATURE = CDSProperty.ENGINE_TEMPERATURE
		val TORQUE = CDSProperty.ENGINE_TORQUE
	}
	object ENTERTAINMENT {
		val MULTIMEDIA = CDSProperty.ENTERTAINMENT_MULTIMEDIA
		val RADIOFREQUENCY = CDSProperty.ENTERTAINMENT_RADIOFREQUENCY
		val RADIOSTATION = CDSProperty.ENTERTAINMENT_RADIOSTATION
	}
	object HMI {
		val GRAPHICALCONTEXT = CDSProperty.HMI_GRAPHICALCONTEXT
		val IDRIVE = CDSProperty.HMI_IDRIVE
		val TTS = CDSProperty.HMI_TTS
	}
	object NAVIGATION {
		val CURRENTPOSITIONDETAILEDINFO = CDSProperty.NAVIGATION_CURRENTPOSITIONDETAILEDINFO
		val FINALDESTINATION = CDSProperty.NAVIGATION_FINALDESTINATION
		val FINALDESTINATIONDETAILEDINFO = CDSProperty.NAVIGATION_FINALDESTINATIONDETAILEDINFO
		val GPSEXTENDEDINFO = CDSProperty.NAVIGATION_GPSEXTENDEDINFO
		val GPSPOSITION = CDSProperty.NAVIGATION_GPSPOSITION
		val GUIDANCESTATUS = CDSProperty.NAVIGATION_GUIDANCESTATUS
		val INFOTOFINALDESTINATION = CDSProperty.NAVIGATION_INFOTOFINALDESTINATION
		val INFOTONEXTDESTINATION = CDSProperty.NAVIGATION_INFOTONEXTDESTINATION
		val NEXTDESTINATION = CDSProperty.NAVIGATION_NEXTDESTINATION
		val NEXTDESTINATIONDETAILEDINFO = CDSProperty.NAVIGATION_NEXTDESTINATIONDETAILEDINFO
		val ROUTEELAPSEDINFO = CDSProperty.NAVIGATION_ROUTEELAPSEDINFO
		val UNITS = CDSProperty.NAVIGATION_UNITS
	}
	object SENSORS {
		val BATTERY = CDSProperty.SENSORS_BATTERY
		val BATTERYTEMP = CDSProperty.SENSORS_BATTERYTEMP
		val DOORS = CDSProperty.SENSORS_DOORS
		val FUEL = CDSProperty.SENSORS_FUEL
		val LID = CDSProperty.SENSORS_LID
		val PDCRANGEFRONT = CDSProperty.SENSORS_PDCRANGEFRONT
		val PDCRANGEFRONT2 = CDSProperty.SENSORS_PDCRANGEFRONT2
		val PDCRANGEREAR = CDSProperty.SENSORS_PDCRANGEREAR
		val PDCRANGEREAR2 = CDSProperty.SENSORS_PDCRANGEREAR2
		val PDCSTATUS = CDSProperty.SENSORS_PDCSTATUS
		val SEATBELT = CDSProperty.SENSORS_SEATBELT
		val SEATOCCUPIEDDRIVER = CDSProperty.SENSORS_SEATOCCUPIEDDRIVER
		val SEATOCCUPIEDPASSENGER = CDSProperty.SENSORS_SEATOCCUPIEDPASSENGER
		val SEATOCCUPIEDREARLEFT = CDSProperty.SENSORS_SEATOCCUPIEDREARLEFT
		val SEATOCCUPIEDREARRIGHT = CDSProperty.SENSORS_SEATOCCUPIEDREARRIGHT
		val SOCBATTERYHYBRID = CDSProperty.SENSORS_SOCBATTERYHYBRID
		val TEMPERATUREEXTERIOR = CDSProperty.SENSORS_TEMPERATUREEXTERIOR
		val TEMPERATUREINTERIOR = CDSProperty.SENSORS_TEMPERATUREINTERIOR
		val TRUNK = CDSProperty.SENSORS_TRUNK
	}
	object VEHICLE {
		val COUNTRY = CDSProperty.VEHICLE_COUNTRY
		val LANGUAGE = CDSProperty.VEHICLE_LANGUAGE
		val SYSTEMTIME = CDSProperty.VEHICLE_SYSTEMTIME
		val TIME = CDSProperty.VEHICLE_TIME
		val TYPE = CDSProperty.VEHICLE_TYPE
		val UNITS = CDSProperty.VEHICLE_UNITS
		val UNITSPEED = CDSProperty.VEHICLE_UNITSPEED
		val VIN = CDSProperty.VEHICLE_VIN
	}
}