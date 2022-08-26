package rodrigues.rafael.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

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

    //Mesmo ideia do método da camera para a MainActivity,
    // só que nesse caso para o botão de compartilhamento
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opShare:
                sharePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}