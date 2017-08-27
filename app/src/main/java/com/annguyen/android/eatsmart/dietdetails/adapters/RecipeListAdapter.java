package com.annguyen.android.eatsmart.dietdetails.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.adapters.SelectableAdapter;
import com.annguyen.android.eatsmart.libs.base.ImageLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by annguyen on 29/07/2017.
 */

public class RecipeListAdapter extends SelectableAdapter<RecipeListAdapter.DietViewHolder> {

    private List<Recipe> recipeList;
    private ImageLoader imageLoader;
    private Context context;
    private Diet curDiet;
    private OnRecipeClickListener listener;
    private boolean addable;

    public RecipeListAdapter(ImageLoader imageLoader, Diet curDiet, List<Recipe> recipeList,
                             OnRecipeClickListener listener, boolean addable) {
        this.listener = listener;
        this.recipeList = new ArrayList<>();
        this.imageLoader = imageLoader;
        this.curDiet = curDiet;
        this.recipeList.addAll(recipeList);
        this.addable = addable;
    }

    public void onDietChanged(Diet newDiet) {
        curDiet = newDiet;
        notifyDataSetChanged();
    }

//    public void modifyRecipe(Recipe newRecipe) {
//        for (int i = 0; i < recipeList.size(); ++i) {
//            if (recipeList.get(i).equals(newRecipe)) {
//                recipeList.set(i, newRecipe);
//                notifyItemChanged(i);
//                break;
//            }
//        }
//    }

    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
        notifyItemInserted(recipeList.size() - 1);
    }

    public void removeRecipe(int position) {
        recipeList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     SOURCE: https://enoent.fr/blog/2015/01/18/recyclerview-basics/
     */
    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeRecipe(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeRecipe(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    /**
     SOURCE: https://enoent.fr/blog/2015/01/18/recyclerview-basics/
     */
    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            recipeList.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public DietViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == context)
            context = parent.getContext();

        View recipeItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_thumbnail, parent, false);
        return new DietViewHolder(recipeItem);
    }

    @Override
    public void onBindViewHolder(DietViewHolder holder, int position) {
        Recipe curRecipe = recipeList.get(position);

        imageLoader.load(holder.recipeImg, curRecipe.getImage());
        holder.recipeTitle.setText(curRecipe.getTitle());
        holder.readyTime.setText(String.format(context.getString(R.string.ready_time),
                curRecipe.getReadyInMinutes()));
        holder.serve.setText(String.format(context.getString(R.string.serving),
                curRecipe.getServings()));

        //init circles
        checkCircle(holder.calCircle, curDiet.getMaxCalories(), curRecipe.getCalories());
        checkCircle(holder.fatCircle, curDiet.getMaxFat(), curRecipe.getFatValue());
        checkCircle(holder.carbsCircle, curDiet.getMaxCarbs(), curRecipe.getCarbsValue());
        checkCircle(holder.proCircle, curDiet.getMaxProtein(), curRecipe.getProteinValue());

        //set click listeners
        holder.setClickListener(listener, curRecipe.getId(), holder.getAdapterPosition(), curRecipe);

        //set overlay if recipe is selected
        holder.selectedOverlay.setVisibility(
                isSelected(holder.getAdapterPosition()) ? View.VISIBLE : View.INVISIBLE);

        //set buttons for adding and removing recipe from diet
        if (addable) {
            holder.addToDietBtn.setVisibility(View.VISIBLE);
//            if (curRecipe.isInDiet()) {
//                holder.addToDietBtn.setVisibility(View.GONE);
//                holder.removeFromDietBtn.setVisibility(View.VISIBLE);
//            }
//            else {
//                holder.addToDietBtn.setVisibility(View.VISIBLE);
//                holder.removeFromDietBtn.setVisibility(View.GONE);
//            }
        }
        else {
            holder.addToDietBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public Collection<? extends Long> getSelectedRecipeIds() {
        List<Integer> positions = getSelectedItems();
        List<Long> recipeIds = new ArrayList<>();

        for (Integer recipe : positions) {
            recipeIds.add(recipeList.get(recipe).getId());
        }

        return recipeIds;
    }

    public void clearRecipes() {
        recipeList.clear();
        notifyDataSetChanged();
    }

    class DietViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_thumb_title)
        TextView recipeTitle;
        @BindView(R.id.recipe_thumb_img)
        ImageView recipeImg;
        @BindView(R.id.recipe_thumb_ready)
        TextView readyTime;
        @BindView(R.id.recipe_thumb_serving)
        TextView serve;
        @BindView(R.id.calorie_circle_small)
        CircleProgressView calCircle;
        @BindView(R.id.fat_circle_small)
        CircleProgressView fatCircle;
        @BindView(R.id.carbs_circle_small)
        CircleProgressView carbsCircle;
        @BindView(R.id.protein_circle_small)
        CircleProgressView proCircle;
        @BindView(R.id.recipe_selected_overlay)
        View selectedOverlay;
        @BindView(R.id.btn_remove_from_diet)
        Button removeFromDietBtn;
        @BindView(R.id.btn_add_to_diet)
        Button addToDietBtn;

        private View itemView;

        public DietViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }

        public void setClickListener(final OnRecipeClickListener listener, final long id,
                                     final int pos, final Recipe recipe) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecipeClick(id, pos);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onRecipeLongClick(pos);
                    return false;
                }
            });

            if (addable) {
//                removeFromDietBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        listener.onRemoveFromDietClick(id);
//                    }
//                });

                addToDietBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onAddToDietClick(recipe);
                    }
                });
            }
        }
    }

    private void checkCircle(CircleProgressView cpv, int max, int current) {
        if (0 != max) {
            cpv.setMaxValue(max);

            if (current < max)
                setCircleOk(cpv);
            else
                setCircleAbove(cpv);
            cpv.setValue(current);
        }
        else
            setCircleNah(cpv);
    }

    private void setCircleBelow(CircleProgressView cpv) {
        cpv.setBarColor(ContextCompat.getColor(context, R.color.circle_bar_below));
        cpv.setRimColor(ContextCompat.getColor(context, R.color.circle_rim_below));
    }

    private void setCircleOk(CircleProgressView cpv) {
        cpv.setBarColor(ContextCompat.getColor(context, R.color.circle_bar_ok));
        cpv.setRimColor(ContextCompat.getColor(context, R.color.circle_rim_ok));
    }

    private void setCircleAbove(CircleProgressView cpv) {
        cpv.setBarColor(ContextCompat.getColor(context, R.color.circle_bar_above));
        cpv.setRimColor(ContextCompat.getColor(context, R.color.circle_rim_above));
    }

    private void setCircleNah(CircleProgressView cpv) {
        cpv.setBarColor(ContextCompat.getColor(context, R.color.circle_nah));
        cpv.setRimColor(ContextCompat.getColor(context, R.color.circle_nah));
    }
}
