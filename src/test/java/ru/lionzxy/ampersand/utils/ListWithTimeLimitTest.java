package ru.lionzxy.ampersand.utils;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ListWithTimeLimitTest extends TestCase {
    private ListWithTimeLimit<Integer> underTest = new ListWithTimeLimit(1000L);

    @After
    public void clear() {
        underTest.clear();
    }

    @Test
    public void testValidAdd() {
        underTest.add(1, 1L);
        underTest.add(50, 500L);
        underTest.add(60, 999L);
        assertEquals(underTest.size(), 3);
    }

    @Test
    public void testMoreAdd() {
        underTest.add(1, 1L);
        underTest.add(50, 500L);
        underTest.add(60, 999L);
        underTest.add(80, 1002L);
        assertEquals(underTest.size(), 3);
        assertEquals((int) underTest.get(0).snd, 50);
        assertEquals((int) underTest.get(1).snd, 60);
        assertEquals((int) underTest.get(2).snd, 80);
    }
    @Test
    public void testMoreAddAndEraseAll() {
        underTest.add(1, 1L);
        underTest.add(50, 500L);
        underTest.add(60, 999L);
        underTest.add(80, 10001L);
        assertEquals(underTest.size(), 1);
        assertEquals((int) underTest.get(0).snd, 80);
    }
}
