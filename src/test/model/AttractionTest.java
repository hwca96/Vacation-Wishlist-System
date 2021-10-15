package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttractionTest {

    private Attraction testAttraction;

    @BeforeEach
    void runBefore () {
        testAttraction = new Attraction("test");
    }

    @Test
    void markCompletedTest() {
        assertFalse(testAttraction.isCompleted());
        testAttraction.markCompleted();
        assertTrue(testAttraction.isCompleted());
    }

    @Test
    void markNotCompletedTest() {
        testAttraction.markCompleted();
        assertTrue(testAttraction.isCompleted());
        testAttraction.markNotCompleted();
        assertFalse(testAttraction.isCompleted());
    }

    @Test
    void changePriorityTest() {
        for (int i=-5; i<=10; i++) {
            if (i >= 0 && i <= 5) {
                assertTrue(testAttraction.changePriority(i));
            } else {

                assertFalse(testAttraction.changePriority(i));
            }
        }
    }

    @Test
    void addCommentTest() {
        for (int i=0; i<=5; i++) {
            assertEquals(i, testAttraction.getComments().size());
            testAttraction.addComment("test");
        }
    }

    @Test
    void removeCommentTest() {
        for (int i=0; i<=5; i++) {
            testAttraction.addComment("test" + i);
        }
        //Beginning
        testAttraction.removeComment(1);
        assertEquals("test1", testAttraction.getComments().get(0));
        assertEquals(5, testAttraction.getComments().size());

        //End
        testAttraction.removeComment(5);
        assertEquals("test4", testAttraction.getComments().get(3));
        assertEquals(4, testAttraction.getComments().size());

        //Middle
        testAttraction.removeComment(2);
        assertEquals("test3", testAttraction.getComments().get(1));
        assertEquals(3, testAttraction.getComments().size());

        //Not in Range
        testAttraction.removeComment(100);
        assertEquals("test3", testAttraction.getComments().get(1));
        assertEquals(3, testAttraction.getComments().size());

    }
}
