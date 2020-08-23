package com.example.rikuwaapp.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.rikuwaapp.Config.Helper;
import com.example.rikuwaapp.R;
import com.example.rikuwaapp.Vista.Fragment.HorarioFragment;
import com.example.rikuwaapp.Vista.Fragment.NotificarFragment;
import com.example.rikuwaapp.Vista.Fragment.PersonalFragment;
import com.example.rikuwaapp.Vista.Fragment.ProductosFragment;
import com.example.rikuwaapp.Vista.Fragment.UnidadFragment;
import com.google.android.material.navigation.NavigationView;

public class AdministradorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    UnidadFragment unidadFragment;
    ProductosFragment productosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        drawerLayout = findViewById(R.id.drawerLayoutAdministrador);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        InicializarFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent i = new Intent();
        switch (menuItem.getItemId()) {
            case R.id.nav_unidades:
                unidadFragment = new UnidadFragment();
                setFragment(unidadFragment);
                return true;
            case R.id.nav_productos_gestionar:
                ProductosFragment productosFragment = new ProductosFragment();
                setFragment(productosFragment);
                return true;
            case R.id.nav_horarios:
                HorarioFragment horarioFragment = new HorarioFragment();
                setFragment(horarioFragment);
                return true;
            case R.id.nav_personal:
                PersonalFragment personalFragment = new PersonalFragment();
                setFragment(personalFragment);
                return true;
            case R.id.nav_notificar:
                NotificarFragment notificarFragment = new NotificarFragment();
                setFragment(notificarFragment);
                return true;
            case R.id.nav_cerrarseion:
                Helper.LimpiarSharedPreferences(this);
                i = new Intent(AdministradorActivity.this, LoginActivity.class);
                startActivity(i);
                return true;

            case R.id.AcercaDe:
                // startActivity(new Intent(this,AcercaDe.class));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("RIKUWA -Version 1.0 es una aplicacion de mercado móvil. Encuentra tu mercado más cercano respecto a tu zona....")
                        .setTitle("RIKUWA APP")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                break;

            case R.id.nav_compartir:
                Intent paramView;
                paramView = new Intent("android.intent.action.SEND");
                paramView.setType("text/plain");
                paramView.putExtra("android.intent.extra.TEXT", "Descarga nuestra app de la PlayStore" +
                        " \n" + "https://play.google.com/store/apps/details?id=app.product.demarktec.alo14_pasajero");
                startActivity(Intent.createChooser(paramView, "Comparte Nuestro aplicativo RIKUWA, tiempos de COVID-19"));
                break;

            case R.id.Pro_seg:
                Intent paramView2;
                paramView = new Intent("android.intent.action.SEND");
                paramView.setType("text/plain");
                paramView.putExtra("android.intent.extra.TEXT", "Protocolo Bioseguridad Abastos" +
                        " \n" + "https://www.minsalud.gov.co/Normatividad_Nuevo/Resoluci%C3%B3n%20887%20de%202020.pdf");
                startActivity(Intent.createChooser(paramView, "Comparte Nuestro aplicativo RIKUWA, tiempos de COVID-19"));
                break;

            case R.id.nav_salir:
                Helper.LimpiarSharedPreferences(this);
                i = new Intent(AdministradorActivity.this, LoginActivity.class);
                System.exit(0);
                return true;
        }
        return false;
    }

    private void InicializarFragment() {
        UnidadFragment perfilFragment = new UnidadFragment();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.fragment_container, perfilFragment);
        fragmentTransaction2.commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}