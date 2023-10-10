package com.capstone.videoeffect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;


public class FragmentTabsVideos extends Fragment {

    TextView empty;
    GridView gridView;
    public MyVideosAdapter adapter;
    ArrayList<String> myList;
    private File file;
    private static final String FILEPATH = "filepath";

    Intent intent = null;


    public FragmentTabsVideos() {
        // Required empty public constructor
    }

    public static FragmentTabsVideos newInstance() {
        FragmentTabsVideos fragment = new FragmentTabsVideos();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View main_view = inflater.inflate(R.layout.fragment_tabs_videos, container, false);

        empty = (TextView) main_view.findViewById(R.id.empty);
        gridView = (GridView) main_view.findViewById(R.id.gridView1);

        adapter = new MyVideosAdapter(getContext(), getFromSdcard());
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filepath = (String) parent.getAdapter().getItem(position);
                intent = new Intent(getContext(), PreviewActivity.class);
                intent.putExtra(FILEPATH, filepath);
                intent.putExtra("fromMyVideos",true);
                startActivity(intent);
            }
        });


        if (myList.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }

        return main_view;
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