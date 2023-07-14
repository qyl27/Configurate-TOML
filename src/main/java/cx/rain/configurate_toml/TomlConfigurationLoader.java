package cx.rain.configurate_toml;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.CommentHandler;
import org.spongepowered.configurate.loader.CommentHandlers;
import org.spongepowered.configurate.loader.ParsingException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.*;

public class TomlConfigurationLoader extends AbstractConfigurationLoader<CommentedConfigurationNode> {

    protected TomlConfigurationLoader(Builder builder) {
        super(builder,  new CommentHandler[] {CommentHandlers.HASH});
    }

    @Override
    protected void loadInternal(CommentedConfigurationNode node, BufferedReader reader) throws ParsingException {
        TomlParseResult result;

        try {
            result = Toml.parse(reader);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (result.hasErrors()) {
            for (var error : result.errors()) {
                throw new ParsingException(error.position().line(), error.position().column(), null, error.getMessage(), error);
            }
        }

        try {
            TomlParseUtil.from(result, node);
        } catch (SerializationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void saveInternal(ConfigurationNode node, Writer writer) throws ConfigurateException {

//        try {
//            // We can't save comment because of FasterXML/jackson-dataformats-text#254.
////            tomlMapper.writeValue(writer, node.raw());
//        } catch (IOException ex) {
//            throw new ConfigurateException(ex);
//        }
    }

    @Override
    public CommentedConfigurationNode createNode(ConfigurationOptions options) {
        return CommentedConfigurationNode.root(options);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends AbstractConfigurationLoader.Builder<Builder, TomlConfigurationLoader> {
        private Builder() {

        }

//        /**
//         * Toml has a Time data type, according to ISO 8601 format.
//         * <p>
//         * When this option is set, these time types will be parsed to their proper {@code java.time} counterparts and
//         * appear as {@link com.fasterxml.jackson.core.JsonToken#VALUE_EMBEDDED_OBJECT} tokens.
//         * <p>
//         * Parser will treat them as string by default.
//         *
//         * @return Builder
//         */
//        public Builder parseJavaTime() {
//            tomlFactoryBuilder.enable(TomlReadFeature.PARSE_JAVA_TIME);
//            return this;
//        }
//
//        /**
//         * The TOML spec does not allow null values. We instead write an empty string by default.
//         * <p>
//         * When this option is set, any attempt to write a null value will error instead.
//         *
//         * @return Builder
//         */
//        public Builder failOnWriteNull() {
//            tomlFactoryBuilder.enable(TomlWriteFeature.FAIL_ON_NULL_WRITE);
//            return this;
//        }
//
//        public Builder configure(TomlReadFeature feature, boolean state) {
//            tomlFactoryBuilder.configure(feature, state);
//            return this;
//        }
//
//        public Builder configure(TomlWriteFeature feature, boolean state) {
//            tomlFactoryBuilder.configure(feature, state);
//            return this;
//        }

        @Override
        public TomlConfigurationLoader build() {
            return new TomlConfigurationLoader(this);
        }
    }
}
