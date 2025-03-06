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

		// extra properties that BMW Connected doesn't know about
		// ident numbers are made up and seem to be accepted
		val CLIMATE_SEATHEATDRR = CDSProperty(500, "climate.seatHeatDRR")
		val CLIMATE_SEATHEATPSR = CDSProperty(501, "climate.seatHeatPSR")
		val CLIMATE_SEATCLIMATE = CDSProperty(502, "climate.seatClimate")
		val CLIMATE_SEATCLIMATEPASSENGER = CDSProperty(503, "climate.seatClimatePassenger")
		val CLIMATE_SEATCLIMATEDRR = CDSProperty(504, "climate.seatClimateDRR")
		val CLIMATE_SEATCLIMATEPSR = CDSProperty(505, "climate.seatClimatePSR")
		val CLIMATE_FONDSETTINGS = CDSProperty(506, "climate.fondSettings")
		val CLIMATE_VENTILATION = CDSProperty(507, "climate.ventilation")
		val CLIMATE_SEATAUTOCLIMATEDR = CDSProperty(508, "climate.seatAutoClimateDR")
		val CLIMATE_SEATAUTOCLIMATEPS = CDSProperty(509, "climate.seatAutoClimatePS")
		val CLIMATE_SEATAUTOCLIMATEDRR = CDSProperty(510, "climate.seatAutoClimateDRR")
		val CLIMATE_SEATAUTOCLIMATEPSR = CDSProperty(511, "climate.seatAutoClimatePSR")
		val CLIMATE_ARMRESTHEATING = CDSProperty(512, "climate.armRestHeating")
		val COMFORT_SEATCONTROLPS = CDSProperty(521, "comfort.seatControlPS")
		val COMFORT_SEATCONTROLDRR = CDSProperty(522, "comfort.seatControlDRR")
		val COMFORT_SEATCONTROLPSR = CDSProperty(523, "comfort.seatControlPSR")
		val COMFORT_SEATMASSAGERL = CDSProperty(524, "comfort.seatMassageRL")
		val COMFORT_SEATMASSAGERR = CDSProperty(525, "comfort.seatControlRR")
		val COMFORT_SEATLUMBARRL = CDSProperty(526, "comfort.seatLumbarRL")
		val COMFORT_SEATLUMBARRR = CDSProperty(527, "comfort.seatLumbarRR")
		val COMFORT_SCENTGENERATOR = CDSProperty(528, "comfort.scentGenerator")
		val COMFORT_AMBIENTLIGHT = CDSProperty(529, "comfort.ambientLight")
		val COMFORT_AMBIENTCOLOUR = CDSProperty(530, "comfort.ambientColour")
		val COMFORT_BODYTRAINING = CDSProperty(531, "comfort.bodyTraining")
		val COMFORT_BODYTRAININGSTATUS = CDSProperty(532, "comfort.bodyTrainingStatus")
		val COMFORT_SEATMEMORY = CDSProperty(533, "comfort.seatMemory")
		val COMMUNICATION_CONTACTLIST = CDSProperty(540, "communication.contactList")
		val COMMUNICATION_CONTACTDETAIL = CDSProperty(541, "communication.contactDetail")
		val COMMUNICATION_CONTACTPICTURE = CDSProperty(542, "communication.contactPicture")
		val COMMUNICATION_CONTACTSERVICEPROVIDER = CDSProperty(543, "communication.contactServiceProvider")
		val COMMUNICATION_CONTROLCALL = CDSProperty(544, "communication.controlCall")
		val COMMUNICATION_CALL = CDSProperty(545, "communication.call")
		val COMMUNICATION_PHONESTATUS = CDSProperty(546, "communication.phoneStatus")
		val COMMUNICATION_CALLSTATUS0 = CDSProperty(547, "communication.callStatus0")
		val COMMUNICATION_CALLSTATUS1 = CDSProperty(548, "communication.callStatus1")
		val COMMUNICATION_CONTROLPHONE = CDSProperty(549, "communication.controlPhone")
		val COMMUNICATION_PHONE = CDSProperty(550, "communication.phone")
		val COMMUNICATION_CALLDURATION0 = CDSProperty(551, "communication.callDuration0")
		val COMMUNICATION_CALLDURATION1 = CDSProperty(552, "communication.callDuration1")
		val COMMUNICATION_CALLINFO0 = CDSProperty(553, "communication.callInfo0")
		val COMMUNICATION_CALLINFO1 = CDSProperty(554, "communication.callInfo1")
		val COMMUNICATION_CALLAVAILABILITY = CDSProperty(555, "communication.callAvailability")
		val COMMUNICATION_GSMSIASTATUS = CDSProperty(556, "communication.gsmSIAStatus")
		val COMMUNICATION_INFOPLUS = CDSProperty(557, "communication.infoPlus")
		val COMMUNICATION_ATMID = CDSProperty(558, "communication.atmId")
		val CONTROLS_SUNROOF2 = CDSProperty(580, "controls.sunroof2")
		val CONTROLS_TCPROXY = CDSProperty(581, "controls.tcProxy")
		val CONTROLS_SHADOWINGROOF = CDSProperty(582, "controls.shadowingRoof")
		val CONTROLS_SUNBLIND = CDSProperty(583, "controls.sunblind")
		val DRIVING_MODE35UP = CDSProperty(590, "driving.mode35up")
		val DRIVING_TRAFFICSIGNINFO1 = CDSProperty(591, "driving.trafficSignInfo1")
		val DRIVING_TRAFFICSIGNINFO2 = CDSProperty(592, "driving.trafficSignInfo2")
		val DRIVING_TRAFFICSIGNINFO3 = CDSProperty(593, "driving.trafficSignInfo3")
		val DRIVING_TRAFFICSIGNINFO4 = CDSProperty(594, "driving.trafficSignInfo4")
		val DRIVING_TRAFFICSIGNINFOVISIBILITY = CDSProperty(595, "driving.trafficSignInfoVisibility")
		val DRIVING_RSCONFIGSTATUS = CDSProperty(596, "driving.rsConfigStatus")
		val DRIVING_ORNPITEXTMESSAGE = CDSProperty(597, "driving.orNPITextMessage")
		val DRIVING_ORINFO = CDSProperty(598, "driving.orInfo")
		val DRIVING_SLITEXTMESSAGE = CDSProperty(599, "driving.sliTextMessage")
		val DRIVING_SLINEXTSPEEDLIMITRECOGNITION = CDSProperty(600, "driving.sliNextSpeedLimitRecognition")
		val DRIVING_SLIACTUALSPEEDLIMITINFO = CDSProperty(601, "driving.sliActualSpeedLimitInfo")
		val DRIVING_LANEBOUNDARIESLOFRQ = CDSProperty(602, "driving.laneBoundariesLoFrq")
		val DRIVING_LANEBOUNDARIES = CDSProperty(603, "driving.laneBoundaries")
		val DRIVING_LANECHANGE = CDSProperty(604, "driving.laneChange")
		val DRIVING_SIGNRECOGNITIONFRONTCAMERA = CDSProperty(605, "driving.signRecognitionFrontCamera")
		val DRIVING_ENVLEARNING = CDSProperty(606, "driving.envLearning")
		val ENTERTAINMENT_DESCRIPTIONCURRENTSOURCE = CDSProperty(620, "entertainment.descriptionCurrentSource")
		val ENTERTAINMENT_STATUSAVAILABLESOURCES = CDSProperty(621, "entertainment.statusAvailableSources")
		val ENTERTAINMENT_STATUSALLOWEDSOURCES = CDSProperty(622, "entertainment.statusAllowedSources")
		val ENTERTAINMENT_MEDIABROWSING = CDSProperty(623, "entertainment.mediabrowsing")
		val ENTERTAINMENT_TUNER_STATUSCURRENTSOURCE = CDSProperty(624, "entertainment.tuner.statusCurrentSource")
		val ENTERTAINMENT_TUNER_STATUSPLAYBACK = CDSProperty(625, "entertainment.tuner.statusPlayback")
		val ENTERTAINMENT_TUNER_STATUSMETAINFO = CDSProperty(626, "entertainment.tuner.statusMetaInfo")
		val ENTERTAINMENT_TUNER_INFO = CDSProperty(627, "entertainment.tuner.info")
		val ENTERTAINMENT_TUNER_PLAYLIST = CDSProperty(628, "entertainment.tuner.playList")
		val ENTERTAINMENT_TUNER_COVERART = CDSProperty(629, "entertainment.tuner.coverArt")
		val ENTERTAINMENT_TUNER_EPGEVENT = CDSProperty(630, "entertainment.tuner.epgEvent")
		val ENTERTAINMENT_TUNER_SIGNALQUALITY = CDSProperty(631, "entertainment.tuner.signalQuality")
		val ENTERTAINMENT_RHMI_STATUSCURRENTSOURCE = CDSProperty(632, "entertainment.rhmi.statusCurrentSource")
		val ENTERTAINMENT_RHMI_COVERART = CDSProperty(633, "entertainment.rhmi.coverArt")
		val ENTERTAINMENT_RHMI_PROVIDERLOGO = CDSProperty(634, "entertainment.rhmi.providerLogo")
		val ENTERTAINMENT_RHMI_STATUSMETAINFO = CDSProperty(635, "entertainment.rhmi.statusMetaInfo")
		val ENTERTAINMENT_MD_COVERART = CDSProperty(636, "entertainment.md.coverArt")
		val ENTERTAINMENT_MD_STATUSCURRENTSOURCE = CDSProperty(637, "entertainment.md.statusCurrentSource")
		val ENTERTAINMENT_MD_MEDIABROWSING = CDSProperty(638, "entertainment.md.mediabrowsing")
		val ENTERTAINMENT_MD_STATUSPLAYBACK = CDSProperty(639, "entertainment.md.statusPlayback")
		val ENTERTAINMENT_MD_CONTROLPLAYBACK = CDSProperty(640, "entertainment.md.controlPlayback")
		val ENTERTAINMENT_MD_STATUSMETAINFO = CDSProperty(641, "entertainment.md.statusMetaInfo")
		val ENTERTAINMENT_MD_PLAYLIST = CDSProperty(642, "entertainment.md.playList")
		val ENTERTAINMENT_MD_RANDOM = CDSProperty(643, "entertainment.md.random")
		val ENTERTAINMENT_MD_BROWSINGCAPABILITIES = CDSProperty(644, "entertainment.md.browsingCapabilities")
		val ENTERTAINMENT_MD_PLAYINGCAPABILITIES = CDSProperty(645, "entertainment.md.playingCapabilities")
		val ENTERTAINMENT_DRIVE_COVERART = CDSProperty(646, "entertainment.drive.coverArt")
		val ENTERTAINMENT_DRIVE_STATUSCURRENTSOURCE = CDSProperty(647, "entertainment.drive.statusCurrentSource")
		val ENTERTAINMENT_DRIVE_PLAYLIST = CDSProperty(648, "entertainment.drive.playList")
		val ENTERTAINMENT_DRIVE_STATUSPLAYBACK = CDSProperty(649, "entertainment.drive.statusPlayback")
		val ENTERTAINMENT_DRIVE_CONTROLPLAYBACK = CDSProperty(650, "entertainment.drive.controlPlayback")
		val ENTERTAINMENT_DRIVE_STATUSMETAINFO = CDSProperty(651, "entertainment.drive.statusMetaInfo")
		val ENTERTAINMENT_DRIVE_CHAPTERLIST = CDSProperty(652, "entertainment.drive.chapterList")
		val ENTERTAINMENT_DRIVE_SUBTITLE = CDSProperty(653, "entertainment.drive.subtitle")
		val ENTERTAINMENT_DRIVE_LANGUAGE = CDSProperty(654, "entertainment.drive.language")
		val ENTERTAINMENT_DRIVE_VIDEOANGLE = CDSProperty(655, "entertainment.drive.videoAngle")
		val ENTERTAINMENT_DRIVE_CONTROLCURRENTSOURCE = CDSProperty(656, "entertainment.drive.controlCurrentSource")
		val ENTERTAINMENT_DRIVE_RANDOM = CDSProperty(657, "entertainment.drive.random")
		val ENTERTAINMENT_DRIVE_COLORBUTTONCOLORLIST = CDSProperty(658, "entertainment.drive.colorButtonColorList")
		val ENTERTAINMENT_DRIVE_PIPSUPPRESS = CDSProperty(659, "entertainment.drive.pipSuppress")
		val ENTERTAINMENT_DRIVE_BLURAYMEMORY = CDSProperty(660, "entertainment.drive.blurayMemory")
		val ENTERTAINMENT_DRIVE_MEDIABROWSING = CDSProperty(661, "entertainment.drive.mediabrowsing")
		val ENTERTAINMENT_VIDEO_STATUSCURRENTSOURCE = CDSProperty(662, "entertainment.video.statusCurrentSource")
		val ENTERTAINMENT_VIDEO_STATUSPLAYBACK = CDSProperty(663, "entertainment.video.statusPlayback")
		val ENTERTAINMENT_VIDEO_PLAYLIST = CDSProperty(664, "entertainment.video.playList")
		val ENTERTAINMENT_VIDEO_STATUSMETAINFO = CDSProperty(665, "entertainment.video.statusMetaInfo")
		val ENTERTAINMENT_VIDEO_SUBTITLE = CDSProperty(666, "entertainment.video.subtitle")
		val ENTERTAINMENT_VIDEO_LANGUAGE = CDSProperty(667, "entertainment.video.language")
		val ENTERTAINMENT_VIDEO_CONTROLCURRENTSOURCE = CDSProperty(668, "entertainment.video.controlCurrentSource")
		val ENTERTAINMENT_VIDEO_EPGEVENT = CDSProperty(669, "entertainment.video.epgEvent")
		val ENTERTAINMENT_VIDEO_SIGNALQUALITY = CDSProperty(670, "entertainment.video.signalQuality")
		val ENTERTAINMENT_VIDEO_TELETEXT = CDSProperty(671, "entertainment.video.teletext")
		val ENTERTAINMENT_VIDEO_STATIONSCAN = CDSProperty(672, "entertainment.video.stationScan")
		val HMI_VIDEOLOCK = CDSProperty(680, "hmi.videolock")
		val HMI_SPELLERINPUT = CDSProperty(681, "hmi.spellerInput")
		val HMI_DISPLAYCONTROL = CDSProperty(682, "hmi.displayControl")
		val HMI_HTTPTRANSFER = CDSProperty(683, "hmi.httpTransfer")
		val HMI_STATISTIC = CDSProperty(684, "hmi.statistic")
		val HMI_SEEALLINFO = CDSProperty(685, "hmi.seeAllInfo")
		val MMI_RSEACCESS = CDSProperty(686, "mmi.rseAccess")
		val RHMI_LUMVALUES = CDSProperty(687, "rhmi.lumValues")
		val ATM_ATMSETTINGS = CDSProperty(688, "atm.atmSettings")
		val NAVIGATION_GPSRAWDATA = CDSProperty(700, "navigation_GPSRawData")
		val NAVIGATION_MAPFEATURE = CDSProperty(701, "navigation.mapFeature")
		val NAVIGATION_TRIPLIST = CDSProperty(702, "navigation.triplist")
		val NAVIGATION_SHOWDESTINATION = CDSProperty(703, "navigation.showDestination")
		val NAVIGATION_CURRENTPOSITIONCOUNTRY = CDSProperty(704, "navigation.currentPositionCountry")
		val NAVIGATION_GETCABURL = CDSProperty(705, "navigation.getCABUrl")
		val NAVIGATION_ONLINESTARTUSECASE_POIENRICHMENT = CDSProperty(706, "navigation.onlineStartUseCase.POIEnrichment")
		val NAVIGATION_ONLINESTARTUSECASE_PICTUREONDESTINATION = CDSProperty(707, "navigation.onlineStartUseCase.PictureOnDestination")
		val NAVIGATION_ONLINESTARTUSECASE_ONLINESEARCH = CDSProperty(708, "navigation.onlineStartUseCase.OnlineSearch")
		val NAVIGATION_ONLINESTARTUSECASE_INTERMODALINFORMATION = CDSProperty(709, "navigation.onlineStartUseCase.IntermodalInformation")
		val NAVIGATION_ONLINESTARTUSECASE_INTERMODALROUTECHANGE = CDSProperty(710, "navigation.onlineStartUseCase.IntermodalRouteChange")
		val NAVIGATION_ONLINESTARTUSECASE_INTERMODALROUTEINFO = CDSProperty(711, "navigation.onlineStartUseCase.IntermodalRouteInfo")
		val NAVIGATION_ONLINESTARTUSECASE_COUNTRYINFORMATION = CDSProperty(712, "navigation.onlineStartUseCase.CountryInformation")
		val NAVIGATION_ONLINESTARTUSECASE_ROUTEINFOUPLOADBROWSER = CDSProperty(713, "navigation.onlineStartUseCase.RouteInfoUploadBrowser")
		val NAVIGATION_ONLINESTARTUSECASE_TRIPCHOICEHMI = CDSProperty(714, "navigation.onlineStartUseCase.TripChoiceHMI")
		val NAVIGATION_REQUESTJOURNEYS = CDSProperty(715, "navigation.requestJourneys")
		val NAVIGATION_ONLINEAPPINFORMATION = CDSProperty(716, "navigation.onlineAppInformation")
		val NAVIGATION_POICATEGORYSEARCH = CDSProperty(717, "navigation.poiCategorySearch")
		val NAVIGATION_NAVIGUIDANCEINFO = CDSProperty(718, "navigation.naviguidanceinfo")
		val SPECIALVEHICLE_MENUCONTROL = CDSProperty(725, "specialvehicle.menucontrol")
		val SENSORS_TIREPRESSUREWARNING = CDSProperty(730, "sensors.tirePressureWarning")
		val SENSORS_CHILDSAFETYLOCK = CDSProperty(731, "sensors.childSafetyLock")
		val VEHICLE_DAYNIGHTCHANGEOVER = CDSProperty(740, "vehicle.dayNightChangeOver")
		val VEHICLE_INTERACTIONDETECTION = CDSProperty(741, "vehicle.interactionDetection")
		val VEHICLE_POWERSTATEHEADUNIT = CDSProperty(742, "vehicle.powerStateHeadUnit")
		val VEHICLE_PARTIALNETWORKREQUEST = CDSProperty(743, "vehicle.partialNetworkRequest")
		val VEHICLE_STATEPWF = CDSProperty(744, "vehicle.statePWF")
		val VEHICLE_USERTIMEZONE = CDSProperty(745, "vehicle.userTimeZone")
		val VEHICLE_FASSTATUS = CDSProperty(746, "vehicle.fasStatus")
		val VEHICLE_PARTTYPE = CDSProperty(747, "vehicle.partType")
		val VEHICLE_FASTCHARGING = CDSProperty(748, "vehicle.fastCharging")

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