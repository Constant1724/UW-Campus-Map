package hw8;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import hw6.utils.LabelledParameterized;
import hw6.utils.LabelledParameterized.*;

/**
 * This class, along with HW8TestDriver, can be used to testPathAllBuildings.test your Campus Paths application. It is
 * assumed that the files are located in the same directory.
 *
 * <p>It works by parameterizing testPathAllBuildings.test methods over some data values, and then creating an instance
 * for the cross-product of testPathAllBuildings.test methods and data values. In this case, it will create one
 * HW8TestDriver instance per .expected file, and for each of those it will run the
 * checkAgainstExpectedOutput() testPathAllBuildings.test. See the JUnit4 Javadocs for more information, or Google for
 * more examples.
 */
@RunWith(LabelledParameterized.class)
public class ScriptFileTests {

  // static fields and methods used during setup of the parameterized runner
  private static FileFilter testFileFilter =
      new FileFilter() {
        public boolean accept(File file) {
          return file.getName().endsWith(".testPathAllBuildings.test");
        }
      };
  private static List<String> testScriptNames = null; // not yet calculated
  private static List<File> testScriptFiles = null; // not yet calculated

  // used by the actual testPathAllBuildings.test instance
  private final File testScriptFile;

  /**
   * This method searches for and creates file handles for each script testPathAllBuildings.test. It only searches the
   * immediate directory where the ScriptFileTests.class classfile is located.
   */
  public static void calculateTestFiles() {
    if (ScriptFileTests.testScriptFiles != null || ScriptFileTests.testScriptNames != null) {
      // already initialized
      return;
    }

    ScriptFileTests.testScriptNames = new LinkedList<String>();
    ScriptFileTests.testScriptFiles = new LinkedList<File>();
    try {
      // getResource() cannot be null: this file itself is ScriptFileTests
      // getParentFile() cannot be null: ScriptFileTests has a package
      File myDirectory =
          new File(ScriptFileTests.class.getResource("ScriptFileTests.class").toURI())
              .getParentFile();
      for (File f : myDirectory.listFiles(ScriptFileTests.testFileFilter)) {
        testScriptNames.add(f.getName());
        testScriptFiles.add(f);
      }

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This method is called in the constructor of Parameterized.
   *
   * @return list of argument arrays that should be invoked on the ScriptFileTests constructor by
   *     the Parameterized testPathAllBuildings.test runner. Since that runner's constructor has one parameter, the array
   *     only has one element.
   */
  @Parameters
  public static List<Object[]> getTestFiles() {
    ScriptFileTests.calculateTestFiles();

    if (ScriptFileTests.testScriptFiles == null)
      throw new IllegalStateException("Did not initialise any files to testPathAllBuildings.test");

    // we have to wrap testScriptFiles here so Parameterized.class receives a list of arg array.
    List<Object[]> filesToTest = new ArrayList<>(testScriptFiles.size());
    for (File f : ScriptFileTests.testScriptFiles) {
      filesToTest.add(new Object[] {f});
    }

    return filesToTest;
  }

  /**
   * This method is called in the constructor of LabelledParameterized. Since getTestFiles (and thus
   * calculateTestFiles()) should have already been called by the Parameterized constructor, the
   * testPathAllBuildings.test script names should already have been computed.
   *
   * @return list of labels to be used as names for each of the parameterized tests. These names are
   *     the same as the script file used to run the testPathAllBuildings.test.
   */
  @Labels
  public static List<String> getTestLabels() {
    if (ScriptFileTests.testScriptNames == null)
      throw new IllegalStateException("Must initialize list of testPathAllBuildings.test names before creating tests.");

    return ScriptFileTests.testScriptNames;
  }

  /**
   * This constructor is reflectively called by the Parameterized runner. It creates a script file
   * testPathAllBuildings.test instance, representing one script file to be tested.
   */
  public ScriptFileTests(File testScriptFile) {
    this.testScriptFile = testScriptFile;
  }

  /**
   * Reads in the contents of a file.
   *
   * @throws FileNotFoundException, IOException
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

  /**
   * @throws IOException
   * @spec.requires there exists a testPathAllBuildings.test file indicated by testScriptFile
   * @spec.effects runs the testPathAllBuildings.test in filename, and output its results to a file in the same directory
   *     with name filename+".actual"; if that file already exists, it will be overwritten.
   * @returns the contents of the output file
   */
  private String runScriptFile() throws IOException {
    if (testScriptFile == null) {
      throw new RuntimeException("No file specified");
    }

    File actual = fileWithSuffix("actual");

    HW8TestDriver td = new HW8TestDriver(testScriptFile, actual);
    td.runTests();

    return fileContents(actual);
  }

  /**
   * @param newSuffix
   * @return a File with the same name as testScriptFile, except that the testPathAllBuildings.test suffix is replaced by
   *     the given suffix
   */
  private File fileWithSuffix(String newSuffix) {
    File parent = testScriptFile.getParentFile();
    String driverName = testScriptFile.getName();
    String baseName = driverName.substring(0, driverName.length() - "testPathAllBuildings.test".length());

    return new File(parent, baseName + newSuffix);
  }

  /**
   * The only testPathAllBuildings.test that is run: run a script file and testPathAllBuildings.test its output.
   *
   * @throws IOException
   */
  @Test(timeout = 30000)
  public void checkAgainstExpectedOutput() throws IOException {
    File expected = fileWithSuffix("expected");
    assertEquals(testScriptFile.getName(), fileContents(expected), runScriptFile());
  }
}
