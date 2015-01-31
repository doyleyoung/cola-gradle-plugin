package com.github.bmsantos.gradle.cola.provider

import com.github.bmsantos.core.cola.provider.IColaProvider
import static com.github.bmsantos.core.cola.utils.ColaUtils.isSet
import static com.github.bmsantos.core.cola.utils.ColaUtils.CLASS_EXT
import static java.io.File.separator
import static java.lang.System.getProperties
import static java.util.Arrays.asList

import groovy.util.logging.Slf4j
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.List

import org.codehaus.plexus.util.DirectoryScanner

@Slf4j
class GradleColaProvider implements IColaProvider {
    
    private final def targetDirectory
    private final def classpathElements, deltas
    private final def includes, excludes

    GradleColaProvider(final String targetDirectory, final List<String> classpathElements,
        final String[] includes, final String[] excludes, final List<String> deltas) {

        this.targetDirectory = targetDirectory.endsWith(separator) ? targetDirectory : targetDirectory + separator
        this.classpathElements = classpathElements
        this.includes = includes
        this.excludes = excludes
        this.deltas = deltas
    }

    @Override
    String getTargetDirectory() {
        targetDirectory
    }

    @Override
    URLClassLoader getTargetClassLoader() throws Exception {
        final def urls = []

        classpathElements.each { path ->
            urls.add(new File(path).toURI().toURL())
        }

        new URLClassLoader(urls.toArray(new URL[urls.size()]), GradleColaProvider.class.getClassLoader())
    }

    @Override
    List<String> getTargetClasses() {
        if (isSet(deltas)) {
            return deltas
        }

        final def resolvedIncludes = resolveIncludes()

        final def scanner = new DirectoryScanner()
        if (isSet(resolvedIncludes)) {
            scanner.setIncludes(resolvedIncludes)
        }

        if (isSet(excludes)) {
            scanner.setExcludes(excludes)
        }

        scanner.setBasedir(targetDirectory)
        scanner.setCaseSensitive(true)
        scanner.scan()

        new ArrayList<String>(asList(scanner.getIncludedFiles()))
    }

    private String[] resolveIncludes() {
        final def list = []
        final def props = [ "test", "test.single", "it.test" ]
        props.each { prop ->
            def filter = getProperties().getProperty(prop)
            if (isSet(filter)) {
                list.add(filter.endsWith(CLASS_EXT) ? filter : filter + CLASS_EXT)
            }
        }

        if (!list.isEmpty()) {
            return list
        }

        if (isSet(includes)) {
            return includes
        }

        null
    }
}