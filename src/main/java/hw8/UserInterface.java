package hw8;

import hw3.Graph;

import java.util.*;

public class UserInterface {
    public static String campusPathFileName = "src/main/java/hw8/data/campus_paths.tsv";
    public static String campusBuildingFileName = "src/main/java/hw8/data/campus_buildings.tsv";

    public static void main(String[] args) {
        CampusMapModel model = CampusMapModel.makeInstance(campusPathFileName, campusBuildingFileName);
        Scanner reader = new Scanner(System.in, "UTF-8"); // Reading from System.in
        Map<String, String> fullToShort = model.listBuildings();
        Map<String, CampusMapModel.Coordinate> fullToLocation = model.listLocations();
        Map<String, String> shortToFull = new HashMap<>();
        List<String> buildings = new ArrayList<>();

        for (Map.Entry<String, String> entry : fullToShort.entrySet()) {
            shortToFull.put(entry.getValue(), entry.getKey());
            buildings.add(String.format("%s: %s", entry.getValue(), entry.getKey()));
        }

        assert fullToShort.size() == shortToFull.size();

        printMenu();
        while (true) {
            System.out.print("Enter an option ('m' to see the menu): ");
            String command = reader.nextLine();
            switch (command) {
                case "b" :
                    for (String line : buildings) {
                        System.out.println(line);
                    }
                    break;
                case "r" :
                    printPaths(reader, shortToFull, fullToLocation, model);
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
    private static void printMenu() {
        System.out.println("Menu:");
        System.out.println("\tr to find a route");
        System.out.println("\tb to see a list of all buildings");
        System.out.println("\tq to quit");
    }

    public static void printPaths(Scanner reader, Map<String, String> shortToFull, Map<String, CampusMapModel.Coordinate> fullToLocation, CampusMapModel model) {
        System.out.print("Abbreviated name of starting building: ");
        String start = reader.nextLine();
        System.out.print("Abbreviated name of ending building: ");
        String end = reader.nextLine();
        boolean ifUnknownBuilding = false;
        if (!shortToFull.containsKey(start)) {
            System.out.println(String.format("Unknown building: %s", start));
            ifUnknownBuilding = true;
        }
        if (!shortToFull.containsKey(end)) {
            System.out.println(String.format("Unknown building: %s", end));
            ifUnknownBuilding = true;
        }
        if (ifUnknownBuilding) {
            return;
        }
        List<Graph<CampusMapModel.Coordinate, Double>.Edge> results = model.findPath(
                fullToLocation.get(shortToFull.get(start)), fullToLocation.get(shortToFull.get(end)));

        System.out.println(String.format("Path from %s to %s:", shortToFull.get(start), shortToFull.get(end)));
        double totalCost = 0;
        for (Graph<CampusMapModel.Coordinate, Double>.Edge edge : results) {
            totalCost += edge.getLabel();
            System.out.println(formatPath(edge));
        }
        System.out.println(String.format("Total distance: %.0f feet", totalCost));
    }

    private static String formatPath(Graph<CampusMapModel.Coordinate, Double>.Edge edge) {
        String direction = "";

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
