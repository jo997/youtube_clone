package de.unituebingen.streamapp.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.unituebingen.streamapp.MainActivity;
import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.login.LoginViewModel;
import de.unituebingen.streamapp.tools.User;


public class Register extends Fragment {

    private EditText real_name;
    private EditText username;
    private EditText e_mail;
    private EditText password;
    private EditText password_check;
    private Button registerButton;
    private TextView to_login_btn;


    private RegisterViewModel mViewModel;
    private LoginViewModel mLoginViewModel;

    public static Register newInstance() {
        return new Register();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_fragment, container, false);


        username = view.findViewById(R.id.input_username);
        real_name = view.findViewById(R.id.input_realName);
        e_mail = view.findViewById(R.id.input_email);
        password = view.findViewById(R.id.input_password);
        password_check = view.findViewById(R.id.input_confirm_password);
        registerButton = view.findViewById(R.id.registerButton);
        to_login_btn = view.findViewById(R.id.to_login_btn);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Get Context
        Fragment context = this;

        // User Data object
        UserData ud = new UserData(this.getActivity());

        // Set observer for register request
        Observer<User> registerObserver = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Toast.makeText(context.getActivity().getApplicationContext(),
                            "Welcome " + user.getUserName(),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context.getActivity().getApplicationContext(),
                            "Failed to register!",
                            Toast.LENGTH_LONG).show();
                }
            }
        };


        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateUsername() && validateEmail() && validatePassword()) {       // if everything is right we can register

                    String userName = username.getText().toString();
                    String realName = real_name.getText().toString();
                    String email = e_mail.getText().toString();
                    String password = password_check.getText().toString();

                    MutableLiveData<User> user = mViewModel.getRegisterUser(realName, userName, email, password);
                    user.observe(context.getActivity(), registerObserver);

                    MutableLiveData<String> auth = mLoginViewModel.getLoginData(userName,password);

                    if (user.getValue() != null && auth.getValue() != null) {
                        ud.setUserInfo(user.getValue());
                        ud.setAuthentication(auth.getValue());
                        ud.setLoggedIn(true);
                        Log.println(Log.ERROR, "Login Register", ud.getRealname());
                    }

                    NavHostFragment.findNavController(context).navigate(R.id.action_nav_register_to_nav_home);
                }
                MainActivity.drawerMenu.setGroupVisible(R.id.afterAuth, true);
                MainActivity.drawerMenu.setGroupVisible(R.id.LogReg, false);
            }
        });


        to_login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                NavHostFragment.findNavController(context).navigate(R.id.action_nav_register_to_nav_login);

            }
        });
    }


    private boolean validateUsername() {           //check for suitable username
        if (username.length() == 0) {
            username.setError("Field can't be empty");
            return false;
        } else if (username.length() > 16) {
            username.setError("too long!");
            return false;
        }
        return true;
    }

    private boolean validateEmail() {              //check for suitable email
        String e_mail_str = e_mail.getText().toString();
        if (e_mail.length() == 0) {
            e_mail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(e_mail_str).matches()) {
            e_mail.setError("Please enter a valid email address.");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {           //check for suitable password
        String password_str = password.getText().toString();
        String password_check_str = password_check.getText().toString();
        if (password.length() == 0) {
            password.setError("Field can't be empty");
            return false;
        } else if (password.length() < 8) {
            password.setError("too short!");
            return false;
        } else if (password.length() > 16) {
            password.setError("too long!");
            return false;
        } else if (!password_str.equals(password_check_str)) {
            password_check.setError("Passwords do not match!");
            return false;
        }
        return true;
    }


}
