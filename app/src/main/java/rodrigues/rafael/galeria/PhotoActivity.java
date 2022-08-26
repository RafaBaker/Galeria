package rodrigues.rafael.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.net.URI;

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

    //Criando o método que compartilhará a foto
    void sharePhoto() {
        //Gerando a URI que será usada para compartilhar a foto com outros apps
        Uri photoUri = FileProvider.getUriForFile(PhotoActivity.this, "rodrigues.rafael.galeria.fileprovider", new File(photoPath));
        //Intent do tipo ACTION_SEND para enviar a foto para qualquer app que seja capaz de utilizá-la
        Intent i = new Intent(Intent.ACTION_SEND);
        //Especificando qual arquivo será compartilhado
        i.putExtra(Intent.EXTRA_STREAM, photoUri);
        //E também qual o tipo do arquivo
        i.setType("image/jpeg");
        //No final, o Intent é executado
        startActivity(i);
    }
}