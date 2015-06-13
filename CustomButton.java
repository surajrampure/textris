/*

CustomButton.java
Suraj Rampure

This class extends JButton to make buttons to my taste
The images of these buttons are replaced with files found in the Images folder

*/

import javax.swing.*;

public class CustomButton extends JButton {

    // Constructor method
    public CustomButton (String s) {

        // Disabling the default view
        setContentAreaFilled(false);
        setBorderPainted(false);

        // Loading the new icons and setting them
        ImageIcon up = new ImageIcon("Images/Buttons/" + s + " Up.png");
        ImageIcon hover = new ImageIcon("Images/Buttons/" + s + " Hover.png");
        ImageIcon down = new ImageIcon("Images/Buttons/" + s + " Down.png");

        // Setting the custom button images
        setIcon(up);
        setRolloverIcon(hover);
        setPressedIcon(down);


    }

    // Given a generic JButton, this makes it invisible (used in the options pane)
    public static void makeInvisible (JButton b) {
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
    }

}