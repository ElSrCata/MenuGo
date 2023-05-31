package com.oriol.menugo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oriol.menugo.Common.Common;
import com.oriol.menugo.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oriol
 */
public class SignUp extends AppCompatActivity {

    //Definición variables
    MaterialEditText edtPhone, edtName, edtPassword, edtSecureCode;
    Button btnSignUp;

    /**
     * Método onCreate de la actividad SignUp
     * @param savedInstanceState Si la actividad se reinicializa después de
     *      * previamente cerrado, entonces este paquete contiene los datos que más
     *      * suministrado recientemente en {@link #onSaveInstanceState}.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText) findViewById(R.id.edtName);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtSecureCode = (MaterialEditText)findViewById(R.id.edtSecureCode);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        //Iniciamos Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://menugo-9451c-default-rtdb.europe-west1.firebasedatabase.app/");
        final DatabaseReference user_table = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (isStrongPassword(edtPassword.getText().toString()) == false)
                    {

                    }
                    else
                    {
                        final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                        mDialog.setMessage("Espera...");
                        mDialog.show();

                        user_table.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Comprobamos si el teléfono ya está en uso

                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                    mDialog.dismiss();
                                    Toast.makeText(SignUp.this, "El teléfono ya está en uso", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDialog.dismiss();
                                    User user = new User(edtName.getText().toString(),
                                            edtPassword.getText().toString(),
                                            edtSecureCode.getText().toString());
                                    user_table.child(edtPhone.getText().toString()).setValue(user);
                                    Toast.makeText(SignUp.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(SignUp.this, R.string.connection, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    public boolean isStrongPassword(String password)
    {
        // Minimum length of 8 characters
        if (password.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener mínimo 8 carácteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // At least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(this, "La contraseña debe incluir mínimo una mayúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        // At least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(this, "La contraseña debe incluir mínimo una minúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        // At least one digit
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(this, "La contraseña debe incluir mínimo un número", Toast.LENGTH_SHORT).show();
            return false;
        }

        // At least one special character
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~-]");
        Matcher specialCharMatcher = specialCharPattern.matcher(password);
        if (!specialCharMatcher.find()) {
            Toast.makeText(this, "La contraseña debe incluir mínimo un carácter especial", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Passed all the checks, password is strong
        return true;
    }
}