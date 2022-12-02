package de.unituebingen.streamapp.login;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import de.unituebingen.streamapp.MainActivity;
import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.User;


public class Login extends Fragment {


    private static final String TAG = " ";
    private EditText user_login;
    private EditText user_password;
    private Button login_bt;
    private Button toRegister_bt;
    private LoginViewModel mViewModel;

    public static Login newInstance() {
        return new Login();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);

        user_login = view.findViewById(R.id.log_user);
        user_password = view.findViewById(R.id.log_password);
        login_bt = view.findViewById(R.id.log_bt);
        toRegister_bt = view.findViewById(R.id.toRegister_bt);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // get context
        Fragment context = this;

        // set observer for login request
        Observer<String> loginObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == "") {
                    //   Toast.makeText(context.getActivity().getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                } else {
                    //    Toast.makeText(context.getActivity().getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // make observer observe the data
        UserData ud = new UserData(this.getActivity());
        mViewModel.getLoginData(null, null).observe(context.getActivity(), loginObserver);


        //OnCLickListener for LoginButton
        login_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateInput()) {      // try login if Input is valid

                    String user_login_str = user_login.getText().toString();
                    String user_password_str = user_password.getText().toString();

                    mViewModel.getLoginData(user_login_str, user_password_str);

                    String auth = mViewModel.getLoginData(user_login_str, user_password_str).getValue();
                    ud.setAuthentication(auth);
                    Log.d(TAG, "onClickLogin: " + auth);
                    // get auth
                    if (ud.getAuthentication() != null)
                        Log.println(Log.ERROR, "Login", ud.getAuthentication());

                    if (auth != null) {
                        ud.setLoggedIn(true);
                        MutableLiveData<User> me = mViewModel.getMe(auth);
                        ud.setUserInfo(me.getValue());
                        Log.println(Log.ERROR, "Login User", ud.getRealname());

                        // set menu visibility
                        MainActivity.drawerMenu.setGroupVisible(R.id.afterAuth, true);
                        MainActivity.drawerMenu.setGroupVisible(R.id.LogReg, false);
                        Log.d(TAG, "onClickLogin: " + auth);
                        Toast.makeText(context.getActivity().getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // do after 1 sec
                                NavHostFragment.findNavController(context).navigate(R.id.action_nav_login_to_nav_home);
                            }
                        }, 1000);
                    } else {
                        Toast.makeText(context.getActivity().getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        // nav to register
        toRegister_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(context).navigate(R.id.action_nav_login_to_nav_register);
            }
        });
    }


    private boolean validateInput() {           //check for suitable username and password
        if (user_login.length() == 0) {
            user_login.setError("Field can't be empty");
            return false;
        } else if (user_password.length() == 0) {
            user_password.setError("Field can't be empty");
        }
        return true;
    }

}

