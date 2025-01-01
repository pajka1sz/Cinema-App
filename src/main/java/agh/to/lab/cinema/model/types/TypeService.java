package agh.to.lab.cinema.model.types;

import agh.to.lab.cinema.model.movies.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public Type findTypeByName(String type) {
        List<Type> types = typeRepository.findAll();
        for (Type t : types) {
            if (t.toString().equals(type.toLowerCase())) {
                return t;
            }
        }
        return null;
    }
}
