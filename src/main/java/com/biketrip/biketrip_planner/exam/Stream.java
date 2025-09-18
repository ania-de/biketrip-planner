package com.biketrip.biketrip_planner.exam;

import java.util.Arrays;
import java.util.List;

public class Stream {
        public static void main(String[] args) {
            int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
         Arrays.stream(numbers)
                    .filter(n-> n% 2 == 0)
            .map(n->n * 2 )
                 .sum();

        }
    }
