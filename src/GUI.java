import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

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
            loadSolveGui();
        });
        generateTab.addActionListener(e -> loadGenerateOptionsGUI(null));
        gameTab.addActionListener(e -> loadGameGui());

        return topBar;
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
                //Get the file and load the options Gui
                loadSolveOptionsGui(UIFileChooser());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        generateButton.addActionListener(e -> loadGenerateOptionsGUI("loadSolveOptionsGui"));

        System.out.println("Repainting primary");
        primaryGui.setBackground(Color.PINK);
        primaryGui.revalidate();
        primaryGui.repaint();
        gui.revalidate();
        gui.repaint();
    }

    /**
     * Shows all the options for generating a maze
     */
    private void loadGenerateOptionsGUI(String nextMethod) {
        System.out.println("Show generate gui");
        customGrid = new CustomGrid();

        AtomicBoolean perfectChecked = new AtomicBoolean(false);

        //Set the size
        int panelHeight = 750;
        int panelWidth = 750;
        int elementHeight = 50;
        Dimension panelHalves = new Dimension(panelWidth / 2, elementHeight);

        customGrid.setSize(panelWidth, panelHeight);

        //Add the labels
        JLabel widthLabel = new JLabel("Width");
        JLabel heightLabel = new JLabel("Height");
        JLabel perfectLabel = new JLabel("Perfect Maze");
        widthLabel.setPreferredSize(panelHalves);
        heightLabel.setPreferredSize(panelHalves);
        perfectLabel.setPreferredSize(panelHalves);
        customGrid.addElement(widthLabel, 0, 0, 1);
        customGrid.addElement(heightLabel, 0, 1, 1);
        customGrid.addElement(perfectLabel, 0, 2, 1);

        //Text inputs
        JTextField widthIn = new JTextField();
        JTextField heightIn = new JTextField();
        widthIn.setPreferredSize(panelHalves);
        heightIn.setPreferredSize(panelHalves);
        customGrid.addElement(widthIn, 1, 0, 1);
        customGrid.addElement(heightIn, 1, 1, 1);

        //Checkbox
        JCheckBox perfect = new JCheckBox();
        //todo verify that this works
        perfect.addActionListener(e -> {
            if (perfectChecked.get()) perfectChecked.set(false);
            else perfectChecked.set(true);
        });
        customGrid.addElement(perfect, 1, 2, 1);

        //Submit button
        JButton submit = new JButton("Generate");
        //todo verify the input parameters
        //todo add a spinner wheel while the maze generating
        submit.addActionListener(e -> {
            //Generate the maze
              Solver.generateMaze(widthIn.getText(), heightIn.getText(), perfectChecked.get());

            //Go to the save menu
            try {
                //todo make the image properly generate
                imgPanel = new ImagePanel(ImageIO.read(new File("Images/Tiny.png")), 750, 750, primaryGui); //todo remove and replace with the generated image

                if (nextMethod.equals("loadSolveOptionsGui")) {
                    try {
                        loadSolveOptionsGui(null);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        customGrid.addElement(submit, 0, 3, 2);

        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }

    /**
     * Load the game GUI
     */
    //todo implement me
    private void loadGameGui() {
        System.out.println("Generating maze");
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
                //Get the file and load the options Gui
                playGameGui(UIFileChooser());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        //todo update load generate options to handle this
        generateButton.addActionListener(e -> loadGenerateOptionsGUI("playGameGui"));

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
    private void playGameGui(File fileIn) {
        customGrid = new CustomGrid();

        //Set the size
        //todo look at making these globals
        int panelHeight = 750;
        int panelWidth = 750;
        int elementHeight = 50;
        Dimension panelWhole = new Dimension(panelWidth, elementHeight);
        Dimension panelThirds = new Dimension(panelWidth / 3, elementHeight);
        Dimension panelHalves = new Dimension(panelWidth / 2, elementHeight);

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
    private ImageFile UIFileChooser() throws IOException {
        System.out.println("Load image");
        //Get the file
        final JFileChooser filePicker = new JFileChooser();
        int fileReturn = filePicker.showOpenDialog(filePicker);

        if (fileReturn == JFileChooser.APPROVE_OPTION) {
            File fileIn = filePicker.getSelectedFile();
            System.out.println("Opened: " + fileIn);
            return new ImageFile(ImageIO.read(fileIn));
        } else {
            //todo deal with this
            throw new Error("Failed to open file");
        }
    }

    /**
     * Loads the options for solving a maze
     * @param fileIn the file
     * @throws IOException whoops, something broke
     */
    private void loadSolveOptionsGui(ImageFile fileIn) throws IOException {
        customGrid = new CustomGrid();

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

        JLabel fileName;
        if (fileIn != null) {
            String[] file = fileIn.getAbsolutePath().split("/");
            fileName = new JLabel("File name: " + file[file.length - 1]);
        } else {
            fileName = new JLabel("No file selected");
        }
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
                            solvedImg[0] = Solver.solve(imgPanel.getOriginalImage(),selectAlgorithm.getSelectedItem(),selectSearch.getSelectedItem(), primaryGui);
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
    private void displayImage(ImageFile fileIn, int gridX, int gridY, int gridWidth) throws IOException {
        //The Image
        if (imgPanel == null) imgPanel = new ImagePanel(fileIn, 750, 750, primaryGui);
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
     * Make the control buttons for the images
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
    //fixme make this display the solved image
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
            //reset the image panel
            imgPanel = null;
            customGrid = null;
            loadSolveGui();
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

    class Worker extends Thread{
        private CountDownLatch latch;

        Worker (CountDownLatch latch, String name) {
            super(name);
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }
}
