package com.example.e_xplorer.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class ExplorerFragment extends Fragment implements OnFileSelectListener {
    private FileAdapter fileAdapter;
    private List<File> fileList;
    private RecyclerView recyclerView;
    String filePath = "";
    String path = "Storage/0/";
    FileAdapter prevAdapter;

    TextView tv_pathHolder;
    ImageView img_back;
    View view;
    String[] items = {"Open", "Details", "Rename", "Delete", "Share", "Move", "Copy"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_explorer, container, false);

        tv_pathHolder = view.findViewById(R.id.tv_pathHolder);
        img_back = view.findViewById(R.id.img_back);

        tv_pathHolder.setText(Environment.getExternalStorageDirectory().getAbsolutePath());

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileAdapter = prevAdapter;
                recyclerView.setAdapter(fileAdapter);
            }
        });

        runtimePermission();
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
        recyclerView = view.findViewById(R.id.recycler_main);
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
                arrayList.add(singleFile);
            }
        }
        for (File singleFile: files)
        {
            if (singleFile.getName().endsWith(".JPEG") || singleFile.getName().endsWith(".png") || singleFile.getName().endsWith(".mp3")  || singleFile.getName().endsWith(".mp4") || singleFile.getName().endsWith(".wav") || singleFile.getName().endsWith(".pdf") || singleFile.getName().endsWith(".doc") ){
                arrayList.add(singleFile);
            }
        }

        return arrayList;
    }

    @Override
    public void onFileSelected(File file) {
        if (file.isDirectory()){
            prevAdapter = fileAdapter;
            fileList = new ArrayList<>();
            fileList.addAll(findFiles(file));
            fileAdapter = new FileAdapter(getContext(), fileList, this);
            recyclerView.setAdapter(fileAdapter);
            String new_path = file.getAbsolutePath();
            tv_pathHolder.setText(new_path);
        }
        else {
            Uri uri = Uri.fromFile(file).normalizeScheme();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            this.startActivity(Intent.createChooser(intent, "Open file with"));
        }
    }

    @Override
    public void onFileLongPressed(File file) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.option_dialog);
        dialog.setTitle("Select Options.");
        ListView options= (ListView) dialog.findViewById(R.id.List);
        CustomAdapter customAdapter = new CustomAdapter();
        options.setAdapter(customAdapter);
        dialog.show();

        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFile = (String) options.getItemAtPosition(i);
            }
        });
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = getLayoutInflater().inflate(R.layout.option_layout, null);
            TextView txtOption = myView.findViewById(R.id.txtOption);
            ImageView imgOption = myView.findViewById(R.id.imgOption);
            txtOption.setText(items[i]);
            if (items[i].equals("Open")){
                imgOption.setImageResource(R.drawable.ic_open);
            }
            if (items[i].equals("Details")){
                imgOption.setImageResource(R.drawable.ic_details);
            }
            if (items[i].equals("Rename")){
                imgOption.setImageResource(R.drawable.ic_rename);
            }
            if (items[i].equals("Delete")){
                imgOption.setImageResource(R.drawable.ic_delete);
            }
            if (items[i].equals("Share")){
                imgOption.setImageResource(R.drawable.ic_share);
            }
            if (items[i].equals("Move")){
                imgOption.setImageResource(R.drawable.ic_move);
            }
            if (items[i].equals("Copy")){
                imgOption.setImageResource(R.drawable.ic_copy);
            }
            return myView;
        }
    }
}
