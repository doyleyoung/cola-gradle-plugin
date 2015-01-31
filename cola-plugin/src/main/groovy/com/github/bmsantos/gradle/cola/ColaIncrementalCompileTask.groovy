package com.github.bmsantos.gradle.cola

import com.github.bmsantos.gradle.cola.provider.GradleColaProvider

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.artifacts.ProjectDependency

import com.github.bmsantos.core.cola.main.ColaMain

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING

class ColaIncrementalCompileTask extends AbstractColaCompileTask {

    @InputDirectory
    def File inputDir

    @OutputDirectory
    def File destinationDir

    @TaskAction
    def icolac(IncrementalTaskInputs inputs) {
        logger.info("COLA Tests - Input directory: " + inputDir.absolutePath)
        logger.info("COLA Tests - Output directory: " + destinationDir.absolutePath)

        // Fix test task classpath (this should be done automatically by the test task
        // since the destinationDir is modified through configuration.
        def cp = project.sourceSets.test.runtimeClasspath
        def filteredCp = project.files(destinationDir).plus(cp.filter(){ f -> !f.equals(inputDir) })
        project.sourceSets.test.runtimeClasspath = filteredCp

        def deltas = null
        if (inputs.incremental) {
            logger.info("COLA Tests - Out of date inputs...")
            deltas = findDeltas(inputs)
        } else {
            logger.info("COLA Tests - All inputs considered out of date. Checking all tests...")
            prepareFirstRun()
        }
        executeTask(destinationDir.absolutePath, deltas)
    }

    def findDeltas(IncrementalTaskInputs inputs) {
        def deltas = []
        inputs.outOfDate { change ->
            def source = Paths.get(change.file.absolutePath)
            def (destination, classPath) = prepareDestination(change.file.absolutePath)

            Files.copy(source, destination, COPY_ATTRIBUTES, REPLACE_EXISTING)
            deltas.add(classPath)
        }
        return deltas
    }

    def prepareDestination(String sourceClassPath) {
        def classPath = sourceClassPath.replace(inputDir.absolutePath + File.separator, "")
        def destination = Paths.get(destinationDir.absolutePath, classPath)
        Files.createDirectories(Paths.get(new File(destinationDir.absolutePath + File.separator + classPath).getParent()))
        return [destination, classPath]
    }

    private void prepareFirstRun() {
        destinationDir.mkdir()
        new AntBuilder().copy( todir: destinationDir.absolutePath ) {
            fileset( dir: inputDir.absolutePath )
        }
    }
}