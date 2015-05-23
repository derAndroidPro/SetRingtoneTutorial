package de.derandroidpro.setringtonetutorial;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.AssetManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity {

    Button btn;

    File pathfile = new File(Environment.getExternalStorageDirectory(), "Ringtones");
    File outputfile = new File(pathfile , "ringtone_video.mp3");

    InputStream inputStream;
    OutputStream outputStream;
    AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyfiles();
                setringtone();

            }
        });
    }



    private void copyfiles() {


        try {
            if(pathfile.exists() == false){
                pathfile.mkdirs();
            }

            assetManager = getAssets();
            inputStream = assetManager.open("alarm_ringtone.mp3");
            outputStream = new FileOutputStream(outputfile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0 ,read);
            }
            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Datei nicht kopiert", Toast.LENGTH_SHORT).show();
        }

    }

    private void setringtone() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DATA, outputfile.getAbsolutePath());
        contentValues.put(MediaStore.MediaColumns.TITLE,"Ringtone der Android Pro" );
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"audio/mp3" );
        contentValues.put(MediaStore.MediaColumns.SIZE, outputfile.length());
        contentValues.put(MediaStore.Audio.Media.ARTIST,"der Android Pro" );
        contentValues.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        contentValues.put(MediaStore.Audio.Media.IS_NOTIFICATION,true );
        contentValues.put(MediaStore.Audio.Media.IS_ALARM, false);
        contentValues.put(MediaStore.Audio.Media.IS_MUSIC, false);

        ContentResolver contentResolver = MainActivity.this.getContentResolver();

        Uri generalaudiouri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        contentResolver.delete(generalaudiouri,MediaStore.MediaColumns.DATA + "='" + outputfile.getAbsolutePath() + "'",null );
        Uri ringtoneuri = contentResolver.insert(generalaudiouri , contentValues);

        RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this, RingtoneManager.TYPE_RINGTONE,ringtoneuri);
        Toast.makeText(getApplicationContext(), "Klingelton gesetzt", Toast.LENGTH_SHORT).show();




    }


}
