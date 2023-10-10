package com.capstone.videoeffect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MyVideosActivity extends AppCompatActivity {

    TextView empty;
    GridView gridView;
    public MyVideosAdapter adapter;
    ArrayList<String> myList;
    private File file;
    private static final String FILEPATH = "filepath";
    
    Intent intent = null;

    Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setActionBar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_videos);

        toolbar = findViewById(R.id.toolbar);
        setActionBar(getResources().getString(R.string.my_videos));

        findById();

        if (myList.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }

    private void findById() {
        empty = (TextView) findViewById(R.id.empty);
        gridView = (GridView) findViewById(R.id.gridView1);

        adapter = new MyVideosAdapter(this, getFromSdcard());
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filepath = (String) parent.getAdapter().getItem(position);
                intent = new Intent(MyVideosActivity.this, PreviewActivity.class);
                intent.putExtra(FILEPATH, filepath);
                intent.putExtra("fromMyVideos",true);
                startActivity(intent);
            }
        });

    }

    public ArrayList<String> getFromSdcard() {
        myList = new ArrayList<String>();
        file = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES + "/" + getResources().getString(R.string.app_name));
        if (file.isDirectory()) {
            File list[] = file.listFiles();

            for (int i = 0; i < list.length; i++) {
                myList.add(list[i].getAbsolutePath());
            }
        }

        return myList;
    }
}
