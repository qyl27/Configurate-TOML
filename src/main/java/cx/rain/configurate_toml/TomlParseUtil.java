package cx.rain.configurate_toml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.Map;

public class TomlParseUtil {
    protected static void fromJsonNode(final JsonNode jsonNode, final ConfigurationNode node) throws SerializationException {
        if (jsonNode.isContainerNode()) {
            fromContainer(jsonNode, node);
            return;
        }

        if (jsonNode.isValueNode()) {
            fromValue(jsonNode, node);
            return;
        }

        if (jsonNode.isMissingNode()) {
            throw new SerializationException("Missing node: " + jsonNode);
        }

        if (jsonNode.isNull()) {
            throw new SerializationException("Null node: " + jsonNode);
        }
    }

    private static void fromContainer(final JsonNode jsonNode, final ConfigurationNode node) throws SerializationException {
        if (jsonNode.isObject()) {
            ObjectNode objNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> it = objNode.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> child = it.next();
                fromJsonNode(child.getValue(), node.node(child.getKey()));
            }
            return;
        }

        if (jsonNode.isArray()) {
            ArrayNode arrNode = (ArrayNode) jsonNode;
            Iterator<JsonNode> it = arrNode.elements();
            while (it.hasNext()) {
                JsonNode child = it.next();
                fromJsonNode(child, node.appendListNode());
            }
        }
    }

    private static void fromValue(final JsonNode jsonNode, final ConfigurationNode node) throws SerializationException {
        if (jsonNode.isBoolean()) {
            boolean boolNode = jsonNode.asBoolean();
            node.set(Boolean.class, boolNode);
            return;
        }

        if (jsonNode.isShort()) {
            short shortNode = (short) jsonNode.asInt();
            node.set(Short.class, shortNode);
            return;
        }

        if (jsonNode.isInt()) {
            int intNode = jsonNode.asInt();
            node.set(Integer.class, intNode);
            return;
        }

        if (jsonNode.isLong() || jsonNode.isBigInteger()) {
            long longNode = jsonNode.asLong();
            node.set(Long.class, longNode);
            return;
        }

        if (jsonNode.isFloat()) {
            float floatNode = jsonNode.floatValue();
            node.set(Float.class, floatNode);
            return;
        }

        if (jsonNode.isDouble() || jsonNode.isBigDecimal()) {
            double doubleNode = jsonNode.doubleValue();
            node.set(Double.class, doubleNode);
            return;
        }

        if (jsonNode.isPojo()) {
            POJONode pojoNode = (POJONode) jsonNode;
            Object pojo = pojoNode.getPojo();
            // There is only four pojo value about time.
            if (pojo instanceof OffsetDateTime) {
                OffsetDateTime offsetDateTime = (OffsetDateTime) pojo;
                node.set(OffsetDateTime.class, offsetDateTime);
            } else if (pojo instanceof LocalDateTime) {
                LocalDateTime localDateTime = (LocalDateTime) pojo;
                node.set(LocalDateTime.class, localDateTime);
            } else if (pojo instanceof LocalDate) {
                LocalDate localDate = (LocalDate) pojo;
                node.set(LocalDate.class, localDate);
            } else if (pojo instanceof LocalTime) {
                LocalTime localTime = (LocalTime) pojo;
                node.set(LocalTime.class, localTime);
            }
            return;
        }

        if (jsonNode.isTextual()) {
            String textNode = jsonNode.asText();
            node.set(String.class, textNode);
        }
    }
}
