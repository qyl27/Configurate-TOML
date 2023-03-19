# Configurate-TOML
Toml format support for SpongePowered/Configurate.  
A library for [kalculos/dash](https://github.com/kalculos/dash).

## License
Apache-2.0

## Use as a dependency
1. Add the JitPack repository to your build file
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```
2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.qyl27:Configurate-TOML:main-SNAPSHOT'
}
```

## Todo
- [ ] More test for `TomlConfigurationLoader`.

## Known issues
- Generated TOML file can't use toml tables, because of [FasterXML/jackson-dataformats-text#254](https://github.com/FasterXML/jackson-dataformats-text/issues/254).
- Can't save comments, [FasterXML/jackson-dataformats-text#269](https://github.com/FasterXML/jackson-dataformats-text/issues/269).

## Credits
[SpongePowered/Configurate](https://github.com/SpongePowered/Configurate) (Apache-2.0)  
[FasterXML/jackson-core](https://github.com/FasterXML/jackson-core) (Apache-2.0)  
[FasterXML/jackson-dataformats-text](https://github.com/FasterXML/jackson-dataformats-text) (Apache-2.0)
