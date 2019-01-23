package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RoomDatabase.Recipes;
import com.example.android.bakingapp.Utilities.GridUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectRecipeRecyclerAdapter extends RecyclerView.Adapter<SelectRecipeRecyclerAdapter.ViewHolder> {

    private List<Recipes> mRecipes;
    private RecipeItemClickListener mClickListener;
    private final Context mContext;

    // data is passed into the constructor
    public SelectRecipeRecyclerAdapter(Context context, List<Recipes> recipes) {
        this.mRecipes = recipes;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        int height = GridUtils.gridItemHeightInPixelsFromWidth(mContext);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        layoutParams.height = height;
        return new ViewHolder(view);
    }

    // binds the recipe name & image to the CardView in each cell
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.itemView.setTag(mRecipes.get(position));
        Recipes recipe = mRecipes.get(position);
        holder.myTextView.setText(recipe.getRecipeName());
        if (!recipe.getRecipeImage().isEmpty()) {
            Picasso.with(mContext).load(recipe.getRecipeImage()).fit().placeholder(R.drawable.cupcake).into(holder.myImageView);
        } else if (recipe.getRecipeName().equals(mContext.getString(R.string.nutella_pie)) ) {
            holder.myImageView.setImageResource(R.drawable.nutella_pie);
        } else if (recipe.getRecipeName().equals(mContext.getString(R.string.brownies))) {
            holder.myImageView.setImageResource(R.drawable.plate_of_brownies);
        } else if (recipe.getRecipeName().equals(mContext.getString(R.string.cheesecake))) {
            holder.myImageView.setImageResource(R.drawable.cheesecake);
        } else if (recipe.getRecipeName().equals(mContext.getString(R.string.yellow_cake))) {
            holder.myImageView.setImageResource(R.drawable.yellow_cake);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mRecipes.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView myImageView;
        final TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.recipe_iv);
            myTextView = itemView.findViewById(R.id.recipe_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onRecipeItemClick(getAdapterPosition());
        }
    }


    public void setRecipesData(List<Recipes> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(RecipeItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface RecipeItemClickListener {
        void onRecipeItemClick(int position);
    }

}
