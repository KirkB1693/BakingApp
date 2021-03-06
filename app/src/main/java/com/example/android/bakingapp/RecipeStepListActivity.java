package com.example.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android.bakingapp.Adapters.RecipeStepsRecyclerAdapter;
import com.example.android.bakingapp.IdlingResources.EspressoIdlingResource;
import com.example.android.bakingapp.RoomDatabase.Recipes;
import com.example.android.bakingapp.RoomDatabase.Steps;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

/**
 * An activity representing a list of RecipeSteps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity implements RecipeStepsRecyclerAdapter.RecipeStepClickListener {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private boolean mVideoFullScreen;

    public static final String RECIPE_DATA_KEY = "recipe_data_key";
    private static final String RECIPE_STEPS_TAG = "recipe_steps_fragment";
    private static final String STEP_DETAILS_TAG = "step_details_fragment";
    private static final String CURRENT_STEP_KEY = "current_step";

    private Recipes mRecipe;
    private RelativeLayout mVideoContainer;

    private int mCurrentStep;

    private RecipeStepsRecyclerAdapter mRecipeStepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra(RECIPE_DATA_KEY);

        this.setTitle(mRecipe.getRecipeName());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTwoPane = getResources().getBoolean(R.bool.twoPaneMode);
        mVideoFullScreen = getResources().getBoolean(R.bool.fullScreenVideo);

        EspressoIdlingResource.increment();
        setupStepsRecyclerViewAdapter();


        if (savedInstanceState == null) {

            if (mTwoPane) {
                addStepsListFragment(R.id.steps_list_fragment);
                setStepDetailsFragment(R.id.step_detail_fragment, mRecipe.getStepsList(), 0);
            } else {
                addStepsListFragment(R.id.recipe_steps_fragment_container);
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(RECIPE_STEPS_TAG);
            if (fragment instanceof RecipeStepsFragment) {
                ((RecipeStepsFragment) fragment).setStepsAdapter(mRecipeStepsAdapter);
                ((RecipeStepsFragment) fragment).setRecipe(mRecipe);
                mCurrentStep = savedInstanceState.getInt(CURRENT_STEP_KEY);
            }
            if (mTwoPane) {
                mCurrentStep = savedInstanceState.getInt(CURRENT_STEP_KEY);
                setStepSelected(mCurrentStep);
            } else {
                Fragment detailFragment = getSupportFragmentManager().findFragmentByTag(STEP_DETAILS_TAG);
                if (detailFragment instanceof RecipeStepDetailFragment) {
                    if (mVideoFullScreen) {
                        if (actionBar != null) {
                            actionBar.hide();
                        }
                    } else {
                        if (actionBar != null) {
                            actionBar.show();
                        }
                    }

                }
            }

        }
        EspressoIdlingResource.decrement();

    }

    private void setStepSelected(int stepSelected) {
        mCurrentStep = stepSelected;
        if (mRecipeStepsAdapter != null) {
            mRecipeStepsAdapter.setLastStepSelected(stepSelected);
            mRecipeStepsAdapter.notifyDataSetChanged();
        }
    }

    private void setStepDetailsFragment(int fragmentContainerId, List<Steps> stepsList, int currentStep) {
        setStepSelected(currentStep);
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        recipeStepDetailFragment.setStepsList(stepsList);
        recipeStepDetailFragment.setCurrentStep(currentStep);
        recipeStepDetailFragment.setIsTwoPane(mTwoPane);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(fragmentContainerId, recipeStepDetailFragment, STEP_DETAILS_TAG).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(STEP_DETAILS_TAG);
        if (fragment instanceof RecipeStepDetailFragment) {
            mCurrentStep = ((RecipeStepDetailFragment) fragment).getCurrentStepIndex();
            setStepSelected(mCurrentStep);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
        }
    }

    private void addStepsListFragment(int fragmentContainerId) {
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        recipeStepsFragment.setStepsAdapter(mRecipeStepsAdapter);
        recipeStepsFragment.setRecipe(mRecipe);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(fragmentContainerId, recipeStepsFragment, RECIPE_STEPS_TAG).commit();
    }

    private void setupStepsRecyclerViewAdapter() {
        mRecipeStepsAdapter = new RecipeStepsRecyclerAdapter(this, mRecipe);
        mRecipeStepsAdapter.setClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStepClick(int position) {
        if (mTwoPane) {
            setStepDetailsFragment(R.id.step_detail_fragment, mRecipe.getStepsList(), position);
        } else {
            setStepDetailsFragment(R.id.recipe_steps_fragment_container, mRecipe.getStepsList(), position);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STEP_KEY, mCurrentStep);
    }

    public void onNextClicked(View view) {
        setStepDetailsFragment(R.id.recipe_steps_fragment_container, mRecipe.getStepsList(), mCurrentStep + 1);
    }

    public void onPreviousClicked(View view) {
        setStepDetailsFragment(R.id.recipe_steps_fragment_container, mRecipe.getStepsList(), mCurrentStep - 1);
    }

    @Override
    public void onUserLeaveHint() {
        if (Util.SDK_INT >= 24) {
            if (iWantToBeInPipModeNow()) {
                enterPictureInPictureMode();
            }
        }
    }

    private boolean iWantToBeInPipModeNow() {
        if (findViewById(R.id.recipe_instructions_player) != null && !mTwoPane) {
            mVideoContainer = findViewById(R.id.video_container_rl);
            return true;
        }
        return false;
    }

    @Override
    public void onPictureInPictureModeChanged (boolean isInPictureInPictureMode, Configuration newConfig) {
        if (isInPictureInPictureMode) {
            // Hide the full-screen UI (controls, etc.) while in picture-in-picture mode.
            hideAllUIExceptVideo();
        } else {
            // Restore the full-screen UI.
            restoreAllUI();
        }
    }

    private void restoreAllUI() {
        if(mTwoPane) {

        } else {
            if (!mVideoFullScreen) {
                getSupportActionBar().show();
            }
            if (mVideoContainer != null) {
                setMargins(mVideoContainer, (int) getResources().getDimension(R.dimen.normal_margin), (int) getResources().getDimension(R.dimen.normal_margin), (int) getResources().getDimension(R.dimen.normal_margin), 0);
            }
            findViewById(R.id.recipe_step_instructions_tv).setVisibility(View.VISIBLE);
            findViewById(R.id.button_container_ll).setVisibility(View.VISIBLE);
        }
    }

    private void hideAllUIExceptVideo() {
        if(mTwoPane) {

        } else {
            getSupportActionBar().hide();
            if (mVideoContainer != null) {
                setMargins(mVideoContainer, 0, 0, 0, 0);
            }
            findViewById(R.id.recipe_step_instructions_tv).setVisibility(View.GONE);
            findViewById(R.id.button_container_ll).setVisibility(View.GONE);
        }
    }

    public static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean fullscreen = getResources().getBoolean(R.bool.fullScreenVideo);
        if (fullscreen) {
            getSupportActionBar().hide();


        } else {
            getSupportActionBar().show();
        }

    }
}
