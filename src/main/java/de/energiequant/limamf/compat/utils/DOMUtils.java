package de.energiequant.limamf.compat.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Helper methods for {@link org.w3c.dom}.
 */
public class DOMUtils {
    private DOMUtils() {
        // utility class; hide constructor
    }

    /**
     * Returns the specified attribute's value for the given element {@link Node}, if present.
     *
     * @param node          DOM element to read attribute from
     * @param attributeName name of attribute to read
     * @return attribute value, if present
     */
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

    /**
     * Filters the given {@link NodeList} to return only {@link Node}s representing XML elements.
     *
     * @param nodes {@link NodeList} to filter
     * @return element {@link Node}s of given list
     */
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

    /**
     * Returns the given {@link Node}'s direct children having the specified XML element name.
     *
     * @param node        parent to find children for
     * @param elementName XML element name to filter for
     * @return all direct child elements having the specified name
     */
    public static List<Node> findChildElementsNamed(Node node, String elementName) {
        return onlyElementsNamed(node.getChildNodes(), elementName);
    }

    /**
     * Filters the given {@link NodeList} to return only {@link Node}s representing XML elements having the specified
     * name.
     *
     * @param nodes       {@link NodeList} to filter
     * @param elementName XML element name to filter for
     * @return element {@link Node}s of given list having the specified name
     */
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

    /**
     * Converts the given {@link NodeList} to a standard {@link List} of {@link Node}s.
     *
     * @param nodes {@link NodeList} to convert
     * @return standard {@link List} of {@link Node}s
     */
    public static List<Node> asList(NodeList nodes) {
        List<Node> out = new ArrayList<>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            out.add(nodes.item(i));
        }
        return out;
    }
}
