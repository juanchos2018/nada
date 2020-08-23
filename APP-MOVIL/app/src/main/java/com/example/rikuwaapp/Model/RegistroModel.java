package com.example.rikuwaapp.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rikuwaapp.Entidad.Mercado;
import com.example.rikuwaapp.Entidad.Usuario;
import com.example.rikuwaapp.Interface.RegistroInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroModel implements RegistroInterface.Modelo {

    RegistroInterface.TaskListener listener;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private FirebaseUser user1;

    public RegistroModel(RegistroInterface.TaskListener listener) {
        this.listener = listener;
        auth = FirebaseAuth.getInstance();
    }

    @Override
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
                                                                    listener.mtdOnSuccess();
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
                                    listener.mtdOnError(task.getException().getMessage());
                                }
                            }
                        });
                    }

                } else if (task.getException() != null) {
                    listener.mtdOnError(task.getException().getMessage());
                }
            }
        });
    }
}
