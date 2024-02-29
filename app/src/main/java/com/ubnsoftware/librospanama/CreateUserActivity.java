package com.ubnsoftware.librospanama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextInputLayout name;
    private TextInputLayout username;
    private TextInputLayout correo;
    private TextInputLayout phone;
    private TextInputLayout contra;
    private String uid;
    String horaActual = obtenerHoraActual();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        correo = findViewById(R.id.email);
        phone = findViewById(R.id.phoneno);
        contra = findViewById(R.id.password);
    }

    private String obtenerHoraActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void registrarUsuarios (View view){
        mAuth.createUserWithEmailAndPassword(correo.getEditText().getText().toString(), contra.getEditText().getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){

                   String UID = mAuth.getCurrentUser().getUid();
                   Map<String, Object> map = new HashMap<>();
                   map.put("Nombre", name.getEditText().getText().toString());
                   map.put("Usuario", username.getEditText().getText().toString());
                   map.put("Correo", correo.getEditText().getText().toString());
                   map.put("Telefono", phone.getEditText().getText().toString());
                   map.put("Contraseña", contra.getEditText().getText().toString());
                   map.put("UID", UID);

                   FirebaseUser user = mAuth.getCurrentUser();
                   String nombrePersona = username.getEditText().getText().toString();


                   mDatabase.child("Usuarios").child(nombrePersona + " - " + horaActual).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task2) {
                           if (task2.isSuccessful()){
                               Toast.makeText(getApplicationContext(), "Usuario Registrado.", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                               startActivity(intent);
                               finish();
                           }
                       }
                   });
               } else{
                   Toast.makeText(getApplicationContext(), "Error en la creación de usuario.", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}