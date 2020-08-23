package com.example.rikuwaapp.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rikuwaapp.Entidad.Unidad;
import com.example.rikuwaapp.Interface.RegistrarUnidadInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarUnidadModel implements RegistrarUnidadInterface.Modelo {

    RegistrarUnidadInterface.TaskListener listener;
    DatabaseReference databaseReference,reference2;

    private FirebaseAuth mAuth;

    String id_usuario;
    String user_id,keycarpeta;


    public RegistrarUnidadModel(RegistrarUnidadInterface.TaskListener listener){
        this.listener = listener;
    }

    @Override
    public void mtdOnRegistrarUnidad(final Unidad obj) {

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
       // databaseReference = FirebaseDatabase.getInstance().getReference("unidad").child(user_id);
        databaseReference = FirebaseDatabase.getInstance().getReference("UnidadUsuario").child(user_id);

        final String key  = databaseReference.push().getKey();

        obj.setId_unidad(key);
        databaseReference.child(key).setValue(obj, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                reference2=FirebaseDatabase.getInstance().getReference("unidad");
                reference2.child(key).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.mtdOnSuccess();
                    }
                });


            }
        });
    }
}
