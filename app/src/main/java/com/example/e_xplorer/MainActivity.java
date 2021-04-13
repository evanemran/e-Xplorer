package com.example.e_xplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_xplorer.Fragments.ExplorerFragment;
import com.example.e_xplorer.Fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnFileSelectListener, NavigationView.OnNavigationItemSelectedListener {
    private FileAdapter fileAdapter;
    private List<File> fileList;
    private RecyclerView recyclerView;
    String filePath = "";
    String path = "Storage/0/";
    FileAdapter prevAdapter;

    private DrawerLayout drawerLayout;

    TextView tv_pathHolder;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_pathHolder = findViewById(R.id.tv_pathHolder);
        img_back = findViewById(R.id.img_back);
        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);

//        tv_pathHolder.setText(Environment.getExternalStorageDirectory().getAbsolutePath());
//
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fileAdapter = prevAdapter;
//                recyclerView.setAdapter(fileAdapter);
//            }
//        });
//
//        runtimePermission();

    }

    private void runtimePermission() {
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayFiles();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Permission is Required!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void displayFiles() {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        fileList = new ArrayList<>();
        fileList.addAll(findFiles(Environment.getExternalStorageDirectory()));
        fileAdapter = new FileAdapter(this, fileList, this);
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
            if (singleFile.getName().endsWith(".JPEG") || singleFile.getName().endsWith(".png") || singleFile.getName().endsWith(".mp3")  ||
                    singleFile.getName().endsWith(".mp4") || singleFile.getName().endsWith(".wav") || singleFile.getName().endsWith(".pdf") ||
                    singleFile.getName().endsWith(".doc") || singleFile.getName().endsWith(".apk")){
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
            fileAdapter = new FileAdapter(this, fileList, this);
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

    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStackImmediate();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_internal:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExplorerFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_external:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExplorerFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExplorerFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}