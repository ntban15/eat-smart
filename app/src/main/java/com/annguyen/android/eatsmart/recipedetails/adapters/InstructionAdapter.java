package com.annguyen.android.eatsmart.recipedetails.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.entities.Equipment;
import com.annguyen.android.eatsmart.entities.Ingredient;
import com.annguyen.android.eatsmart.entities.Instruction;
import com.annguyen.android.eatsmart.entities.Step;
import com.annguyen.android.eatsmart.libs.GlideImageLoader;
import com.annguyen.android.eatsmart.libs.base.ImageLoader;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annguyen on 19/08/2017.
 */

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstViewHolder> {

    private Context context;
    private List<Step> instructions;
    private ImageLoader imageLoader;

    public InstructionAdapter(ImageLoader imageLoader) {
        this.instructions = new ArrayList<>();
        this.imageLoader = imageLoader;
    }

    @Override
    public InstViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instruction_card_layout, parent, false);
        context = parent.getContext();
        return new InstViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InstViewHolder holder, int position) {
        Step curInst = instructions.get(position);
        int stepNum = curInst.getNumber();
        String stepDetail = curInst.getStep();
        List<Equipment> equipments = curInst.getEquipment();
        List<Ingredient> ingredients = curInst.getIngredients();
        String imgUrl = null;
        if (null != equipments) {
            //if (!equipments.isEmpty())
                //imgUrl = equipments.get(0).getImage();
        }
        else if (null != ingredients) {
            //if (!ingredients.isEmpty())
                //imgUrl = ingredients.get(0).getImage();
        }

        holder.instStep.setText(String.format(context.getString(R.string.inst_step), stepNum));
        holder.instDetail.setText(stepDetail);
        //imageLoader.load(holder.instImg, imgUrl);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public void addStep(Step step) {
        instructions.add(step);
        notifyItemInserted(instructions.size() - 1);
    }

    class InstViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.inst_step)
        TextView instStep;
        @BindView(R.id.inst_detail)
        TextView instDetail;
        @BindView(R.id.inst_img)
        ImageView instImg;

        public InstViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
