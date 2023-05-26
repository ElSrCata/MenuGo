package com.oriol.menugo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oriol.menugo.Common.Common;
import com.oriol.menugo.Model.User;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnSignUp;
    TextView txtSlogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        txtSlogan = (TextView)findViewById(R.id.txtSlogan);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

        //Init Paper
        Paper.init(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(signIn);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUp);
            }
        });

        //Check remember button
        String user = Paper.book().read(Common.USER_KEY);
        String pswd = Paper.book().read(Common.PWD_KEY);

        if(user != null && pswd != null)
        {
            if(!user.isEmpty() && !pswd.isEmpty())
            {
                login(user, pswd);
            }
        }

    }

    private void login(String phone, String pwd) {

        //Iniciamos Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://menugo-9451c-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference user_table = database.getReference("User");

        if (Common.isConnectedToInternet(getBaseContext())) {

            ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Espera...");
            mDialog.show();

            user_table.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Comprueba si el usuario existe en la BBDD
                    if (dataSnapshot.child(phone).exists()) {
                        //Cojemos la información del usuario
                        mDialog.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);
                        //Hacemos el set del número de teléfono
                        user.setPhone(phone);

                        if (user.getPassword().equals(pwd)) {
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.current_User = user;
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this, R.string.connection, Toast.LENGTH_SHORT).show();
            return;
        }
    }

}