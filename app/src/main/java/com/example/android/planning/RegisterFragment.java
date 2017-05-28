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
public class RegisterFragment extends Fragment implements View.OnClickListener{
    //UI Items
    Button registerButton;
    View v;

    /**
     * Constructor for Register Fragment
     */
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_register, container, false);
        registerButton = (Button) v.findViewById(R.id.resgisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Check if input in valid and if it is, add the data to the database and go to LoginFragment
     */
    private void createAccount() {
        EditText username = (EditText) v.findViewById(R.id.usernameInput);
        EditText password = (EditText) v.findViewById(R.id.passwordInput);
        EditText passwordValidation = (EditText) v.findViewById(R.id.passwordInput2);
        EditText phoneNumber = (EditText) v.findViewById(R.id.phoneNumberInput);

        String usernameS = username.getText().toString();
        String passwordS = password.getText().toString();
        String passwordValidationS= passwordValidation.getText().toString();
        String phoneNumberS = phoneNumber.getText().toString();

        if (usernameS.equals("") || passwordS.equals("") || phoneNumberS.equals("")) {
            //Pop-Up Message
            Toast.makeText(getActivity(), "Fill al the fields, please!", Toast.LENGTH_LONG).show();
        } else {
            if (!passwordS.equals(passwordValidationS)) {
                //Pop-Up Message
                Toast.makeText(getActivity(), "Password doesn't match", Toast.LENGTH_LONG).show();
            } else {
                //Insert the data in database
                Account account = new Account();
                account.setUsername(usernameS);
                account.setPassword(passwordS);
                account.setPhoneNumber(phoneNumberS);

                if (((Welcome)this.getActivity()).getDatabase().insertAccount(account)) {
                    //Pop-Up Message
                    username.setText("");
                    password.setText("");
                    passwordValidation.setText("");
                    phoneNumber.setText("");
                    Toast.makeText(getActivity(), "Account created successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), Welcome.class);
                    startActivity(intent);

                } else {
                    //Pop-Up Message
                    Toast.makeText(getActivity(), "This username is unavailable", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
