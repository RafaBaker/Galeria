package rodrigues.rafael.galeria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> photos = new ArrayList<>();

    MainAdapter mainAdapter;

    static int RESULT_TAKE_PICTURE = 1;
    String currentPhotoPath;

    static int RESULT_REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tbMain);
        //Seta a toolbar como padrão
        setSupportActionBar(toolbar);

        //Chamando as funções de requisição para a permissão de recursos externos à app
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.CAMERA);

        checkForPermissions(permissions);


        //Lendo a lista de fotos já salvas e adicionando-as em outra lista.
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = dir.listFiles();
        for(int i = 0; i < files.length; i++) {
            photos.add(files[i].getAbsolutePath());
        }

        //Criando o mainAdapter e setando ele no RecycleView
        mainAdapter = new MainAdapter(MainActivity.this, photos);

        RecyclerView rvGallery = findViewById(R.id.rvGallery);
        rvGallery.setAdapter(mainAdapter);

        //Calculando a quantidade de colunas de fotos que cabem na tela do celular
        float w = getResources().getDimension(R.dimen.itemWidth);
        int numberOfColumns = Utils.calculateNoOfColumns(MainActivity.this, w);
        //Configurando o RecycleView para exibir as fotos em formato GRID, considerando o número
        //máximo das colunas calculadas previamente
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns);
        rvGallery.setLayoutManager(gridLayoutManager);
    }

    //Método que recebe como parametro a foto que deve ser aberta.
    //É chamado dentro do onBindviewHolder na classe MainAdapter, quando o usuário clica na foto
    //O caminho é passado via Intent

    public void startPhotoActivity(String photoPath) {
        Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        i.putExtra("photo_path", photoPath);
        startActivity(i);
    }

    //Método que irá guardar a imagem, salvando-a com a data e hora do exato momento em que a foto foi tirada
    //Para que, assim não tenham arquivos com nomes repetidos.
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);
        return f;
    }

    //Método que dispara a câmera
    private void dispatchTakePictureIntent() {
        //Criando um arquivo vazio na pasta Pictures e,
        //caso não seja possível, exibir mensagem de erro e retornar o método imediatamente.
        File f = null;
        try {
            //Chamando o método de criar a imagem
            f = createImageFile();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Não foi possível criar o arquivo", Toast.LENGTH_LONG).show();
            return;
        }

        //Salvando o local do arquivo na variável f(apenas da foto que está sendo usada no momento)
        currentPhotoPath = f.getAbsolutePath();

        //Caso ela não esteja vazia, entra no if
        if(f != null) {
            //Gerando um URI para o arquivo da foto
            Uri fUri = FileProvider.getUriForFile(MainActivity.this, "rodrigues.rafael.galeria.fileprovider", f);
            //Intent para disparar o app da câmera
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Passando o URI para o app da câmera junto ao intent
            i.putExtra(MediaStore.EXTRA_OUTPUT, fUri);
            //Iniciando o intent e, efetivamente, o app da câmera
            startActivityForResult(i, RESULT_TAKE_PICTURE);
        }
    }


    //Método para criar um "inflador de menu"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_tb, menu);
        return true;
    }

    //Método para definir qual ação será feita quando o usuário clicar no ícone de câmera
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Caso clique, a câmera do celular será aberta
            case R.id.opCamera:
                dispatchTakePictureIntent ();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Método que será chamado quando a aplicação voltar após o app da câmera ser chamado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_TAKE_PICTURE) {
            //Se a foto foi tirada
            if(resultCode == Activity.RESULT_OK) {
                //Seu local é adicionado na lista de fotos
                photos.add(currentPhotoPath);
                //E o RV será atualizado também
                mainAdapter.notifyItemInserted(photos.size()-1);
            }
            //Se a foto NÃO foi tirada
            else {
                //O Arquivo que seria usado para armazená-la será excluído
                File f = new File(currentPhotoPath);
                f.delete();
            }
        }
    }

    //Verifica se uma permissão foi ou não concedida
    private boolean hasPermission (String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    //Método que recebe uma lista de permissões e verifica todas elas
    //Caso alguma permissão não seja dada, ela é posta em uma lista de permissões não concedidas ainda
    //As permissões não concedidas pelo USUÁRIO são requisitadas, que é quando aparece um dialog pedindo ao usuário
    //permissão para o uso da funcionalidade
    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();
        for(String permission : permissions) {
            if(!hasPermission(permission)) {
                permissionsNotGranted.add(permission);
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(permissionsNotGranted.size() > 0) {
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]),RESULT_REQUEST_PERMISSION);
            }
        }
    }

    //Após o usuário confirmar ou nào as requisições, esse método será chamado.
    //É checado então quais permissões foram concedidas ou não.
    //Se alguma permissão não concedida for fundamental para que a app funcione, então o usuário é avisado
    //de que tais permissões são necessárias para o uso da app, e requeridas novamente.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<String> permissionsRejected = new ArrayList<>();
        if (requestCode == RESULT_REQUEST_PERMISSION) {
            for(String permission: permissions) {
                if (!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }

        if (permissionsRejected.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(MainActivity.this).setMessage
                            ("Para usar essa app é preciso conceder essas permissões.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                             requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), RESULT_REQUEST_PERMISSION);
                        }
                    }).create().show();
                }
            }
        }

    }
}

