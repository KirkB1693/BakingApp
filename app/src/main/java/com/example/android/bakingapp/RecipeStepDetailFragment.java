package com.example.android.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.bakingapp.RoomDatabase.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is contained in a {@link RecipeStepListActivity}
 */
public class RecipeStepDetailFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private static final String TWO_PANE_KEY = "two_pane";
    private static final String STEP_DATA_KEY = "step_data";
    private static final String STEP_POSITION_ID = "step_id";
    private static final String STEP_VIDEO_POSITION_KEY = "step_video_position";
    private static final String STEP_DETAILS_EXOPLAYER_USER_AGENT = "step_details_exoplayer";

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private long mPlaybackPosition;

    private List<Steps> mStepList;
    private Steps mCurrentStep;
    private int mCurrentStepIndex;
    private boolean mTwoPane;
    private boolean mFullScreenVideo;
    private RelativeLayout mVideoContainer;
    private RecipeStepDetailFragment.onPreviousClickListener mPreviousClickListener;
    private RecipeStepDetailFragment.onNextClickListener mNextClickListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTwoPane = savedInstanceState.getBoolean(TWO_PANE_KEY);
            mStepList = savedInstanceState.getParcelableArrayList(STEP_DATA_KEY);
            mCurrentStepIndex = savedInstanceState.getInt(STEP_POSITION_ID);
            mPlaybackPosition = savedInstanceState.getLong(STEP_VIDEO_POSITION_KEY);
        }
        mCurrentStep = mStepList.get(mCurrentStepIndex);
        mFullScreenVideo = getResources().getBoolean(R.bool.fullScreenVideo);

        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        setupUI(rootView);

        mVideoContainer = rootView.findViewById(R.id.video_contanier_rl);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupExoPlayer(mCurrentStep.getVideoUrl());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlayer == null) {
            setupExoPlayer(mCurrentStep.getVideoUrl());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void setupUI(View view) {
        mPlayerView = view.findViewById(R.id.recipe_instructions_player);
        TextView instructionsTextView = view.findViewById(R.id.recipe_step_instructions_tv);
        instructionsTextView.setText(mCurrentStep.getDescription());

        ImageView thumbnailImageView = view.findViewById(R.id.recipe_instructions_thumbnail_iv);
        String thumbnailUrl = mCurrentStep.getThumbnailUrl();
        if (thumbnailUrl.isEmpty()) {
            thumbnailImageView.setVisibility(View.GONE);
        } else {
            Picasso.with(getContext()).load(thumbnailUrl).into(thumbnailImageView);
            if (thumbnailImageView.getDrawable() == null) {
                // if no image loaded then set ImageView to GONE
                thumbnailImageView.setVisibility(View.GONE);
            } else {
                mPlayerView.setVisibility(View.INVISIBLE);
                thumbnailImageView.setOnClickListener(this);
            }
        }

        Button previousButton = view.findViewById(R.id.previous_button);
        Button nextButton = view.findViewById(R.id.next_button);

        // If two-pane display hide buttons
        if (mTwoPane) {
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        } else {
            previousButton.setVisibility(View.VISIBLE);
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPreviousClickListener != null) {
                        mPreviousClickListener.onPreviousClick(mCurrentStepIndex);
                    }
                }
            });
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mNextClickListener != null) {
                        mNextClickListener.onNextClick(mCurrentStepIndex);
                    }
                }
            });

            // If first step make previous button invisible
            if (mCurrentStepIndex == 0) {
                previousButton.setVisibility(View.INVISIBLE);
            }
            // If last step maxt next button invisible
            if (mCurrentStepIndex == mStepList.size() - 1) {
                nextButton.setVisibility(View.INVISIBLE);
            }
        }


    }

    private void setupExoPlayer(String videoUrl) {
        if (!videoUrl.isEmpty()) {
            if (mPlayer == null) {
                // if player doesn't exist yet, create it
                mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), new DefaultTrackSelector(), new DefaultLoadControl());
                mPlayer.setPlayWhenReady(true);
            }



            Uri uri = Uri.parse(videoUrl);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(STEP_DETAILS_EXOPLAYER_USER_AGENT)).createMediaSource(uri);
            mPlayer.prepare(mediaSource);

            mPlayer.seekTo(mPlaybackPosition);
            if (mFullScreenVideo) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
                params.width = width;
                params.height = height;
                mPlayerView.setLayoutParams(params);

            } else {
                mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            }

            mPlayerView.setPlayer(mPlayer);
        } else {
            mPlayerView.setVisibility(View.GONE);
            if (mCurrentStep.getThumbnailUrl().isEmpty()) {
                mVideoContainer.setVisibility(View.GONE);
            }
        }
    }

    public void setStepsList(List<Steps> stepsList) {
        mStepList = stepsList;
    }

    public void setCurrentStep(int currentStep) {
        mCurrentStepIndex = currentStep;
    }

    public int getCurrentStepIndex() {
        return mCurrentStepIndex;
    }

    public void setIsTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TWO_PANE_KEY, mTwoPane);
        outState.putInt(STEP_POSITION_ID, mCurrentStepIndex);
        outState.putParcelableArrayList(STEP_DATA_KEY, (ArrayList<? extends Parcelable>) mStepList);
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            outState.putLong(STEP_VIDEO_POSITION_KEY, mPlaybackPosition);
        } else {
            outState.putLong(STEP_VIDEO_POSITION_KEY, 0);
        }
    }

    public void setPreviousClickListener(onPreviousClickListener mPreviousClickListener) {
        this.mPreviousClickListener = mPreviousClickListener;
    }

    @Override
    public void onClick(View view) {
        view.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    public interface onPreviousClickListener {
        void onPreviousClick(int position);
    }

    public void setNextClickListener(onNextClickListener mNextClickListener) {
        this.mNextClickListener = mNextClickListener;
    }

    public interface onNextClickListener {
        void onNextClick(int position);
    }
}