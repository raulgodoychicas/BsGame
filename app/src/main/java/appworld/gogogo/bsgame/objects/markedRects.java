package appworld.gogogo.bsgame.objects;

/**
 * Created by Raul on 08.02.2017.
 * Mark the different rectangles and set the Player to know
 */
public class markedRects {
    public boolean marked;
    public int player;

    public markedRects(boolean marked, int player) {
        this.marked = marked;
        this.player = player;
    }
}
