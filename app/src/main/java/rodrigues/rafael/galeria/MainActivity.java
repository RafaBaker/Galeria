package rodrigues.rafael.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tbMain);
        //Seta a toolbar como padrão
        setSupportActionBar(toolbar);


    }

    //Método para criar um "inflador de menu"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_tb, menu);
        return true;
    }

    //Método para definir qual ação será feita quando o usuário clicar no ícone de camera
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Caso clique, a camera do celular será aberta
            case R.id.opCamera:
                dispatchTakePictureIntent ();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}