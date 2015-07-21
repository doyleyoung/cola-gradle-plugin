package org.gradle.cola.tests;

import static java.lang.System.err;

import com.github.bmsantos.core.cola.story.annotations.IdeEnabler;
import org.junit.Test;

public class BaseTest {

    @IdeEnabler
    @Test
    public void toBeRemoved() {
        err.println("COLA Tests: this test will not execute.");
    }
}
