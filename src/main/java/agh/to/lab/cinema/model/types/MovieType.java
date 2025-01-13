package agh.to.lab.cinema.model.types;

public enum MovieType {
    DRAMA, ACTION, COMEDY, ADVENTURE, SCIFI, HORROR, FANTASY, ROMANCE, THRILLER, MYSTERY, WAR, MUSIC, MUSICAL;

    public String toString() {
        return this.name().toLowerCase();
    }

    public static boolean isTypeValid(String type) {
        for (MovieType movieType : MovieType.values()) {
            if (movieType.toString().equals(type.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static MovieType fromString(String type) {
        String sanitizedType = type.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
        for (MovieType movieType : MovieType.values()) {
            if (movieType.toString().equals(sanitizedType.toLowerCase())) {
                return movieType;
            }
        }
        return null;
    }
}
