package com.github.bmsantos.gradle.cola.provider

import org.junit.Before
import org.junit.Test
import static com.github.bmsantos.maven.cola.utils.ColaUtils.CLASS_EXT
import static com.github.bmsantos.maven.cola.utils.ColaUtils.binaryToOsClass
import static com.github.bmsantos.maven.cola.utils.ColaUtils.toOSPath
import static java.lang.System.getProperties;
import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.*
import static org.hamcrest.Matchers.*

class GradleColaProviderTest {

    private final String targetDirectory = toOSPath("build/classes/test/");
    private List<String> classpathElements, deltas;
    private String[] includes, excludes;

    private GradleColaProvider uut;

    @Before
    void setUp() {
        uut = new GradleColaProvider(targetDirectory, classpathElements, includes, excludes, deltas)
    }

    @Test
    void shouldReturnTargetDirectory() {
        // Then
        assertThat uut.targetDirectory, is(targetDirectory)
    }

    @Test
    void shouldReturnNormalizedTargetDirectory() {
        // When
        uut = new GradleColaProvider(toOSPath("build/classes/test"), classpathElements, includes, excludes, deltas);

        // Then
        assertThat uut.getTargetDirectory(), is(targetDirectory)
    }

    @Test
    void shoudGetTargetCloassLoader() throws Exception {
        // When
        def loader = uut.getTargetClassLoader()

        // Then
        assertThat loader, notNullValue()
    }

    @Test
    void shouldLoadCurrentTest() throws Exception {
        // Given
        def binaryName = getClass().getCanonicalName()
        def loader = uut.getTargetClassLoader()

        // When
        def clazz = loader.loadClass(binaryName)

        // Then
        assertThat clazz, notNullValue()
    }

    @Test
    void shouldGetTargetClasses() {
        // When
        def classes = uut.getTargetClasses()

        // Then
        assertThat classes.isEmpty(), is(false)
        assertThat classes, hasItem(binaryToOsClass(getClass().getCanonicalName()))
    }

    @Test
    void shouldFilterByTestSystemProperty() {
        // Given
        getProperties().setProperty("test", "**/*" + getClass().getSimpleName() + CLASS_EXT)

        // When
        def classes = uut.getTargetClasses()

        // Then
        assertThat classes.isEmpty(), is(false)
        assertThat classes, contains(binaryToOsClass(getClass().getCanonicalName()))
    }

    @Test
    void shouldFilterByIntegrationTestSystemProperty() {
        // Given
        getProperties().setProperty("it.test", "**/*" + getClass().getSimpleName() + CLASS_EXT)

        // When
        def classes = uut.getTargetClasses()

        // Then
        assertThat classes.isEmpty(), is(false)
        assertThat classes, contains(binaryToOsClass(getClass().getCanonicalName()))
    }

    @Test
    void shouldGetDeltas() {
        // Given
        deltas = [ targetDirectory ]

        uut = new GradleColaProvider(targetDirectory, classpathElements, includes, excludes, deltas)

        // When
        def classes = uut.getTargetClasses()

        // Then
        assertThat classes, is(deltas)
    }
}