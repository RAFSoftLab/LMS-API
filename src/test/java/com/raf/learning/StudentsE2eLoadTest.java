package com.raf.learning;

import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.jupiter.extension.ParallelLoadExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@LoadWith("load_generation.properties")
@ExtendWith({ParallelLoadExtension.class})
public class StudentsE2eLoadTest {
    @Test
    @DisplayName("Test parallel load for students api")
    @TestMapping(testClass = StudentsE2ETests.class, testMethod = "testRafStudentsGetApi")
    public void testLoad() {
        // This space remains empty
    }
}
