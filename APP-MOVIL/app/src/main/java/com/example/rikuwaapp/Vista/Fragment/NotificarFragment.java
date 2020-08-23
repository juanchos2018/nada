package com.example.rikuwaapp.Vista.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rikuwaapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText ettitulo,etdetalle;
    private Button btnenviar;
    private Spinner spinner;

    ArrayList<String> zonas;
    ArrayAdapter<String> adaperspinner;
    private static final String TAG = "NotificarFragment";

    public NotificarFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NotificarFragment newInstance(String param1, String param2) {
        NotificarFragment fragment = new NotificarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View vista = inflater.inflate(R.layout.fragment_notificar, container, false);
        ettitulo=vista.findViewById(R.id.txttitul);
        etdetalle=vista.findViewById(R.id.txtddetalle);
        btnenviar=(Button)vista.findViewById(R.id.btnenviar);
        spinner=(Spinner)vista.findViewById(R.id.spinner_zona);



        zonas=new ArrayList<String>();
        zonas.add("Alto de la Alianza");
        zonas.add("Ciudad Nueva");
        zonas.add("Inclan");
        zonas.add("Pachia");
        zonas.add("Palca");
        zonas.add("Pocollay");
        zonas.add("Sama");
        zonas.add("Tacna");

        adaperspinner= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,zonas);
        spinner.setAdapter(adaperspinner);


        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zona =spinner.getSelectedItem().toString();
                String titulo =ettitulo.getText().toString();
                String detalle =etdetalle.getText().toString();

                enviarnotificacion(zona,titulo,detalle);
            }
        });

        return vista;

    }

    private  void mensaje(){
        Toast.makeText(getContext(), "Holl we", Toast.LENGTH_SHORT).show();
    }
    private  void mensaje2(){
        Toast.makeText(getContext(), "Holl we", Toast.LENGTH_SHORT).show();
    }

    private void enviarnotificacion(String zona,String titulo,String detalle ) {
        RequestQueue myrequest = Volley.newRequestQueue(getActivity());
        JSONObject json = new JSONObject();
        try {

            Toast.makeText(getContext(), zona, Toast.LENGTH_SHORT).show();
            json.put("to", "/topics/" + zona);
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", titulo);
            notificacion.put("detalle", detalle);
            json.put("data", notificacion);
            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA3pSHnAY:APA91bFscBqj3YP-51iX2sXTLMdwV9I9rJsLEvwCCxZF5dWMWLUqCzpUcfMwOf-1akcJQs6l8-_S4mR41QLVRIP1eqJhCH5PPMp-h3IxuEv96xAet7916z6q3qy4VkJr9dfFBvfk4Ba1");
                //    Toast.makeText(getContext(), "Envaiado we", Toast.LENGTH_SHORT).show();
                    return header;
                }
            };
            myrequest.add(request);

        } catch (JSONException ex) {
            ex.printStackTrace();
            Log.e(TAG, "onComplete: " + ex.getMessage());
        }

    }
}