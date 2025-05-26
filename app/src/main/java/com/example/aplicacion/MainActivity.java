package com.example.aplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Establece el layout de la pantalla principal

        Button btnGoToDrawingApp = findViewById(R.id.btnGoToDrawingApp);
        Button btnGoToCredits = findViewById(R.id.btnGoToCredits);

        // Configura el botón para ir a la aplicación de dibujo
        btnGoToDrawingApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para iniciar DrawingAppActivity
                Intent intent = new Intent(MainActivity.this, DrawingAppActivity.class);
                startActivity(intent); // Inicia la nueva Activity
            }
        });

        // Configura el botón para ir a la vista de créditos
        btnGoToCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un Intent para iniciar CreditsActivity
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intent); // Inicia la nueva Activity
            }
        });
    }
}