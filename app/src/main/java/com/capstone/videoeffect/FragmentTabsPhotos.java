package com.capstone.videoeffect;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.capstone.videoeffect.Adapters.CreationAdapter;
import com.capstone.videoeffect.utils.Glob;

import java.io.File;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentTabsPhotos extends Fragment {

    private static final int MY_REQUEST_CODE = 5;
    private CreationAdapter galleryAdapter;
    private GridView lstList;
    private ImageView noImage;

    public FragmentTabsPhotos() {
        // Required empty public constructor
    }

    public static FragmentTabsPhotos newInstance() {
        FragmentTabsPhotos fragment = new FragmentTabsPhotos();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View main_view = inflater.inflate(R.layout.fragment_tabs_photos, container, false);

        this.noImage = (ImageView) main_view.findViewById(R.id.novideoimg);
        this.lstList = (GridView) main_view.findViewById(R.id.lstList);
        getImages();
        if (Glob.IMAGEALLARY.size() <= 0) {
            this.noImage.setVisibility(View.VISIBLE);
            this.lstList.setVisibility(View.GONE);
        } else {
            this.noImage.setVisibility(View.GONE);
            this.lstList.setVisibility(View.VISIBLE);
        }
        Collections.sort(Glob.IMAGEALLARY);
        Collections.reverse(Glob.IMAGEALLARY);
        this.galleryAdapter = new CreationAdapter(getActivity(), Glob.IMAGEALLARY);
        this.lstList.setAdapter(this.galleryAdapter);

        return main_view;
    }

    private void getImages() {
        if (Build.VERSION.SDK_INT < 23) {
            fetchImage();
        } else if (getContext().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            fetchImage();
        } else if (getContext().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 5);
        }
    }

    private void fetchImage() {
        Glob.IMAGEALLARY.clear();
        Glob.listAllImages(new File("/storage/emulated/0/" + Glob.Edit_Folder_name + "/"));
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5:
                if (grantResults[0] == 0) {
                    fetchImage();
                    return;
                } else if (getContext().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 5);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}