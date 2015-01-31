package org.gradle.cola.tests;

import static java.lang.System.err;

import org.junit.Test;

public class TestBase {

    @Test
    public void toBeRemoved() {
        err.println("COLA Tests: this test will not execute.");
    }
}
