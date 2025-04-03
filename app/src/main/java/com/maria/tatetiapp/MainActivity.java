package com.maria.tatetiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etNombre;
    RadioGroup rgSimbolo;
    Button btnComenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.etNombre);
        rgSimbolo = findViewById(R.id.rgSimbolo);
        btnComenzar = findViewById(R.id.btnComenzar);

        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = etNombre.getText().toString();
                if (nombre.isEmpty()) {
                    nombre = "extraño";
                }

                int simboloId = rgSimbolo.getCheckedRadioButtonId();
                String simbolo = "Cruz"; // por defecto
                if (simboloId != -1) {
                    RadioButton seleccionado = findViewById(simboloId);
                    simbolo = seleccionado.getText().toString();
                }

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("simbolo", simbolo);
                startActivity(intent);
            }
        });
    }
}
