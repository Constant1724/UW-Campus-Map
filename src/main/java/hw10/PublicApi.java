package hw10;

import hw8.CampusMapModel;
import hw8.CampusPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class PublicApi {

    @Autowired
    private CampusMapModel model;

    public

    public List<CampusPath> findPath(String startBuilding, String endBuilding) {
        model.findPath()
    }
}
