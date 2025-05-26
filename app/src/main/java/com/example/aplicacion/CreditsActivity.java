package com.example.aplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits); // Establece el layout de créditos

        Button btnBackToMain = findViewById(R.id.btnBackToMainFromCredits);

        // Configura el botón para regresar a la pantalla principal
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra esta Activity y regresa a la anterior en la pila (MainActivity)
            }
        });
    }
}