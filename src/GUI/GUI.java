package GUI;

import Image.*;
import ArticulationPoints.ArticulationPoints;
import Location.Coordinates;
import MinimumSpanningTree.MST;
import Solve.*;
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
import java.util.HashMap;
import java.util.Objects;

/**
 * Class that controls the gui of the program.
 */
public class GUI implements ItemListener {
    final JFrame gui = new JFrame("Maze Solve.Solver");
    final int WIDTH = 1000;
    final int HEIGHT = 1000;
    ImagePanel imgPanel = null;
    CustomGrid customGrid = null;

    Color greyBackground = new Color(235, 235, 235);
    Color darkGreyBackground = new Color(186, 186, 186);

    final JPanel primaryGui = new JPanel();
    JMenuBar topBar = new JMenuBar();

    //Size variable
    final int panelHeight = 750;
    final int panelWidth = 750;
    final int elementHeight = 50;
    final Dimension panelWhole = new Dimension(panelWidth, elementHeight);
    final Dimension panelThirds = new Dimension(panelWidth / 3, elementHeight);
    final Dimension panelSixths = new Dimension(panelWidth / 6, elementHeight);
    final Dimension entirePanel = new Dimension(panelWidth, panelHeight);

    boolean hasDisplayedTradeOff = false;


    /**
     * Constructor that loads the gui
     */
    GUI() {
        //Creating the Frame
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(WIDTH, HEIGHT);
        gui.setBackground(Color.GRAY);

        loadTopBar();
        loadSolveGui();

        //Adding Components to the gui.
        gui.getContentPane().add(BorderLayout.NORTH, topBar);
        gui.getContentPane().add(BorderLayout.CENTER, primaryGui);
        primaryGui.setBackground(darkGreyBackground);
        gui.setVisible(true);
    }

    /**
     * Display an error message popup
     *
     * @param message the message
     */
    public static void displayMessage(JPanel parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }

    /**
     * Load the components of the top bar
     */
    private void loadTopBar() {
        //Create the buttons
        topBar = new JMenuBar();
        topBar.setBorder(null);
        topBar.setBackground(Color.YELLOW);
        Button solveTab = new Button("Solve a Maze");
        Button gameTab = new Button("Game");
        solveTab.setBackground(darkGreyBackground);
        gameTab.setBackground(greyBackground);
        topBar.add(solveTab);
        topBar.add(gameTab);

        //Set onclick listeners
        solveTab.addActionListener(e -> {
            solveTab.setBackground(darkGreyBackground);
            gameTab.setBackground(greyBackground);
            imgPanel = null;
            loadSolveGui();
        });
        gameTab.addActionListener(e -> {
            solveTab.setBackground(greyBackground);
            gameTab.setBackground(darkGreyBackground);
            loadGameGui();
        });

    }

    /**
     * Load the gui required for solving
     */
    private void loadSolveGui() {
        //todo update this to use tbe custom grid
        primaryGui.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //This is an empty panel to create space
        JPanel panel = new JPanel();
        panel.setBackground(darkGreyBackground);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 100;
        primaryGui.add(panel, c);

        JLabel title = new JLabel("Load Image To Solve");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100;
        c.gridx = 0;
        c.gridy = 1;
        title.setFont(new Font("Sans-Serif", Font.PLAIN, 40));
        title.setHorizontalAlignment((int) JComponent.CENTER_ALIGNMENT);
        primaryGui.add(title, c);

        JButton solveButton = new JButton("Load Image");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 50;
        c.ipadx = panelWidth;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;

        solveButton.addActionListener(e -> {
            try {
                //Get the file and load the options Gui
                ImageFile tmp = UIFileChooser();
                if (tmp != null) loadSolveOptionsGui(tmp);
            } catch (IOException | InvalidColourException | InvalidMazeException ioException) {
                ioException.printStackTrace();
            }
        });

        primaryGui.add(solveButton, c);
    }

    /**
     * Load the game GUI.GUI
     */
    private void loadGameGui() {
        customGrid = new CustomGrid(primaryGui);

        customGrid.addElement(new JLabel("Load Game Maze"), 0, 0, 1);

        JButton loadButton = new JButton("Load Image For Game");
        customGrid.addElement(loadButton, 0, 1, 1);

        loadButton.addActionListener(e -> {
            try {
                ImageFile tmp = UIFileChooser();
                if (tmp != null) playGameGui(tmp);
            } catch (IOException | InvalidColourException | InvalidMazeException ioException) {
                ioException.printStackTrace();
            }
        });

        System.out.println("Repainting primary");
        primaryGui.revalidate();
        primaryGui.repaint();
        gui.revalidate();
        gui.repaint();
    }

    /**
     * The gui that displays the game
     */
    private void playGameGui(ImageFile imageFile) {
        customGrid = new CustomGrid(primaryGui);
        customGrid.setSize(panelWidth, panelHeight);

        //Title of sorts
        customGrid.addElement(new JLabel("The Race"), 0, 0, 3);

        //Load and play buttons
        String[] algorithms = {"Depth First", "Breadth First", "Dijkstra", "AStar"};
        JComboBox progOne = new JComboBox(algorithms);
        JComboBox progTwo = new JComboBox(algorithms);
        JButton start = new JButton("▶");

        progOne.setPreferredSize(panelThirds);
        progTwo.setPreferredSize(panelThirds);
        start.setPreferredSize(panelThirds);

        start.addActionListener(e -> {
            //Make a thread for each of the programs
            Thread progThreadOne = new Thread() {
                //todo implement, currently it is just a test and does not use the parser
                @Override
                public void run() {
                    try {
                        ImageFile copyImg = imageFile.clone();
                        System.out.println("Program one is using: " + progOne.getSelectedItem());
                        Animate animate = null;
                        if (progOne.getSelectedItem().toString().equals("Depth First")) {
                            DepthFirstSearch search = new DepthFirstSearch();
                            HashMap<Coordinates, MazeNode> nodes = ImageManipulation.findNeighboursForAll(copyImg);
                            search.solve(copyImg, nodes.get(copyImg.getEntry()), nodes.get(copyImg.getExit()), nodes, primaryGui);
                            animate = new Animate(search.getPath(), copyImg, "Player One");
                        } else if (progOne.getSelectedItem().toString().equals("Breadth First")) {
                            BreadthFirstSearch search = new BreadthFirstSearch();
                            HashMap<Coordinates, MazeNode> nodes = ImageManipulation.findNeighboursForAll(copyImg);
                            search.solve(copyImg, nodes.get(copyImg.getEntry()), nodes.get(copyImg.getExit()), nodes, primaryGui);
                            animate = new Animate(search.getPath(), copyImg, "Player One");
                        } else if (progOne.getSelectedItem().toString().equals("Dijkstra")) {
                            DijkstraSearch search = new DijkstraSearch();
                            HashMap<Coordinates, MazeNode> nodes = ImageManipulation.findNeighboursForAll(copyImg);
                            search.solve(copyImg, nodes.get(copyImg.getEntry()), nodes.get(copyImg.getExit()), nodes, primaryGui);
                            animate = new Animate(search.getPath(), copyImg, "Player One");
                        } else {
                            AStarSearch search = new AStarSearch();
                            HashMap<Coordinates, MazeNode> nodes = ImageManipulation.findNeighboursForAll(copyImg);
                            search.solve(copyImg, nodes.get(copyImg.getEntry()), nodes.get(copyImg.getExit()), nodes, primaryGui);
                            animate = new Animate(search.getPath(), copyImg, "Player One");
                        }

                        animate.play((byte) 3);
                        GUI.displayMessage(primaryGui, "Player one has finished");
                    } catch (SolveFailureException | InterruptedException | CloneNotSupportedException solveFailureException) {
                        solveFailureException.printStackTrace();
                    }
                }
            };

            Thread progThreadTwo = new Thread() {
                //todo implement, currently it is just a test and does not use the parser
                @Override
                public void run() {
                    try {
                        ImageFile copyImg = imageFile.clone();
                        System.out.println("Program two is using: " + progOne.getSelectedItem());
                        Animate animate = null;
                        if (progTwo.getSelectedItem().toString().equals("Depth First")) {
                            DepthFirstSearch search = new DepthFirstSearch();
                            HashMap<Coordinates, MazeNode> nodes = ImageManipulation.findNeighboursForAll(copyImg);
                            search.solve(copyImg, nodes.get(copyImg.getEntry()), nodes.get(copyImg.getExit()), nodes, primaryGui);
                            animate = new Animate(search.getPath(), copyImg, "Player Two");
                        } else if (progTwo.getSelectedItem().toString().equals("Breadth First")) {
                            BreadthFirstSearch search = new BreadthFirstSearch();
                            HashMap<Coordinates, MazeNode> nodes = ImageManipulation.findNeighboursForAll(copyImg);
                            search.solve(copyImg, nodes.get(copyImg.getEntry()), nodes.get(copyImg.getExit()), nodes, primaryGui);
                            animate = new Animate(search.getPath(), copyImg, "Player Two");
                        } else if (progTwo.getSelectedItem().toString().equals("Dijkstra")) {
                            DijkstraSearch search = new DijkstraSearch();
                            HashMap<Coordinates, MazeNode> nodes = ImageManipulation.findNeighboursForAll(copyImg);
                            search.solve(copyImg, nodes.get(copyImg.getEntry()), nodes.get(copyImg.getExit()), nodes, primaryGui);
                            animate = new Animate(search.getPath(), copyImg, "Player Two");
                        } else {
                            AStarSearch search = new AStarSearch();
                            HashMap<Coordinates, MazeNode> nodes = ImageManipulation.findNeighboursForAll(copyImg);
                            search.solve(copyImg, nodes.get(copyImg.getEntry()), nodes.get(copyImg.getExit()), nodes, primaryGui);
                            animate = new Animate(search.getPath(), copyImg, "Player Two");
                        }

                        animate.play((byte) 4);
                        GUI.displayMessage(primaryGui, "Player two has finished");
                    } catch (SolveFailureException | InterruptedException | CloneNotSupportedException solveFailureException) {
                        solveFailureException.printStackTrace();
                    }
                }
            };
            progThreadOne.start();
            progThreadTwo.start();
        });

        customGrid.addElement(progOne, 0, 1, 1);
        customGrid.addElement(start, 1, 1, 1);
        customGrid.addElement(progTwo, 2, 1, 1);

        //Display the image
        displayImage(imageFile, 2, 3);


        System.out.println("Repainting primary");
        gui.revalidate();
        gui.repaint();
    }


    /**
     * Get and return the file that the user wants
     *
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
            return new ImageFile(ImageIO.read(fileIn), fileIn.getAbsolutePath(), primaryGui);
        } else {
            return null;
        }
    }

    /**
     * Loads the options for solving a maze
     *
     * @param imageFile the file
     */
    public void loadSolveOptionsGui(ImageFile imageFile) {
        customGrid = new CustomGrid(primaryGui);

        customGrid.setSize(panelWidth, panelHeight);

        String[] algorithms = {"Depth First", "Breadth First", "Dijkstra", "AStar"};
        JComboBox<String> selectAlgorithm = new JComboBox<>(algorithms);
        selectAlgorithm.setPreferredSize(panelThirds);
        selectAlgorithm.setSelectedIndex(3);

        //A title
        customGrid.addElement(new JLabel("Solve The Maze"), 0, 0, 6);

        customGrid.addElement(selectAlgorithm, 0, 1, 2);

        String[] searchType = {"Search for neighbours during loading", "Search for neighbours during solving"};
        JComboBox<String> selectSearch = new JComboBox<>(searchType);
        selectSearch.setSelectedIndex(0);
        customGrid.setFill(GridBagConstraints.HORIZONTAL);
        selectSearch.setPreferredSize(panelThirds);
        customGrid.addElement(selectSearch, 2, 1, 2);

        JLabel fileName;
        if (imageFile != null) {
            String[] file = imageFile.getAbsolutePath().split("/");
            fileName = new JLabel("File name: " + file[file.length - 1]);
        } else {
            fileName = new JLabel("No file selected");
        }
        fileName.setPreferredSize(panelThirds);
        customGrid.addElement(fileName, 4, 1, 2);

        //Display the image
        displayImage(imageFile, 2, 6);

        makeImageControlButtons(imageFile, panelSixths, 3);

        JButton generic = new JButton("Solve");
        generic.addActionListener(e -> {
            try {
                assert imageFile != null;
                Spinner spinner = new Spinner(imageFile);
                final ImageFile[] solvedImg = {null};
                Thread solver = new Thread() {
                    public synchronized void run() {
                        try {
                            solvedImg[0] = Solver.solve(imgPanel.getOriginalImage(), selectAlgorithm.getSelectedItem(), selectSearch.getSelectedItem(), primaryGui);
                        } catch (SolveFailureException ioException) {
                            ioException.printStackTrace();
                        }
                        solvedImg[0].resetZoom();
                        imgPanel.setImage(solvedImg[0]); //Save the solved image
                        spinner.interrupt();
                        loadSaveGui(Objects.requireNonNull(selectAlgorithm.getSelectedItem()).toString(), imageFile);
                    }
                };
                spinner.start();
                solver.start();
            } catch (Exception error) {
                error.printStackTrace();
            }
        });
        generic.setPreferredSize(panelThirds);
        customGrid.addElement(generic, 0, 4, 2);

        generic = new JButton("Minimum Spanning Tree");
        generic.setPreferredSize(panelThirds);
        generic.addActionListener(e -> {
            assert imageFile != null;
            Spinner spinner = new Spinner(imageFile);
            Thread mspThread = new Thread(() -> {
                MST minimumTree = new MST(imageFile);
                imageFile.setSegments(minimumTree.kruskalsAlgorithm());

                ImageManipulation.drawImage(imageFile, null, null, imageFile.getSegments(), imageFile.getArtPoints());
                spinner.interrupt();
                loadSolveOptionsGui(imageFile);
            });
            spinner.start();
            mspThread.start();
        });
        customGrid.addElement(generic, 2, 4, 2);

        generic = new JButton("Articulation Points");
        generic.setPreferredSize(panelThirds);
        generic.addActionListener(e -> {
            assert imageFile != null;
            Spinner spinner = new Spinner(imageFile);
            Thread apThread = new Thread(() -> {
                ArticulationPoints aps = new ArticulationPoints();
                imageFile.setArtPoints(aps.findAps(imageFile));
                ImageManipulation.drawImage(imageFile, null, null, imageFile.getSegments(), imageFile.getArtPoints());
                spinner.interrupt();
                loadSolveOptionsGui(imageFile);
            });

            spinner.start();
            apThread.start();
        });
        customGrid.addElement(generic, 4, 4, 2);

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
     * NOTE the image always takes up the entire row, this gridX is always 0
     *
     * @param fileIn the file containing the image
     */
    private void displayImage(ImageFile fileIn, int gridY, int gridWidth) {
        //The Image
        if (imgPanel == null) imgPanel = new ImagePanel(fileIn, 750, 750, primaryGui);
        JPanel displayImg = imgPanel;
        displayImg.setSize(750, 750);

        customGrid.setIpadY(750);
        customGrid.setIpadX(750);
        customGrid.addElement(displayImg, 0, gridY, gridWidth);
        customGrid.setIpadY(0);
        customGrid.setIpadX(0);
    }

    /**
     * Make the control buttons for the images
     *
     * @param fileIn      the file, used for making the image if required
     * @param panelSixths the size of each sixth of the grid in the x direction
     */
    private void makeImageControlButtons(ImageFile fileIn, Dimension panelSixths, int gridY) {
        //Generic because it is reused several times
        JButton generic = new JButton("▲");
        generic.addActionListener(e -> {
            imgPanel.panUp();
            loadSolveOptionsGui(fileIn);
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 0, gridY, 1);

        generic = new JButton("▼");
        generic.addActionListener(e -> {
            imgPanel.panDown();
            loadSolveOptionsGui(fileIn);
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 1, gridY, 1);

        generic = new JButton("+");
        generic.addActionListener(e -> {
            imgPanel.zoomIn();
            loadSolveOptionsGui(fileIn);
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 2, gridY, 1);

        generic = new JButton("-");
        generic.addActionListener(e -> {
            imgPanel.zoomOut();
            loadSolveOptionsGui(fileIn);
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 3, gridY, 1);

        generic = new JButton("<");
        generic.addActionListener(e -> {
            imgPanel.panLeft();
            loadSolveOptionsGui(fileIn);
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 4, gridY, 1);

        generic = new JButton(">");
        generic.addActionListener(e -> {
            imgPanel.panRight();
            loadSolveOptionsGui(fileIn);
        });
        generic.setPreferredSize(panelSixths);
        customGrid.addElement(generic, 5, gridY, 1);
    }

    /**
     * Gui that allows the user the save an image
     *
     * @param algorithmUsed the algorithm that was used to solve the maze
     * @param fileIn        file containing the image
     */
    private void loadSaveGui(String algorithmUsed, ImageFile fileIn) {
        customGrid = new CustomGrid(primaryGui);

        //Small title
        JLabel fileName = new JLabel("Solved using " + algorithmUsed);
        fileName.setPreferredSize(panelWhole);
        customGrid.addElement(fileName, 0, 0, 6);

        //The Image
        displayImage(fileIn, 1, 6);

        //Control buttons
        makeImageControlButtons(fileIn, new Dimension(750 / 6, 50), 2);

        JButton save = new JButton("Save");
        save.addActionListener(e -> saveImage(imgPanel.getOriginalImage()));
        save.setPreferredSize(panelThirds);
        customGrid.addElement(save, 0, 3, 2);

        JButton reset = new JButton("Reset Maze");
        reset.addActionListener(e -> {
            customGrid = null;
            imgPanel.setImage(fileIn);
            fileIn.reset();
            loadSolveOptionsGui(fileIn);
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
     *
     * @param image the image to save
     */
    private void saveImage(ImageFile image) {
        JFileChooser save = new JFileChooser();
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

    static class Spinner extends Thread {
        //Spinning wheel
        final JFrame spinnerFrame = new JFrame("Please Wait");
        final int size;


        public Spinner(ImageFile imageFile) {
            size = imageFile.getTrueWidth() * imageFile.getTrueHeight();
        }

        public void run() {
            //Only show the spinner if the maze is large enough
            if (size > 40401) {
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
    }

    public static void main(String[] args) {
        new GUI();
    }
}
