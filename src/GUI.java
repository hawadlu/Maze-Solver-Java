import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * Class that controls the gui of the program.
 */
public class GUI implements ItemListener {
    JFrame gui = new JFrame("Maze Solver");
    JMenuBar topBar = new JMenuBar();
    JPanel primaryGui = new JPanel();
    int WIDTH = 600, HEIGHT = 600;

    /**
     * Constructor that loads the gui
     */
    GUI() {
        //Creating the Frame
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(WIDTH, HEIGHT);
        gui.setBackground(Color.RED);

        loadTopBar();
        loadSolveGui();

        //Adding Components to the gui.
        gui.getContentPane().add(BorderLayout.NORTH, topBar);
        gui.getContentPane().add(BorderLayout.CENTER, primaryGui);
        gui.setVisible(true);
    }

    /**
     * Load the components of the top bar
     */
    private JMenuBar loadTopBar() {
        //Create the buttons
        topBar = new JMenuBar();
        topBar.setBackground(Color.YELLOW);
        Button solveTab = new Button("Solve");
        Button generateTab = new Button("Generate");
        Button gameTab = new Button("Game");
        topBar.add(solveTab);
        topBar.add(generateTab);
        topBar.add(gameTab);

        //Set onclick listeners
        solveTab.addActionListener(e -> showSolveGui());
        generateTab.addActionListener(e -> showGenerateGui());
        gameTab.addActionListener(e -> showGameGui());

        return topBar;
    }

    //todo implement me
    private void showGameGui() {
        System.out.println("Show game gui");
    }

    //todo implement me
    private void showGenerateGui() {
        System.out.println("Show generate gui");
    }

    //todo implement me
    private void showSolveGui() {
        System.out.println("Show solve gui");
        loadSolveGui();
    }

    /**
     * Load the gui required for solving
     * @return
     */
    private void loadSolveGui() {
        primaryGui.removeAll();
        //Setup constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill= GridBagConstraints.HORIZONTAL;

        //Setup JPanel
        primaryGui.setLayout(new GridBagLayout());
        Button solveButton = new Button("Load Image");
        Button generateButton = new Button("Generate Maze");
        primaryGui.add(solveButton, constraints);
        primaryGui.add(generateButton, constraints);

        //Make the buttons do stuff when they are clicked
        solveButton.addActionListener(e -> loadImage());
        generateButton.addActionListener(e -> generateMaze());

        System.out.println("Repainting primary");
        primaryGui.revalidate();
        primaryGui.repaint();
        gui.revalidate();
        gui.repaint();
    }

    //todo implement me
    private void generateMaze() {
        System.out.println("Generate maze");
    }

    //todo implement me

    /**
     * Get the file that the user wants to use and then call the relevant load and solve methods
     */
    private void loadImage() {
        System.out.println("Load image");
        //Get the file
        final JFileChooser filePicker = new JFileChooser();
        int fileReturn = filePicker.showOpenDialog(filePicker);

        if (fileReturn == JFileChooser.APPROVE_OPTION) {
            File fileIn = filePicker.getSelectedFile();
            System.out.println("Opened: " + fileIn);

            loadSolveOptionsGui(fileIn);
        } else {
            throw new Error("Failed to open file");
        }
    }

    private void loadSolveOptionsGui(File image) {
        final boolean shouldFill = true;
        final boolean shouldWeightX = true;
        final boolean RIGHT_TO_LEFT = false;
        primaryGui.removeAll();
        if (RIGHT_TO_LEFT) {
            primaryGui.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        JButton button;
        primaryGui.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.HORIZONTAL;
        }

        button = new JButton("Button 1");
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        primaryGui.add(button, c);

        button = new JButton("Button 2");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        primaryGui.add(button, c);

        button = new JButton("Button 3");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        primaryGui.add(button, c);


        JPanel displayImg = new ImagePanel(image, WIDTH, HEIGHT);
        displayImg.setBackground(Color.DARK_GRAY);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 500;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        primaryGui.add(displayImg, c);

        button = new JButton("Solve");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 0;       //aligned with button 2
        c.gridwidth = 1;   //2 columns wide
        c.gridy = 2;       //third row
        primaryGui.add(button, c);

        button = new JButton("Minimum Spanning Tree");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 1;       //aligned with button 2
        c.gridwidth = 1;
        c.gridy = 2;       //third row
        primaryGui.add(button, c);

        button = new JButton("Articulation Points");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 2;       //aligned with button 2
        c.gridwidth = 1;
        c.gridy = 2;       //third row
        primaryGui.add(button, c);

        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
