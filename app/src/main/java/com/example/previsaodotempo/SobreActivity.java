package com.example.previsaodotempo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega o layout da tela "Sobre"
        setContentView(R.layout.activity_sobre);

        // Opcional: Configura a Toolbar para mostrar o botão de voltar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sobre");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Lida com o clique no botão de voltar (seta para a esquerda) na Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
