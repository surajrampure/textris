/*

CustomCheckBox.java
Suraj Rampure

This class extends JCheckBox to make toggle switches to my taste
The images of these buttons are replaced with files found in the Images folder

*/

import javax.swing.*;

public class CustomCheckBox extends JCheckBox {

    // Constructor method
    public CustomCheckBox (String a, String b) {

        // Disabling the default view
        setContentAreaFilled(false);
        setBorderPainted(false);

        // Loading the new icons and setting them
        ImageIcon up = new ImageIcon("Images/Buttons/" + a + "png");
        ImageIcon down = new ImageIcon("Images/Buttons/" + b + " Down.png");

        // Setting the custom button images
        setIcon(up);
        setPressedIcon(down);


    }

}