package com.example.e_xplorer;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {
    private Context context;
    private List<File> file;
    private OnFileSelectListener listener;

    public FileAdapter(Context context, List<File> file, OnFileSelectListener listener) {
        this.context = context;
        this.file = file;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.file_container, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.tvName.setText(file.get(position).getName());
        holder.tvName.setSelected(true);
        int items = 0;
        if (file.get(position).isDirectory()){
            File[] files = file.get(position).listFiles();
            for (File singleFile: files)
            {
                if (!singleFile.isHidden()){
                    items+=1;
                }
            }
            holder.tvfileSize.setText(String.valueOf(items) + " Files");
        }else holder.tvfileSize.setText(Formatter.formatShortFileSize(context,file.get(position).length()));

        if (file.get(position).getName().endsWith(".pdf")) {
            holder.imgFile.setImageResource(R.drawable.ic_pdf);
        } else if (file.get(position).getName().endsWith(".doc")) {
            holder.imgFile.setImageResource(R.drawable.ic_docs);
        } else if (file.get(position).getName().endsWith(".JPEG")) {
            holder.imgFile.setImageResource(R.drawable.ic_image);
        } else if (file.get(position).getName().toLowerCase().endsWith(".jpg")) {
            holder.imgFile.setImageResource(R.drawable.ic_image);
        } else if (file.get(position).getName().endsWith(".png")) {
            holder.imgFile.setImageResource(R.drawable.ic_image);
        } else if (file.get(position).getName().endsWith(".mp3")) {
            holder.imgFile.setImageResource(R.drawable.ic_music);
        } else if (file.get(position).getName().endsWith(".wav")) {
            holder.imgFile.setImageResource(R.drawable.ic_music);
        } else if (file.get(position).getName().endsWith(".mp4")) {
            holder.imgFile.setImageResource(R.drawable.ic_play);
        } else if (file.get(position).getName().endsWith(".apk")) {
            holder.imgFile.setImageResource(R.drawable.ic_android);
        }else {
            holder.imgFile.setImageResource(R.drawable.ic_folder);
        }

        holder.tvName.setSelected(true);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFileSelected(file.get(position));
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onFileLongPressed(file.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return file.size();
    }
}
