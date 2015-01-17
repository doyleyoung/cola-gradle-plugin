Questions? [![Join Chat](https://badges.gitter.im/JoinChat.svg)](https://gitter.im/bmsantos/cola-maven-plugin)

COLA Tests Gradle Plugin
========================

## Build

```shell
cd cola-plugin
gradle clean build uploadArchives
cd ..
cd consumer
gradle clean test -i 
```

## Usage

```gradle
buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath group: 'com.github.bmsantos', name: 'cola-tests', version: '0.0.3-SNAPSHOT'
        classpath group: 'com.github.bmsantos', name: 'cola-gradle-plugin', version: '0.0.3-SNAPSHOT'
    }
}

dependencies {
    // testCompile 'org.slf4j:slf4j-simple:1.7.7' // Optional - Can use other slf4j bridge.
    testCompile 'com.github.bmsantos:cola-tests:0.0.3-SNAPSHOT'
    testCompile 'junit:junit:4.+'
}

apply plugin: 'cola'
cola {
    ideBaseClass = 'org.gradle.cola.tests.TestBase'
    ideBaseClassTest = 'toBeRemoved'
    targetDirectory = compileTestJava.destinationDir
    includes = ['org/gradle/cola/tests/**'] 
    excludes = ['something/else/**', 'and/some/CompiledFile.class']
}
colac.dependsOn testClasses
colac.mustRunAfter testClasses

test {
    useJUnit()
    dependsOn colac
}
```
