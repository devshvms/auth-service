package in.dev.shvms.authservice.mysqltest;


import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Table(name = "user")
public class User {

    @Id
    Long id;
    String username;
    String password;
    String name;
    Boolean isActive;
    String token;

    public User(Long id, String username, String password, String name, Boolean isActive, String token) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.isActive = isActive;
        this.token = token;
    }
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
