package hw8;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

/**
 * A simple text user interface allowing client to
 *      list buildings
 *      find path between buildings
 *      print menu
 *      quit the program
 *
 * through command line text interface.
 */
public class UserInterface {
    /**
     * file name for campus path data set.
     */
    public static String campusPathFileName = "src/main/java/hw8/data/campus_paths.tsv";

    /**
     * file name for campus building data set.
     */
    public static String campusBuildingFileName = "src/main/java/hw8/data/campus_buildings.tsv";

    /**
     * Standard main method. read user input from System.in and
     *  perform corresponding functions and output result to System.out
     *
     *      b lists all buildings in the form abbreviated name: long name.
     *              Buildings are listed in alphabetical order of abbreviated name.
     *      r prompts the user for the abbreviated names of two buildings and prints directions
     *              for the shortest route between them.
     *      q quits the program.
     *      m prints a menu of all commands.
     *
     * @param args list of command line arguments
     */
    public static void main(String[] args) {
        CampusMapModel model = CampusMapModel.makeInstance(campusPathFileName, campusBuildingFileName);
        Scanner reader = new Scanner(System.in, "UTF-8"); // Reading from System.in

        // Creates a new mapping from Building.shortName to Building.
        Map<String, Building> shortToBuilding = new HashMap<>();

        // Creates a list of Building list in format "shortName: longName" for each Building.
        // Used when building list will be printOut.
        List<String> formattedBuilding = new ArrayList<>();

        // Fill the shortToBuilding and formattedBuilding.
        for (Building building :  model.listBuildings()) {
            shortToBuilding.put(building.getShortName(), building);
            formattedBuilding.add(String.format("\t%s: %s", building.getShortName(), building.getLongName()));
        }

        // Sort the formattedBuilding based on the alphabetical order of shortName.
        formattedBuilding.sort((o1, o2) -> {
            String sub1 = o1.substring(0, o1.indexOf(":"));
            String sub2 = o2.substring(0, o2.indexOf(":"));
            return sub1.compareTo(sub2);
        });


        // Quick sanity check.
        assert formattedBuilding.size() == shortToBuilding.size() && shortToBuilding.size() == model.listBuildings().size();

        formattedBuilding.add(0, "Buildings:");

        printMenu();
        System.out.println();

        while (true) {
            System.out.print("Enter an option ('m' to see the menu): ");

            String command = reader.nextLine();
            while (command.startsWith("#") || command.isEmpty()) {
                System.out.println(command);
                command = reader.nextLine();
            }

            switch (command) {
                case "b" :
                    for (String line : formattedBuilding) {
                        System.out.println(line);
                    }
                    break;
                case "r" :
                    printPaths(reader, shortToBuilding, model);
                    break;
                case "q" :
                    return;
                case "m" :
                    printMenu();
                    break;
                default :
                    System.out.println("Unknown option");
            }
            System.out.println();

        }

    }

  /**
   * Prints the menu out to System.out.
   *
   * Menu:
   *    r to find a route
   *    b to see a list of all buildings
   *    q to quit
   */
  private static void printMenu() {
        System.out.println("Menu:");
        System.out.println("\tr to find a route");
        System.out.println("\tb to see a list of all buildings");
        System.out.println("\tq to quit");
    }

    /**
     * take user input and find and printout a path from one Building to another.
     *
     * @param reader read user input.
     * @param shortToBuilding  map from abbreviated name to Building for all buildings.
     * @param model the campus map used to find a path from one Building to another.
     */
    public static void printPaths(Scanner reader, Map<String, Building> shortToBuilding, CampusMapModel model) {
        System.out.print("Abbreviated name of starting building: ");
        String start = reader.nextLine();
        System.out.print("Abbreviated name of ending building: ");
        String end = reader.nextLine();
        boolean ifUnknownBuilding = false;
        if (!shortToBuilding.containsKey(start)) {
            System.out.println(String.format("Unknown building: %s", start));
            ifUnknownBuilding = true;
        }
        if (!shortToBuilding.containsKey(end)) {
            System.out.println(String.format("Unknown building: %s", end));
            ifUnknownBuilding = true;
        }
        if (ifUnknownBuilding) {
            return;
        }

        // We can prove that if either start or end is not a key in the shortToBuilding, ifUnknownBuilding must be true.
        // Then the method will definitely return before the following to shortToBuilding.get method.
        // Otherwise, start and end must be key for shortBuilding,
        //      as a result there is no way for shortToBuilding.get to return null
        @SuppressWarnings("incompatible")
        @NonNull Building startBuilding = shortToBuilding.get(start);

        @SuppressWarnings("incompatible")
        @NonNull Building endBuilding = shortToBuilding.get(end);

        System.out.println(String.format("Path from %s to %s:", startBuilding.getLongName(), endBuilding.getLongName()));

        List<CampusPath> results = model.findPath
                (startBuilding.getLocation(), endBuilding.getLocation());

        if (results == null) {
            System.out.println("no path found");
            return;
        }

        double totalCost = 0;

        for (CampusPath path : results) {
            totalCost += path.getCost();
            System.out.println(formatPath(path));
        }
        System.out.println(String.format("Total distance: %.0f feet", totalCost));
    }

    /**
     * Format a CampusPath into:
     *      Walk CampusPath.getCost() feet someDirection to
     *          (CampusPath.getDestination.getX(), CampusPath.getDestination.getY())
     *
     *      For example:
     *          Walk 36 feet NW to (2187, 950)
     *
     *      All number should round to the nearest integer.
     *
     * @param path CampusPath instance to be formatted
     * @return formatted string of a path, as described above.
     */
    private static String formatPath(CampusPath path) {
        String direction;

        Double endX = path.getDestination().getX();
        Double endY = path.getDestination().getY();
        Double startX = path.getOrigin().getX();
        Double startY = path.getOrigin().getY();

        Double directionCost = Math.atan2(startY - endY, endX - startX) / (Math.PI / 8);

        if (directionCost < 0) {
            if (directionCost < -7) {
                direction = "W";
            } else if (directionCost <= -5) {
                direction = "SW";
            } else if (directionCost < -3) {
                direction = "S";
            } else if (directionCost <= -1) {
                direction = "SE";
            } else {
                direction = "E";
            }
        } else {
            if (directionCost > 7) {
                direction = "W";
            } else if ( directionCost >= 5) {
                direction = "NW";
            } else if (directionCost > 3) {
                direction = "N";
            } else if (directionCost >= 1) {
                direction = "NE";
            } else {
                direction = "E";
            }
        }

        String verboseStep = String.format("\tWalk %.0f feet %s to (%.0f, %.0f)", path.getCost(), direction, endX, endY);
        return verboseStep;
    }
}
