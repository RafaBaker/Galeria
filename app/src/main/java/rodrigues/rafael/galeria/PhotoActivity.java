package rodrigues.rafael.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

public class PhotoActivity extends AppCompatActivity {

    String photoPath;

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


        //Pegando o intent da MainActivity, carregando a foto em um bitmap e colocando o bitmap no IV
        Intent i = getIntent();
        photoPath = i.getStringExtra("photo_path");

        Bitmap bitmap = Utils.getBitmap(photoPath);
        ImageView imPhoto = findViewById(R.id.imPhoto);
        imPhoto.setImageBitmap(bitmap);
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