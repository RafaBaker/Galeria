package rodrigues.rafael.galeria;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Toolbar toolbar = findViewById(R.id.tbPhoto);
        //Seta a toolbar como padrão
        setSupportActionBar(toolbar);

        //Pega a ActionBar padrão da activity (setada em cima)
        ActionBar actionBar = getSupportActionBar();
        //Habilita o botão de voltar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}