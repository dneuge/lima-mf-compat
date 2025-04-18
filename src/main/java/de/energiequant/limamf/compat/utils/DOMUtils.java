package de.energiequant.limamf.compat.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMUtils {
    private DOMUtils() {
        // utility class; hide constructor
    }

    public static Optional<String> getAttribute(Node node, String attributeName) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return Optional.empty();
        }

        Node attributeNode = attributes.getNamedItem(attributeName);
        if (attributeNode == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(attributeNode.getNodeValue());
    }

    public static List<Node> onlyElements(NodeList nodes) {
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                out.add(node);
            }
        }
        return out;
    }

    public static List<Node> findChildElementsNamed(Node node, String elementName) {
        return onlyElementsNamed(node.getChildNodes(), elementName);
    }

    public static List<Node> onlyElementsNamed(NodeList nodes, String elementName) {
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && elementName.equals(node.getNodeName())) {
                out.add(node);
            }
        }
        return out;
    }

    public static List<Node> asList(NodeList nodes) {
        List<Node> out = new ArrayList<>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            out.add(nodes.item(i));
        }
        return out;
    }
}
