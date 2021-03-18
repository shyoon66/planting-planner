package com.yoonbae.planting.planner.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yoonbae.planting.planner.InsertActivity;
import com.yoonbae.planting.planner.R;
import com.yoonbae.planting.planner.ViewActivity;
import com.yoonbae.planting.planner.alarm.AlarmService;
import com.yoonbae.planting.planner.data.Plant;
import com.yoonbae.planting.planner.viewmodel.PlantViewModel;

import java.io.File;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Plant> plantList;
    private final Context context;
    private final PlantViewModel plantViewModel;

    public MyRecyclerViewAdapter(List<Plant> plantList, Context context) {
        this.plantList = plantList;
        this.context = context;
        plantViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(PlantViewModel.class);
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
        RowCell rowCell = (RowCell) holder;
        rowCell.name.setText(plant.getName());
        Uri imageUri = Uri.fromFile(new File(plant.getImagePath()));
        Glide.with(rowCell.imageView.getContext()).load(imageUri).into((rowCell).imageView);

        rowCell.imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewActivity.class);
            intent.putExtra("id", plant.getId());
            context.startActivity(intent);
        });

        rowCell.imageButton.setOnClickListener(view -> {
            String[] items = {"식물수정", "식물삭제", "취소"};
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setTitle("");
            ab.setItems(items, (dialog, index) -> {
                if (index == 0) {
                    showAlertDialogToUpdatePlant(plant.getId());
                } else if (index == 1) {
                    showAlertDialog2DeletePlant(plant);
                }
                dialog.dismiss();
            });
            ab.show();
        });
    }

    private void showAlertDialogToUpdatePlant(Integer id) {
        Intent intent = new Intent(context, InsertActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
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

    private void showAlertDialog2DeletePlant(Plant plant) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        String name = plant.getName();
        ab.setTitle(name + "을(를) 삭제하시겠습니까?");
        String[] items = {"예", "아니오"};
        ab.setItems(items, (dialog, index) -> {
            if (index == 0) {
                AlarmService.INSTANCE.cancelAlarm(context, plant.getId());
                deletePlant(plant);
            }
            dialog.dismiss();
        });
        ab.show();
    }

    private void deletePlant(Plant plant) {
        plantViewModel.delete(plant);
    }
}
