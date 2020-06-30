import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//todo, clean this code up

/**
 * Class that controls the gui of the program.
 */
public class GUI implements ItemListener {
    JFrame gui = new JFrame("Maze Solver");
    JMenuBar topBar = new JMenuBar();
    JPanel primaryGui = new JPanel();
    int WIDTH = 1000, HEIGHT = 1000;
    ImagePanel imgPanel = null;
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
     * Display an error message popup
     * @param message the message
     */
    public static void displayError(JPanel parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message);
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
        constraints.gridwidth = WIDTH;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill= GridBagConstraints.HORIZONTAL;

        //Setup JPanel
        primaryGui.setLayout(new GridBagLayout());
        Button solveButton = new Button("Load Image");
        Button generateButton = new Button("Generate Maze");
        primaryGui.add(solveButton, constraints);
        primaryGui.add(generateButton, constraints);

        //Make the buttons do stuff when they are clicked
        solveButton.addActionListener(e -> {
            try {
                loadImage();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        generateButton.addActionListener(e -> generateMaze());

        System.out.println("Repainting primary");
        primaryGui.setBackground(Color.PINK);
        primaryGui.revalidate();
        primaryGui.repaint();
        gui.revalidate();
        gui.repaint();
    }

    //todo implement me
    private void generateMaze() {
        System.out.println("Generate maze");
    }


    /**
     * Get the file that the user wants to use and then call the relevant load and solve methods
     */
    private void loadImage() throws IOException {
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

    private void loadSolveOptionsGui(File fileIn) throws IOException {
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

        String[] algorithms = {"Depth First", "Breadth First", "Dijkstra", "AStar"};
        JComboBox selectAlgorithm = new JComboBox(algorithms);
        selectAlgorithm.setSelectedIndex(3);
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        primaryGui.add(selectAlgorithm, c);

        //todo, display a dialogue box explaining the tradeoffs
        String[] searchType = {"Search for neighbours during loading", "Search for neighbours during solving"};
        JComboBox selectSearch = new JComboBox(searchType);
        selectSearch.setSelectedIndex(0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        primaryGui.add(selectSearch, c);


        String[] file = fileIn.getAbsolutePath().split("/");
        JLabel fileName = new JLabel("File name: " + file[file.length - 1]);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        primaryGui.add(fileName, c);

        //The Image
        if (imgPanel == null) imgPanel = new ImagePanel(ImageIO.read(fileIn), 750, 750, primaryGui);
        JPanel displayImg = imgPanel;
        displayImg.setBackground(Color.magenta);
        displayImg.setSize(750, 750);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 750;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        primaryGui.add(displayImg, c);


        button = new JButton("▲");
        ImagePanel finalImgPanel3 = imgPanel;
        button.addActionListener(e -> {
            try {
                imgPanel.panUp();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("▼");
        button.addActionListener(e -> {
            try {
                imgPanel.panDown();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("+");
        button.addActionListener(e -> {
            try {
                imgPanel.zoomIn();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 2;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("-");
        button.addActionListener(e -> {
            try {
                imgPanel.zoomOut();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 3;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("<");
        ImagePanel finalImgPanel2 = imgPanel;
        button.addActionListener(e -> {
            try {
                imgPanel.panLeft();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 4;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton(">");
        button.addActionListener(e -> {
            try {
                imgPanel.panRight();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 5;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("Solve");
        button.addActionListener(e -> {
            try {
                BufferedImage solvedImg = Solver.solve(imgPanel.getImage(), selectAlgorithm.getSelectedItem(), selectSearch.getSelectedItem());
                imgPanel = new ImagePanel(solvedImg, 750, 750, primaryGui);
                loadSaveGui(selectAlgorithm.getSelectedItem().toString());
            } catch (IOException | IllegalAccessException ioException) {
                ioException.printStackTrace();
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy = 3;
        primaryGui.add(button, c);

        button = new JButton("Minimum Spanning Tree");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 2;       //aligned with button 2
        c.gridwidth = 2;
        c.gridy = 3;       //third row
        primaryGui.add(button, c);

        button = new JButton("Articulation Points");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 4;       //aligned with button 2
        c.gridwidth = 2;
        c.gridy = 3;       //third row
        primaryGui.add(button, c);

        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }

    private void loadSaveGui(String algorithmUsed) {
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

        String[] algorithms = {"Depth First", "Breadth First", "Dijkstra", "AStar"};
        JComboBox selectAlgorithm = new JComboBox(algorithms);
        selectAlgorithm.setSelectedIndex(3);
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        primaryGui.add(selectAlgorithm, c);

        //todo, display a dialogue box explaining the tradeoffs
        String[] searchType = {"Search for neighbours during loading", "Search for neighbours during solving"};
        JComboBox selectSearch = new JComboBox(searchType);
        selectSearch.setSelectedIndex(0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        primaryGui.add(selectSearch, c);


        JLabel fileName = new JLabel("File name: ");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        primaryGui.add(fileName, c);

        JPanel displayImg = imgPanel;
        displayImg.setBackground(Color.magenta);
        displayImg.setSize(750, 750);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 750;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        primaryGui.add(displayImg, c);


        button = new JButton("▲");
        button.addActionListener(e -> {
                imgPanel.panUp();
                loadSaveGui(algorithmUsed);
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("▼");
        button.addActionListener(e -> {
            imgPanel.panDown();
            loadSaveGui(algorithmUsed);
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("+");
        button.addActionListener(e -> {
            imgPanel.zoomIn();
            loadSaveGui(algorithmUsed);
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 2;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("-");
        button.addActionListener(e -> {
            imgPanel.zoomOut();
            loadSaveGui(algorithmUsed);
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 3;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("<");
        button.addActionListener(e -> {
            imgPanel.panLeft();
            loadSaveGui(algorithmUsed);
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 4;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton(">");
        button.addActionListener(e -> {
            imgPanel.panRight();
            loadSaveGui(algorithmUsed);
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 5;
        c.gridwidth = 1;
        c.gridy = 2;
        primaryGui.add(button, c);

        button = new JButton("Save");
        button.addActionListener(e -> saveImage(imgPanel.getOrignalImage()));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 0;
        c.gridwidth = 6;
        c.gridy = 3;
        primaryGui.add(button, c);

        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }

    private void saveImage(BufferedImage image) {
        JFileChooser save =new JFileChooser();
        int ret = save.showSaveDialog(primaryGui);
        if (ret == JFileChooser.APPROVE_OPTION) {
            String fileName = save.getSelectedFile().getName();
            String directory = save.getCurrentDirectory().toString();
            System.out.println(fileName);
            System.out.println(directory);
            System.out.println("Concat: " + directory + "/" + fileName);
            ImageManipulation.saveImage(image, directory + "/" + fileName);
        } else if (ret == JFileChooser.CANCEL_OPTION) {
            System.out.println("You pressed cancel");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
