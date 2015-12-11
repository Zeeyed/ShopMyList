package materialshop.curiousbat.shopmylist.model;

/**
 * User class
 * Created by Zied on 27/11/2015.
 */
public class User {
    int id;
    public String name;
    public String email;
    public String password;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }



    public String getPassword() {
        return password;
    }

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name " + name +
                " / email " + email +
                " / password " + password ;
    }

}

