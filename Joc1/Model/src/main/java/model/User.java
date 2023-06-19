package model;

public class User extends Entity<Integer> {
    String name, password;

    public User() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String toString() { return "Id= " + getId() + " " + name + " " + password; }
}
