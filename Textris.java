/*

Textris.java
Suraj Rampure

Textris is based off of the wildly popular Tetris.
It features two modes - a classic Tetris mode where the user
has to clear 30 lines as quicky as possible, and a new TEXT BASED MODE
where they use intuitive text based commnands to control the falling tetromino.

Build and run this program to play the game.

*/

import java.awt.*;

public class Textris {

    // Storage for the options in the options pane
    static DataStorage data = new DataStorage();

    public static void main (String [] args) {

        int app_width = 1024, app_height = 768;

        // Positions the frame in the middle of the user's screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locx = ((int)screenSize.getWidth() - app_width)/2;
        int locy = ((int)screenSize.getHeight() - app_height)/2;

        new IntroFrame (new Point (locx, locy));
    }
}