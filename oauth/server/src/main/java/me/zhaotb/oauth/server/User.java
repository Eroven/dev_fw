package me.zhaotb.oauth.server;

import java.util.Objects;
import java.util.Set;

public class User {

    private String nameName;
    private String password;

    private String comment;

    private Set<String> scopes;

    public User() {
    }

    public User(String nameName, String password) {
        this.nameName = nameName;
        this.password = password;
    }

    public User(String nameName, String password, String comment) {
        this.nameName = nameName;
        this.password = password;
        this.comment = comment;
    }

    public String getNameName() {
        return nameName;
    }

    public void setNameName(String nameName) {
        this.nameName = nameName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(nameName, user.nameName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameName);
    }
}
