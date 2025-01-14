package agh.to.lab.cinema.model.rates;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieRateDTO {
    Integer movie_id;
    Integer user_id;
    Integer rate;
    String description;
}
