package com.example.e_xplorer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FileViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public CardView container;
    public ImageView imgFile;
    public TextView tvfileSize;

    public FileViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tv_fileName);
        container = itemView.findViewById(R.id.container);
        imgFile = itemView.findViewById(R.id.img_fileType);
        tvfileSize = itemView.findViewById(R.id.tvFileSize);

    }
}
