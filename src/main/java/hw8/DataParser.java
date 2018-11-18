package hw8;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.awt.geom.Arc2D;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DataParser {

    /**
     *  Reads Campus Path data set. Each line of the input file represents a path and is defined by:
     *      x1,y1   x2,y2   distance
     *
     *      x1,y1 is the start and x2,y2 is the end. distance is the distance of from x1,y1 to x2,y2.
     *      All separated by tab.
     *
     *      There should not be duplicate paths in data set.
     *
     * @spec.requires filename is a valid file path.
     * @param filename the name of the file that will be read
     * @return a list of CampusPath Objects,
     *          each one representing an edge defined by one line in the data set, respectively.
     */
  public static List<CampusPath> parsePathData(String filename) {

    CsvToBean<CampusPath> csvToBean = null;

    try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
      csvToBean = new CsvToBeanBuilder<CampusPath>(reader)
              .withType(CampusPath.class)
              .withIgnoreLeadingWhiteSpace(true)
              .build();
    } catch (IOException e) {
      System.err.println(e.toString());
      e.printStackTrace(System.err);
    }
    return csvToBean.parse();
  }

    /**
     *  Reads Campus Building data set. Each line of the input file represents a building and is defined by:
     *      shortName   longName    x   y
     *
     *      shorName and longName are abbreviated and full name for a building.
     *      x and y represents the location of that building.
     *
     * @spec.requires filename is a valid file path
     * @param filename the name of the file that will be read
     * @param longNameToShort map from longName to short Name for all buildings appear in the data set;
     *                        typically empty when the routine is called
     * @param longNameToLocation map from longName to a list of coordinates representing that building
     *                           for all buildings appear in the data set; typically empty when the routine is called.
     *                              Note that, a building may have multiple entrances and any entrance can represent
     *                              that building.
     * @spec.modifies longNameToShort, longNameToLocation
     * @spec.effects fills longNameToShort with a map from each longName of a building to its corresponding shortName.
     * @spec.effects fills longNameToLocation with a map from each longName of a building to
     *                  all coordinates representing that building
     *
     */
  public static void parseBuildingData(
          String filename, Map<String, String> longNameToShort, Map<String, List<Coordinates>> longNameToLocation) {

        CsvToBean<Building> csvToBean = null;

        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            csvToBean = new CsvToBeanBuilder<Building>(reader)
                    .withType(Building.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }

      Iterator<Building> iterator = csvToBean.iterator();
        while(iterator.hasNext()) {
            Building building = iterator.next();
            String short_name = building.getShort_name();
            String long_name = building.getLong_name();
            Double x = Double.parseDouble(building.getX());
            Double y = Double.parseDouble(building.getY());
            if (longNameToShort.containsKey(long_name)) {
                assert short_name.equals(longNameToShort.get(long_name));
            } else {
                longNameToShort.put(long_name, short_name);
            }

            if (!longNameToLocation.containsKey(long_name)) {
                longNameToLocation.put(long_name, new ArrayList<Coordinates>());
            }
            longNameToLocation.get(long_name).add(new Coordinates(x, y));
        }
  }

}
