package appworld.gogogo.bsgame.interfaces;

import appworld.gogogo.bsgame.objects.markedRects;

/**
 * Created by Raul on 15.12.2016.
 */

public interface PlayerListener {
    void changePlayer(int player);
    void changeScore(markedRects[] markedRects);
    void onGameFinished();
}
