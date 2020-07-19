package DAO;

public class user {
    private String name;
    private String username;
    private String password;

    public user(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    public user(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public boolean compare(user iUser){
        if(this.username.equals(iUser.username) && this.password.equals(iUser.password)){
            return true;
        }
        return false;
    }
}
