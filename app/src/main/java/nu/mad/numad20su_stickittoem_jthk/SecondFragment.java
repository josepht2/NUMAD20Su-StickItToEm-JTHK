package nu.mad.numad20su_stickittoem_jthk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Iterator;

import nu.mad.numad20su_stickittoem_jthk.models.User;

public class SecondFragment extends Fragment {

    private User user;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = MainActivity.user;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(user.username).child("stickerUserPairs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> dataSnapshotIterator = snapshot.getChildren().iterator();

                        if (dataSnapshotIterator.hasNext()) {
                            ConstraintLayout constraintLayout = view.findViewById(R.id.secondfragment_ConstraintLayout);

                            DataSnapshot dataSnapshot = dataSnapshotIterator.next();
                            String username = dataSnapshot.child("sentByUsername").getValue().toString();
                            int sticker = getSticker(dataSnapshot.child("stickerName").getValue().toString());
                            int currentUsernameId = View.generateViewId();
                            int currentStickerId = View.generateViewId();
                            int nextUsernameId = View.generateViewId();
                            int nextStickerId = View.generateViewId();

                            TextView textView0 = new TextView(getContext());
                            TextView textView1 = new TextView(getContext());

                            textView0.setId(currentUsernameId);
                            textView1.setId(currentStickerId);

                            textView0.setText(username);
                            textView1.setText(sticker);

                            constraintLayout.addView(textView0);
                            constraintLayout.addView(textView1);

                            ConstraintSet constraintSet0 = new ConstraintSet();
                            ConstraintSet constraintSet1 = new ConstraintSet();

                            constraintSet0.constrainHeight(currentUsernameId, ConstraintSet.WRAP_CONTENT);
                            constraintSet0.constrainWidth(currentUsernameId, ConstraintSet.WRAP_CONTENT);
                            constraintSet1.constrainHeight(currentStickerId, ConstraintSet.WRAP_CONTENT);
                            constraintSet1.constrainWidth(currentStickerId, ConstraintSet.WRAP_CONTENT);

                            constraintSet0.connect(currentUsernameId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                            constraintSet0.connect(currentUsernameId, ConstraintSet.END, currentStickerId, ConstraintSet.START, 0);
                            constraintSet1.connect(currentStickerId, ConstraintSet.START, currentUsernameId, ConstraintSet.END, 0);
                            constraintSet1.connect(currentStickerId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

                            constraintSet0.connect(currentUsernameId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                            constraintSet1.connect(currentStickerId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);

                            if (dataSnapshotIterator.hasNext()) {
                                constraintSet0.connect(currentUsernameId, ConstraintSet.BOTTOM, nextUsernameId, ConstraintSet.TOP, 0);
                                constraintSet1.connect(currentStickerId, ConstraintSet.BOTTOM, nextStickerId, ConstraintSet.TOP, 0);
                            } else {
                                constraintSet0.connect(currentUsernameId, ConstraintSet.BOTTOM, R.id.button_second, ConstraintSet.TOP, 0);
                                constraintSet1.connect(currentStickerId, ConstraintSet.BOTTOM, R.id.button_second, ConstraintSet.TOP, 0);
                            }
                            constraintSet0.applyTo(constraintLayout);
                            constraintSet1.applyTo(constraintLayout);

                            if (dataSnapshotIterator.hasNext()) {
                                int previousUsernameId;
                                int previousStickerId;

                                dataSnapshot = dataSnapshotIterator.next();

                                while (dataSnapshotIterator.hasNext()) {
                                    username = dataSnapshot.child("sentByUsername").getValue().toString();
                                    sticker = getSticker(dataSnapshot.child("stickerName").getValue().toString());

                                    previousUsernameId = currentUsernameId;
                                    previousStickerId = currentStickerId;
                                    currentUsernameId = nextUsernameId;
                                    currentStickerId = nextStickerId;
                                    nextUsernameId = View.generateViewId();
                                    nextStickerId = View.generateViewId();

                                    textView0 = new TextView(getContext());
                                    textView1 = new TextView(getContext());

                                    textView0.setId(currentUsernameId);
                                    textView1.setId(currentStickerId);

                                    textView0.setText(username);
                                    textView1.setText(sticker);

                                    constraintLayout.addView(textView0);
                                    constraintLayout.addView(textView1);

                                    constraintSet0 = new ConstraintSet();
                                    constraintSet1 = new ConstraintSet();

                                    constraintSet0.constrainHeight(currentUsernameId, ConstraintSet.WRAP_CONTENT);
                                    constraintSet0.constrainWidth(currentUsernameId, ConstraintSet.WRAP_CONTENT);
                                    constraintSet1.constrainHeight(currentStickerId, ConstraintSet.WRAP_CONTENT);
                                    constraintSet1.constrainWidth(currentStickerId, ConstraintSet.WRAP_CONTENT);

                                    constraintSet0.connect(currentUsernameId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                                    constraintSet0.connect(currentUsernameId, ConstraintSet.END, currentStickerId, ConstraintSet.START, 0);
                                    constraintSet1.connect(currentStickerId, ConstraintSet.START, currentUsernameId, ConstraintSet.END, 0);
                                    constraintSet1.connect(currentStickerId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

                                    constraintSet0.connect(currentUsernameId, ConstraintSet.TOP, previousUsernameId, ConstraintSet.BOTTOM, 0);
                                    constraintSet0.connect(currentUsernameId, ConstraintSet.BOTTOM, nextUsernameId, ConstraintSet.TOP, 0);
                                    constraintSet1.connect(currentStickerId, ConstraintSet.TOP, previousStickerId, ConstraintSet.BOTTOM, 0);
                                    constraintSet1.connect(currentStickerId, ConstraintSet.BOTTOM, nextStickerId, ConstraintSet.TOP, 0);

                                    constraintSet0.applyTo(constraintLayout);
                                    constraintSet1.applyTo(constraintLayout);

                                    dataSnapshot = dataSnapshotIterator.next();
                                }

                                username = dataSnapshot.child("sentByUsername").getValue().toString();
                                sticker = getSticker(dataSnapshot.child("stickerName").getValue().toString());

                                previousUsernameId = currentUsernameId;
                                previousStickerId = currentStickerId;
                                currentUsernameId = nextUsernameId;
                                currentStickerId = nextStickerId;

                                textView0 = new TextView(getContext());
                                textView1 = new TextView(getContext());

                                textView0.setId(currentUsernameId);
                                textView1.setId(currentStickerId);

                                textView0.setText(username);
                                textView1.setText(sticker);

                                constraintLayout.addView(textView0);
                                constraintLayout.addView(textView1);

                                constraintSet0 = new ConstraintSet();
                                constraintSet1 = new ConstraintSet();

                                constraintSet0.constrainHeight(currentUsernameId, ConstraintSet.WRAP_CONTENT);
                                constraintSet0.constrainWidth(currentUsernameId, ConstraintSet.WRAP_CONTENT);
                                constraintSet1.constrainHeight(currentStickerId, ConstraintSet.WRAP_CONTENT);
                                constraintSet1.constrainWidth(currentStickerId, ConstraintSet.WRAP_CONTENT);

                                constraintSet0.connect(currentUsernameId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                                constraintSet0.connect(currentUsernameId, ConstraintSet.END, currentStickerId, ConstraintSet.START, 0);
                                constraintSet1.connect(currentStickerId, ConstraintSet.START, currentUsernameId, ConstraintSet.END, 0);
                                constraintSet1.connect(currentStickerId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);

                                constraintSet0.connect(currentUsernameId, ConstraintSet.TOP, previousUsernameId, ConstraintSet.BOTTOM, 0);
                                constraintSet0.connect(currentUsernameId, ConstraintSet.BOTTOM, R.id.button_second, ConstraintSet.TOP, 0);
                                constraintSet1.connect(currentStickerId, ConstraintSet.TOP, previousStickerId, ConstraintSet.BOTTOM, 0);
                                constraintSet1.connect(currentStickerId, ConstraintSet.BOTTOM, R.id.button_second, ConstraintSet.TOP, 0);

                                constraintSet0.applyTo(constraintLayout);
                                constraintSet1.applyTo(constraintLayout);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    private int getSticker(String stickerName) {
        int sticker = -1;
        switch (stickerName) {
            case "smile":
                sticker = R.string.smile;
                break;
            case "laugh":
                sticker = R.string.laugh;
                break;
            case "sad":
                sticker = R.string.sad;
                break;
        }
        return sticker;
    }
}
