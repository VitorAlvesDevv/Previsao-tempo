package com.example.previsaodotempo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class TelaDeInicio extends AppCompatActivity {

    // Constante para definir a duração do splash (3000ms = 3 segundos)
    private static final long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mostrarPagina();
            }
        }, SPLASH_TIME_OUT);


    }

    private void mostrarPagina() {
        Intent intent = new Intent(TelaDeInicio.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
