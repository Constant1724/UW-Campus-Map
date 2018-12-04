package campuspaths;

import org.springframework.stereotype.Repository;

@Repository
public class CampusRepository {
    /** file name for campus path data set. */
    public static String campusPathFileName = "src/main/java/hw8/data/campus_paths.tsv";

    /** file name for campus building data set. */
    public static String campusBuildingFileName = "src/main/java/hw8/data/campus_buildings.tsv";
}
