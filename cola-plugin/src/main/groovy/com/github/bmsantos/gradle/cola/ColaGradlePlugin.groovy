package com.github.bmsantos.gradle.cola

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.Plugin

class ColaGradlePlugin implements Plugin<Project> {
    
    void apply(Project project) {
        project.extensions.create('cola', ColaConfig)
        project.task('colac', type: ColaCompileTask, description: 'Cola Tests Compiler Task')
    }
}