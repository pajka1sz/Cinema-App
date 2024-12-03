package agh.to.lab.cinema.model.roles;

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
}
