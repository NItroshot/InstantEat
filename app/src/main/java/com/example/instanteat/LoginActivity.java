package com.example.instanteat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.*;

public class LoginActivity extends AppCompatActivity {
    //Variable declaration
    EditText emailField, passwordField;
    Button loginButton, registerButton;
    CheckBox rememberCheckBox;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String[] cacheData = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //Importamos preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        //Utilities.deleteDb(getApplicationContext());
        //getApplicationContext().deleteDatabase("dishDB");

        emailField = findViewById(R.id.emailFieldLogin);
        passwordField = findViewById(R.id.passwordFieldLogin);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);
        //Listeners
        loginButton.setOnClickListener(v -> validate());
        registerButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        //Ponemos las credenciales recordadas si existen
        cacheData[0] = prefs.getString("savedEmail", "");
        cacheData[1] = prefs.getString("savedPassword", "");
        emailField.setText(cacheData[0]);
        passwordField.setText(cacheData[1]);
    }

    private void validate() {
        //Establecemos conexion con la db
        ConnectSQLiteHelper conn = new ConnectSQLiteHelper(this, "userDB", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        //Query para db
        String[] parameters = {emailField.getText().toString()};
        String inputPassword = passwordField.getText().toString();
        //Los campos que necesitamos
        String[] fields = {Utilities.email, Utilities.password, Utilities.userType, Utilities.name};
        try {
            Cursor cursor = db.query(Utilities.userTable, fields, Utilities.email+"=?", parameters, null, null, null);
            //El cursor empieza en el campo email
            cursor.moveToFirst();
            //String email = cursor.getString(0);
            String password = cursor.getString(1);
            String userType = cursor.getString(2);
            String name = cursor.getString(3);
            if(password.equals(inputPassword)) {
                //SI EL LOGIN ES EXITOSO:
                editor.putString("email", parameters[0]);
                editor.putString("name", name);
                if (rememberCheckBox.isChecked()) {
                    editor.putString("savedEmail", parameters[0]);
                    editor.putString("savedPassword", inputPassword);
                }
                Toast.makeText(getApplicationContext(), "Inicio de sesión correcto\nBienvenido " + name, Toast.LENGTH_SHORT).show();
                if (userType.equals("client")) {
                    editor.putString("userType", "client");
                    //Vamos al menú
                    finish();
                    startActivity(new Intent(getApplicationContext(), ClientMenuActivity.class));
                }
                else {
                    editor.putString("userType", "owner");
                    finish();
                    startActivity(new Intent(getApplicationContext(), OwnerMenuActivity.class));
                }
                editor.commit();
            }
            else{
                Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            //Una excepción significa que no se ha encontrado el email en la db
            e.printStackTrace();
            db.close();
            Toast.makeText(getApplicationContext(), "Email incorrecto", Toast.LENGTH_SHORT).show();
        }

    }
}