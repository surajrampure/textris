/*

IntroPane.java
Suraj Rampure

Controls the introduction screen. Allows the user to choose between
playing one of the two game modes or getting instructions or choosing from a few options.

*/

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class IntroPane extends JLayeredPane implements ActionListener {

    Image background, text;
    IntroFrame introFrame;
    CustomButton playClassicButton, playTextButton, instructionsButton, optionsButton;
    javax.swing.Timer myTimer;
    int logoIn;

    public IntroPane (IntroFrame introFrame) {
        this.introFrame = introFrame;

        background = new ImageIcon("Images/Background.png").getImage();
        text = new ImageIcon("Images/Front Text.png").getImage();           // Says "TEXTRIS"

        myTimer = new javax.swing.Timer(500, this);
        myTimer.start();

        logoIn = -2;                    // The TEXTRIS text falls in like a block, this specifies its y value

        // Creating each of the custom buttons and positioning them on the screen
        playClassicButton = new CustomButton("Play Classic");
        playClassicButton.setBounds(125, 510, 359, 70);
        playClassicButton.addActionListener(this);
        add(playClassicButton, 0);

        playTextButton = new CustomButton("Play Text");
        playTextButton.setBounds(540, 510, 359, 70);
        playTextButton.addActionListener(this);
        add(playTextButton, 0);

        instructionsButton = new CustomButton("Instructions");
        instructionsButton.setBounds(125, 638, 359, 70);
        instructionsButton.addActionListener(this);
        add(instructionsButton, 0);

        optionsButton = new CustomButton("Options");
        optionsButton.setBounds(540, 638, 359, 70);
        optionsButton.addActionListener(this);
        add(optionsButton, 0);

        setVisible(true);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void actionPerformed (ActionEvent evt) {
        Object src = evt.getSource();

        if (src == myTimer) {
            repaint();
        }

        // Sets up a classic game session if they click play classic, text if text
        if (src == playClassicButton) {
            Point p = introFrame.getLocationOnScreen();
            introFrame.setVisible(false);
            new GameFrame(p, "classic");
        }

        else if (src == playTextButton) {
            Point p = introFrame.getLocationOnScreen();
            introFrame.setVisible(false);
            new GameFrame(p, "text");
        }

        else if (src == optionsButton) {
            Point p = introFrame.getLocationOnScreen();
            introFrame.setVisible(false);
            new OptionsFrame(p);
        }

        else if (src == instructionsButton) {
            Point p = introFrame.getLocationOnScreen();
            introFrame.setVisible(false);
            new InstructionsFrame(p);
        }

    }

    @Override
    public void paintComponent (Graphics g) {
        g.drawImage(background, 0, 0, this);

        // Lets the TEXTRIS text drop in like a piece
        if (logoIn < 9) {
            logoIn += 1;
        }

        g.drawImage(text, 91, 32*logoIn,this);
    }

}