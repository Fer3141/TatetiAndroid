package com.maria.tatetiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView tvJugador, tvSimbolo;
    Button[] botones = new Button[9];
    String simboloJugador;
    String simboloMaquina;

    boolean turnoJugador = true; // alternancia básica

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvJugador = findViewById(R.id.tvJugador);
        tvSimbolo = findViewById(R.id.tvSimbolo);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        simboloJugador = intent.getStringExtra("simbolo");

        if (simboloJugador.equalsIgnoreCase("Cruz")) {
            simboloJugador = "X";
            simboloMaquina = "O";
        } else {
            simboloJugador = "O";
            simboloMaquina = "X";
        }

        tvJugador.setText("Jugador: " + nombre);
        tvSimbolo.setText("Símbolo: " + simboloJugador);

        // Conectamos los 9 botones
        for (int i = 0; i < 9; i++) {
            String btnID = "btn" + i;
            int resID = getResources().getIdentifier(btnID, "id", getPackageName());
            botones[i] = findViewById(resID);
            int finalI = i;
            botones[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jugar(finalI);
                }
            });
        }
    }

    private void jugar(int i) {
        if (!botones[i].getText().toString().equals("")) return;

        if (turnoJugador) {
            botones[i].setText(simboloJugador);
            botones[i].setEnabled(false);
            if (verificarGanador()) return;
            turnoJugador = false;


            // Esperar 1 segundo y que juegue la máquina
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!verificarJuegoTerminado()) {
                        jugarMaquina();
                    }
                }
            }, 1000);
        }
    }


    private boolean verificarGanador() {
        String[][] combinaciones = {
                {get(0), get(1), get(2)},
                {get(3), get(4), get(5)},
                {get(6), get(7), get(8)},
                {get(0), get(3), get(6)},
                {get(1), get(4), get(7)},
                {get(2), get(5), get(8)},
                {get(0), get(4), get(8)},
                {get(2), get(4), get(6)},
        };

        for (String[] trio : combinaciones) {
            if (trio[0].equals(trio[1]) && trio[1].equals(trio[2]) && !trio[0].equals("")) {
                mostrarGanador(trio[0]);
                return true;
            }
        }

        boolean hayEmpate = true;
        for (Button b : botones) {
            if (b.getText().toString().equals("")) {
                hayEmpate = false;
                break;
            }
        }

        if (hayEmpate) {
            mostrarGanador("Empate");
            return true;
        }

        return false;
    }


    private String get(int i) {
        return botones[i].getText().toString();
    }

    private void mostrarGanador(String ganador) {
        String mensaje;
        if (ganador.equals("Empate")) {
            mensaje = "¡Empate!";
        } else if (ganador.equals(simboloJugador)) {
            mensaje = "¡Ganaste!";
        } else {
            mensaje = "Ganó la máquina";
        }

        // Desactivar todos los botones
        for (Button b : botones) {
            b.setEnabled(false);
        }

        // Mostrar diálogo con dos opciones
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Resultado")
                .setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Volver a jugar", (dialog, which) -> reiniciarJuego())
                .setNegativeButton("Salir", (dialog, which) -> {
                    // Volver a MainActivity
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish(); // cerrar GameActivity
                })
                .show();
    }



    private void jugarMaquina() {
        // Buscar celdas vacías
        List<Integer> celdasLibres = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (botones[i].getText().toString().equals("")) {
                celdasLibres.add(i);
            }
        }

        if (celdasLibres.isEmpty()) return; // No hay más celdas

        // Elegir una al azar
        int index = new Random().nextInt(celdasLibres.size());
        int celda = celdasLibres.get(index);

        // Hacer que la máquina juegue
        botones[celda].setText(simboloMaquina);
        botones[celda].setEnabled(false);
        verificarGanador();
        turnoJugador = true;
    }

    private boolean verificarJuegoTerminado() {
        for (Button b : botones) {
            if (b.getText().toString().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void reiniciarJuego() {
        for (Button b : botones) {
            b.setText("");
            b.setEnabled(true);
        }
        turnoJugador = true;
    }


}
