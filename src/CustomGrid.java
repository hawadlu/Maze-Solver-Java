import javax.swing.*;
import java.awt.*;

/**
 * Class that allows me to easily make custom grids
 */
public class CustomGrid {
    final GridBagConstraints c = new GridBagConstraints();
    final GridBagLayout layout = new GridBagLayout();
    final JComponent primaryComponent;

    /**
     * Setup the grid
     */
    CustomGrid(JComponent primaryComponent) {
        this.primaryComponent = primaryComponent;
        primaryComponent.removeAll();
        primaryComponent.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.CENTER;
        layout.setConstraints(primaryComponent, c);
        System.out.println("Primary width is: " + primaryComponent.getWidth());
    }

    /**
     * @param padY, set the y padding
     */
    public void setIpadY(int padY) {
        c.ipady = padY;
    }

    /**
     * @param padX, set the x padding
     */
    public void setIpadX(int padX) {
        c.ipadx = padX;
    }

    /**
     * Add an element
     * @param component the component
     * @param gridX the grid position x
     * @param gridY the grid position y
     * @param width the width of this element
     */
    public void addElement(JComponent component, int gridX, int gridY, int width){
        c.gridx = gridX;
        c.gridy = gridY;
        c.gridwidth = width;
        primaryComponent.add(component, c);
    }

    /**
     * @param fill int value for grid fill
     */
    public void setFill(int fill) {
        c.fill = fill;
    }

    /**
     * Set the size of the grid
     */
    public void setSize(int width, int height) {
        primaryComponent.setSize(width, height);
    }

    /**
     * Set the background of the component
     */
    public void setColour(Color colour) {
        primaryComponent.setBackground(colour);
    }

}