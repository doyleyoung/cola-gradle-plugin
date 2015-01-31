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

class ColaCompileTask extends AbstractColaCompileTask {

	@TaskAction
	def colac() {
		executeTask(project.cola.targetDirectory, null)
	}
}