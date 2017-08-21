package com.annguyen.android.eatsmart.diets.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.entities.Diet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annguyen on 6/11/2017.
 */

public class DietListAdapter extends RecyclerView.Adapter<DietListAdapter.CustomViewHolder> {

    private int activePos = -1;

    private List<Diet> dietDataset;
    private OnDietItemClickListener onDietItemClickListener;

    public DietListAdapter(List<Diet> dietDataset, OnDietItemClickListener onDietItemClickListener) {
        this.dietDataset = dietDataset;
        this.onDietItemClickListener = onDietItemClickListener;
    }

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
            holder.dietActive.setChecked(true);
        }
        else {
            holder.dietActive.setChecked(false);
        }

        //set click listener
        holder.setOnDietItemClickListener(onDietItemClickListener, curDiet, position);
    }

    public void addNewDiet(Diet diet) {
        dietDataset.add(diet);
        notifyItemInserted(dietDataset.size() - 1);
    }

    public void removeDiet(int pos) {
        //index of removed diet
        dietDataset.remove(pos);
        //notify the adapter of the removal
        notifyItemRemoved(pos);
    }

    public void setActiveDiet(int pos) {
        if (pos == activePos) {
            unsetActiveDiet();
            return;
        }

        unsetActiveDiet();
        //set diet given in arg active
        dietDataset.get(pos).setActive(true);
        //notify the adapter of the change
        notifyItemChanged(pos);
    }

    public void unsetActiveDiet() {
        //set last active diet unactive
        if (activePos < 0)
            return;

        dietDataset.get(activePos).setActive(false);
        notifyItemChanged(activePos);
        activePos = -1;
    }

    public void setDietDataset(List<Diet> dietList) {
        dietDataset.addAll(dietList);
        notifyDataSetChanged();
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
        RadioButton dietActive;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            dietItemView = itemView;
        }

        void setOnDietItemClickListener(final OnDietItemClickListener listener,
                                        final Diet diet, final int pos) {

            //on radio button click
            dietActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSwitchClick(diet.getDietKey(), pos);
                }
            });

            //on one click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(diet.getDietKey(), diet.isActive(), pos);
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
