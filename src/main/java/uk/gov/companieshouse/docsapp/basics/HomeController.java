package uk.gov.companieshouse.docsapp.basics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import uk.gov.companieshouse.docsapp.DocsAppConfigProps;

import java.util.Date;

@RestController
public class HomeController {

    DocsAppConfigProps props;

    public HomeController(DocsAppConfigProps props) {
        this.props = props;
    }

    @GetMapping("/")
    public RestResult<String> homepage(@RequestParam(required = false) String name, HttpServletResponse response) {
        response.setDateHeader("X-UkGov-CH-Timestamp", new Date().getTime());
        if (name == null) {
            return new RestResult<>("Hello World!");
        } else {
            return new RestResult<>(String.format("Hello %s!", name));
        }
    }

    @GetMapping("/info/{topic}")
    public RestResult<String> info(@PathVariable String topic, HttpServletResponse response) {
        response.setDateHeader("X-UkGov-CH-Timestamp", new Date().getTime());
        if (topic == null) {
            return new RestResult<>((String)null);
        } else {
            return new RestResult<>(String.format("A lot of information on %s!", topic));
        }
    }

    @GetMapping("/config")
    public RestResult<DocsAppConfigProps> info(HttpServletResponse response) {
        response.setDateHeader("X-UkGov-CH-Timestamp", new Date().getTime());
        return new RestResult<>(props);
    }

}
