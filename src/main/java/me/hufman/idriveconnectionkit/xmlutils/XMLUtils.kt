package me.hufman.idriveconnectionkit.xmlutils

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException

import java.io.ByteArrayInputStream
import java.io.IOException
import java.lang.reflect.Field

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


fun Node.getAttribute(attr: String): String? {
	return XMLUtils.getAttribute(this, attr)
}
fun Node.getAttributesMap(): Map<String, String> {
	return XMLUtils.getAttributes(this)
}
fun Node.getChildNamed(nodeName: String): Node? {
	return XMLUtils.getChildNodeNamed(this, nodeName)
}
fun Node?.getChildElements(): List<Node> {
	return XMLUtils.childNodes(this)
}

object XMLUtils {
	fun convertNodeList(list: NodeList): List<Node> {
		val outputList = ArrayList<Node>(list.length)
		for (i in 0 until list.length) {
			outputList.add(list.item(i))
		}
		return outputList
	}
	fun hasAttribute(element: Node, attr: String): Boolean {
		return element.attributes.getNamedItem(attr) != null
	}

	fun getAttribute(element: Node, attr: String): String? {
		return element.attributes.getNamedItem(attr).nodeValue
	}

	fun getAttributes(element: Node): Map<String, String> {
		val attributes = HashMap<String, String>()
		if (element.attributes == null) {
			return attributes
		}
		for (i in 0 until element.attributes.length) {
			val attr = element.attributes.item(i)
			attributes[attr.nodeName] = attr.nodeValue
		}
		return attributes
	}

	fun getChildNodeNamed(element: Node, nodeName: String): Node? {
		return convertNodeList(element.childNodes).find { it.nodeName == nodeName }
	}

	fun childNodes(node: Node?): List<Node> {
		if (node == null) {
			return ArrayList()
		}
		return convertNodeList(node.childNodes).filter { it.nodeType == Node.ELEMENT_NODE }
	}
	fun childNodes(nodes: NodeList): List<Node> {
		return convertNodeList(nodes).filter { it.nodeType == Node.ELEMENT_NODE }
	}

	fun loadXML(xml: String): Document {
		return loadXML(xml.toByteArray())
	}
	fun loadXML(xml: ByteArray): Document {
		val builderFactory = DocumentBuilderFactory.newInstance()
		val builder: DocumentBuilder
		try {
			builder = builderFactory.newDocumentBuilder()
		} catch (e: ParserConfigurationException) {
			throw IllegalArgumentException(e.toString())   // unlikely
		}

		try {
			return builder.parse(ByteArrayInputStream(xml))
		} catch (e: SAXException) {
			throw IllegalArgumentException(e.toString())
		} catch (e: IOException) {
			throw IllegalArgumentException(e.toString())   // unlikely
		}
	}

	fun getClassField(c: Class<in Any>, name: String): Field? {
		return c.declaredFields.find {
			it.name == name
		} ?: if (c.superclass != null) getClassField(c.superclass, name) else null
	}

	fun getSetterMethodName(name: String): String {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1)
	}

	fun unmarshalAttributes(obj: Any, attrs: Map<String, String>) {
		/** Given a map of attributes, apply any that fit to an object */
		attrs.filter { entry ->
			// we don't change the id after creating the object
			// and the value setter actually talks to the car, so let's not set it
			// the value always starts at 0 anyways
			entry.key != "id" && entry.key != "value"
		}.filter { entry ->
			/** Check for a field with this attribute's name
			 * either in this object or in the parent object
			 * and look for a setter
			 */
			val classType = obj.javaClass
			getClassField(classType, entry.key) != null &&
			classType.methods.any {
				it.name == getSetterMethodName(entry.key)
			}
		}.forEach { (key,value) ->
			/** Look at the (possibly parent's) field's data type, to parse the string attribute as
			 * and then find the matching setter and set it
			 */
			val classType = obj.javaClass
			val field = getClassField(classType, key)
			val setterName = getSetterMethodName(key)
			when(field?.type) {
				Integer::class.javaPrimitiveType -> classType.getMethod(setterName, Integer::class.javaPrimitiveType).invoke(obj, value.toInt())
				Boolean::class.javaPrimitiveType -> classType.getMethod(setterName, Boolean::class.javaPrimitiveType).invoke(obj, value.toBoolean())
				String::class.java -> classType.getMethod(setterName, String::class.java).invoke(obj, value)
				null -> Unit
				else -> field.set(obj, value)
			}
		}
	}
}
