/*

GamePane.java
Suraj Rampure

Contains everything related to actual gameplay.

*/

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GamePane extends JLayeredPane implements ActionListener, KeyListener {

    GameFrame gameFrame;
    Image classicBackground, textBackground, toppedBackground, winBackground, shadow18, shadow47, gridLines;
    CustomButton pauseButton, playClassicButton, playTextButton, quitButton, quitButton2, menuButton;
    javax.swing.Timer myTimer;

    final int EMPTY = 0;
    int linestoclear;
    int [][] board, fallingSpace, ghostSpace;
    int [][][] currentPiece, holdPiece;      // Current piece and hold piece
    int [][][][] queue;                      // Next 3 pieces to come in

    LinkedList <String> lastFourCommands;

    int board_x = 10, board_y = 15,
        px = board_x/2 -1, py = 0,
        linesCompleted, keyCode, dir; // Dir - Direction the piece is in (0, 1, 2, 3)

    long frameCount, highClassic, highText;

    boolean isDown, playing, musicFlag, ghostFlag, gridFlag, holdLock,
            toppedScreen, pauseScreen, winScreen;
    String mode, input, currentLine;
    HashMap <String, MethodInterface> methods;

    // Constructor method
    public GamePane (GameFrame g, String m) {

        gameFrame = g;
        mode = m;               // Mode is either "classic" or "text", for the two game modes\

        if (mode.equals("classic")) {
            linestoclear = 30;
        }

        else if (mode.equals("text")) {
            linestoclear = 20;
        }

        // Importing all images
        classicBackground = new ImageIcon("Images/Classic Background.png").getImage();
        textBackground = new ImageIcon("Images/Text Background.png").getImage();
        toppedBackground = new ImageIcon("Images/Topped Out.png").getImage();
        winBackground = new ImageIcon("Images/Win Background.png").getImage();
        shadow18 = new ImageIcon("Images/Shadow Block 18.png").getImage();
        shadow47 = new ImageIcon("Images/Shadow Block 47.png").getImage();
        gridLines = new ImageIcon("Images/Grid.png").getImage();

        // Setting up all custom buttons on the various screens
        pauseButton = new CustomButton("Pause");
        pauseButton.setBounds(12, 711, 475, 45);
        pauseButton.addActionListener(this);
        add(pauseButton);
        pauseButton.setVisible(false);

        playClassicButton = new CustomButton("Play Classic");
        playClassicButton.setBounds(337, 258, 359, 70);
        playClassicButton.addActionListener(this);
        add(playClassicButton, 0);
        playClassicButton.setVisible(false);

        playTextButton = new CustomButton("Play Text");
        playTextButton.setBounds(337, 384, 359, 70);
        playTextButton.addActionListener(this);
        add(playTextButton, 0);
        playTextButton.setVisible(false);

        quitButton = new CustomButton("Quit");
        quitButton.setBounds(337, 517, 359, 70);
        quitButton.addActionListener(this);
        add(quitButton, 0);
        quitButton.setVisible(false);

        menuButton = new CustomButton("Main Menu");
        menuButton.setBounds(129, 641, 359, 70);
        menuButton.addActionListener(this);
        add(menuButton, 0);
        menuButton.setVisible(false);

        quitButton2 = new CustomButton("Quit");
        quitButton2.setBounds(544, 641, 359, 70);
        quitButton2.addActionListener(this);
        add(quitButton2, 0);
        quitButton2.setVisible(false);

        myTimer = new javax.swing.Timer (5, this);
        myTimer.start();

        // board contains the pieces stuck to the board
        // fallingSpace contains the sole piece that is being controlled
        // ghostSpace contains the sole ghost piece
        board = new int [board_x][board_y];
        fallingSpace = new int [board_x][board_y];
        ghostSpace = new int [board_x][board_y];

        // linesCompleted starts at 0 and counts to either 20 or 30, based on the mode
        linesCompleted = 0;
        frameCount = 0;             // Used to control how often the pieces are drawn
        keyCode = 0;                // Used to control keyboard input (right arrow key, space, etc.)

        handleHighScores();         // Loads the current high scores into their variables

        lastFourCommands = new LinkedList <String> ();      // Stores the last four text commands for the text mode

        currentPiece = randomPiece();   // Sets the initial piece to a random one
        queue = null;
        holdPiece = null;

        updateQueue();                  // Adds three pieces to the queue

        isDown = false;                 // Ensures that there aren't repeat key events
        holdLock = true;                // Ensures the user doesn't use the hold function more than once per piece

        playing = true;                 // Flags for the various screens
        pauseScreen = false;
        toppedScreen = false;
        winScreen = false;

        updatePreferences();            // Loading the flag for the music, ghost and grid

        input = "";                     // String variables for the text mode
        currentLine = "";

        methods = new HashMap <String, MethodInterface> ();     // HashMap that maps string commands to methods
        loadMethods();                                          // Loads all of these said loadMethods

        addKeyListener(this);
        setVisible(true);
    }

    // addNotify
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void updatePreferences() {
        musicFlag = Textris.data.flags[0];
        ghostFlag = Textris.data.flags[1];
        gridFlag = Textris.data.flags[2];
    }

    // loads methods into the method hashmap
    // so that they can be called upon by controlTextInput() by the user's text input
    public void loadMethods() {

        // Move right and left
        for (int i = 0; i < board_x; i ++) {
            final int x = i;                    // i needs to be a final variable for this to work (probably very poor style)
            methods.put("r" + Integer.toString(i+1), new MethodInterface() { public void execute () { moveFallingPiece(x + 1, 0); }});
            methods.put("l" + Integer.toString(i+1), new MethodInterface() { public void execute () { moveFallingPiece(-(x+1), 0); }});
        }

        // This is what the above code does, for r1 to r9 and l1 to l9
        /*
        methods.put("r1", new MethodInterface() { public void execute () { moveFallingPiece(1, 0); }});
        methods.put("r2", new MethodInterface() { public void execute () { moveFallingPiece(2, 0); }});
        methods.put("r3", new MethodInterface() { public void execute () { moveFallingPiece(3, 0); }});
        */

        // Rotating right and left
        methods.put("rr", new MethodInterface() { public void execute () { rotate(1); }});
        methods.put("rl", new MethodInterface() { public void execute () { rotate(-1); }});

        // Qutting, holding
        methods.put("quit", new MethodInterface() { public void execute () { System.exit(0); }});
        methods.put("h", new MethodInterface() { public void execute () { hold(); }});

        methods.put("letmewin", new MethodInterface() { public void execute () { linesCompleted = linestoclear; }});
    }

    // Clears a grid (board, fallingSpace or ghostSpace)
    public void clearSpace (int [][] space) {
        for (int x = 0; x < board_x; x ++) {
            for (int y = 0; y < board_y; y ++) {
                space[x][y] = EMPTY;
            }
        }
    }

    // Generates a new random piece
    public int[][][] randomPiece() {
        int index = (int) (Math.random()*Piece.PIECES.length + 1);
        return Piece.PIECES[index -1];
    }

    // Converts a time in milliseconds to a standard time format for displaying
    public String milliToString (long n) {
        long second = (n/ 1000) % 60;
        long minute = (n/ (1000 * 60)) % 60;
        long hour = (n/ (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d", minute, second);
    }

    // Inserts a piece at rotation r into a space
    public void insertPiece (int [][] space, int [][][] piece, int r) {
        for (int x = 0; x < piece[r].length; x ++) {
            for (int y = 0; y < piece[r][0].length; y ++) {
                if (piece[r][x][y] != EMPTY) {
                    space[px + x][py + y] = piece[r][x][y];
                }
            }
        }
    }

    // Positions the ghost piece based on the number of lines
    // the falling piece can drop before a collision
    public void positionGhost () {
        int d_y = howManyDown();    // Finds the number of lines the piece can be dropped
        for (int x = 0; x < currentPiece[dir].length; x ++) {
            for (int y = 0; y < currentPiece[dir][0].length; y ++) {
                if (currentPiece[dir][x][y] != EMPTY) {
                    ghostSpace[px + x][py + y + d_y] = currentPiece[dir][x][y];
                }
            }
        }
    }

    public void updateQueue() {
        if (queue == null) {
            queue = new int[3][][][];
            for (int i = 0; i < 3; i ++) {
                queue[i] = randomPiece();
            }
        }

        else {
            queue[0] = queue[1];
            queue[1] = queue[2];
            queue[2] = randomPiece();
        }
    }

    // Places the falling piece on the board
    // and generates a new falling piece
    public void stickOnBoard () {
        insertPiece(board, currentPiece, dir);
        newPiece();
    }

    // Generates a new piece
    public void newPiece() {
        currentPiece = queue[0];
        px = board_x/2 -1;
        py = 0;
        dir = 0;
        holdLock = true;                    // So that the user can use the hold function again
        updateQueue();
    }

    // Moves the falling piece d_x units horizontally
    // and d_y units vertically
    public void moveFallingPiece (int d_x, int d_y) {
        if (canMoveHorizontal(d_x)) {
            px += d_x;
        }

        if (canMoveLower(d_y)) {
            py += d_y;
        }

        clearSpace(fallingSpace);
        clearSpace(ghostSpace);
        insertPiece(fallingSpace, currentPiece, dir);
    }

    // Controls user input for the classic mode
    public void controlClassicInput() {
        if (isDown) {
            // Space bar - hard drop
            if (keyCode == KeyEvent.VK_SPACE) {
                moveFallingPiece(0, howManyDown());
            }

            // Left arrow key - move left
            else if (keyCode == KeyEvent.VK_LEFT) {
                moveFallingPiece(-1, 0);
            }

            // Up arrow key - rotate
            else if (keyCode == KeyEvent.VK_UP) {
                rotate(1);
            }

            // Right arrow key - move right
            else if (keyCode == KeyEvent.VK_RIGHT) {
                moveFallingPiece(1, 0);
            }

            // Down arrow key - move down
            else if (keyCode == KeyEvent.VK_DOWN) {
                moveFallingPiece(0, 1);
            }

            else if (keyCode == KeyEvent.VK_SHIFT) {
                hold();
            }

            isDown = false;
        }
    }

    // Controls user input for the text based mode
    public void controlTextInput() {
        if (isDown) {
            currentLine += input;

            if (keyCode == KeyEvent.VK_BACK_SPACE) {
                if (currentLine.length() > 1) {
                    currentLine = currentLine.substring(0, currentLine.length()-2);
                }
            }

            if (methods.containsKey(currentLine.trim())) {
                methods.get(currentLine.trim().toLowerCase()).execute();
                currentLine = "";
            }

            if (keyCode == KeyEvent.VK_ENTER) {
                moveFallingPiece(0, howManyDown());
                currentLine = "";
            }


            isDown = false;
        }
    }

    // A "control input" method to be called by GameFrame
    public void controlInput() {
        if (mode.equals("classic")) {
            controlClassicInput();
        }

        else if (mode.equals("text")) {
            controlTextInput();
        }
    }

    // Returns true if the falling piece can move down d_y units
    // Returns false otherwise
    public boolean canMoveLower (int d_y) {
        for (int x = 0; x < currentPiece[dir].length; x++) {
            for (int y = 0; y < currentPiece[dir][0].length; y++) {
                if (currentPiece[dir][x][y] != EMPTY) {
                    if (px + x >= 0 && px + x < board_x && py + y >= 0 && py + y < board_y) {
                        if (py + y + d_y >= board_y) {
                            return false;
                        }

                        if (board[px + x][py + y + d_y] != EMPTY) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    // Returns true if the falling piece can move horizontally d_y units
    // Returns false otherwise
    public boolean canMoveHorizontal (int d_x) {
        for (int x = 0; x < currentPiece[dir].length; x ++) {
            for (int y = 0; y < currentPiece[dir][0].length; y ++) {
                if (currentPiece[dir][x][y] != EMPTY) {
                    if (px + x >= 0 && px + x < board_x && py + y >= 0 && py + y < board_y) {
                        if (px + x + d_x >= board_x || px + x + d_x < 0) {
                            return false;
                        }

                        if (board[px + x + d_x][py + y] != EMPTY) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    // Finds the number of vertical lines the falling piece
    // can drop before having to be stuck to the board
    // This is used for both the hard slam and the ghost piece
    public int howManyDown () {
        int count = 0;
        if (! canMoveLower(1)) {
            return 0;
        }

        else {
            boolean flag = true;
            while (flag) {
                count ++;
                if (! canMoveLower(count + 1)) {
                    flag = false;
                }
            }
        }

        return count;
    }

    // Rotates the falling piece n times
    public void rotate (int n) {
        if (canRotate(n)) {
            clearSpace(fallingSpace);
            clearSpace(ghostSpace);
            dir = (dir + n) % 4;
        }
    }

    // Checks if the rotation is possible
    public boolean canRotate (int n) {
        int [][] tmp = currentPiece[(dir + n) % 4];
        for (int x = 0; x < tmp.length; x ++) {
            for (int y = 0; y < tmp[0].length; y ++) {
                if (tmp[x][y] != EMPTY && (px + x < 0 || px + x >= board_x || py + y < 0 || py + y >= board_y)) {
                    return false;
                }
            }
        }

        return true;
    }

    // Sends the falling piece to the hold block
    // And sets the falling piece to the hold piece
    public void hold () {
        if (holdLock) {
            if (holdPiece == null) {
                holdPiece = currentPiece;
                currentPiece = randomPiece();
                dir = 0;
            }

            else {
                holdLock = false;
                int [][][] tmp = currentPiece;
                currentPiece = holdPiece;
                holdPiece = tmp;
            }
            px = board_x/2 - 1;
            py = 0;
            clearSpace(fallingSpace);
            clearSpace(ghostSpace);
            holdLock = false;
        }
    }

    // Returns the lines that are full and require deletion
    public ArrayList <Integer> getFullLines () {
        int count = 0;
        ArrayList <Integer> lines = new ArrayList <Integer> ();
        for (int y = board_y - 1; y > -1; y --) {                // Starts at the bottom row so that [lines] is sorted from bottom to top
            count = 0;
            for (int x = 0; x < board_x; x ++) {
                if (board[x][y] != EMPTY) {
                    count ++;
                }
            }

            if (count == board_x) {
                lines.add(y);
            }
        }

        return lines;
    }

    // Clears the full lines
    public void clearFullLines (ArrayList <Integer> lines) {
        for (int i = 0; i < lines.size(); i ++) {
            int y = lines.get(i) + i;

            // Clears the row
            for (int x = 0; x < board_x; x ++) {
                board[x][y] = EMPTY;
                for (int j = y; j > 0; j --) {
                    board[x][j] = board[x][j-1];
                }
            }
        }
        linesCompleted += lines.size();
    }

    public void updateState () {
        isPlaying();
        if (playing) {
            //pauseButton.setVisible(true);
            insertPiece(fallingSpace, currentPiece, dir);
            positionGhost();

            // If we can move the piece the incremental one step down
            // we do, but otherwise we stick it on the board
                // and generate a new falling piece
            int mod = -1;

            // Pieces fall quicker in the classic mode
            if (mode.equals("classic")) {
                mod = 100 - linesCompleted;
            }
            else if (mode.equals("text")) {
                mod = 300 - linesCompleted*2;
            }

            if (frameCount % mod == 0) {
                if (canMoveLower(1)) {              // The incremental one piece drop
                    moveFallingPiece(0, 1);
                }
                else {
                    stickOnBoard();
                }
            }

            // Clears the full lines
            clearFullLines(getFullLines());
        }

        else {
            pauseButton.setVisible(false);
        }
    }

    // Checks if the user has topped out
    public boolean toppedOut () {
        for (int x = 0; x < board_x; x++) {
            if (board[x][0] != EMPTY) {
                return true;
            }
        }
        return false;
    }

    public void isPlaying () {
        if (toppedOut()) {
            playing = false;
            toppedScreen = true;
        }

        if (linesCompleted >= linestoclear) {
            playing = false;
            winScreen = true;
            handleHighScores();
        }
    }

    // ALL DRAWING METHODS
    /******/
    // Draws everything
    public void paintComponent (Graphics g) {
        if (playing) {
            if (mode.equals("classic")) {
                g.drawImage(classicBackground, 0, 0, this);
            }

            else if (mode.equals("text")) {
                g.drawImage(textBackground, 0, 0, this);
            }

            if (gridFlag) {
                g.drawImage(gridLines, 511, 23, this);
            }

            drawSpace(g, board);

            if (ghostFlag) {
                drawSpace(g, ghostSpace);
            }

            drawSpace(g, fallingSpace);

            if (holdPiece != null) {
                drawHold(g);
            }

            drawQueue(g);

            g.setColor(Color.WHITE);

            drawInfo(g);

            if (mode.equals("text")) {
                drawTextController(g);
            }
        }

        else if (toppedScreen) {
            g.drawImage(toppedBackground, 0, 0, this);
            playClassicButton.setVisible(true);
            playTextButton.setVisible(true);
            quitButton.setVisible(true);
        }

        else if (winScreen) {
            g.drawImage(winBackground, 0, 0, this);
            quitButton2.setVisible(true);
            menuButton.setVisible(true);
            drawScores(g);
        }
    }

    // Draws a given space on the game grid (board, fallingSpace or ghostSpace)
    public void drawSpace (Graphics g, int [][] space) {
        for (int x = 0; x < board_x; x ++) {
            for (int y = 0; y < board_y; y ++) {
                if (space[x][y] != EMPTY) {
                    if (space == ghostSpace) {
                        g.setColor(Piece.COLORS1[7]);
                    }
                    else {
                        g.setColor(Piece.COLORS1[space[x][y] -1]);
                        g.drawImage(shadow47, 505 + 48*x, 20 + 48*y, this);
                    }
                    g.fillRect(512 + 48*x, 25 + 48*y, 47, 47);
                }
            }
        }
    }

    // Draws the hold grid at the left side
    public void drawHold (Graphics g) {
        for (int x = 0; x < holdPiece[0].length; x++) {
            for (int y = 0; y < holdPiece[0][0].length; y++) {
                if (holdPiece[0][x][y] != EMPTY) {
                    g.setColor(Piece.COLORS1[holdPiece[0][x][y] -1]);

                    // The location of the hold piece is slightly different in the two different modes
                    if (mode.equals("classic")) {
                        g.drawImage(shadow18, 35 + 20*(x+1), 407 + 20*y, this);
                        g.fillRect(41 + 20*(x+1), 410 + 20*y, 18, 18);
                    }

                    else if (mode.equals("text")) {
                        g.drawImage(shadow18, 35 + 20*(x+1), 329 + 20*y, this);
                        g.fillRect(41 + 20*(x+1), 332 + 20*y, 18, 18);
                    }

                }
            }
        }
    }

    // Draws the queue
    public void drawQueue (Graphics g) {
        int [] displacements = {181, 281, 381};
        for (int i = 0; i < 3; i ++) {
            for (int x = 0; x < queue[i][0].length; x ++) {
                for (int y = 0; y < queue[i][0][0].length; y ++) {
                    if (queue[i][0][x][y] != EMPTY) {
                        g.setColor(Piece.COLORS1[queue[i][0][x][y] -1]);

                        // It draws at x+1 just because it looks nicer
                        if (mode.equals("classic")) {
                            g.drawImage(shadow18, displacements[i] -6 + 20*(x+1), 407 + 20*y, this);
                            g.fillRect(displacements[i] + 20*(x+1), 410 + 20*y, 18, 18);
                        }

                        else if (mode.equals("text")) {
                            g.drawImage(shadow18, displacements[i] -6 + 20*(x+1), 329 + 20*y, this);
                            g.fillRect(displacements[i] + 20*(x+1), 332 + 20*y, 18, 18);
                        }

                    }
                }
            }
        }
    }

    // Draws the text for the number of lines completed and the elapsed time
    public void drawInfo (Graphics g) {

        String timeString = milliToString(frameCount*myTimer.getDelay());

        // Drawing the number of lines completed
        if (mode.equals("classic")) {
            g.setFont(new Font ("Segoe UI", Font.PLAIN, 72));
            g.drawString(Integer.toString(linestoclear - linesCompleted), 45, 305);
            g.drawString(timeString, 250, 305);
        }

        else if (mode.equals("text")) {
            g.setFont(new Font ("Segoe UI", Font.PLAIN, 36));
            g.drawString(Integer.toString(linestoclear - linesCompleted), 157, 269);
            g.drawString(timeString, 250, 269);

        }
    }

    // Draws the user's achieved score and the two high scores
    public void drawScores (Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font ("Segoe UI", Font.PLAIN, 64));
        g.drawString(milliToString(highClassic), 220, 503);
        g.drawString(milliToString(highText), 640, 503);

        g.setFont(new Font ("Segoe UI", Font.PLAIN, 96));
        g.drawString(milliToString(frameCount*myTimer.getDelay()), 400, 315);
    }

    // Draws the text control area (for the text mode only)
    public void drawTextController (Graphics g) {
        g.setFont(new Font("Courier New", Font.PLAIN, 18));
        g.drawString(">>", 37, 540);
        g.drawString(currentLine, 64, 540);
    }

    /******/

    // Sets up a new game (simply calls a new GameFrame)
    public void newGame (String s) {
        Point p = gameFrame.getLocationOnScreen();
        gameFrame.setVisible(false);
        setVisible(false);
        new GameFrame(p, s);
    }

    // Checks if the user beat the current high score in their game mode
    // If they did, this writes their new score to the "data.txt" file
    public void handleHighScores() {
        File f = new File ("data.txt");
        Scanner sc = null;
        try {
            sc = new Scanner (f);
        }
        catch (IOException ex) {
            System.out.println(ex);
        }

        highClassic = Long.parseLong(sc.nextLine());
        highText = Long.parseLong(sc.nextLine());

        if (mode.equals("classic")) {
            if (frameCount*myTimer.getDelay() > highClassic) {
                try {
                    PrintWriter writer = new PrintWriter("data.txt", "UTF-8");
                    writer.println(Long.toString(frameCount*myTimer.getDelay()));
                    writer.println(highText);
                    writer.close();
                }
                catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    System.out.println(ex);
                }
            }
        }

        if (mode.equals("text")) {
            if (frameCount*myTimer.getDelay() > highText) {
                try {
                    PrintWriter writer = new PrintWriter("data.txt", "UTF-8");
                    writer.println(highClassic);
                    writer.println(Long.toString(frameCount*myTimer.getDelay()));
                    writer.close();
                }
                catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    // ActionListener implementation
    public void actionPerformed (ActionEvent evt) {

        Object src = evt.getSource();

        // Game loop
        if (playing) {
            if (src == myTimer) {
                frameCount ++;

                controlInput();

                updateState();

                repaint();
            }
        }

        // Buttons that are visible when the player tops out or finishes the game
        if (src == quitButton || src == quitButton2) {
            System.exit(0);
        }

        // Sets up new games from the screen where the user topped out
        else if (src == playClassicButton) {
            newGame("classic");
        }

        else if (src == playTextButton) {
            newGame("text");
        }

        // Reroutes to the main menu from the screen where the user finished their game
        else if (src == menuButton) {
            Point p = gameFrame.getLocationOnScreen();
            gameFrame.setVisible(false);
            setVisible(false);
            new IntroFrame(p);
        }
    }

    // KeyListener implementation
    public void keyTyped(KeyEvent evt) {
        if (mode.equals("text")) {
            input = "" + evt.getKeyChar();
            isDown = true;
            controlTextInput();
        }
    }

    public void keyPressed(KeyEvent evt) {
        keyCode = evt.getKeyCode();             // Classic mode
        isDown = true;
    }

    public void keyReleased(KeyEvent evt) {
        isDown = false;
    }

}
