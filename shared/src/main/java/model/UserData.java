package model;

public record UserData(String username, String password, String email) {

    public UserData getUser() {
        return this;
    }
}
