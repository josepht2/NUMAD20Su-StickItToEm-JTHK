package nu.mad.numad20su_stickittoem_jthk.models;

import java.util.ArrayList;

public class User {

    public String username;
    public Integer numObjectsSent;
    public ArrayList<Integer> objectsReceivedMappings;


    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.username = username;
        this.numObjectsSent = 0;
        this.objectsReceivedMappings = new ArrayList<>();
    }
}
