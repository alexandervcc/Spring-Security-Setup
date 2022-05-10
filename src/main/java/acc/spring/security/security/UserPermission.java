package acc.spring.security.security;

public enum UserPermission {
    DOG_READ("dog:read"),
    DOG_WRITE("dog:write"),
    FOOD_READ("food:read"),
    FOOD_WRITE("food:write");

    private final String permission;

    UserPermission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}
