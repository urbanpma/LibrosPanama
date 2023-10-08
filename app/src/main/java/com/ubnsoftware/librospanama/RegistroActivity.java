package com.ubnsoftware.librospanama;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout correo;
    private TextInputLayout contra;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        correo = findViewById(R.id.email);
        contra = findViewById(R.id.password);

        EditText editTextEmail = correo.getEditText();
        EditText editTextPassword = contra.getEditText();

        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

    }

    public void registrarUsuarios (View view){

        mAuth.createUserWithEmailAndPassword(correo.getEditText().getText().toString(), contra.getEditText().getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){
                   Toast.makeText(getApplicationContext(), "Registro Creado Correctamente", Toast.LENGTH_SHORT).show();
                   FirebaseUser user = mAuth.getCurrentUser();
                   Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                   startActivity(intent);
                   finish();
               } else{
                   Toast.makeText(getApplicationContext(), "Fallo en la autenticación", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}