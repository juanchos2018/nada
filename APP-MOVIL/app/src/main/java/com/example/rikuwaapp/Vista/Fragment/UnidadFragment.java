package com.example.rikuwaapp.Vista.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.rikuwaapp.Adapter.AdapterUnidad;
import com.example.rikuwaapp.Config.Helper;
import com.example.rikuwaapp.Entidad.Unidad;
import com.example.rikuwaapp.R;
import com.example.rikuwaapp.Vista.LoginActivity;
import com.example.rikuwaapp.Vista.MapActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UnidadFragment extends Fragment  {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    List<Unidad> unidadList;
    DatabaseReference databaseReference;
    Button btnRegistrarUnidad;
    AdapterUnidad adapterUnidad;
    DatabaseReference databaseReference2;
    private FirebaseAuth mAuth;

    private static final String TAG = "UnidadFragment";
    String id_usuario;
    String user_id,keycarpeta;
    public UnidadFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unidad, container, false);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        Log.e(TAG,  user_id);

        setViews(view);
      //  listarUnidads();
        return view;
    }

    private void setViews(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("UnidadUsuario").child(user_id);
        btnRegistrarUnidad = view.findViewById(R.id.btnRegistrarUnidad);
        unidadList = new ArrayList<>();
     //   listarUnidads();
        recyclerView = view.findViewById(R.id.recycler_unidad);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        btnRegistrarUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarUnidadFragment registrarUnidadFragment = new RegistrarUnidadFragment();
                FragmentManager fragmentManager = getFragmentManager();
                Helper.setFragmentManager(registrarUnidadFragment,fragmentManager);
            }
        });
    }
   // final int condador=0;


    @Override
    public void onStart() {
        super.onStart();
        Query q =databaseReference;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unidadList.clear();
                int c =0;
                if (snapshot.exists()){
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        Unidad un = obj.getValue(Unidad.class);
                        unidadList.add(un);
                        // unidadList.add(obj.getValue(Unidad.class));
                    }
                    //    adapter.notifyDataSetChanged();
                    adapterUnidad = new AdapterUnidad(unidadList);
                    recyclerView.setAdapter(adapterUnidad);
                    adapterUnidad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Bundle carro= new Bundle();
                            Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                        //    nuclase = listaSalones.get(recycler.getChildAdapterPosition(v)).getNumeroclase();
                            EditarUnidadFragment editarUnidadFragment = new EditarUnidadFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("nombrePlaca", unidadList.get(recyclerView.getChildAdapterPosition(view)).getNombrePlaca());
                            FragmentManager fragmentManager = getFragmentManager();
                            editarUnidadFragment.setArguments(bundle);
                            Helper.setFragmentManager(editarUnidadFragment,fragmentManager);
                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void listarUnidads() {

       // final int condador=0;
        Query q =databaseReference;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unidadList.clear();
                int c =0;
                if (snapshot.exists()){
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        Unidad un = obj.getValue(Unidad.class);
                        unidadList.add(un);
                       // unidadList.add(obj.getValue(Unidad.class));
                    }
                //    adapter.notifyDataSetChanged();
                    adapter = new AdapterUnidad(unidadList);
                    recyclerView.setAdapter(adapter);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

/*
    @Override
    public void onItemClick(AdapterUnidad.ViewHolder holder, int posicion) {

        String nombrePlaca = unidadList.get(posicion).getNombrePlaca();
        EditarUnidadFragment editarUnidadFragment = new EditarUnidadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nombrePlaca", nombrePlaca);
        FragmentManager fragmentManager = getFragmentManager();
        editarUnidadFragment.setArguments(bundle);
        Helper.setFragmentManager(editarUnidadFragment,fragmentManager);
    }

 */
}