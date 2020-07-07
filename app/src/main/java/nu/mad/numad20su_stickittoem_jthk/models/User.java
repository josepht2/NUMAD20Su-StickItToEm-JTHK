package nu.mad.numad20su_stickittoem_jthk.models;

import java.util.ArrayList;

public class User {

    public String username;
    public String token;
    public int numberOfStickersSent;
    public ArrayList<Integer> objectsReceivedMappings;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String token) {
        this.username = username;
        this.token = token;
        this.numberOfStickersSent = 0;
        objectsReceivedMappings = new ArrayList<>();
    }
}
