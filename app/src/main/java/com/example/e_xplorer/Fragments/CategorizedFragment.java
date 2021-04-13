package com.example.e_xplorer.Fragments;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_xplorer.FileAdapter;
import com.example.e_xplorer.OnFileSelectListener;
import com.example.e_xplorer.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategorizedFragment extends Fragment implements OnFileSelectListener {
    private FileAdapter fileAdapter;
    private List<File> fileList;
    private RecyclerView recyclerView;
    View view;
    String value = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_categorized, container, false);

        runtimePermission();
        Bundle bundle = this.getArguments();
        bundle.getString("fileType");

        return view;
    }
    private void runtimePermission() {
        Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayFiles();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(), "Permission is Required!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    private void displayFiles() {
        recyclerView = view.findViewById(R.id.recycler_categorized);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fileList = new ArrayList<>();
        fileList.addAll(findFiles(Environment.getExternalStorageDirectory()));
        fileAdapter = new FileAdapter(getContext(), fileList, this);
        recyclerView.setAdapter(fileAdapter);
    }
    public ArrayList<File> findFiles (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile: files)
        {
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findFiles(singleFile));
            }
            else {
                switch (getArguments().getString("fileType")){
                    case "image":
                        if (singleFile.getName().toLowerCase().endsWith(".jpg") || singleFile.getName().toLowerCase().endsWith(".png")){
                            arrayList.add(singleFile);
                        }
                        break;
                    case "video":
                        if (singleFile.getName().toLowerCase().endsWith(".mp4") /*|| singleFile.getName().toLowerCase().endsWith(".jpg") */){
                            arrayList.add(singleFile);
                        }
                        break;
                    case "audio":
                        if (singleFile.getName().toLowerCase().endsWith(".mp3") || singleFile.getName().toLowerCase().endsWith(".amr") || singleFile.getName().toLowerCase().endsWith(".wav")){
                            arrayList.add(singleFile);
                        }
                        break;
                    case "doc":
                        if (singleFile.getName().toLowerCase().endsWith(".doc") || singleFile.getName().toLowerCase().endsWith(".pdf")){
                            arrayList.add(singleFile);
                        }
                        break;
                    case "apk":
                        if (singleFile.getName().toLowerCase().endsWith(".apk")){
                            arrayList.add(singleFile);
                        }
                        break;
                    case "download":
                        if (singleFile.getName().toLowerCase().endsWith(".txt")){
                            arrayList.add(singleFile);
                        }
                        break;
                }
            }
        }

        return arrayList;
    }

    @Override
    public void onFileSelected(File file) {

    }

    @Override
    public void onFileLongPressed(File file) {

    }
}
