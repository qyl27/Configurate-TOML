import cx.rain.configurate_toml.TomlConfigurationLoader;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestMain {
    final TomlConfigurationLoader testLoader = TomlConfigurationLoader.builder()
            .path(new File("test.toml").toPath())
//            .parseJavaTime()
//            .failOnWriteNull()
            .build();

    final static OffsetDateTime dateTime = OffsetDateTime.now();

    private enum Mood {
        HAPPY,
        SATISFIED,
        CONFUSED,
        UNHAPPY;
    }

    private class Person {
        public String name;
        public Mood mood;

        protected Person(String name, Mood mood) {
            this.name = name;
            this.mood = mood;
        }
    }

    @Test
    @Order(2)
    void testSimpleSaving() throws SerializationException, IOException {
        CommentedConfigurationNode root = testLoader.createNode();

        root.node("name").set("test.toml");
        root.node("owner", "name").set("qyl27").comment("Owner of text.toml.");

        CommentedConfigurationNode tomlNode = root.node("toml");
        String[] credits = new String[] {
                "jackson", "configurate"
        };
        tomlNode.node("credits").set(credits);

        CommentedConfigurationNode tomlData = root.node("toml", "data");
        tomlData.node("number").set(27);
        tomlData.node("pi").set(3.14);

        CommentedConfigurationNode peopleNode = root.node("people");
        List<Person> people = new ArrayList<>();
        people.add(new Person("qyl27", Mood.HAPPY));
        people.add(new Person("iceBear67", Mood.SATISFIED));
        people.add(new Person("IamNotExist", Mood.CONFUSED));
        peopleNode.set(people);

        tomlData.node("time").set(dateTime.toString());

        testLoader.save(root);
    }

    @Test
    @Order(1)
    void testLoad() throws ConfigurateException {
        CommentedConfigurationNode root = testLoader.load();

        assert root.node("people").isList();

        assert root.node("people").childrenList().get(1).node("mood").getString().equals("SATISFIED");

        assert root.node("toml", "data", "number").getInt() == 27;
        assert root.node("toml", "data", "pi").getDouble() == 3.14;

        String time = root.node("toml", "data", "time").getString();
        assert dateTime.getHour() == OffsetDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME).getHour();

        assert root.node("toml", "credits").childrenList().size() == 2;

        assert root.node("owner", "name").getString().equals("qyl27");
        assert root.node("name").getString().equals("test.toml");
    }
}
