package com.github.bmsantos.gradle.cola

import com.github.bmsantos.gradle.cola.provider.GradleColaProvider
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.artifacts.ProjectDependency

import com.github.bmsantos.core.cola.main.ColaMain

class ColaCompileTask extends DefaultTask {

    @TaskAction
    def colac() {
        def classpathElements = []

        project.sourceSets.each { i ->
            String path = (i.output.classesDir as File).absolutePath
            classpathElements.add(path)
        }

        project.configurations.testRuntime.resolve().each { i ->
            classpathElements.add(i.absolutePath)
        }

        def provider = new GradleColaProvider(project.cola.targetDirectory, classpathElements, project.cola.includes, project.cola.excludes, null)

        def main = new ColaMain(project.cola.ideBaseClass, project.cola.ideBaseClassTest)
        main.execute(provider)
    }
}