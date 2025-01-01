package agh.to.lab.cinema.model.types;

public enum MovieType {
    DRAMA, ACTION, COMEDY;

    public String toString() {
        return this.name().toLowerCase();
    }

    public static boolean isTypeValid(String type) {
        for (MovieType movieType : MovieType.values()) {
            if (movieType.toString().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
