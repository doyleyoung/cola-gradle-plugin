Questions? [![Join Chat](https://badges.gitter.im/JoinChat.svg)](https://gitter.im/bmsantos/cola-maven-plugin)

COLA Tests Gradle Plugin
========================

COLA Tests [Web Site](http://bmsantos.github.io/cola-maven-plugin/)

COLA Tests [Wiki](https://github.com/bmsantos/cola-tests/wiki)

## Build cola-gradle-plugin

```shell
cd cola-plugin
gradle clean build uploadArchives
```

## Build usage example: non-incremental build

```shell
cd consumer
gradle clean test -i 
```

## Build usage example: incremental build

```shell
cd incremental-consumer
gradle clean test -i 
```

## Usage (non-incremental)

```gradle
buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath group: 'com.github.bmsantos', name: 'cola-tests', version: '0.1.0-SNAPSHOT'
        classpath group: 'com.github.bmsantos', name: 'cola-gradle-plugin', version: '0.1.0-SNAPSHOT'
    }
}

dependencies {
    // testCompile 'org.slf4j:slf4j-simple:1.7.7' // Optional - Can use other slf4j bridge.
    testCompile 'com.github.bmsantos:cola-tests:0.1.0-SNAPSHOT'
    testCompile 'junit:junit:4.+'
}

apply plugin: 'cola'
cola {
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

## Incremental Usage

```gradle
apply plugin: 'java'
compileJava {
    options.incremental = true
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath 'com.github.bmsantos:cola-tests:0.1.0-SNAPSHOT'
        classpath 'com.github.bmsantos:cola-gradle-plugin:0.1.0-SNAPSHOT'
    }
}

dependencies {
    compile 'commons-collections:commons-collections:3.2'
    // testCompile 'org.slf4j:slf4j-simple:1.7.7' // Optional - Can use other slf4j bridge.
    testCompile 'com.github.bmsantos:cola-tests:0.1.0-SNAPSHOT'
    testCompile 'junit:junit:4.+'
}

apply plugin: 'cola'
cola {
    includes = ['org/gradle/cola/tests/**'] 
    excludes = ['something/else/**', 'and/some/CompiledFile.class']
}
icolac.inputDir = file(compileTestJava.destinationDir)
icolac.destinationDir = file("${project.buildDir}/colac")
icolac.dependsOn testClasses
icolac.mustRunAfter testClasses

task(fixTestClassPath) << {
    // Fix test task classpath (this should be done automatically by the test task
    // since the destinationDir is modified through configuration.
    def cp = sourceSets.test.runtimeClasspath
    def filteredCp = files(test.testClassesDir).plus(cp.filter() { f -> !f.equals(icolac.inputDir) })
    sourceSets.test.runtimeClasspath = filteredCp
}
fixTestClassPath.mustRunAfter icolac

test {
    useJUnit()
    dependsOn icolac
    dependsOn fixTestClassPath
    testClassesDir = icolac.destinationDir
}
```
