package com.example.instantEat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import backend.Buscador;
import backend.BuscadorConcreto;
import backend.Plato;

public class OwnerDashboardActivity extends AppCompatActivity {
    SharedPreferences prefs;
    TextView totalDishesText, totalVeganDishesText, totalGlutenFreeDishesText;
    ImageButton goBackButton;
    Button addDishButton;
    ListView dishList;
    String restaurantName;
    AdapterDish adapterDish;
    ArrayList<Plato> dishes;
    ArrayList<String> dishNames, dishIngredients, dishPrices;
    ArrayList<Boolean> dishVegan, dishGlutenFree;
    BuscadorConcreto buscador;
    Buscador iBuscador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantName = prefs.getString("name", "NULL");

        setTitle("Platos de " + restaurantName);
        dishList = findViewById(R.id.dishList);

        addDishButton = findViewById(R.id.addDishMenuButton);
        goBackButton = findViewById(R.id.goBackButton);
        totalDishesText = findViewById(R.id.totalDishesText);
        totalGlutenFreeDishesText = findViewById(R.id.totalGlutenFreeDishesText);
        totalVeganDishesText = findViewById(R.id.totalVeganDishesText);

        dishes = Utilities.getDishList(this, restaurantName);

        buscador = new BuscadorConcreto(dishes);
        iBuscador = buscador;
        fillLists(dishes);
        fillDashboard();
        adapterDish = new AdapterDish(this, dishNames, dishIngredients, dishPrices, dishGlutenFree, dishVegan);
        dishList.setAdapter(adapterDish);
        dishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), "Has pulsado: " + dishes.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditDishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("dishName", dishNames.get(i)); //Parámetro para la actividad
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        addDishButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), EditDishActivity.class)));
        goBackButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OwnerMenuActivity.class).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK)));
    }

    private void fillLists(ArrayList<Plato> dishList){
        dishNames = new ArrayList<String>();
        dishIngredients = new ArrayList<String>();
        dishPrices = new ArrayList<String>();
        dishVegan = new ArrayList<Boolean>();
        dishGlutenFree = new ArrayList<Boolean>();
        for (Plato plato:dishList){
            dishNames.add(plato.getNombre());
            dishIngredients.add(Utilities.arrayListToString(plato.getIngredientes()));
            dishPrices.add(String.valueOf(plato.getPrecio()));
            dishVegan.add(plato.isVegano());
            dishGlutenFree.add(plato.isGlutenFree());
        }
    }
        private void fillDashboard(){
            totalDishesText.setText(String.valueOf(dishes.size()));
            totalGlutenFreeDishesText.setText(String.valueOf(buscador.mostrarGlutenFree().size()));
            buscador.resetLista();
            totalVeganDishesText.setText(String.valueOf(buscador.mostrarVeganos().size()));
        }

}