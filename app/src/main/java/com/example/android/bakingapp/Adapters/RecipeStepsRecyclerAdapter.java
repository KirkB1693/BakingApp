package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RoomDatabase.Recipes;
import com.example.android.bakingapp.RoomDatabase.Steps;

public class RecipeStepsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Recipes mRecipe;
    private RecipeStepsRecyclerAdapter.RecipeStepClickListener mClickListener;
    private final Context mContext;
    private int mLastStepSelected;
    private LinearLayout mLastSelectedStepWrapper;

    // data is passed into the constructor
    public RecipeStepsRecyclerAdapter(Context context, Recipes recipe) {
        this.mRecipe = recipe;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_item, parent, false);
        return new StepViewHolder(view);

    }

    // binds the recipe name & image to the CardView in each cell
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        StepViewHolder stepViewHolder = (StepViewHolder) holder;
        Steps currentStep = mRecipe.getStepsList().get(position);
        String stepNumber;
        if(position == 0) {
            stepNumber = " ";
        } else {
            stepNumber = currentStep.getStepNumber() + ") ";
        }
        String stepDescription = currentStep.getShortDescription();
        stepViewHolder.stepNumberTextView.setText(stepNumber);
        stepViewHolder.stepDescriptionTextView.setText(stepDescription);
        // highlight selected step in two-pane mode
        if (mLastStepSelected == position) {
            if (mLastSelectedStepWrapper != null) {
                mLastSelectedStepWrapper.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary_light));
            }
            mLastSelectedStepWrapper = ((StepViewHolder) holder).linearLayout;
            mLastSelectedStepWrapper.setBackgroundColor(ContextCompat.getColor(mContext, R.color.accent));
        }
    }


    // total number of cells
    @Override
    public int getItemCount() {
        return mRecipe.getStepsList().size();
    }

    public void setLastStepSelected(int stepSelected) {
        mLastStepSelected = stepSelected;
    }

    // stores and recycles views as they are scrolled off screen
    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final LinearLayout linearLayout;
        final TextView stepNumberTextView;
        final TextView stepDescriptionTextView;

        StepViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.step_wrapper_ll);
            stepNumberTextView = (TextView) itemView.findViewById(R.id.step_number_tv);
            stepDescriptionTextView = (TextView) itemView.findViewById(R.id.step_short_description_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onStepClick(getAdapterPosition());
        }
    }


    // convenience method for getting data at click position
    public Steps getStep(int id) {
        return mRecipe.getStepsList().get(id);
    }

    public void setStepData(Recipes recipe) {
        mRecipe = recipe;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(RecipeStepsRecyclerAdapter.RecipeStepClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface RecipeStepClickListener {
        void onStepClick(int position);
    }

}

