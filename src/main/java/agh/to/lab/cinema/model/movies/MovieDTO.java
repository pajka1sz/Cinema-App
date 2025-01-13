package agh.to.lab.cinema.model.movies;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class MovieDTO {
    private String title;
    private String description;
    private Integer length;
    private String thumbnail;
    private Set<String> types;
}
