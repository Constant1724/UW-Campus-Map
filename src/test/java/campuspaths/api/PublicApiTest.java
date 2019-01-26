package campuspaths.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import campuspaths.service.CampusService;
import UI.Building;
import UI.Coordinate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(PublicApi.class)
public class PublicApiTest {

  @Autowired private MockMvc mvc;

  @MockBean private CampusService service;

  /** Since PublicApi is just a thin layer, only sanity check will be doing here. */
  @Test
  public void testFindPath() throws Exception {
    given(service.findPath("CSE", "CSE")).willReturn(new ArrayList<>());

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/findPath?start=CSE&end=CSE");

    mvc.perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("[]")));
  }

  /** Since PublicApi is just a thin layer, only sanity check will be doing here. */
  @Test
  public void testListBuildings() throws Exception {
    Set<Building> parameter = new HashSet<>();
    // test empty case
    testListBuildingsHelper(parameter);

    // test Single case
    parameter.add(new Building(new Coordinate(0.1, 1.9), "aaa", "AAA"));
    testListBuildingsHelper(parameter);

    // test Double case
    parameter.add(new Building(new Coordinate(159.47, 25.69), "", " "));
    testListBuildingsHelper(parameter);

    // test Triple case
    parameter.add(new Building(new Coordinate(5.1, 9.2), "***", "asdas"));
    testListBuildingsHelper(parameter);
  }

  private void testListBuildingsHelper(Set<Building> parameter) throws Exception {
    JSONArray json = new JSONArray();
    for (Building building : parameter) {
      JSONObject buildingJson = new JSONObject();
      JSONObject coordinateJson = new JSONObject();
      coordinateJson.put("x", building.getLocation().getX());
      coordinateJson.put("y", building.getLocation().getY());
      buildingJson.put("location", coordinateJson);
      buildingJson.put("shortName", building.getShortName());
      buildingJson.put("longName", building.getLongName());
      json.put(buildingJson);
    }
    given(service.listBuildings()).willReturn(parameter);

    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/listBuilding");

    mvc.perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo(json.toString())));
  }
}
