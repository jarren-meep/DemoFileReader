package com.myapplicationdev.android.demofilereader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    String folderLocation;
    Button btnWrite, btnRead;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRead = findViewById(R.id.btnRead);
        btnWrite = findViewById(R.id.btnWrite);
        tv = findViewById(R.id.textView);
        //UI handlers to be defined

        int permissionCheck_Storage = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck_Storage != PermissionChecker.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            finish();
        }

        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";

        File folder = new File(folderLocation);
        if (folder.exists() == false){
            boolean result = folder.mkdir();
            if (result == true){
                Log.d("File Read/Write", "Folder created");
            } else {
                Log.d("File Read/Write", "Folder creation failed");
            }
        }

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File targetFile = new File(folderLocation, "data.txt");

                try {
                    FileWriter writer = new FileWriter(targetFile, true);
                    writer.write("Hello world \n");
                    writer.flush();
                    writer.close();
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Failed to write!",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File targetFile = new File(folderLocation, "data.txt");
                if (targetFile.exists() == true) {
                    String data = "";
                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line != null) {
                            data += line + "\n";
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    tv.setText(data);
                }
            }
        });
    }
}

