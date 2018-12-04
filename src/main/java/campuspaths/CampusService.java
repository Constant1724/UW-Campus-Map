package campuspaths;

import hw8.Building;
import hw8.CampusMapModel;
import hw8.CampusPath;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A wrapper class for CampusMapModel.
 *
 *      It adjust the following behavior to make it works better with web responses:
 *          findPath() now is able to accept any strings as start and end buildings,
 *          for those invalid strings (Short name of building is not in our data set)
 *          a ServerSideException will be thrown
 */
@Service
public class CampusService {

    /** file name for campus path data set. */
    public static String campusPathFileName = "src/main/java/hw8/data/campus_paths.tsv";

    /** file name for campus building data set. */
    public static String campusBuildingFileName = "src/main/java/hw8/data/campus_buildings.tsv";

    /**
     * model implements the main functionality of the CampusService:
     *      findPath form one building to another
     *      list all buildings in the campus
     */
    private CampusMapModel model;

    /**
     * a mapping from building's short name to Building instance, for all buildings in the campus.
     */
    private Map<String, Building> shortToBuilding;

    /**
     * Constructor of the service.
     *
     * @spec.modifies this.model, this.shortToBuilding
     * @spec.effects this.model : obtain an instance of CampusMapModel with
     *                              Campus Path Data and Campus Building Data loaded.
     * @spec.effects this.shortToBuilding : create a mapping from building's short name to Building instance,
     *                                          for all buildings in the campus.
     */
    public CampusService() {
    model = CampusMapModel.makeInstance(campusPathFileName, campusBuildingFileName);

    // Creates a new mapping from Building.shortName to Building.
    shortToBuilding = new HashMap<>();

    // Fill the shortToBuilding and formattedBuilding.
    for (Building building : model.listBuildings()) {
      shortToBuilding.put(building.getShortName(), building);
    }

    }

    /**
     * Find the path in the campus from the shortName of Start Building to the shortName of End Building.
     * Note that this guarantees to find the shortest path.
     *
     * <p>A path consists of one or more CampusPath instances.
     *
     * <p>If multiple least weight path exists, it will return any of them.
     *
     * <p>The path will be returned in the form of a list, where the first element is start Building-Point1,
     * second element is Point1-Point2, ... the last element is Point(N-1) - end Building. (N is the length of the
     * list)
     *
     * <p>If such a path does not exist, it will return null.
     *
     * <p>For self-edge, it will return an empty list.
     *
     * @param start the short Name (abbreviated name) of Start Building
     * @param end the short Name (abbreviated name) of end Building
     * @return a list holding the path from Start Building to End Building if there exists one, or null otherwise.
     * @throws ServerSideException if short name of either is invalid
     *              (i.e not found or does not have a corresponding full name)
     */
    public @Nullable List<CampusPath> findPath(String start, String end) throws ServerSideException{
      String errorMessage = "";
        if (!shortToBuilding.containsKey(start)) {
            errorMessage += "Unknown Building\t" + start + "\n";
        }
        if (!shortToBuilding.containsKey(end)) {
            errorMessage += "Unknown Building\t" + end + "\n";
        }
        if (errorMessage.isEmpty()) {
            throw new ServerSideException(errorMessage);
        }

        // We can prove that if either start or end is not a key in the shortToBuilding,
        // ifUnknownBuilding must be true.
        // Then the method will definitely return before the following to shortToBuilding.get method.
        // Otherwise, start and end must be key for shortBuilding,
        //      as a result there is no way for shortToBuilding.get to return null
        @SuppressWarnings("incompatible")
        @NonNull Building startBuilding = shortToBuilding.get(start);

        @SuppressWarnings("incompatible")
        @NonNull Building endBuilding = shortToBuilding.get(end);

        return model.findPath(startBuilding.getLocation(), endBuilding.getLocation());
    }

    /**
     * List all Buildings in the campus.
     *
     * <p>It returns an unmodifiable view of all Buildings, each with its own short name, full name and cost.
     *
     * @return an unmodifiable view of all Buildings, each with its own short name, full name and cost.
     */
    public Set<Building> listBuildings() {
      return model.listBuildings();
    }
}
