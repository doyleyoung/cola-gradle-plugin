package com.github.bmsantos.gradle.cola

import org.junit.Before
import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import org.gradle.api.Task
import static org.hamcrest.CoreMatchers.*
import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

class ColaGradlePluginTest {

    def project

    @Before
    void setUp() {
        // When
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'cola'
    }

    @Test
    void shouldAddPlugin() {
        // Then
        assertThat project.plugins.hasPlugin(ColaGradlePlugin), is(true)
    }
    
    @Test
    void shouldAddColaCompilerTaskToProject() {
        // Then
        assertThat project.tasks.colac, isA(ColaCompileTask.class)
    }

    @Test
    void shouldSetTaskDescription() {
        // Then
        assertThat project.tasks.colac.description, is('Cola Tests Compiler Task')
    }
}