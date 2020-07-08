package nu.mad.numad20su_stickittoem_jthk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nu.mad.numad20su_stickittoem_jthk.models.User;

// resource: https://developer.android.com/guide/topics/ui/dialogs
public class RegisterDialogFragment extends DialogFragment {

    private String token;

    /**
     * Constructor for RegisterDialogFragment.
     *
     * @author Joseph Triolo
     */
    RegisterDialogFragment(String token) {
        super();
        this.token = token;
    }

    /**
     * onCreateDialog for LinkCollectorDialogFragment.
     * Gets layout, adds buttons with listeners.
     * Dialog requires a name and a url to add a link. If either are empty, the appropriate
     * EditText will give an error and the dialog won't dismiss.
     *
     * @param savedInstanceState the context associated with this dialog
     * @author Joseph Triolo
     */
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(R.layout.dialog_register)
                .setPositiveButton(R.string.register, null);

        // resource: https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText usernameEditText = alertDialog
                                .findViewById(R.id.register_dialog_username);
                        boolean isEmptyUsername = TextUtils.isEmpty(usernameEditText.getText());

                        if (isEmptyUsername) {
                            usernameEditText.setError("Link Name is required.");
                        } else {
                            String username = usernameEditText.getText().toString();
                            User user = new User(username, token);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference();

                            // resource: https://firebase.google.com/docs/database/android/read-and-write
                            databaseReference.child("users").child(username).setValue(user);
                            // resource: https://stackoverflow.com/questions/20702333/refresh-fragment-at-reload
//                            getParentFragmentManager().beginTransaction()
//                                    .detach(mainActivity).attach(mainActivity).commit();
                            alertDialog.dismiss();
                            Snackbar.make(getActivity().findViewById(R.id.firstfragment_layout),
                                    "Successfully registered.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return alertDialog;
    }
}
