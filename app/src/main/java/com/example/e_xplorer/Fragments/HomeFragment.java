package com.example.e_xplorer.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_xplorer.FileAdapter;
import com.example.e_xplorer.OnFileSelectListener;
import com.example.e_xplorer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements OnFileSelectListener {
    LinearLayout linearImages, linearVideos, linearMusics, linearDocs, linearDownloads, linearApks;

    private FileAdapter fileAdapter;
    private List<File> fileList;
    private RecyclerView recyclerView;
    String filePath = "";
    String path = "Storage/0/";
    FileAdapter prevAdapter;
    View view;
    String[] items = {"Open", "Details", "Rename", "Delete", "Share", "Move", "Copy"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        linearImages = view.findViewById(R.id.linearImage);
        linearVideos = view.findViewById(R.id.linearVideos);
        linearMusics = view.findViewById(R.id.linearMusic);
        linearDocs = view.findViewById(R.id.linearDocs);
        linearDownloads = view.findViewById(R.id.linearDownloads);
        linearApks = view.findViewById(R.id.linearApk);

        displayFiles();

        linearImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("fileType", "image");
                CategorizedFragment categorizedFragment = new CategorizedFragment();
                categorizedFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.fragment_container, categorizedFragment).addToBackStack(null).commit();

            }
        });
        linearApks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategorizedFragment categorizedFragment = new CategorizedFragment();
                Bundle args = new Bundle();
                args.putString("fileType", "apk");
                categorizedFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.fragment_container, categorizedFragment).addToBackStack(null).commit();

            }
        });
        linearVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategorizedFragment categorizedFragment = new CategorizedFragment();
                Bundle args = new Bundle();
                args.putString("fileType", "video");
                categorizedFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.fragment_container, categorizedFragment).addToBackStack(null).commit();

            }
        });
        linearMusics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategorizedFragment categorizedFragment = new CategorizedFragment();
                Bundle args = new Bundle();
                args.putString("fileType", "audio");
                categorizedFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.fragment_container, categorizedFragment).addToBackStack(null).commit();

            }
        });
        linearDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategorizedFragment categorizedFragment = new CategorizedFragment();
                Bundle args = new Bundle();
                args.putString("fileType", "doc");
                categorizedFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.fragment_container, categorizedFragment).addToBackStack(null).commit();

            }
        });
        linearDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategorizedFragment categorizedFragment = new CategorizedFragment();
                Bundle args = new Bundle();
                args.putString("fileType", "downloads");
                categorizedFragment.setArguments(args);

                getFragmentManager().beginTransaction().add(R.id.fragment_container, categorizedFragment).addToBackStack(null).commit();

            }
        });

        return view;
    }
    private void displayFiles() {
        recyclerView = view.findViewById(R.id.recycler_recents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        fileList = new ArrayList<>();
        fileList.addAll(findFiles(Environment.getExternalStorageDirectory()));
        fileAdapter = new FileAdapter(getContext(), fileList, this);
        recyclerView.setAdapter(fileAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<File> findFiles (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        for (File singleFile: files)
        {
            if (!singleFile.isDirectory() && !singleFile.isHidden()){
                if (singleFile.getName().endsWith(".JPEG") || singleFile.getName().endsWith(".png") || singleFile.getName().endsWith(".mp3")  || singleFile.getName().endsWith(".mp4") || singleFile.getName().endsWith(".wav") || singleFile.getName().endsWith(".pdf") || singleFile.getName().endsWith(".doc") ){
                    arrayList.add(singleFile);
                }
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

    class CustomAdapter extends BaseAdapter {

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
