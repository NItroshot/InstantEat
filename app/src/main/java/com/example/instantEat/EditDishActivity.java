package com.example.instantEat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import backend.AbstractFactoryPlato;
import backend.IAbstractFactoryPlato;
import backend.Plato;

public class EditDishActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Button addDishEditorButton, addIngredientButton, removeIngredientButton, deleteDishEditorButton;
    EditText dishNameField, ingredientField, priceField;
    TextView ingredientsListText;
    CheckBox isVeganCheckBox, isGlutenFreeCheckBox;
    AbstractFactoryPlato factoryPlato = new AbstractFactoryPlato();
    IAbstractFactoryPlato iAbstractFactoryPlato;
    ArrayList<String> ingredients = new ArrayList();
    Bundle bundle;
    String restaurantName, dishName, strIngredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dish_editor);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bundle = getIntent().getExtras();
        dishName = "null";
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantName = prefs.getString("name", "NULL");

        iAbstractFactoryPlato = factoryPlato;

        dishNameField = findViewById(R.id.dishNameField);
        ingredientField = findViewById(R.id.ingredientField);
        ingredientsListText = findViewById(R.id.ingredientsListText);
        priceField = findViewById(R.id.priceField);

        isVeganCheckBox = findViewById(R.id.isVeganTextBox);
        isGlutenFreeCheckBox = findViewById(R.id.isGlutenFreeCheckBox);

        addIngredientButton = findViewById(R.id.addIngredientButton);
        removeIngredientButton = findViewById(R.id.removeIngredientButton);
        addDishEditorButton = findViewById(R.id.saveDishEditorButton);
        deleteDishEditorButton = findViewById(R.id.deleteDishEditorButton);

        if(bundle != null) {
            dishName = bundle.getString("dishName");
            fillFields();
        }
        else {
            removeIngredientButton.setEnabled(false);
            ((ViewGroup) deleteDishEditorButton.getParent()).removeView(deleteDishEditorButton);
        }

        addIngredientButton.setOnClickListener(v -> {
            if(checkIngredient()) {
                removeIngredientButton.setEnabled(true);
                ingredients.add(ingredientField.getText().toString());
                ingredientField.setText("");
                ingredientsListText.setText(Utilities.arrayListToString(ingredients));
            }
        });

        removeIngredientButton.setOnClickListener(v -> {
            if (ingredients.size() == 0) removeIngredientButton.setEnabled(false);
            else {
                ingredients.remove(ingredients.size() - 1);
                ingredientsListText.setText(Utilities.arrayListToString(ingredients));
            }
        });

        addDishEditorButton.setOnClickListener(v -> {
            if (checkDishName() && checkPrice()) {
                if (bundle != null) updateDish();
                else registerDish();
            }
        });

        deleteDishEditorButton.setOnClickListener(v -> deleteDish());
    }

    private void registerDish() {
        ConnectSQLiteHelper conn = new ConnectSQLiteHelper(this, Utilities.dishTable, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues values = createDish();

        long index = db.insert(Utilities.dishTable, Utilities.dishName, values);
        if (index > 0) {
            Toast.makeText(getApplicationContext(), "Registrado nuevo plato", Toast.LENGTH_SHORT).show();
            db.close();
            startActivity(new Intent(getApplicationContext(), OwnerDashboardActivity.class));
        } else {
            db.close();
            Toast.makeText(getApplicationContext(), "ERROR, NO SE PUDO REGISTRAR " + index, Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private void updateDish() {
        ConnectSQLiteHelper conn = new ConnectSQLiteHelper(this, Utilities.dishTable, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parameters = {restaurantName, dishName};
        ContentValues values = createDish();
        int index = db.update(Utilities.dishTable, values, Utilities.restaurant + "=? AND " + Utilities.dishName + "=?", parameters);

        if (index > 0) {
            Toast.makeText(getApplicationContext(), "Plato actualizado", Toast.LENGTH_SHORT).show();
            db.close();
            startActivity(new Intent(getApplicationContext(), OwnerDashboardActivity.class));
        } else {
            db.close();
            Toast.makeText(getApplicationContext(), "ERROR, NO SE PUDO ACTUALIZAR " + index, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteDish() {
        ConnectSQLiteHelper conn = new ConnectSQLiteHelper(this, Utilities.dishTable, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parameters = {restaurantName, dishName};
        db.delete(Utilities.dishTable, Utilities.restaurant + "=? AND " + Utilities.dishName + "=?", parameters);

        Toast.makeText(getApplicationContext(), "Plato eliminado", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), OwnerDashboardActivity.class));
        db.close();
    }

    private void fillFields() {
        //Establecemos la conexión con la db
        ConnectSQLiteHelper conn = new ConnectSQLiteHelper(this, Utilities.dishTable, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parameters = {restaurantName, dishName};
        //String[] fields = {Utilities.dishName, Utilities.ingredients, Utilities.isGlutenFree, Utilities.isVegan};
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + Utilities.dishTable + " WHERE restaurant=? AND name=?", parameters);
            cursor.moveToFirst();
            dishNameField.setText(cursor.getString(0));
            //El 1 es restaurant name
            strIngredients = cursor.getString(2);
            priceField.setText(cursor.getString(3));
            isGlutenFreeCheckBox.setChecked(cursor.getInt(4) > 0);
            isVeganCheckBox.setChecked(cursor.getInt(5) > 0);

            ingredients = Utilities.stringToArrayList(strIngredients);
            ingredientsListText.setText(strIngredients);
            cursor.close();
            db.close();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error en la base de datos", Toast.LENGTH_SHORT).show();
            db.close();
            e.printStackTrace();
        }
    }
    //Instancia la clase plato y mete sus datos en un content values
    private ContentValues createDish() {
        Plato dish = factoryPlato.creaPlato(dishNameField.getText().toString(), restaurantName, ingredients, Double.parseDouble(priceField.getText().toString()),isGlutenFreeCheckBox.isChecked(),isVeganCheckBox.isChecked());
        ContentValues values = new ContentValues();
        values.put(Utilities.dishName, dish.getNombre());
        values.put(Utilities.restaurant, dish.getRestaurante());
        values.put(Utilities.ingredients, Utilities.arrayListToString(dish.getIngredientes()));
        values.put(Utilities.price, dish.getPrecio());
        values.put(Utilities.isGlutenFree, dish.isGlutenFree());
        values.put(Utilities.isVegan, dish.isVegano());
        return values;
    }

    private Boolean checkDishName() {
        String name;
        name = dishNameField.getText().toString();
        if (name.matches(".*\\d.*")) {
            Utilities.showToast(getApplicationContext(), "El nombre del plato no puede contener números");
            return false;
        }
        if (name.length() < 3) {
            Utilities.showToast(getApplicationContext(), "Nombre del plato incorrecto");
            return false;
        } else {
            return true;
        }
    }

    private Boolean checkIngredient() {
        String ingredient;
        ingredient = ingredientField.getText().toString();
        if (ingredient.equals("")) {
            Utilities.showToast(getApplicationContext(), "Ingrediente vacío");
            return false;
        }
        else {
            return true;
        }
    }

    private Boolean checkPrice() {
        String price = priceField.getText().toString();
        if (price.length() < 1) {
            Utilities.showToast(getApplicationContext(), "El precio no puede estar vacío");
            return false;
        } else {
            return true;
        }
    }
}