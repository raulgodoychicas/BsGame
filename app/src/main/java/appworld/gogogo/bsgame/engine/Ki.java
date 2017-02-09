package appworld.gogogo.bsgame.engine;

import appworld.gogogo.bsgame.objects.LineOnCanvas;

/**
 * Created by Raul on 01.11.2016.
 */

public class Ki {

    public static int simulateKiMove(LineOnCanvas[] linesOnCanvas) {
        //TODO: test if -1 is needed
        int numberOfRects = linesOnCanvas.length - 1;
        int randomRectNumber = (int) (Math.random() * numberOfRects);
        boolean areLinesFree = false;

        for (LineOnCanvas lineOnCanvas : linesOnCanvas) {
            if (lineOnCanvas.player == -1) {
                areLinesFree = true;
            }
        }
        if (areLinesFree) {
            while (linesOnCanvas[randomRectNumber].player != -1) {
                randomRectNumber = (int) (Math.random() * numberOfRects);
            }
            return randomRectNumber;
        } else {
            return -1;
        }

    }

}