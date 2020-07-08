package nu.mad.numad20su_stickittoem_jthk.models;

import java.util.HashMap;

public class User {

    public String username;
    public String token;
    public int numberOfStickersSent;
    public HashMap<String, StickerUserPair> stickerUserPairs;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String token) {
        this.username = username;
        this.token = token;
        this.numberOfStickersSent = 0;
        this.stickerUserPairs = new HashMap<>();

        // add a welcome smiley
        this.stickerUserPairs.put("first-welcome", new StickerUserPair("Welcome From Devs!", "smile"));
    }
}
