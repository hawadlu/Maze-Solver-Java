package Tests;

import java.io.File;
import java.util.ArrayList;

/**
 * Class containing utility methods used by the tests.
 */
public class TestUtilities {
  /**
   * Delete all the files in a given folder.
   *
   * @param file the folder
   */
  public static void deleteFiles(File file) {
    System.out.println("Deleting images");
    if (file.listFiles() != null) {
      for (File toDelete : file.listFiles()) {
        if (!toDelete.isDirectory()) toDelete.delete();
      }
    }
  }

  /**
   * @param dir the directory to check
   * @return all the files in the directory
   */
  public static ArrayList<File> getAllFiles(final File dir) {
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
  public static ArrayList<File> removeNonImages(ArrayList<File> files, boolean inclDeliberateInvalid, String optionParam) {
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
}
