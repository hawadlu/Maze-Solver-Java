import Application.Application;
import Game.Game;
import Image.ImageProcessor;
import Parser.Parser;
import Utility.Exceptions.GenericError;
import Utility.Exceptions.ParserFailure;
import Utility.Location;
import Utility.Node;
import Utility.PathMaker;
import Utility.Thread.AlgorithmDispatcher;
import Parser.Handler;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Tests {

    //TESTS OF NODE SCANNING

    /**
     * Test the scanning of nodes to ensure that the correct number are found
     */
    @Test
    public void testScanNode() {
        Application application = new Application();
        File image = new File("Images/Tiny.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        ImageProcessor processor = new ImageProcessor(application);

        processor.scanAll();
        ConcurrentHashMap<Location, Node> nodes = processor.getNodes();
        int expectedSize = 12;
        assertEquals(expectedSize, nodes.size(), "Size is: " + nodes.size() + ",  expected size: " + expectedSize);
    }


    //TESTS OF THE DEPTH FIRST SEARCH
    @Test
    public void DFSTinyOnly() throws GenericError, InterruptedException {
        String algorithm = "Depth First";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Tiny");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }

    }

    @Test
    public void DFSTwoKOnly() throws GenericError, InterruptedException {
        String algorithm = "Depth First";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "TwoK");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("DFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void DFSUnevenOnly() throws GenericError, InterruptedException {
        String algorithm = "Depth First";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Uneven");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("DFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void DFSSmallOnly() throws GenericError, InterruptedException {
        String algorithm = "Depth First";


        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Small");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("DFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void DFSMediumOnly() throws GenericError, InterruptedException {
        String algorithm = "Depth First";


        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Medium");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("DFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void DFSHugeOnly() throws GenericError, InterruptedException {
        String algorithm = "Depth First";


        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Huge");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("DFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void testDFS() throws InterruptedException, GenericError {
        String algorithm = "Depth First";


        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("DFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }

    }

    /**
     * Test select few images and save the results
     *
     * @throws InterruptedException
     */
    @Test
    public void testDFSSaveResults() throws InterruptedException, GenericError, IOException {
        String algorithm = "Depth First";


        ArrayList<String> testFiles = new ArrayList<>();
        testFiles.add("Tiny.png");
        testFiles.add("Small Imperfect.png");
        testFiles.add("Medium Imperfect.png");
        testFiles.add("Large Imperfect.png");
        testFiles.add("Huge Imperfect.png");
        testFiles.add("OneK Imperfect.png");
        testFiles.add("TwoK Imperfect.png");
        testFiles.add("FourK Imperfect.png");
        testFiles.add("SixK Imperfect.png");

        ResultTracker tracker = new ResultTracker();
        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (String fileStr : testFiles) {
            File file = new File("Images/" + fileStr);
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("DFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    //Add these to the tracker
                    String loading = null, solving = null;
                    if (option.equals("Loading")) loading = "Loading";
                    else if (option.equals("Solving")) solving = "Solving";
                    tracker.addResult(algorithm, fileStr, thread.getMazeSize(), loading, solving, thread.getExecTime(), multi, "Success");
                }
            }
        }

        tracker.saveResult();
    }


    //TESTS OF THE BREADTH FIRST SEARCH
    @Test
    public void BFSTinyOnly() throws GenericError, InterruptedException {
        String algorithm = "Breadth First";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Tiny");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }

    }

    @Test
    public void BFSTwoKOnly() throws GenericError, InterruptedException {
        String algorithm = "Breadth First";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "TwoK");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void BFSUnevenOnly() throws GenericError, InterruptedException {
        String algorithm = "Breadth First";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Uneven");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void BFSSmallOnly() throws GenericError, InterruptedException {
        String algorithm = "Breadth First";

        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Small");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void BFSMediumOnly() throws GenericError, InterruptedException {
        String algorithm = "Breadth First";

        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Medium");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void BFSHugeOnly() throws GenericError, InterruptedException {
        String algorithm = "Breadth First";


        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Huge");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void testBFS() throws InterruptedException, GenericError {
        String algorithm = "Breadth First";


        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }

    }

    /**
     * Test select few images and save the results
     *
     * @throws InterruptedException
     */
    @Test
    public void testBFSSaveResults() throws InterruptedException, GenericError, IOException {
        String algorithm = "Breadth First";


        ArrayList<String> testFiles = new ArrayList<>();
        testFiles.add("Tiny.png");
        testFiles.add("Small Imperfect.png");
        testFiles.add("Medium Imperfect.png");
        testFiles.add("Large Imperfect.png");
        testFiles.add("Huge Imperfect.png");
        testFiles.add("OneK Imperfect.png");
        testFiles.add("TwoK Imperfect.png");
        testFiles.add("FourK Imperfect.png");
        testFiles.add("SixK Imperfect.png");

        ResultTracker tracker = new ResultTracker();
        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (String fileStr : testFiles) {
            File file = new File("Images/" + fileStr);
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    //Add these to the tracker
                    String loading = null, solving = null;
                    if (option.equals("Loading")) loading = "Loading";
                    else if (option.equals("Solving")) solving = "Solving";
                    tracker.addResult(algorithm, fileStr, thread.getMazeSize(), loading, solving, thread.getExecTime(), multi, "Success");
                }
            }
        }

        tracker.saveResult();
    }


    //TESTS OF THE DIJKSTRA SEARCH
    @Test
    public void DijkstraTinyOnly() throws GenericError, InterruptedException {
        String algorithm = "Dijkstra";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Tiny");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }

    }

    @Test
    public void DijkstraTwoKOnly() throws GenericError, InterruptedException {
        String algorithm = "Dijkstra";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "TwoK");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void DijkstraUnevenOnly() throws GenericError, InterruptedException {
        String algorithm = "Dijkstra";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Uneven");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void DijkstraSmallOnly() throws GenericError, InterruptedException {
        String algorithm = "Dijkstra";

        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Small");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void DijkstraMediumOnly() throws GenericError, InterruptedException {
        String algorithm = "Dijkstra";

        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Medium");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void DijkstraHugeOnly() throws GenericError, InterruptedException {
        String algorithm = "Dijkstra";


        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Huge");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void testDijkstra() throws InterruptedException, GenericError {
        String algorithm = "Dijkstra";


        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }

    }

    /**
     * Test select few images and save the results
     *
     * @throws InterruptedException
     */
    @Test
    public void testDijkstraSaveResults() throws InterruptedException, GenericError, IOException {
        String algorithm = "Dijkstra";


        ArrayList<String> testFiles = new ArrayList<>();
        testFiles.add("Tiny.png");
        testFiles.add("Small Imperfect.png");
        testFiles.add("Medium Imperfect.png");
        testFiles.add("Large Imperfect.png");
        testFiles.add("Huge Imperfect.png");
        testFiles.add("OneK Imperfect.png");
        testFiles.add("TwoK Imperfect.png");
        testFiles.add("FourK Imperfect.png");
        testFiles.add("SixK Imperfect.png");

        ResultTracker tracker = new ResultTracker();
        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (String fileStr : testFiles) {
            File file = new File("Images/" + fileStr);
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println(algorithm + " " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    //Add these to the tracker
                    String loading = null, solving = null;
                    if (option.equals("Loading")) loading = "Loading";
                    else if (option.equals("Solving")) solving = "Solving";
                    tracker.addResult(algorithm, fileStr, thread.getMazeSize(), loading, solving, thread.getExecTime(), multi, "Success");
                }
            }
        }

        tracker.saveResult();
    }


    //TESTS OF THE ASTAR SEARCH
    @Test
    public void AStarTinyOnly() throws GenericError, InterruptedException {
        String algorithm = "AStar";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Tiny");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }

    }

    @Test
    public void AStarTwoKOnly() throws GenericError, InterruptedException {
        String algorithm = "AStar";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "TwoK");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void AStarUnevenOnly() throws GenericError, InterruptedException {
        String algorithm = "AStar";


        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Uneven");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void AStarSmallOnly() throws GenericError, InterruptedException {
        String algorithm = "AStar";

        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Small");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void AStarMediumOnly() throws GenericError, InterruptedException {
        String algorithm = "AStar";

        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Medium");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void AStarHugeOnly() throws GenericError, InterruptedException {
        String algorithm = "AStar";


        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Huge");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};

        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }
    }

    @Test
    public void testAStar() throws InterruptedException, GenericError {
        String algorithm = "AStar";


        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};
        for (File file : files) {
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println("BFS solve " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    application.saveImage("Images/Solved/Test " + algorithm + " " + option + " multi threading " + multi + " " + file.getName());
                    System.out.println("Complete");
                }
            }
        }

    }

    /**
     * Test select few images and save the results
     *
     * @throws InterruptedException
     */
    @Test
    public void testAStarSaveResults() throws InterruptedException, GenericError, IOException {
        String algorithm = "AStar";


        ArrayList<String> testFiles = new ArrayList<>();
        testFiles.add("Tiny.png");
        testFiles.add("Small Imperfect.png");
        testFiles.add("Medium Imperfect.png");
        testFiles.add("Large Imperfect.png");
        testFiles.add("Huge Imperfect.png");
        testFiles.add("OneK Imperfect.png");
        testFiles.add("TwoK Imperfect.png");
        testFiles.add("FourK Imperfect.png");
        testFiles.add("SixK Imperfect.png");

        ResultTracker tracker = new ResultTracker();
        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (String fileStr : testFiles) {
            File file = new File("Images/" + fileStr);
            for (String option : options) {
                for (Boolean multi : threading) {
                    System.out.println(algorithm + " " + file.getName() + " " + option);

                    Application application = new Application();
                    application.parseImageFile(file);

                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                    thread.start();
                    thread.join();
                    System.out.println("Thread complete");

                    //Add these to the tracker
                    String loading = null, solving = null;
                    if (option.equals("Loading")) loading = "Loading";
                    else if (option.equals("Solving")) solving = "Solving";
                    tracker.addResult(algorithm, fileStr, thread.getMazeSize(), loading, solving, thread.getExecTime(), multi, "Success");
                }
            }
        }

        tracker.saveResult();
    }


    //TEST ALL OF THE ALGORITHMS

    /**
     * Test select few images and save the results
     *
     * @throws InterruptedException
     */
    @Test
    public void testAllSolversSaveResults() throws InterruptedException, GenericError, IOException {
        String[] algorithmArr = new String[]{"Depth First", "Breadth First", "Dijkstra", "AStar"};

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        ResultTracker tracker = new ResultTracker();
        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (File file : files) {
            for (String option : options) {
                //Check if the maze is too big for this option
                if (option.equals("Loading") && (file.getName().contains("Eight") || file.getName().contains("Ten")))
                    ; //Do nothing
                else {
                    for (String algorithm : algorithmArr) {
                        if ((algorithm.equals("Depth First") || algorithm.equals("Breadth First")) && (file.getName().contains("Eight") || file.getName().contains("Ten")))
                            ; //Do nothing
                        else {
                            for (Boolean multi : threading) {
                                System.out.println(algorithm + " " + file.getName() + " " + option);

                                Application application = new Application();
                                application.parseImageFile(file);

                                AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                                thread.start();
                                thread.join();
                                System.out.println("Thread complete");

                                //Add these to the tracker
                                String loading = null, solving = null;
                                if (option.equals("Loading")) loading = "Loading";
                                else if (option.equals("Solving")) solving = "Solving";
                                tracker.addResult(algorithm, file.getName(), thread.getMazeSize(), loading, solving, thread.getExecTime(), multi, "Success");
                            }
                        }
                    }
                }
            }
        }

        tracker.saveResult();
    }


    //TEST PRIMS ALGORITHM
    @Test
    public void PrimsTinyOnly() throws GenericError, InterruptedException {
        String algorithm = "Prims";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Tiny");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void PrimsTwoKOnly() throws GenericError, InterruptedException {
        String algorithm = "Prims";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "TwoK");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void PrimsUnevenOnly() throws GenericError, InterruptedException {
        String algorithm = "Prims";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Uneven");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void PrimsSmallOnly() throws GenericError, InterruptedException {
        String algorithm = "Prims";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Small");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void PrimsMediumOnly() throws GenericError, InterruptedException {
        String algorithm = "Prims";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Medium");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void PrimsHugeOnly() throws GenericError, InterruptedException {
        String algorithm = "Prims";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Huge");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void testPrims() throws InterruptedException, GenericError {
        String algorithm = "Prims";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }

    }

    /**
     * Test select few images and save the results
     *
     * @throws InterruptedException
     */
    @Test
    public void testPrimsSaveResults() throws InterruptedException, GenericError, IOException {
        String algorithm = "Prims";


        ArrayList<String> testFiles = new ArrayList<>();
        testFiles.add("Tiny.png");
        testFiles.add("Small Imperfect.png");
        testFiles.add("Medium Imperfect.png");
        testFiles.add("Large Imperfect.png");
        testFiles.add("Huge Imperfect.png");
        testFiles.add("OneK Imperfect.png");
        testFiles.add("TwoK Imperfect.png");
        testFiles.add("FourK Imperfect.png");
        testFiles.add("SixK Imperfect.png");

        ResultTracker tracker = new ResultTracker();
        ;

        for (String fileStr : testFiles) {
            File file = new File("Images/" + fileStr);
            System.out.println(algorithm + " " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            //Add these to the tracker
            String loading = null, solving = null;
            tracker.addResult(algorithm, fileStr, thread.getMazeSize(), loading, solving, thread.getExecTime(), false, "Success");
        }

        tracker.saveResult();
    }


    //TEST KRUSKALS ALGORITHM
    @Test
    public void KruskalsTinyOnly() throws GenericError, InterruptedException {
        String algorithm = "Kruskals";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Tiny");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void KruskalsTwoKOnly() throws GenericError, InterruptedException {
        String algorithm = "Kruskals";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "TwoK");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void KruskalsUnevenOnly() throws GenericError, InterruptedException {
        String algorithm = "Kruskals";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Uneven");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void KruskalsSmallOnly() throws GenericError, InterruptedException {
        String algorithm = "Kruskals";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Small");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void KruskalsMediumOnly() throws GenericError, InterruptedException {
        String algorithm = "Kruskals";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Medium");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void KruskalsHugeOnly() throws GenericError, InterruptedException {
        String algorithm = "Kruskals";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Huge");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void testKruskals() throws InterruptedException, GenericError {
        String algorithm = "Kruskals";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }

    }

    /**
     * Test select few images and save the results
     *
     * @throws InterruptedException
     */
    @Test
    public void testKruskalsSaveResults() throws InterruptedException, GenericError, IOException {
        String algorithm = "Kruskals";


        ArrayList<String> testFiles = new ArrayList<>();
        testFiles.add("Tiny.png");
        testFiles.add("Small Imperfect.png");
        testFiles.add("Medium Imperfect.png");
        testFiles.add("Large Imperfect.png");
        testFiles.add("Huge Imperfect.png");
        testFiles.add("OneK Imperfect.png");
        testFiles.add("TwoK Imperfect.png");
        testFiles.add("FourK Imperfect.png");
        testFiles.add("SixK Imperfect.png");

        ResultTracker tracker = new ResultTracker();
        ;

        for (String fileStr : testFiles) {
            File file = new File("Images/" + fileStr);
            System.out.println(algorithm + " " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            //Add these to the tracker
            String loading = null, solving = null;
            tracker.addResult(algorithm, fileStr, thread.getMazeSize(), loading, solving, thread.getExecTime(), false, "Success");
        }

        tracker.saveResult();
    }


    //TEST ARTICULATION ALGORITHM
    @Test
    public void ArticulationTinyOnly() throws GenericError, InterruptedException {
        String algorithm = "Articulation";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Tiny");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "Loading", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void ArticulationTwoKOnly() throws GenericError, InterruptedException {
        String algorithm = "Articulation";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "TwoK");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "Loading", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void ArticulationUnevenOnly() throws GenericError, InterruptedException {
        String algorithm = "Articulation";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Uneven");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "Loading", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void ArticulationSmallOnly() throws GenericError, InterruptedException {
        String algorithm = "Articulation";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Small");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "Loading", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void ArticulationMediumOnly() throws GenericError, InterruptedException {
        String algorithm = "Articulation";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Medium");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "Loading", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void ArticulationHugeOnly() throws GenericError, InterruptedException {
        String algorithm = "Articulation";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, "Huge");

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "Loading", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }
    }

    @Test
    public void testArticulation() throws InterruptedException, GenericError {
        String algorithm = "Articulation";

        //Delete all the files in the solved folder
        deleteFiles(new File("Images/Solved"));

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);
        for (File file : files) {
            System.out.println("Prims solve " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "Loading", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());
            System.out.println("Complete");
        }

    }

    /**
     * Test select few images and save the results
     *
     * @throws InterruptedException
     */
    @Test
    public void testArticulationSaveResults() throws InterruptedException, GenericError, IOException {
        String algorithm = "Articulation";


        ArrayList<String> testFiles = new ArrayList<>();
        testFiles.add("Tiny.png");
        testFiles.add("Small Imperfect.png");
        testFiles.add("Medium Imperfect.png");
        testFiles.add("Large Imperfect.png");
        testFiles.add("Huge Imperfect.png");
        testFiles.add("OneK Imperfect.png");
        testFiles.add("TwoK Imperfect.png");
        testFiles.add("FourK Imperfect.png");
        testFiles.add("SixK Imperfect.png");

        ResultTracker tracker = new ResultTracker();
        ;

        for (String fileStr : testFiles) {
            File file = new File("Images/" + fileStr);
            System.out.println(algorithm + " " + file.getName());

            Application application = new Application();
            application.parseImageFile(file);

            AlgorithmDispatcher thread = application.solve(algorithm, "", false, 0, null, null);
            thread.start();
            thread.join();
            System.out.println("Thread complete");

            //Add these to the tracker
            String loading = null, solving = null;
            tracker.addResult(algorithm, fileStr, thread.getMazeSize(), loading, solving, thread.getExecTime(), false, "Success");
        }

        tracker.saveResult();
    }


    //TEST ALL OF THE ALGORITHMS
    @Test
    public void testAlgoSaveResults() throws IOException, GenericError, InterruptedException {
        deleteFiles(new File("Images/Solved"));

        String[] solveAlgo = new String[]{"Depth First", "Breadth First", "Dijkstra", "AStar"};
        String[] loadOnlyAlgo = new String[]{"Prims", "Kruskals", "Articulation"};

        ArrayList<File> files = getAllFiles(new File("Images"));

        //Remove anything that is not an image
        files = removeNonImages(files, false, null);

        Comparator<File> smallest = (File f1, File f2) -> {
            if (f1.length() < f2.length()) return -1;
            if (f1.length() > f2.length()) return 1;
            return 0;
        };

        files.sort(smallest);

        System.out.println("files: " + files);

        ResultTracker tracker = new ResultTracker();
        String[] options = {"Loading", "Solving"};
        Boolean[] threading = {true, false};

        for (File file : files) {
            //Do the solve algorithms
            for (String option : options) {
                //Check if the maze is too big for this option
                if (option.equals("Loading") && (file.getName().contains("Eight") || file.getName().contains("Ten")))
                    ; //Do nothing
                else {
                    for (String algorithm : solveAlgo) {
                        if ((algorithm.equals("Depth First") || algorithm.equals("Breadth First")) && (file.getName().contains("Eight") || file.getName().contains("Ten")))
                            ; //Do nothing
                        else {
                            for (Boolean multi : threading) {
                                //Add these to the tracker
                                String loading = null, solving = null;
                                if (option.equals("Loading")) loading = "Loading";
                                else if (option.equals("Solving")) solving = "Solving";

                                try {
                                    System.out.println(algorithm + " " + file.getName() + " " + option);

                                    Application application = new Application();
                                    application.parseImageFile(file);

                                    AlgorithmDispatcher thread = application.solve(algorithm, option, multi, 0, null, null);
                                    thread.start();
                                    thread.join();
                                    System.out.println("Thread complete");

                                    application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName() + " " + multi);
                                    tracker.addResult(algorithm, file.getName(), thread.getMazeSize(), loading, solving, thread.getExecTime(), multi, "Success");
                                } catch (Exception e) {
                                    tracker.addResult(algorithm, file.getName(), 0, loading, solving, 0, multi, "Failed");
                                }
                            }
                        }
                    }
                }
            }

            for (String algorithm : loadOnlyAlgo) {
                Application application = new Application();
                application.parseImageFile(file);

                AlgorithmDispatcher thread = application.solve(algorithm, "Loading", false, 0, null, null);
                thread.start();
                thread.join();
                System.out.println("Thread complete");

                application.saveImage("Images/Solved/Test " + algorithm + " " + file.getName());

                //Add these to the tracker
                String loading = "Loading", solving = null;
                tracker.addResult(algorithm, file.getName(), thread.getMazeSize(), loading, solving, thread.getExecTime(), false, "Success");
            }
        }

        tracker.saveResult();
    }


    //TEST THE WORKER THREAD
    @Test
    public void testWorkerThread() throws InterruptedException {
        String algorithm = "Breadth First";


        Application application = new Application();
        File image = new File("Images/TwoK Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        AlgorithmDispatcher thread = new AlgorithmDispatcher(algorithm, "Loading", application, "test", false, 0, null);
        thread.start();

        thread.join(); //Wait for the other thread to finish

        System.out.println("Thread finished");
    }


    //TEST SAVING
    @Test
    public void testSave() throws InterruptedException {
        String algorithm = "Breadth First";

        Application application = new Application();
        File image = new File("Images/Small Imperfect.png");
        try {
            application.parseImageFile(image);
        } catch (GenericError genericError) {
            genericError.printStackTrace();
            System.out.println("Failed to parse image");
        }

        AlgorithmDispatcher thread = new AlgorithmDispatcher(algorithm, "Loading", application, "test", false, 0, null);
        thread.start();

        thread.join(); //Wait for the other thread to finish

        System.out.println("Thread finished");

        application.saveImage("Images/Solved/Test.png");
    }

    /**
     * @param dir the directory to check
     * @return all the files in the directory
     */
    private ArrayList<File> getAllFiles(final File dir) {
        ArrayList<File> files = new ArrayList<>();
        for (final File fileEntry : dir.listFiles()) {
            if (!fileEntry.isDirectory()) files.add(fileEntry);
        }
        return files;
    }

    /**
     * Remove any files without a supported extension
     *
     * @param files arraylist of files.
     */
    private ArrayList<File> removeNonImages(ArrayList<File> files, boolean inclDeliberateInvalid, String optionParam) {
        ArrayList<File> toReturn = new ArrayList<>();
        for (File file : files) {
            if (file.getName().contains(".png") || file.getName().contains(".jpg") || file.getName().contains(".jpeg")) {
                if (!inclDeliberateInvalid && !file.getName().contains("Invalid")) {
                    if (optionParam != null) {
                        if (file.getName().contains(optionParam)) toReturn.add(file);
                    } else toReturn.add(file);
                }
            }
        }
        return toReturn;
    }

    /**
     * Delete all the files in a given folder.
     *
     * @param file the folder
     */
    private void deleteFiles(File file) {
        System.out.println("Deleting images");
        for (File toDelete : file.listFiles()) {
            if (!toDelete.isDirectory()) toDelete.delete();
        }
    }

    //TEST THE PARSER
    @Test
    public void testDFSParserTiny() {
        Application application = new Application();
        try {
            application.parseImageFile(new File("Images/Tiny.png"));
        } catch (GenericError genericError) {
            genericError.printStackTrace();
        }

        application.scanEntireMaze();


        File dfs = new File("Programs/Working Algorithms/DFS.txt");
        Parser p = new Parser(dfs);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/DFS Custom Algorithm.png");
        System.out.println("Saved image");
    }

    @Test
    public void testDFSParserSmall() {
        Application application = new Application();
        try {
            application.parseImageFile(new File("Images/Small Imperfect.png"));
        } catch (GenericError genericError) {
            genericError.printStackTrace();
        }

        application.scanEntireMaze();

        File dfs = new File("Programs/Working Algorithms/DFS.txt");
        Parser p = new Parser(dfs);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/DFS Small Imperfect Custom Algorithm.png");
        System.out.println("Saved image");
    }

    @Test
    public void testBFSParserTiny() {
        Application application = new Application();
        try {
            application.parseImageFile(new File("Images/Tiny.png"));
        } catch (GenericError genericError) {
            genericError.printStackTrace();
        }

        application.scanEntireMaze();

        File dfs = new File("Programs/Working Algorithms/BFS.txt");
        Parser p = new Parser(dfs);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/BFS Custom Algorithm.png");
        System.out.println("Saved image");
    }

    @Test
    public void testBFSParserSmall() {
        Application application = new Application();
        try {
            application.parseImageFile(new File("Images/Small Imperfect.png"));
        } catch (GenericError genericError) {
            genericError.printStackTrace();
        }

        application.scanEntireMaze();

        File dfs = new File("Programs/Working Algorithms/BFS.txt");
        Parser p = new Parser(dfs);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/BFS Small Imperfect Custom Algorithm.png");
        System.out.println("Saved image");
    }

    @Test
    public void testDijkstraParserTiny() {
        Application application = new Application();
        try {
            application.parseImageFile(new File("Images/Tiny.png"));
        } catch (GenericError genericError) {
            genericError.printStackTrace();
        }

        application.scanEntireMaze();

        File dfs = new File("Programs/Working Algorithms/Dijkstra.txt");
        Parser p = new Parser(dfs);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/Dijkstra Custom Algorithm.png");
        System.out.println("Saved image");
    }

    @Test
    public void testDijkstraParserSmall() {
        Application application = new Application();
        try {
            application.parseImageFile(new File("Images/Small Imperfect.png"));
        } catch (GenericError genericError) {
            genericError.printStackTrace();
        }

        application.scanEntireMaze();

        File dfs = new File("Programs/Working Algorithms/Dijkstra.txt");
        Parser p = new Parser(dfs);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/Dijkstra Small Imperfect Custom Algorithm.png");
        System.out.println("Saved image");
    }

    @Test
    public void testAStarParserTiny() {

        Application application = new Application();
        try {
            application.parseImageFile(new File("Images/Tiny.png"));
        } catch (GenericError genericError) {
            genericError.printStackTrace();
        }

        application.scanEntireMaze();


        File dfs = new File("Programs/Working Algorithms/AStar.txt");
        Parser p = new Parser(dfs);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/AStar Custom Algorithm.png");
        System.out.println("Saved image");
    }

    @Test
    public void testAStarParserSmall() {
        Application application = new Application();
        try {
            application.parseImageFile(new File("Images/Small Imperfect.png"));
        } catch (GenericError genericError) {
            genericError.printStackTrace();
        }

        application.scanEntireMaze();

        File dfs = new File("Programs/Working Algorithms/AStar.txt");
        Parser p = new Parser(dfs);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/AStar Small Imperfect Custom Algorithm.png");
        System.out.println("Saved image");
    }

    @Test
    public void testAllParsersTiny() {
        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Programs/Working Algorithms"));

        for (File file : files) {
            System.out.println("File: " + file);

            Application application = new Application();
            try {
                application.parseImageFile(new File("Images/Tiny.png"));
            } catch (GenericError genericError) {
                genericError.printStackTrace();
            }

            application.scanEntireMaze();

            Parser p = new Parser(file);
            Handler handler = new Handler(application);
            p.setMazeHandler(handler);
            p.compile();
            p.print();
            p.execute(application);

            application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
            application.saveImage("Images/Solved/" + file.getName() + " Tiny Custom Algorithm.png");
            System.out.println("Saved image");
        }
    }

    @Test
    public void testAllParsersSmall() {
        deleteFiles(new File("Images/Solved"));
        ArrayList<File> files = getAllFiles(new File("Programs/Working Algorithms"));

        for (File file : files) {
            System.out.println("File: " + file);

            Application application = new Application();
            try {
                application.parseImageFile(new File("Images/Small Imperfect2.png"));
            } catch (GenericError genericError) {
                genericError.printStackTrace();
            }

            application.scanEntireMaze();

            Parser p = new Parser(file);
            Handler handler = new Handler(application);
            p.setMazeHandler(handler);
            p.compile();
            p.print();
            p.execute(application);

            application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
            application.saveImage("Images/Solved/" + file.getName() + " Small Imperfect 2 Custom Algorithm.png");
            System.out.println("Saved image");
        }
    }

    @Test
    public void testGame() throws InterruptedException {
        Thread runner = new Thread() {
            @Override
            public void run() {
                Application application = new Application();
                try {
                    application.parseImageFile(new File("Images/Tiny.png"));
                } catch (GenericError genericError) {
                    genericError.printStackTrace();
                }
                application.scanEntireMaze();


                Parser pOne = new Parser(new File("Programs/Working Algorithms/AStar No Print.txt"));
                Parser pTwo = new Parser(new File("Programs/Working Algorithms/AStar No Print.txt"));


                pOne.setMazeHandler(new Handler(application));
                pTwo.setMazeHandler(new Handler(application));

                pOne.compile();
                pTwo.compile();

                Game game = new Game(pOne, pTwo, application);

                game.startPlayers(25);
            }
        };

        runner.start();
        runner.join();
        System.out.println("Done");
    }

    /**
     * Run all of the tests in the valid tests folder
     */
    @Test
    public void testValid() throws GenericError {
        File invalidDir = new File("Programs/Valid Tests/");
        File imageFile = new File("Images/Tiny.png");
        ArrayList<File> directories = new ArrayList<>();
        directories.add(invalidDir);

        while (!directories.isEmpty()) {
            File directory = directories.remove(0);
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) directories.add(file);
                else if (file.getName().contains(".solver")) {
                    System.out.println("File: " + file);

                    Application application = new Application();
                    application.parseImageFile(imageFile);
                    application.scanEntireMaze();

                    Parser p = new Parser(file, false);
                    Handler handler = new Handler(application);
                    p.setMazeHandler(handler);
                    p.compile();
                    p.execute(application);

                    System.out.print("\n\n\n");
                }
            }
        }
    }

    /**
     * Launch a UI file chooser to
     */
    @Test
    public void runAnyParser() throws GenericError {
        File image = new File("Images/Tiny.png");
        File parser = new File("Programs/Working Algorithms/AStar.txt");

        Application application = new Application();
        application.parseImageFile(image);
        application.scanEntireMaze();

        Parser p = new Parser(parser);
        Handler handler = new Handler(application);
        p.setMazeHandler(handler);
        p.compile();
        p.print();
        p.execute(application);

        application.getImageFile().fillNodePath(PathMaker.generatePathArraylist(handler.getLastNode()), true);
        application.saveImage("Images/Solved/" + image.getName() + " Small Imperfect 2 Custom Algorithm" + parser.getName() + ".png");
        System.out.println("Saved image");
    }

    @Test
    public void testInvalid() throws GenericError {
        File invalidDir = new File("Programs/Invalid Tests/");
        File imageFile = new File("Images/Tiny.png");
        ArrayList<File> directories = new ArrayList<>();
        directories.add(invalidDir);

        while (!directories.isEmpty()) {
            File directory = directories.remove(0);
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) directories.add(file);
                else if (file.getName().contains(".solver")) {
                    System.out.println("File: " + file);

                    Application application = new Application();
                    application.parseImageFile(imageFile);
                    application.scanEntireMaze();

                    Parser p = new Parser(file, false);
                    Handler handler = new Handler(application);
                    p.setMazeHandler(handler);

                    Exception exception = assertThrows(ParserFailure.class, () -> {
                        p.compile();
                        p.execute(application);
                    });

                    String expectedMessage = "FAIL";
                    String actualMessage = exception.getMessage();
                    System.out.println(exception.getMessage());
                    assertTrue(actualMessage.contains(expectedMessage));
                    System.out.print("\n\n\n");
                }
            }
        }
    }

    @Test
    public void rename() {
        File dir = new File("Programs");
        ArrayList<File> directories = new ArrayList<>();
        directories.add(dir);

        while (!directories.isEmpty()) {
            File directory = directories.remove(0);
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) directories.add(file);
                else if (!file.getName().contains(".solver")) {
                    String newPath = file.getPath() + ".solver";
                    File newFile = new File(newPath);
                    file.renameTo(newFile);
                }
            }
        }
    }

    /**
     * Class used for tracking and saving test results
     */
    private class ResultTracker {
        ArrayList<TestResult> results = new ArrayList<>();

        public void addResult(String algorithm, String file, double size, String onLoad, String onSolve, long execTime, boolean multiThreading, String success) {
            results.add(new TestResult(algorithm, file, size, onLoad, onSolve, execTime, multiThreading, success));
        }

        /**
         * Create a CSV file and save the results
         */
        public void saveResult() throws IOException {
            long timeVal = new Date().getTime();
            Timestamp time = new Timestamp(timeVal);
            String timeStr = time.toString();
            File newCSV = new File("Test Results/Test Results (" + timeStr + ").csv");
            FileWriter csv = new FileWriter(newCSV);

            //Add the headers
            csv.append("File, Algorithm, Size, Load, solve, execTime (ns), Multi Threading, Success\n");

            //Add each of the results
            for (TestResult test : results) csv.append(test.printCSV());

            csv.flush();
            csv.close();
        }
    }

    /**
     * Holds each individual result
     */
    private class TestResult {
        String fileName, onLoad, onSolve, algorithm, success = "success";
        long execTime;
        boolean multiThreading;
        double size;

        public TestResult(String algorithm, String fileName, double size, String onLoad, String onSolve, long execTime, boolean multiThreading, String success) {
            this.fileName = fileName;
            this.onLoad = onLoad;
            this.onSolve = onSolve;
            this.execTime = execTime;
            this.multiThreading = multiThreading;
            this.size = size;
            this.algorithm = algorithm;
            this.success = success;
        }

        /**
         * @return return the data in csv format
         */
        public String printCSV() {
            return fileName + ", " + algorithm + ", " + size + ", " + onLoad + ", " + onSolve + ", " + execTime + ", " + multiThreading + " " + success + "\n";
        }
    }
}


