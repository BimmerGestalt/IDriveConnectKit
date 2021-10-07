package io.bimmergestalt.idriveconnectkit

import de.bmw.idrive.BMWRemoting
import de.bmw.idrive.BMWRemotingServer
import java.io.InputStream
import java.security.MessageDigest

object Utils {
	fun etchAsInt(obj: Any?, default: Int = 0): Int {
		/** Etch likes to shrink numbers to the smallest type that will fit
		 * But JVM wants the number types to match in various places
		 */
		return when (obj) {
			is Byte -> obj.toInt()
			is Short -> obj.toInt()
			is Int -> obj
			else -> default
		}
	}

	fun BMWRemotingServer.rhmi_setResourceCached(handle: Int, type: BMWRemoting.RHMIResourceType, data: InputStream?): Boolean {
		data ?: return false
		val bytes = data.readBytes()
		return rhmi_setResourceCached(handle, type, bytes)
	}

	fun BMWRemotingServer.rhmi_setResourceCached(handle: Int, type: BMWRemoting.RHMIResourceType, data: ByteArray?): Boolean {
		data ?: return false
		val hash = md5sum(data)
		val cached = rhmi_checkResource(hash, handle, data.size, "", type)
		if (!cached) {
			rhmi_setResource(handle, data, type)
		}
		return cached
	}

	fun md5sum(data: ByteArray): ByteArray {
		val hasher = MessageDigest.getInstance("MD5")
		hasher.update(data)
		return hasher.digest()
	}
}