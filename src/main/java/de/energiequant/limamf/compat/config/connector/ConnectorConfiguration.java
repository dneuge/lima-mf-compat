package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.DOMUtils.findChildElementsNamed;
import static de.energiequant.limamf.compat.utils.DOMUtils.onlyElements;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConnectorConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorConfiguration.class);

    private final Map<ConfigItem.Direction, Collection<ConfigItem>> configItemsByDirection = new EnumMap<>(ConfigItem.Direction.class);

    public static ConnectorConfiguration fromXML(File path) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setExpandEntityReferences(false);

            doc = factory.newDocumentBuilder()
                         .parse(path);
        } catch (IOException ex) {
            LOGGER.warn("failed to read {}", path, ex);
            throw new UncheckedIOException(ex);
        } catch (SAXException ex) {
            LOGGER.warn("failed to parse {}", path, ex);
            throw new IllegalArgumentException("failed to parse " + path, ex);
        } catch (ParserConfigurationException ex) {
            LOGGER.warn("incompatible XML parser configuration", ex);
            throw new MissingDependency("incompatible XML parser configuration", ex);
        }

        NodeList rootNodes = doc.getElementsByTagName("MobiflightConnector");
        if (rootNodes.getLength() != 1) {
            throw new IllegalArgumentException("exactly one root node is required, found " + rootNodes.getLength());
        }

        return fromXML(rootNodes.item(0));
    }

    private static ConnectorConfiguration fromXML(Node rootNode) {
        ConnectorConfiguration out = new ConnectorConfiguration();

        for (Node groupNode : onlyElements(rootNode.getChildNodes())) {
            ConfigItem.Direction direction = ConfigItem.Direction.fromXmlParentElementName(groupNode.getNodeName()).orElse(null);
            if (direction == null) {
                continue;
            }

            for (Node configNode : findChildElementsNamed(groupNode, "config")) {
                out.addItem(ConfigItem.fromXML(direction, configNode));
            }
        }

        return out;
    }

    private void addItem(ConfigItem item) {
        configItemsByDirection.computeIfAbsent(item.getDirection(), x -> new ArrayList<>())
                              .add(item);
    }

    public Collection<ConfigItem> getItems(ConfigItem.Direction direction) {
        return new ArrayList<>(configItemsByDirection.getOrDefault(direction, Collections.emptyList()));
    }

    private static class MissingDependency extends RuntimeException {
        MissingDependency(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
