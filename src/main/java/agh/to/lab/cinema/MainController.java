package agh.to.lab.cinema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    @GetMapping
    public List<String> greeting() {
        return List.of("siema", "siema");
    }
}
