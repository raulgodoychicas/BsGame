package appworld.gogogo.bsgame.engine;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;

import appworld.gogogo.bsgame.objects.LineOnCanvas;

/**
 * Created by Raul on 01.11.2016.
 */

public class Ki {

    public static int simulateKiMove(LineOnCanvas[] linesOnCanvas) {
        int randomRectNumber;

        ArrayList<Integer> availableRectNumbers = new ArrayList<>();

        int v = 0;
        for (int i = 0; i < linesOnCanvas.length; i++) {
            if (linesOnCanvas[i].player == -1) {
                availableRectNumbers.add(v, i);
                v++;
            }
        }

        if (!availableRectNumbers.isEmpty()) {
            randomRectNumber = (int) (Math.random() * availableRectNumbers.size());
            randomRectNumber = availableRectNumbers.get(randomRectNumber);

            while (linesOnCanvas[randomRectNumber].player != -1) {
                randomRectNumber = (int) (Math.random() * availableRectNumbers.size());
            }
            return randomRectNumber;
        } else {
            return -1;
        }
    }
}