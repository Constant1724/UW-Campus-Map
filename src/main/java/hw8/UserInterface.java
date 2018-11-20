package hw8;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {
    public static String campusPathFileName = "src/main/java/hw8/data/campus_paths.tsv";
    public static String campusBuildingFileName = "src/main/java/hw8/data/campus_buildings.tsv";

    public static void main(String[] args) {
        CampusPathModel model = CampusPathModel.makeInstance(campusPathFileName, campusBuildingFileName);
        Scanner reader = new Scanner(System.in, "UTF-8"); // Reading from System.in
        Map<String, String> buildings = model.listBuildings();

        while (true) {
            String command = reader.nextLine();
            if (command.equals("b")) {

            }
        }

    }

    public static void printBuildings(CampusPathModel model) {

    }
}
