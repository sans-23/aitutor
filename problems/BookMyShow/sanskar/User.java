package lld.problems.BookMyShow.sanskar;

import java.util.UUID;

public class User {
    public String id;
    public String name;
    public String email;

    User(String name, String email){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
    }
}
