package stocks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by allemaos on 15/01/16.
 */

@RestController
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "Yolo!";
    }
}
