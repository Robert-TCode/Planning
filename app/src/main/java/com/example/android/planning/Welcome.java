package com.example.android.planning;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Welcome extends FragmentActivity {
    //Database with Accounts and Tasks tables
    DatabaseHelper database = new DatabaseHelper(this);

    //UI Items
    Button switchFragmentButton;
    TextView message;
    boolean statusFragmentLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        message = (TextView)findViewById(R.id.message_tv);

        switchFragmentButton = (Button) findViewById(R.id.switch_fragment_button);
        switchFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusFragmentLogin) {
                    displayRegisterFragment();
                } else {
                    displayLoginFragment();
                }
            }
        });

        displayLoginFragment();
    }

    /**
     * Display Login Fragment and hide Register Fragment
     */
    private void displayLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LoginFragment loginFragment  = new LoginFragment();
        fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        fragmentTransaction.commit();

        statusFragmentLogin = true;
        message.setText(this.getResources().getString(R.string.new_user));
        switchFragmentButton.setText(this.getResources().getString(R.string.create_account));
    }

    /**
     * Display Register Fragment and hide Login Fragment
     */
    private void displayRegisterFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RegisterFragment registerFragment  = new RegisterFragment();
        fragmentTransaction.replace(R.id.fragment_container, registerFragment);
        fragmentTransaction.commit();

        statusFragmentLogin = false;
        message.setText(this.getResources().getString(R.string.already_account));
        switchFragmentButton.setText(this.getResources().getString(R.string.login_here));
    }

    /**
     * Return the database
     */
    public DatabaseHelper getDatabase() {
        return database;
    }

}
