package nu.mad.numad20su_stickittoem_jthk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import nu.mad.numad20su_stickittoem_jthk.models.StickerUserPair;
import nu.mad.numad20su_stickittoem_jthk.models.User;

public class FirstFragment extends Fragment {

    private TextView usernameTextView;
    private TextView numberSentTextView;
    private DatabaseReference databaseReference;
    private User user;
    private EditText usernameToSendTo;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTextView = view.findViewById(R.id.textview_first);
        numberSentTextView = view.findViewById(R.id.firstfragment_numbersent_textview);
        usernameToSendTo = view.findViewById(R.id.usernameToSendToTextInput);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                getActivity(), new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        final String token = instanceIdResult.getToken();

                        // resource: https://stackoverflow.com/questions/40366717/firebase-for-android-how-can-i-loop-through-a-child-for-each-child-x-do-y
                        databaseReference.child("users")
                                //make ChildEventListener?
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        boolean isRegistered = false;

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            User storedUser = dataSnapshot.getValue(User.class);
                                            if (storedUser.token.equals(token)) {
                                                isRegistered = true;
                                                user = storedUser;
                                                MainActivity.user = storedUser;
                                            }
                                        }

                                        if (!isRegistered) {
                                            registerUser(token);
                                        } else {
                                            String usernameText = "Hello " + user.username;
                                            usernameTextView.setText(usernameText);
                                            databaseReference.child("users").addChildEventListener(
                                                    new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            User changedUser = snapshot.getValue(User.class);
                                                            if (changedUser != null && changedUser.token.equals(user.token)) {
                                                                String numberSentText = "Number of Stickers Sent: " + changedUser.numberOfStickersSent;
                                                                numberSentTextView.setText(numberSentText);
                                                            }
                                                        }

                                                        @Override
                                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            User changedUser = snapshot.getValue(User.class);
                                                            if (changedUser != null && changedUser.token.equals(user.token)) {
                                                                String numberSentText = "Number of Stickers Sent: " + changedUser.numberOfStickersSent;
                                                                numberSentTextView.setText(numberSentText);
                                                            }
                                                        }

                                                        @Override
                                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                                        }

                                                        @Override
                                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    }
                                            );

                                            // experimental for getting latest sticker user pair....
                                            databaseReference.child("users").child(user.username).child("stickerUserPairs").addChildEventListener(
                                                    new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                                                            StickerUserPair addedStickerUserPair = snapshot.getValue(StickerUserPair.class);
//                                                            if (addedStickerUserPair != null) {
//                                                                Snackbar.make(view, "You got a " + addedStickerUserPair.stickerName + " from " + addedStickerUserPair.sentByUsername, Snackbar.LENGTH_LONG)
//                                                                        .setAction("Action", null).show();
//                                                            }
                                                        }

                                                        @Override
                                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                        }

                                                        @Override
                                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                                        }

                                                        @Override
                                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                });

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.user != null) {
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        });

        // set onClickListeners for sticker buttons
        view.findViewById(R.id.smile_button).setOnClickListener(sendStickerListener);
        view.findViewById(R.id.laugh_button).setOnClickListener(sendStickerListener);
        view.findViewById(R.id.sad_button).setOnClickListener(sendStickerListener);
    }

    // make a listener in the class so we can use one onClick, instead of having to define it
    // multiple times. Append StickerUserPair objects to the user's StickerUserPair arrays.
    private View.OnClickListener sendStickerListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            final StickerUserPair stickerUserPair =
                    new StickerUserPair(user.username, "");

            // only start the sending flow if they enter a username
            if (usernameToSendTo.getText().length() > 0) {

                // check if user exists, before sending data
                databaseReference.child("users").child(String.valueOf(usernameToSendTo.getText()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue() != null) {

                                    // if user exists
                                    switch (view.getId()) {
                                        case R.id.smile_button:
                                            stickerUserPair.stickerName = "smile";

                                            // use push() when appending to an array
                                            databaseReference.child("users").child(String.valueOf(usernameToSendTo.getText()))
                                                    .child("stickerUserPairs").push().setValue(stickerUserPair);
                                            break;
                                        case R.id.laugh_button:
                                            stickerUserPair.stickerName = "laugh";

                                            databaseReference.child("users").child(String.valueOf(usernameToSendTo.getText()))
                                                    .child("stickerUserPairs").push().setValue(stickerUserPair);
                                            break;
                                        case R.id.sad_button:
                                            stickerUserPair.stickerName = "sad";

                                            databaseReference.child("users").child(String.valueOf(usernameToSendTo.getText()))
                                                    .child("stickerUserPairs").push().setValue(stickerUserPair);
                                            break;
                                    }

                                    databaseReference.child("users")
                                            .child(String.valueOf(usernameToSendTo.getText()))
                                            .child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String deviceToSendTo = snapshot.getValue().toString();
                                            sendStickerToDevice(deviceToSendTo, stickerUserPair.stickerName);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    // update number of stickers sent
                                    databaseReference.child("users").child(user.username)
                                            .child("numberOfStickersSent").setValue(user.numberOfStickersSent + 1);
                                } else {
                                    // notify user that they entered an invalid username
                                    Snackbar.make(view, "Sorry, this user doesn't exist, please enter a different username", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            } else {
                // notify user that they need to enter a username
                Snackbar.make(view, "Sorry, please enter a username", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    };

    private void registerUser(String token) {
        DialogFragment dialogFragment = new RegisterDialogFragment(token);
        dialogFragment.show(getParentFragmentManager(), "RegisterDialogFragment");
    }

    private void sendStickerToDevice(final String deviceToSendTo, final String sticker) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jPayload = new JSONObject();
                JSONObject jNotification = new JSONObject();
                JSONObject jData = new JSONObject();

                try {
                    jNotification.put("title", "New Sticker");
                    jNotification.put("body", sticker);
                    jNotification.put("sound", "default");
                    jNotification.put("badge", "1");

                    jData.put("title", "New Sticker");
                    jData.put("content", sticker);

                    jPayload.put("to", deviceToSendTo);
                    jPayload.put("priority", "high");
                    jPayload.put("notification", jNotification);
                    jPayload.put("data", jData);

                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", MainActivity.SERVER_KEY);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(jPayload.toString().getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
