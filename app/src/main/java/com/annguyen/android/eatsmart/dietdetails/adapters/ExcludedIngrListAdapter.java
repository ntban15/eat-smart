package com.annguyen.android.eatsmart.dietdetails.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.listeners.OnExclClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annguyen on 31/07/2017.
 */

public class ExcludedIngrListAdapter extends RecyclerView.Adapter<ExcludedIngrListAdapter.ExclViewHolder> {

    private OnExclClickListener clickListener;
    private List<String> excls;
    private boolean isRemovable;

    public ExcludedIngrListAdapter(OnExclClickListener clickListener, boolean isRemovable) {
        this.clickListener = clickListener;
        this.isRemovable = isRemovable;
        excls = new ArrayList<>();
    }

    @Override
    public ExclViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View exlcView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excluded_ingredient, parent, false);

        return new ExclViewHolder(exlcView);
    }

    @Override
    public void onBindViewHolder(final ExclViewHolder holder, int position) {
        String excludedItem = excls.get(position);
        holder.exclName.setText(excludedItem);
        if (isRemovable) {
            holder.exclRemoveBtn.setVisibility(View.VISIBLE);
            holder.exclRemoveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRemoveClick(holder.getAdapterPosition());
                }
            });
        }
        else
            holder.exclRemoveBtn.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return excls.size();
    }

    public void addItem(String excl) {
        excls.add(excl);
        notifyItemInserted(excls.size() - 1);
    }

    public void removeItem(int position) {
        excls.remove(position);
        notifyItemRemoved(position);
    }

    public void setList(List<String> excls) {
        this.excls.clear();
        this.excls.addAll(excls);
        notifyDataSetChanged();
    }

    public List<String> getList() {
        return excls;
    }

    class ExclViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.excluded_item_name)
        TextView exclName;
        @BindView(R.id.btn_remove_excl_item)
        ImageView exclRemoveBtn;

        public ExclViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
