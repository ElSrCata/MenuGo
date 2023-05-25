package com.oriol.menugo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oriol.menugo.Common.Common;
import com.oriol.menugo.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;

    CheckBox ckbRemember;
    TextView txtForgotPswd;

    FirebaseDatabase database;
    DatabaseReference user_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        ckbRemember = (CheckBox)findViewById(R.id.ckbRemember);
        txtForgotPswd = (TextView)findViewById(R.id.txtForgotPswd);

        //Init Paper
        Paper.init(this);

        //Iniciamos Firebase
       database = FirebaseDatabase.getInstance("https://menugo-9451c-default-rtdb.europe-west1.firebasedatabase.app/");
       user_table = database.getReference("User");

        txtForgotPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPwd();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    //Save user & password
                    if(ckbRemember.isChecked())
                    {
                        Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());
                    }

                    ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Espera...");
                    mDialog.show();

                    user_table.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Comprueba si el usuario existe en la BBDD
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                //Cojemos la información del usuario
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                //Hacemos el set del número de teléfono
                                user.setPhone(edtPhone.getText().toString());

                                if (edtPassword.getText().toString().isEmpty() || edtPhone.getText().toString().isEmpty())
                                {
                                    Toast.makeText(SignIn.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    if (user.getPassword().equals(edtPassword.getText().toString())) {
                                        Intent homeIntent = new Intent(SignIn.this, Home.class);
                                        Common.current_User = user;
                                        startActivity(homeIntent);
                                        finish();

                                        user_table.removeEventListener(this);
                                    } else {
                                        Toast.makeText(SignIn.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(SignIn.this, R.string.connection, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void showForgotPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.forgotpwd);
        builder.setMessage(R.string.introduceSecurityCode);

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password, null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.baseline_security_24);

        MaterialEditText edtPhone = (MaterialEditText) forgot_view.findViewById(R.id.edtPhone);
        MaterialEditText edtSecureCode = (MaterialEditText) forgot_view.findViewById(R.id.edtSecureCode);

        builder.setPositiveButton(R.string.confirmOrder, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user_table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user = snapshot.child(edtPhone.getText().toString()).getValue(User.class);

                        if(user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(SignIn.this, "Tu contraseña: " + user.getPassword(), Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(SignIn.this, "Código de seguridad erróneo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        builder.setNegativeButton(R.string.denegateOrder, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
}