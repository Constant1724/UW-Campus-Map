package hw8;

import hw3.Graph;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class UserInterface {
    public static String campusPathFileName = "src/main/java/hw8/data/campus_paths.tsv";
    public static String campusBuildingFileName = "src/main/java/hw8/data/campus_buildings.tsv";

    public static void main(String[] args) {
        CampusMapModel model = CampusMapModel.makeInstance(campusPathFileName, campusBuildingFileName);
        Scanner reader = new Scanner(System.in, "UTF-8"); // Reading from System.in

        // Creates a new mapping from Building.shortName to Building.
        Map<String, Building> shortToBuilding = new HashMap<>();

        // Creates a list of Building list in format shortName : longName for each Building.
        // Used when building list will be printOut.
        List<String> formattedBuilding = new ArrayList<>();

        // Fill the shortToBuilding and formattedBuilding.
        for (Building building :  model.listBuildings()) {
            shortToBuilding.put(building.getShortName(), building);
            formattedBuilding.add(String.format("\t%s: %s", building.getShortName(), building.getLongName()));
        }
        formattedBuilding.sort((o1, o2) -> {
            String sub1 = o1.substring(0, o1.indexOf(":"));
            String sub2 = o2.substring(0, o2.indexOf(":"));
            return sub1.compareTo(sub2);
        });




        // Quick sanity check.
        assert formattedBuilding.size() == shortToBuilding.size() && shortToBuilding.size() == model.listBuildings().size();


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

        List<Graph<Coordinate, Double>.Edge> results = model.findPath
                (startBuilding.getLocation(), endBuilding.getLocation());

        if (results == null) {
            System.out.println("no path found");
            return;
        }

        double totalCost = 0;

        for (Graph<Coordinate, Double>.Edge edge : results) {
            totalCost += edge.getLabel();
            System.out.println(formatPath(edge));
        }
        System.out.println(String.format("Total distance: %.0f feet", totalCost));
    }

    private static String formatPath(Graph<Coordinate, Double>.Edge edge) {
        String direction;

        Double endX = edge.getEnd().getContent().getX();
        Double endY = edge.getEnd().getContent().getY();
        Double startX = edge.getStart().getContent().getX();
        Double startY = edge.getStart().getContent().getY();

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

        String verboseStep = String.format("\tWalk %.0f feet %s to (%.0f, %.0f)", edge.getLabel(), direction, endX, endY);
        return verboseStep;
    }
}
