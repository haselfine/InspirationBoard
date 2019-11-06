package com.example.PetBoard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.PetBoard.db.Pet;

import java.util.List;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetListViewHolder> {

    private final static String TAG = "PET_LIST_ADAPTER";

    private List<Pet> mPets;

    private PetListListener listener;

    public PetListAdapter(Context context, PetListListener listener){
        this.listener = listener;
    }


    void setPets(List<Pet> pets){
        this.mPets = pets;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public PetListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_list_element, parent, false);
        PetListViewHolder viewHolder = new PetListViewHolder(layout, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PetListViewHolder holder, int position){
        if(mPets != null){
            Pet pet = mPets.get(position);
            holder.bind(pet);
        } else {
            holder.bind(null);
        }
    }

    @Override
    public int getItemCount(){
        if(mPets == null){
            return 0;
        }
        return mPets.size();
    }

    public class PetListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        TextView nameTextView;
        TextView tagTextView;
        ImageView petImageView;
        String photoPath;
        TextView dateTextView;

        PetListListener listener;

        public PetListViewHolder(@NonNull View itemView, PetListListener listener){
            super(itemView);
            this.listener = listener;
            nameTextView = itemView.findViewById(R.id.name_textView);
            tagTextView = itemView.findViewById(R.id.tags_textView);
            petImageView = itemView.findViewById(R.id.pet_imageView);
            dateTextView = itemView.findViewById(R.id.date_textView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view){
            listener.onListClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view){
            listener.onListLongClick(getAdapterPosition());
            return true;
        }

        void bind(Pet pet){
            Log.d(TAG, "binding pet " + pet);
            if(pet == null){
                nameTextView.setText("");
                tagTextView.setText("");
            } else {
                nameTextView.setText(pet.getName());
                tagTextView.setText(pet.getTags());
                dateTextView.setText(pet.getDate().toString());
                photoPath = pet.getPhotoPath();
            }
        }
    }

    public Pet getFirstPet(){
        return mPets.get(0);
    }
}
