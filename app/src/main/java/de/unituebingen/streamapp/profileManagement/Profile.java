package de.unituebingen.streamapp.profileManagement;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.tools.User;

public class Profile extends Fragment {

    private ProfileViewModel mViewModel;
    private EditText realName;
    private EditText username;
    private EditText emailAddress;

    private TextView text_newPassword;
    private TextView text_confirmNewPassword;

    private EditText newPassword;
    private EditText confirmNewPassword;

    private Button changePassword_btn;
    private Button save_btn;

    public static Profile newInstance() {
        return new Profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        realName = view.findViewById(R.id.realName);
        username = view.findViewById(R.id.username);
        emailAddress = view.findViewById(R.id.emailAddress);

        text_newPassword = view.findViewById(R.id.text_new_password);
        text_confirmNewPassword = view.findViewById(R.id.text_confirm_new_password);

        newPassword = view.findViewById(R.id.password);
        confirmNewPassword = view.findViewById(R.id.confirm_password);

        changePassword_btn = view.findViewById(R.id.changePassword_btn);
        save_btn = view.findViewById(R.id.save_btn);


        realName.addTextChangedListener(tw);
        username.addTextChangedListener(tw);
        emailAddress.addTextChangedListener(tw);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel

        Fragment context = this;

        Observer<User> profileObserver = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    realName.setHint(user.getRealName());
                    username.setHint(user.getUserName());
                    emailAddress.setHint(user.getMail());
                }
            }
        };

        Observer<Boolean> profileUpdateObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean.booleanValue())
                    Toast.makeText(context.getActivity().getApplicationContext(), "Change successful!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context.getActivity().getApplicationContext(), "Error: Could not update profile", Toast.LENGTH_SHORT).show();
            }
        };

        mViewModel.getProfileData(this.getActivity()).observe(getActivity(), profileObserver);


        //change Password button
        changePassword_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                changePassword_btn.setVisibility(View.INVISIBLE);

                text_newPassword.setVisibility(View.VISIBLE);
                text_confirmNewPassword.setVisibility(View.VISIBLE);

                newPassword.setVisibility(View.VISIBLE);
                confirmNewPassword.setVisibility(View.VISIBLE);

                save_btn.setVisibility(View.VISIBLE);
            }
        });


        //save button
        save_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get strings
                String realnameString = realName.getText().toString();
                String mailString = emailAddress.getText().toString();
                String passwordString = newPassword.getText().toString(); // add password here

                //make change request
                mViewModel.getUpdateUser(realnameString.equals("") ? null : realnameString,
                        mailString.equals("") ? null : mailString,
                        !validateNewPassword() ? null : passwordString, context.getActivity()).observe(context.getActivity(), profileUpdateObserver);
            }
        });

    }

    private boolean validateNewPassword() {           //check for suitable password
        String password_str = newPassword.getText().toString();
        String password_check_str = confirmNewPassword.getText().toString();

        if (password_str.isEmpty() || password_check_str.isEmpty()) {
            return false;
        }
        if (password_str.length() < 8) {
            newPassword.setError("too short!");
            return false;
        } else if (password_str.length() > 16) {
            newPassword.setError("too long!");
            return false;
        } else if (!password_str.equals(password_check_str)) {
            confirmNewPassword.setError("Passwords do not match!");
            return false;
        }
        return true;
    }


    private TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String realNameInput = realName.getText().toString().trim();
            String usernameInput = username.getText().toString().trim();
            String emailInput = emailAddress.getText().toString().trim();

            if (!realNameInput.isEmpty() || !usernameInput.isEmpty() || !emailInput.isEmpty()) {
                save_btn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}