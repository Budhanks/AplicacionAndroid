package com.example.aplicacion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast; 

import androidx.annotation.Nullable; 
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException; 

public class DrawingAppActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; 

    private DrawView drawView;
    private Button clearButton;
    private Button btnBackToMain;
    private Button addImageButton; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_app); 

        Spinner colorSpinner = findViewById(R.id.colorSpinner);
        drawView = findViewById(R.id.drawView);
        clearButton = findViewById(R.id.clearButton);
        btnBackToMain = findViewById(R.id.btnBackToMainFromDrawing);
        addImageButton = findViewById(R.id.addImageButton); 

        String[] colors = {"Rojo", "Verde", "Azul", "Negro", "Amarillo", "Magenta", "Cian"}; 
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Rojo
                        drawView.setPaintColor(Color.RED);
                        break;
                    case 1: // Verde
                        drawView.setPaintColor(Color.GREEN);
                        break;
                    case 2: // Azul
                        drawView.setPaintColor(Color.BLUE);
                        break;
                    case 3: // Negro
                        drawView.setPaintColor(Color.BLACK);
                        break;
                    case 4: // Amarillo
                        drawView.setPaintColor(Color.YELLOW);
                        break;
                    case 5: // Magenta
                        drawView.setPaintColor(Color.MAGENTA);
                        break;
                    case 6: // Cian
                        drawView.setPaintColor(Color.CYAN);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clearDrawing();
            }
        });

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Configura el listener para agregar imagen
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(); 
            }
        });
    }

    /**
     * Abre el selector de imágenes de la galería del dispositivo.
     */
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK); 
        intent.setType("image/*"); 
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); 

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                drawView.setLoadedImage(bitmap); 
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
