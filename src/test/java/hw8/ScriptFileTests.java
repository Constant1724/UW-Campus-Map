package hw8;

import campuspaths.CampusPathsApplication;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class, along with HW8TestDriver, can be used to test your Campus Paths application. It is
 * assumed that the files are located in the same directory.
 *
 * <p>It works by parameterizing test methods over some data values, and then creating an instance
 * for the cross-product of test methods and data values. In this case, it will create one
 * HW8TestDriver instance per .expected file, and for each of those it will run the
 * checkAgainstExpectedOutput() test. See the JUnit4 Javadocs for more information, or Google for
 * more examples.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={CampusPathsApplication.class})
public class ScriptFileTests {

  private static List<File> testFiles = null;

  @BeforeClass
  public static void setup() {
    calculateTestFiles();
  }

  /**
   * This method searches for and creates file handles for each script test. It only searches the
   * immediate directory where the ScriptFileTests.class classfile is located.
   */
  public static void calculateTestFiles() {
    testFiles = new LinkedList<>();
    File dir = new File("src/test/java/hw8/");
    File[] filesList = dir.listFiles();
    for (File file : filesList) {
      if (file.isFile()) {
        String type = file.getName().split("[.]")[1];
        if (type.equalsIgnoreCase("test")) {
          testFiles.add(file);
        }
      }
    }
  }

  @Test
  public void testFiles() throws IOException {
    for(File test : testFiles) {
      File expected = new File("src/test/java/hw8/" + test.getName().split("[.]")[0] + ".expected");
      String expectedContents = fileContents(expected);
      String actualContents = runScriptFile(test);
      assertEquals(test.getName(), expectedContents, actualContents);
    }
  }

  /**
   * @throws IOException
   * @spec.requires there exists a test file indicated by testScriptFile
   * @spec.effects runs the test in filename, and output its results to a file in the same directory
   *     with name filename+".actual"; if that file already exists, it will be overwritten.
   * @returns the contents of the output file
   */
  private String runScriptFile(File testFile) throws IOException {

    File actual = fileWithSuffix(testFile, "actual");
    HW8TestDriver td = new HW8TestDriver(testFile, actual);
    td.runTests();
    return fileContents(actual);
  }

  /**
   * @param newSuffix
   * @return a File with the same name as testScriptFile, except that the test suffix is replaced by
   *     the given suffix
   */
  private File fileWithSuffix(File testFile, String newSuffix) {
    File parent = testFile.getParentFile();
    String driverName = testFile.getName();
    String baseName = driverName.substring(0, driverName.length() - "test".length());

    return new File(parent, baseName + newSuffix);
  }

  /**
   * Reads in the contents of a file.
   *
   * @throws IOException, IOException
   * @spec.requires that the specified File exists && File ends with a newline
   * @returns the contents of that file
   */
  private String fileContents(File f) throws IOException {
    if (f == null) {
      throw new IllegalArgumentException("No file specified");
    }

    BufferedReader br = new BufferedReader(new FileReader(f));

    StringBuilder result = new StringBuilder();
    String line = null;

    // read line reads up to *any* newline character
    while ((line = br.readLine()) != null) {
      result.append(line.trim());
      result.append('\n');
    }

    br.close();
    return result.toString();
  }

}
