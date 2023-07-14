package cx.rain.configurate_toml;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ParsingException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public class TomlParseUtil {
    protected static void from(final TomlTable result, final ConfigurationNode node) throws ParsingException, SerializationException {
        for (var entry : result.entrySet()) {
            var key = entry.getKey();

            if (result.isTable(key)) {
                from(result.getTable(key), node.node(key));
            } else if (result.isArray(key)) {
                fromArray(result.getArray(key), node.node(key));
            } else if (result.isLocalDate(key)) {
                node.node(key).set(result.getLocalDate(key));
            } else if (result.isLocalTime(key)) {
                node.node(key).set(result.getLocalTime(key));
            } else if (result.isLocalDateTime(key)) {
                node.node(key).set(result.getLocalDateTime(key));
            } else if (result.isOffsetDateTime(key)) {
                node.node(key).set(result.getOffsetDateTime(key));
            } else {
                node.node(key).set(entry.getValue());
            }
        }
    }

    private static void fromArray(final TomlArray array, final ConfigurationNode node) throws ParsingException, SerializationException {
        for (var item : array.toList()) {
            if (item instanceof TomlTable table) {
                from(table, node.appendListNode());
            } else if (item instanceof TomlArray arr) {
                fromArray(arr, node.appendListNode());
            } else if (item instanceof LocalDate date) {
                node.appendListNode().set(date);
            } else if (item instanceof LocalTime time) {
                node.appendListNode().set(time);
            } else if (item instanceof LocalDateTime dateTime) {
                node.appendListNode().set(dateTime);
            } else if (item instanceof OffsetDateTime dateTime) {
                node.appendListNode().set(dateTime);
            } else {
                node.appendListNode().set(item);
            }
        }
    }
}
