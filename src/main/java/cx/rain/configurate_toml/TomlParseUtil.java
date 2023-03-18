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
            var objNode = (ObjectNode) jsonNode;
            var it = objNode.fields();
            while (it.hasNext()) {
                var child = it.next();
                fromJsonNode(child.getValue(), node.node(child.getKey()));
            }
            return;
        }

        if (jsonNode.isArray()) {
            var arrNode = (ArrayNode) jsonNode;
            var it = arrNode.elements();
            while (it.hasNext()) {
                var child = it.next();
                fromJsonNode(child, node.appendListNode());
            }
        }
    }

    private static void fromValue(final JsonNode jsonNode, final ConfigurationNode node) throws SerializationException {
        if (jsonNode.isBoolean()) {
            var boolNode = jsonNode.asBoolean();
            node.set(Boolean.class, boolNode);
            return;
        }

        if (jsonNode.isShort()) {
            var shortNode = (short) jsonNode.asInt();
            node.set(Short.class, shortNode);
            return;
        }

        if (jsonNode.isInt()) {
            var intNode = jsonNode.asInt();
            node.set(Integer.class, intNode);
            return;
        }

        if (jsonNode.isLong() || jsonNode.isBigInteger()) {
            var longNode = jsonNode.asLong();
            node.set(Long.class, longNode);
            return;
        }

        if (jsonNode.isFloat()) {
            var floatNode = jsonNode.floatValue();
            node.set(Float.class, floatNode);
            return;
        }

        if (jsonNode.isDouble() || jsonNode.isBigDecimal()) {
            var doubleNode = jsonNode.doubleValue();
            node.set(Double.class, doubleNode);
            return;
        }

        if (jsonNode.isPojo()) {
            var pojoNode = (POJONode) jsonNode;
            var pojo = pojoNode.getPojo();
            // There is only four pojo value about time.
            if (pojo instanceof OffsetDateTime offsetDateTime) {
                node.set(OffsetDateTime.class, offsetDateTime);
            } else if (pojo instanceof LocalDateTime localDateTime) {
                node.set(LocalDateTime.class, localDateTime);
            } else if (pojo instanceof LocalDate localDate) {
                node.set(LocalDate.class, localDate);
            } else if (pojo instanceof LocalTime localTime) {
                node.set(LocalTime.class, localTime);
            }
            return;
        }

        if (jsonNode.isTextual()) {
            var textNode = jsonNode.asText();
            node.set(String.class, textNode);
        }
    }
}
