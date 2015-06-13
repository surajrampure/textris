/*

DataStorage.java
Suraj Rampure

This class holds the options in the settings menu
And updates then in GamePanel once it is initialized


*/

import java.util.*;
import java.awt.*;

public class DataStorage {

    boolean [] flags = {true, true, true};
    Color [] colors = Piece.COLORS1;
    Color [] defaultColors = Piece.COLORS1;

    public DataStorage () {
    }

    public void updateColours (Color [] ar) {
        colors = ar;
    }

    public void updateFlags (boolean [] ar) {
        flags = ar;
    }
}