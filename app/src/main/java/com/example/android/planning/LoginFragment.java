package com.example.android.planning;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{
    //UI Items
    View v;
    Button logInButton;
    EditText usernameET;
    EditText passwordET;

    /**
     * Constructor for Login Fragment
     */
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_login, container, false);

        logInButton = (Button) v.findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInAccount();
            }
        });

        return v;
    }

    /**
     * Check the input and if it is valid, go to ToDoActivity
     */
    private void getInAccount() {
        usernameET = (EditText) v.findViewById(R.id.usernameET);
        String username = usernameET.getText().toString();
        passwordET = (EditText) v.findViewById(R.id.passwordET);
        String password = passwordET.getText().toString();

        if (username.equals("") || password.equals("")) {
            //Pop-Up Message
            Toast.makeText(getActivity(), "Enter your login data!", Toast.LENGTH_LONG).show();

        } else {
            if (((Welcome)this.getActivity()).getDatabase().verifyPassword(username, password)) {
                usernameET.setText("");
                passwordET.setText("");
                Intent i = new Intent(getActivity(), ToDoList.class);
                i.putExtra("Username", username);
                startActivity(i);

            } else {
                //Pop-Up Message
                Toast.makeText(getActivity(), "Password/Username incorrect!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
