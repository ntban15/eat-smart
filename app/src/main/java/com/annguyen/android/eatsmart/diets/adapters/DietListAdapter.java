package com.annguyen.android.eatsmart.diets.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.entities.Diet;

import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annguyen on 6/11/2017.
 */

public class DietListAdapter extends RecyclerView.Adapter<DietListAdapter.CustomViewHolder> {

    int activePos;
    List<Diet> dietDataset;
    OnDietItemClickListener onDietItemClickListener;

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dietItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diet_list_item, parent, false);

        return new CustomViewHolder(dietItemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Diet curDiet = dietDataset.get(position);

        //init view in holder
        holder.dietTitle.setText(curDiet.getTitle());
        holder.dietExclude.setText(curDiet.getExcludedIngredients());
        holder.dietType.setText(curDiet.getDietType());

        //check if diet is active
        if (curDiet.isActive()) {
            activePos = holder.getAdapterPosition();
            holder.dietActive.setVisibility(View.VISIBLE);
        }
        else {
            holder.dietActive.setVisibility(View.GONE);
        }

        //set click listener
        holder.setOnDietItemClickListener(onDietItemClickListener, curDiet);
    }

    public void addNewDiet(Diet diet) {
        dietDataset.add(diet);
        notifyItemInserted(dietDataset.size() - 1);
    }

    public void removeDiet(String dietKey) {
        //index of removed diet
        int removedDiet = 0;

        //use list iterator to surf through the list and find dietKey match
        ListIterator<Diet> dietIter = dietDataset.listIterator();
        while (dietIter.hasNext()) {
            removedDiet = dietIter.nextIndex();
            Diet nextDiet = dietIter.next();
            //if match
            if (nextDiet.getDietKey().equals(dietKey)) {
                //remove the last diet return by next (which is nextDiet)
                dietIter.remove();
                break;
            }
        }

        //notify the adapter of the removal
        notifyItemRemoved(removedDiet);
    }

    public void setActiveDiet(String dietKey) {
        //set diet given in arg active
        ListIterator<Diet> dietIter = dietDataset.listIterator();
        while (dietIter.hasNext()) {
            activePos = dietIter.nextIndex();
            Diet nextDiet = dietIter.next();
            if (nextDiet.getDietKey().equals(dietKey)) {
                nextDiet.setActive(true);
                break;
            }
        }

        //notify the adapter of the change
        notifyItemChanged(activePos);
    }

    public void unsetActiveDiet() {
        //set last active diet unactive
        dietDataset.get(activePos).setActive(false);
        notifyItemChanged(activePos);
    }

    @Override
    public int getItemCount() {
        return dietDataset.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private View dietItemView;
        @BindView(R.id.diet_title)
        TextView dietTitle;
        @BindView(R.id.diet_type)
        TextView dietType;
        @BindView(R.id.diet_excluded)
        TextView dietExclude;
        @BindView(R.id.diet_active_indicator)
        ImageView dietActive;
        @BindView(R.id.diet_attention_indicator)
        ImageView dietAttention;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            dietItemView = itemView;
        }

        void setOnDietItemClickListener(final OnDietItemClickListener listener, final Diet diet) {
            //on one click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(diet.getDietKey());
                }
            });

            //on long click to active context action bar
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.OnItemLongClick();
                    return true;
                }
            });
        }
    }
}
