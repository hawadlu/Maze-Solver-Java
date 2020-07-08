import customExceptions.InvalidColourException;
import customExceptions.InvalidMazeException;
import customExceptions.SolveFailureException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

    //Size variable
    int panelHeight = 750;
    int panelWidth = 750;
    int elementHeight = 50;
    Dimension panelWhole = new Dimension(panelWidth, elementHeight);
    Dimension panelThirds = new Dimension(panelWidth / 3, elementHeight);
    Dimension panelSixths = new Dimension(panelWidth / 6, elementHeight);

    boolean hasDisplayedTradeOff = false;


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
        Button gameTab = new Button("Game");
        topBar.add(solveTab);
        topBar.add(gameTab);

        //Set onclick listeners
        solveTab.addActionListener(e -> {
            imgPanel = null;
            loadSolveGui();
        });
        gameTab.addActionListener(e -> loadGameGui());

        return topBar;
    }

    /**
     * Load the gui required for solving
     * @return
     */
    private void loadSolveGui() {
        primaryGui.removeAll();
        GridBagConstraints constraints = setupSolveConstraints();

        //Setup JPanel
        primaryGui.setLayout(new GridBagLayout());
        Button solveButton = new Button("Load Image");
        primaryGui.add(solveButton, constraints);

        //Make the buttons do stuff when they are clicked
        solveButton.addActionListener(e -> {
            try {
                //Get the file and load the options Gui
                ImageFile tmp = UIFileChooser();
                if (tmp != null) loadSolveOptionsGui(tmp);
            } catch (IOException | InvalidColourException | InvalidMazeException ioException) {
                ioException.printStackTrace();
            }
        });

        System.out.println("Repainting primary");
        primaryGui.setBackground(Color.PINK);
        primaryGui.revalidate();
        primaryGui.repaint();
        gui.revalidate();
        gui.repaint();
    }

    private GridBagConstraints setupSolveConstraints() {
        //Setup constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = WIDTH;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        return constraints;
    }

    /**
     * Load the game GUI
     */
    private void loadGameGui() {
        primaryGui.removeAll();
        //Setup constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = WIDTH;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill= GridBagConstraints.HORIZONTAL;

        //Setup JPanel
        primaryGui.setLayout(new GridBagLayout());

        Button loadButton = new Button("Load Image");
        primaryGui.add(loadButton);

        loadButton.addActionListener(e -> {
            try {
                ImageFile tmp = UIFileChooser();
                if (tmp != null) playGameGui(tmp);
            } catch (IOException | InvalidColourException | InvalidMazeException ioException) {
                ioException.printStackTrace();
            }
        });

        System.out.println("Repainting primary");
        primaryGui.setBackground(Color.PINK);
        primaryGui.revalidate();
        primaryGui.repaint();
        gui.revalidate();
        gui.repaint();
    }

    /**
     * The gui that displays the game
     * @param fileIn the image file
     */
    private void playGameGui(ImageFile fileIn) {
        customGrid = new CustomGrid(primaryGui);
        customGrid.setSize(panelWidth, panelHeight);

        //Create the title
        String titleText = "Maze Race";
        JLabel title = new JLabel();
        title.setPreferredSize(panelWhole);

        Font labelFont = title.getFont();

        int stringWidth = title.getFontMetrics(labelFont).stringWidth(titleText);
        int componentWidth = title.getWidth();

        // Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = title.getHeight();

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        title.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));

        customGrid.addElement(title, 0, 0, 6);

        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }


    /**
     * Get and return the file that the user wants
     * @return the file
     */
    private ImageFile UIFileChooser() throws IOException, InvalidColourException, InvalidMazeException {
        System.out.println("Load image");
        //Get the file
        final JFileChooser filePicker = new JFileChooser();
        int fileReturn = filePicker.showOpenDialog(filePicker);

        if (fileReturn == JFileChooser.APPROVE_OPTION) {
            File fileIn = filePicker.getSelectedFile();
            System.out.println("Opened: " + fileIn);
            return new ImageFile(ImageIO.read(fileIn), fileIn.getAbsolutePath());
        } else {
            return null;
        }
    }

    /**
     * Loads the options for solving a maze
     * @param imageFile the file
     * @throws IOException whoops, something broke
     */
    public void loadSolveOptionsGui(ImageFile imageFile) throws IOException {
        customGrid = new CustomGrid(primaryGui);

        customGrid.setSize(panelWidth, panelHeight);

        String[] algorithms = {"Depth First", "Breadth First", "Dijkstra", "AStar"};
        JComboBox selectAlgorithm = new JComboBox(algorithms);
        selectAlgorithm.setPreferredSize(panelThirds);
        selectAlgorithm.setSelectedIndex(3);

        customGrid.addElement(selectAlgorithm, 0, 0, 2);

        String[] searchType = {"Search for neighbours during loading", "Search for neighbours during solving"};
        JComboBox selectSearch = new JComboBox(searchType);
        selectSearch.setSelectedIndex(0);
        customGrid.setFill(GridBagConstraints.HORIZONTAL);
        selectSearch.setPreferredSize(panelThirds);
        customGrid.addElement(selectSearch, 2, 0, 2);

        JLabel fileName;
        if (imageFile != null) {
            String[] file = imageFile.getAbsolutePath().split("/");
            fileName = new JLabel("File name: " + file[file.length - 1]);
        } else {
            fileName = new JLabel("No file selected");
        }
        fileName.setPreferredSize(panelThirds);
        customGrid.addElement(fileName, 4, 0, 2);

        //Display the image
        displayImage(imageFile, 0, 1, 6);

        makeImageControlButtons(imageFile, panelSixths);

        JButton generic = new JButton("Solve");
        generic.addActionListener(e -> {
            try {
                Spinner spinner = new Spinner(imageFile);
                final ImageFile[] solvedImg = {null};
                Thread solver = new Thread() {
                    public synchronized void run() {
                        try {
                            solvedImg[0] = Solver.solve(imgPanel.getOriginalImage(),selectAlgorithm.getSelectedItem(),selectSearch.getSelectedItem(), primaryGui);
                        } catch (IOException | IllegalAccessException | SolveFailureException ioException) {
                            ioException.printStackTrace();
                        }
                        solvedImg[0].resetZoom();
                        imgPanel.setImage(solvedImg[0]); //Save the solved image
                        try {
                            spinner.interrupt();
                            loadSaveGui(selectAlgorithm.getSelectedItem().toString(), imageFile);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                };
                spinner.start();
                solver.start();
            } catch (Exception error) {
                error.printStackTrace();
            }
        });
        generic.setPreferredSize(panelThirds);
        customGrid.addElement(generic, 0, 3, 2);

        generic = new JButton("Minimum Spanning Tree");
        generic.setPreferredSize(panelThirds);
        generic.addActionListener(e -> {
            Spinner spinner = new Spinner(imageFile);
            Thread mspThread = new Thread() {
                @Override
                public void run() {
                    MST minimumTree = new MST(imageFile);
                    imageFile.segments = minimumTree.kruskalsAlgorithm();

                    ImageManipulation.drawImage(imageFile, null, null, imageFile.segments, imageFile.artPoints);
                    try {
                        spinner.interrupt();
                        loadSolveOptionsGui(imageFile);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            };
            spinner.start();
            mspThread.start();
        });
        customGrid.addElement(generic,2, 3, 2);

        generic = new JButton("Articulation Points");
        generic.setPreferredSize(panelThirds);
        generic.addActionListener(e -> {
            Spinner spinner = new Spinner(imageFile);
            Thread apThread = new Thread() {
                @Override
                public void run() {
                    ArticulationPoints aps = new ArticulationPoints();
                    aps.findNeighboursForAll(imageFile);
                    imageFile.artPoints = aps.findAps();
                    ImageManipulation.drawImage(imageFile, null, null, imageFile.segments, imageFile.artPoints);
                    try {
                        spinner.interrupt();
                        loadSolveOptionsGui(imageFile);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            };

            spinner.start();
            apThread.start();
        });
        customGrid.addElement(generic, 4, 3, 2);

        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();

        if (!hasDisplayedTradeOff) {
            //Explain the different neighbour methods
            displayMessage(primaryGui, "There are two ways to search for neighbours.\n" +
                    "Searching for neighbours during loading uses more memory \n" +
                    "but may lead to a quicker solve time.\n\n" +
                    "Searching for neighbours while solving uses less memory (good for big mazes) \n" +
                    "but may be slower at solve time.");
            hasDisplayedTradeOff = true;
        }
    }

    /**
     * Displays the image
     * @param fileIn the file containing the image
     * @throws IOException happens when there's a problem
     */
    private void displayImage(ImageFile fileIn, int gridX, int gridY, int gridWidth) throws IOException {
        //The Image
        if (imgPanel == null) imgPanel = new ImagePanel(fileIn, 750, 750, primaryGui);
        JPanel displayImg = imgPanel;
        displayImg.setSize(750, 750);

        customGrid.setIpadY(750);
        customGrid.setIpadX(750);
        customGrid.addElement(displayImg, gridX, gridY, gridWidth);
        customGrid.setIpadY(0);
        customGrid.setIpadX(0);
    }

    /**
     * Make the control buttons for the images
     * @param fileIn the file, used for making the image if required
     * @param panelSixths the size of each sixth of the grid in the x direction
     */
    private void makeImageControlButtons(ImageFile fileIn, Dimension panelSixths) {
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
    private void loadSaveGui(String algorithmUsed, ImageFile fileIn) throws IOException {
        customGrid = new CustomGrid(primaryGui);

        //Small title
        JLabel fileName = new JLabel("Solved using " + algorithmUsed);
        fileName.setPreferredSize(panelWhole);
        customGrid.addElement(fileName, 0, 0, 6);

        //The Image
        displayImage(fileIn, 0, 1, 6);

        //Control buttons
        makeImageControlButtons(fileIn, new Dimension(750 / 6, 50));

        JButton save = new JButton("Save");
        save.addActionListener(e -> saveImage(imgPanel.getOriginalImage()));
        save.setPreferredSize(panelThirds);
        customGrid.addElement(save, 0, 3, 2);

        JButton reset = new JButton("Reset Maze");
        reset.addActionListener(e -> {
            try {
                customGrid = null;
                imgPanel.setImage(fileIn);
                fileIn.reset();
                loadSolveOptionsGui(fileIn);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        reset.setPreferredSize(panelThirds);
        customGrid.addElement(reset, 2, 3, 2);

        JButton diffImg = new JButton("Use a different image");
        diffImg.addActionListener(e -> {
            //reset the image panel
            imgPanel = null;
            customGrid = null;
            loadSolveGui();
        });
        diffImg.setPreferredSize(panelThirds);
        customGrid.addElement(diffImg, 4, 3, 2);


        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }

    /**
     * Save the image in a place of the users choice
     * @param image the image to save
     */
    private void saveImage(ImageFile image) {
        JFileChooser save =new JFileChooser();
        int ret = save.showSaveDialog(primaryGui);
        if (ret == JFileChooser.APPROVE_OPTION) {
            String fileName = save.getSelectedFile().getName();
            String directory = save.getCurrentDirectory().toString();
            String filePath = directory + "/" + fileName;
            ImageManipulation.saveImage(image, filePath);
            displayMessage(primaryGui, "Image saved as: " + filePath);
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
    class CustomGrid {
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        JComponent primaryComponent;

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
         * Turn off interactions
         */
        public void disable(){
            primaryComponent.setEnabled(false);
        }

        /**
         * Turn on interactions
         */
        public void enable() {
            primaryComponent.setEnabled(true);
        }
    }

    static class Spinner extends Thread {
        //Spinning wheel
        JFrame spinnerFrame = new JFrame("Please Wait");
        int size;


        public Spinner(ImageFile imageFile) {
            size = imageFile.width * imageFile.height;
        }

        public void run() {
            //Only show the spinner if the maze is large enough
            if (size >= 40401) {
                ImageIcon loading = new ImageIcon("Animations/Spinning Arrows.gif");
                spinnerFrame.add(new JLabel("Working, please wait... ", loading, JLabel.CENTER));

                spinnerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                spinnerFrame.setSize(300, 150);
                spinnerFrame.setVisible(true);
            } else {
                interrupt();
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            spinnerFrame.setVisible(false);
        }
    };

    public static void main(String[] args) {
        new GUI();
    }
}
