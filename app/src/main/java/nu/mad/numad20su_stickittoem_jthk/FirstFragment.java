package nu.mad.numad20su_stickittoem_jthk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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

import nu.mad.numad20su_stickittoem_jthk.models.User;

public class FirstFragment extends Fragment {

    private TextView usernameTextView;
    private TextView numberSentTextView;
    private DatabaseReference databaseReference;
    private User user;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTextView = view.findViewById(R.id.textview_first);
        numberSentTextView = view.findViewById(R.id.firstfragment_numbersent_textview);
        
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
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    private void registerUser(String token) {
        DialogFragment dialogFragment = new RegisterDialogFragment(token);
        dialogFragment.show(getParentFragmentManager(), "RegisterDialogFragment");
    }
}
