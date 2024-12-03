package agh.to.lab.cinema.model.roles;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleType {
    ADMIN("ADMIN"),
    USER("USER");

    private final String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @JsonCreator
    public static RoleType fromString(String roleName) {
        return RoleType.valueOf(roleName.toUpperCase());
    }
}
