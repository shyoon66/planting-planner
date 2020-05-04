package com.yoonbae.planting.planner.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yoonbae.planting.planner.R;
import com.yoonbae.planting.planner.data.Plant;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Plant> plantList;
    private Context context;

    public MyRecyclerViewAdapter(List<Plant> plantList, Context context) {
        this.plantList = plantList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Plant plant = plantList.get(position);
        ((RowCell) holder).name.setText(plant.getName());
        Glide.with(((RowCell) holder).imageView.getContext()).load(plant.getImagePath()).into(((RowCell) holder).imageView);

        ((RowCell) holder).imageView.setOnClickListener(view -> {

        });

        ((RowCell) holder).imageButton.setOnClickListener(view -> {
            String[] items = {"식물수정", "식물삭제", "취소"};
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle("");

            // 목록 클릭시 설정
            ab.setItems(items, (dialog, index) -> {
                if (index == 0) {

                } else if (index == 1) {
                    deletePlant(position);
                }

                dialog.dismiss();
            });

            ab.show();
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    private class RowCell extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private ImageButton imageButton;

        private RowCell(View view) {
            super(view);
            imageView = view.findViewById(R.id.cardview_imageview);
            name = view.findViewById(R.id.cardview_name);
            imageButton = view.findViewById(R.id.cardview_btn);
        }
    }

    private void deletePlant(final int position) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        String name = plantList.get(position).getName();
        ab.setTitle(name + "을(를) 삭제하시겠습니까?");
        String[] items = {"예", "아니오"};

        // 목록 클릭시 설정
        ab.setItems(items, (dialog, index) -> {
            if(index == 0)
                deletePlantProcess(position);

            dialog.dismiss();
        });

        ab.show();
    }

    private void deletePlantProcess(final int position) {

    }
}
