package agh.to.lab.cinema.model.types;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class TypeInitializer implements CommandLineRunner {

    private final TypeRepository typeRepository;

    public TypeInitializer(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public void run(String... args) {
        for (MovieType movieType : MovieType.values()) {
            if (typeRepository.findByMovieType(movieType).isEmpty()) {
                typeRepository.save(new Type(movieType));
            }
        }
    }
}
