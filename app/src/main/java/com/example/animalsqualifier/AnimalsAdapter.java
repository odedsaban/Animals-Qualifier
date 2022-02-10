package com.example.animalsqualifier;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;

public class AnimalsAdapter extends FirestorePagingAdapter<AnimalModel, AnimalsAdapter.AnimalVH> {


    Context context;

    public AnimalsAdapter(@NonNull FirestorePagingOptions<AnimalModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull AnimalVH holder, int position, @NonNull AnimalModel model) {

        holder.nameTv.setText(model.getName());
        Glide.with(context).load(model.getImage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mainIv);





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ShowDetailsActivity.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("desc",model.getDesc());
                intent.putExtra("type",model.getType());
                intent.putExtra("image",model.getImage());
                intent.putExtra("id",getItem(position).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public AnimalVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.animals_list_layout,null);
        return new AnimalVH(view);
    }

    public class AnimalVH extends RecyclerView.ViewHolder{
         TextView nameTv;
         ImageView mainIv;
        public AnimalVH(@NonNull View itemView) {
            super(itemView);

            nameTv=itemView.findViewById(R.id.nameTv);
            mainIv=itemView.findViewById(R.id.mainIv);
        }
    }
}
