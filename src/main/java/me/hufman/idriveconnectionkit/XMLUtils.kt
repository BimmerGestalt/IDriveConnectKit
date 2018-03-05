package me.hufman.IDriveConnectionKit

import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException

import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.LinkedList

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

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

}
