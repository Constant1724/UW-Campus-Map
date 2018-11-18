package hw8;

import com.opencsv.bean.CsvToBean;
import java.util.List;

public class DataParser {

  public static List<CampusPath> parsePathData() {
    CsvToBean<CampusPath> csvToBean = null;

    return csvToBean.parse();
  }
}
