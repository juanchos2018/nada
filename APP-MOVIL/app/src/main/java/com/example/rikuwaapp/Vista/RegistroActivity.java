package com.example.rikuwaapp.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.rikuwaapp.Config.Helper;
import com.example.rikuwaapp.Entidad.Usuario;
import com.example.rikuwaapp.Interface.LoginInterface;
import com.example.rikuwaapp.Interface.RegistroInterface;
import com.example.rikuwaapp.Presentador.RegistroPresentador;
import com.example.rikuwaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener, RegistroInterface.Vista, AdapterView.OnItemSelectedListener {
    EditText et_correo, et_password;
    Button btn_ir_crearCuenta, btnSendToLogin;
    MaterialDialog dialog;
    RegistroInterface.Presentador presentador;
    Spinner spinner_tipousuario,spinner_zona;
    FirebaseAuth auth;
    private FirebaseUser user1;
    DatabaseReference databaseReference;
    String tipo_usu;

    ArrayList<String> zonas;
    ArrayAdapter<String> adaperspinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setViews();
        btn_ir_crearCuenta.setOnClickListener(this);
        btnSendToLogin.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        zonas=new ArrayList<String>();
        zonas.add("Alto de la Alianza");
        zonas.add("Ciudad Nueva");
        zonas.add("Inclan");
        zonas.add("Pachia");
        zonas.add("Palca");
        zonas.add("Pocollay");
        zonas.add("Sama");
        zonas.add("Tacna");
        spinner_zona=findViewById(R.id.spinner_zona);
        spinner_zona.setVisibility(View.INVISIBLE);
        adaperspinner= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,zonas);
        spinner_zona.setAdapter(adaperspinner);

    }

    public void setViews() {
        presentador = new RegistroPresentador(this);
        et_correo = findViewById(R.id.et_correo);
        et_password = findViewById(R.id.et_password);
        btn_ir_crearCuenta = findViewById(R.id.btn_ir_crearCuenta);
        btnSendToLogin = findViewById(R.id.btnSendToLogin);
        spinner_tipousuario = findViewById(R.id.spinner_tipousuario);

        spinner_tipousuario.setOnItemSelectedListener(this);


        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("CARGANDO")
                .content("Espere porfavor ...")
                .cancelable(false)
                .progress(true, 0);//true para que sea indeterminado y que no tenga maximo
        dialog = builder.build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ir_crearCuenta:
                mtdHandleRegistro();
                break;
            case R.id.btnSendToLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    @Override
    public void mtdMostrarProgress() {
        dialog.show();
    }

    @Override
    public void mtdOcultarProgress() {
        dialog.dismiss();
    }

    @Override
    public void mtdHandleRegistro() {
        if (!mtdValidarEmail()) {
            Toast.makeText(RegistroActivity.this, "No es un email valido", Toast.LENGTH_SHORT).show();
        } else if (!mtdValidarPassword()) {
            Toast.makeText(RegistroActivity.this, "No es un password valido", Toast.LENGTH_SHORT).show();
        } else {
            //trim: para que no haya espacios
            final Usuario obj = new Usuario();
            obj.setEmail(et_correo.getText().toString().trim());
            obj.setPassword(et_password.getText().toString().trim());
            obj.setTipoUsuario(spinner_tipousuario.getSelectedItem().toString());
            tipo_usu=spinner_tipousuario.getSelectedItem().toString();
            obj.setDistrito(spinner_zona.getSelectedItem().toString());

            // suscribirse  a la zona que elige we

            FirebaseMessaging.getInstance().subscribeToTopic(spinner_zona.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //presentador.mtdOnRegistro(obj);
                    mtdOnRegistro(obj);
                }
            });


        }
    }

    public void mtdOnRegistro(final Usuario obj) {

        auth.createUserWithEmailAndPassword(obj.getEmail(), obj.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(obj.getEmail())
                            .build();
                    FirebaseUser user = auth.getCurrentUser();
                    user1 = auth.getCurrentUser();
                    final String current_userID =  auth.getCurrentUser().getUid();
                    if (user != null) {
                        final FirebaseUser finalUser = user;
                        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    obj.setPassword("");

                                    databaseReference = FirebaseDatabase.getInstance().getReference("usuario").child(current_userID);
                                    databaseReference.child("apellido").setValue("apellido");
                                    databaseReference.child("direccion").setValue("");
                                    databaseReference.child("nombre").setValue("");
                                    databaseReference.child("email").setValue(obj.getEmail());
                                    databaseReference.child("password").setValue(obj.getPassword());
                                    databaseReference.child("zona").setValue(obj.getDistrito());
                                    databaseReference.child("tipoUsuario").setValue(obj.getTipoUsuario()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()){
                                                user1=auth.getCurrentUser();
                                                user1.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(RegistroActivity.this, "REgistado", Toast.LENGTH_SHORT).show();
                                                            //  Toast.makeText(Registro.this, "Registrado", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            auth.signOut();
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    });
/*
                                    databaseReference.child("usuario").child(current_userID).setValue(obj, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            listener.mtdOnSuccess();
                                            user1.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        //  Toast.makeText(Registro.this, "Registrado", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        auth.signOut();
                                                    }
                                                }
                                            });
                                        }
                                    });

 */
                                } else if (task.getException() != null) {
                              //      listener.mtdOnError(task.getException().getMessage());
                                    Toast.makeText(RegistroActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                } else if (task.getException() != null) {
                 //   listener.mtdOnError(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public boolean mtdValidarEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(et_correo.getText().toString()).matches();
    }

    @Override
    public boolean mtdValidarPassword() {
        //TextUtils.isEmpty: si esta vacio y es menor que 4, no es valido
        if (TextUtils.isEmpty(et_password.getText().toString()) && et_password.getText().toString().length() < 4) {
            Toast.makeText(RegistroActivity.this, "No es una contraseña valida", Toast.LENGTH_SHORT).show();
            et_password.setError("No es una contraseña valida");
            return false; // devuelve false
        } else {
            return true;
        }
    }

    @Override
    public void mtdOnRegistro() {
        //Toast.makeText(this, "Se ha registrado exitosamente", Toast.LENGTH_SHORT).show();
        Usuario obj = new Usuario();
        obj.setEmail(et_correo.getText().toString().trim());
        Helper.CapturarDataUsuarioSesion(this, obj);

        if (tipo_usu.equals("Administrador")){

            startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
        }
        else  if (tipo_usu.equals("Cliente")){

            startActivity(new Intent(RegistroActivity.this, MapActivity.class));
        }


    }

    @Override
    public void mtdOnError(String error) {

        Toast.makeText(RegistroActivity.this, error, Toast.LENGTH_SHORT).show();//muestra el error que envia el tasklistener
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (spinner_tipousuario.getSelectedItem().equals("Cliente")){
            //Toast.makeText(this, listView.getItemAtPosition(position) +" " +PrecioCaballeros[position], Toast.LENGTH_SHORT).show();
            //  Toast.makeText(this, "Eligio Fondos", Toast.LENGTH_SHORT).show();
            spinner_zona.setVisibility(View.VISIBLE);
        }
        if (spinner_tipousuario.getSelectedItem().equals("Administrador")){
            //Toast.makeText(this, listView.getItemAtPosition(position) +" " +PrecioCaballeros[position], Toast.LENGTH_SHORT).show();
            //  Toast.makeText(this, "Eligio Fondos", Toast.LENGTH_SHORT).show();
            spinner_zona.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}