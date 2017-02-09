package appworld.gogogo.bsgame.objects;

/**
 * Created by Raul on 08.02.2017.
 * Object class to identify all of the Lines
 */
public class LineOnCanvas {
    public int startX;
    public int startY;
    public int stopX;
    public int stopY;
    public int player;

    public LineOnCanvas(int startX, int startY, int stopX, int stopY, int player) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
        this.player = player;
    }
}
