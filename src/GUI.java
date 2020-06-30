import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class that controls the gui of the program.
 */
public class GUI implements ItemListener {
    JFrame gui = new JFrame("Maze Solver");
    int WIDTH = 1000, HEIGHT = 1000;
    ImagePanel imgPanel = null;
    CustomGrid customGrid = null;

    JPanel primaryGui = new JPanel();
    JMenuBar topBar = new JMenuBar();

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
    public static void displayMessage(JPanel parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }

    /**
     * Load the components of the top bar
     */
    private JMenuBar loadTopBar() {
        //Create the buttons
        topBar = new JMenuBar();
        topBar.setBackground(Color.YELLOW);
        Button solveTab = new Button("Solve a Maze");
        Button generateTab = new Button("Generate a Maze");
        Button gameTab = new Button("Game");
        topBar.add(solveTab);
        topBar.add(generateTab);
        topBar.add(gameTab);

        //Set onclick listeners
        solveTab.addActionListener(e -> {
            imgPanel = null;
            showSolveGui();
        });
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

    /**
     * Loads the options for solving a maze
     * @param fileIn the file
     * @throws IOException whoops, something broke
     */
    private void loadSolveOptionsGui(File fileIn) throws IOException {
        if (customGrid == null) customGrid = new CustomGrid();

        //Set the size
        int panelHeight = 750;
        int panelWidth = 750;
        int elementHeight = 50;
        Dimension panelThirds = new Dimension(panelWidth / 3, elementHeight);
        Dimension panelSixths = new Dimension(panelWidth / 6, elementHeight);
        customGrid.setSize(panelWidth, panelHeight);

        String[] algorithms = {"Depth First", "Breadth First", "Dijkstra", "AStar"};
        JComboBox selectAlgorithm = new JComboBox(algorithms);
        selectAlgorithm.setPreferredSize(panelThirds);
        selectAlgorithm.setSelectedIndex(3);

        customGrid.addElement(selectAlgorithm, 0, 0, 2);

        //todo, display a dialogue box explaining the tradeoffs
        String[] searchType = {"Search for neighbours during loading", "Search for neighbours during solving"};
        JComboBox selectSearch = new JComboBox(searchType);
        selectSearch.setSelectedIndex(0);
        customGrid.setFill(GridBagConstraints.HORIZONTAL);
        selectSearch.setPreferredSize(panelThirds);
        customGrid.addElement(selectSearch, 2, 0, 2);


        String[] file = fileIn.getAbsolutePath().split("/");
        JLabel fileName = new JLabel("File name: " + file[file.length - 1]);
        fileName.setPreferredSize(panelThirds);
        customGrid.addElement(fileName, 4, 0, 2);

        //Display the image
        displayImage(fileIn, 0, 1, 6);

        makeImageControlButtons(fileIn, panelSixths);

        JButton generic = new JButton("Solve");
        generic.addActionListener(e -> {
            try {
                final BufferedImage[] solvedImg = {null};
                Thread spinner = new Thread() {
                    public void run() {
                        //Spinning wheel
                        JFrame spinnerFrame = new JFrame("Solve");

                        ImageIcon loading = new ImageIcon("Animations/Spinning Arrows.gif");
                        spinnerFrame.add(new JLabel("Solving, please wait... ", loading, JLabel.CENTER));

                        spinnerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        spinnerFrame.setSize(300, 150);
                        spinnerFrame.setVisible(true);

                        while (!this.isInterrupted()) {
                            //Don't do anything here
                        }
                        spinnerFrame.setVisible(false);
                        System.out.println("Stopping spinner thread");
                        interrupt();
                    }
                };

                Thread solver = new Thread() {
                    public synchronized void run() {
                        try {
                            solvedImg[0] =Solver.solve(imgPanel.getOriginalImage(),selectAlgorithm.getSelectedItem(),selectSearch.getSelectedItem(), primaryGui);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        } catch (IllegalAccessException illegalAccessException) {
                            illegalAccessException.printStackTrace();
                        }
                        imgPanel.setImage(solvedImg[0]); //Save the solved image
                        try {
                            spinner.interrupt();
                            loadSaveGui(selectAlgorithm.getSelectedItem().toString(), fileIn);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                };
                solver.start();
                spinner.start();
            } catch (Exception error) {
                error.printStackTrace();
            }
        });
        generic.setPreferredSize(panelThirds);
        customGrid.addElement(generic, 0, 3, 2);

        generic = new JButton("Minimum Spanning Tree");
        generic.setPreferredSize(panelThirds);
        customGrid.addElement(generic,2, 3, 2);

        generic = new JButton("Articulation Points");
        generic.setPreferredSize(panelThirds);
        customGrid.addElement(generic, 4, 3, 2);

        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }

    /**
     * Displays the image
     * @param fileIn the file containing the image
     * @throws IOException happens when there's a problem
     */
    private void displayImage(File fileIn, int gridX, int gridY, int gridWidth) throws IOException {
        //The Image
        if (imgPanel == null) imgPanel = new ImagePanel(ImageIO.read(fileIn), 750, 750, primaryGui);
        JPanel displayImg = imgPanel;
        displayImg.setBackground(Color.magenta);
        displayImg.setSize(750, 750);

        customGrid.setIpadY(750);
        customGrid.setIpadX(750);
        customGrid.addElement(displayImg, gridX, gridY, gridWidth);
        customGrid.setIpadY(0);
        customGrid.setIpadX(0);
    }

    /**
     * Make teh control buttons for the images
     * @param fileIn the file, used for making the image if required
     * @param panelSixths the size of each sixth of the grid in the x direction
     */
    private void makeImageControlButtons(File fileIn, Dimension panelSixths) {
        //Generic because it is reused several times
        JButton generic = new JButton("▲");
        generic.addActionListener(e -> {
            try {
                imgPanel.panUp();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 0, 2, 1);

        generic = new JButton("▼");
        generic.addActionListener(e -> {
            try {
                imgPanel.panDown();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 1, 2, 1);

        generic = new JButton("+");
        generic.addActionListener(e -> {
            try {
                imgPanel.zoomIn();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 2, 2, 1);

        generic = new JButton("-");
        generic.addActionListener(e -> {
            try {
                imgPanel.zoomOut();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 3, 2, 1);

        generic = new JButton("<");
        generic.addActionListener(e -> {
            try {
                imgPanel.panLeft();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 4, 2, 1);

        generic = new JButton(">");
        generic.addActionListener(e -> {
            try {
                imgPanel.panRight();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 5, 2, 1);
    }

    /**
     * Gui that allows the user the save an image
     * @param algorithmUsed the algorithm that was used to solve the maze
     * @param fileIn file containing the image
     * @throws IOException something bad happened
     */
    private void loadSaveGui(String algorithmUsed, File fileIn) throws IOException {
        customGrid = new CustomGrid();
        Dimension panelThirds = new Dimension(750 / 3, 50);

        //Small title
        JLabel fileName = new JLabel("Solved using " + algorithmUsed);
        //todo use the values already defined in loadSolveOptionsGui
        fileName.setPreferredSize(new Dimension(750, 50));
        customGrid.addElement(fileName, 0, 0, 6);

        //The Image
        displayImage(fileIn, 0, 1, 6);

        //todo reset image panel on save and solve
        JButton save = new JButton("Save");
        save.addActionListener(e -> saveImage(imgPanel.getOriginalImage()));
        save.setPreferredSize(panelThirds);
        customGrid.addElement(save, 0, 2, 1);

        JButton reset = new JButton("Reset Maze");
        reset.addActionListener(e -> {
            try {
                customGrid = null;
                imgPanel.setImage(ImageIO.read(fileIn));
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        reset.setPreferredSize(panelThirds);
        customGrid.addElement(reset, 1, 2, 1);

        JButton diffImg = new JButton("Use a different image");
        diffImg.addActionListener(e -> {
            try {
                //reset the image panel
                imgPanel = null;
                customGrid = null;
                loadImage();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        diffImg.setPreferredSize(panelThirds);
        customGrid.addElement(diffImg, 2, 2, 1);


        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }

    /**
     * Save the image in a place of the users choice
     * @param image the image to save
     */
    private void saveImage(BufferedImage image) {
        JFileChooser save =new JFileChooser();
        int ret = save.showSaveDialog(primaryGui);
        if (ret == JFileChooser.APPROVE_OPTION) {
            String fileName = save.getSelectedFile().getName();
            String directory = save.getCurrentDirectory().toString();
            String filePath = directory + "/" + fileName;
            ImageManipulation.saveImage(image, filePath);
            displayMessage(primaryGui, "Imaged as: " + filePath);
        } else if (ret == JFileChooser.CANCEL_OPTION) {
            System.out.println("You pressed cancel");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    /**
     * Class that allows me to easily make custom grids
     */
    //todo make it so that this does not use a global variable
    class CustomGrid {
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();

        /**
         * Setup the grid
         */
        CustomGrid() {
            primaryGui.removeAll();
            primaryGui.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.CENTER;
            layout.setConstraints(primaryGui, c);
            System.out.println("Primary width is: " + primaryGui.getWidth());
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
            primaryGui.add(component, c);
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
            primaryGui.setSize(width, height);
        }

        /**
         * Turn off interactions
         */
        public void disable(){
            primaryGui.setEnabled(false);
        }

        /**
         * Turn on interactions
         */
        public void enable() {
            primaryGui.setEnabled(true);
        }
    }
}
