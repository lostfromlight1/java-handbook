package com.handbook.jdbc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SumPositiveTest {

    static int sumPositive(int[] values) {
        int total = 0;
        for (int v : values) {
            if (v > 0) {
                total += v;
            }
        }
        return total;
    }

    @Test
    void sumsOnlyPositiveValues() {
        assertEquals(6, sumPositive(new int[] {1, -1, 2, 3}));
    }
}
