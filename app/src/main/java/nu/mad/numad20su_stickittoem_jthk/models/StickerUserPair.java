package nu.mad.numad20su_stickittoem_jthk.models;

import java.util.ArrayList;

public class StickerUserPair {

    public String username;
    public Integer stickerNum;

    public StickerUserPair() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public StickerUserPair(String username, Integer stickerNum) {
        this.username = username;
        this.stickerNum = stickerNum;
    }
}
