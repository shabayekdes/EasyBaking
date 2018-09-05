package com.shabayekdes.easybaking.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.shabayekdes.easybaking.R;
import com.shabayekdes.easybaking.RecipeDetailsActivity;
import com.shabayekdes.easybaking.models.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String TAG = StepDetailsFragment.class.getSimpleName();

    public static final String CURRENT_STEP_POSITION_KEY = "currentStepPosition";
    public static final String CURRENT_PLAYER_POSITION_KEY = "currentPlayerPosition";
    public static final String PLAY_WHEN_READY_KEY = "playWhenReady";
    private static MediaSessionCompat mMediaSession;
    private static List<Step> steps;
    int currentStepPosition;
    long currentPlayerPosition;
    boolean playWhenReady;


    Step step;
    Context context;


    @BindView(R.id.playerView)
    PlayerView playerView;
    @BindView(R.id.error_image)
    ImageView errorImage;
    @BindView(R.id.step_description)
    TextView stepDescription;
    @BindView(R.id.next_step_button)
    ImageButton nextStepButton;
    @BindView(R.id.previous_step_button)
    ImageButton previousStepButton;

    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle arguments;
        if (savedInstanceState == null) {
            arguments = getArguments();
        } else {
            arguments = savedInstanceState;
        }
        if (arguments != null) {
            steps = arguments.getParcelableArrayList(RecipeDetailsActivity.STEPS_LIST_KEY);
            currentStepPosition = arguments.getInt(CURRENT_STEP_POSITION_KEY);
            step = steps.get(currentStepPosition);
            if (arguments.containsKey(CURRENT_PLAYER_POSITION_KEY)) {
                currentPlayerPosition = arguments.getLong(CURRENT_PLAYER_POSITION_KEY);
            }
            playWhenReady = !arguments.containsKey(PLAY_WHEN_READY_KEY) || arguments.getBoolean(PLAY_WHEN_READY_KEY);
        }
        initializeMediaSession();
    }

    @OnClick({R.id.next_step_button, R.id.previous_step_button})
    public void stepsNavigation(View view) {
        switch (view.getId()) {
            case R.id.next_step_button:
                if (currentStepPosition < steps.size() - 1) {
                    currentStepPosition++;
                }
                break;
            case R.id.previous_step_button:
                if (currentStepPosition > 0) {
                    currentStepPosition--;
                }
                break;
        }
        step = steps.get(currentStepPosition);
        currentPlayerPosition = 0;
        setupInfo();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            setupInfo();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            setupInfo();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RecipeDetailsActivity.STEPS_LIST_KEY, (ArrayList<? extends Parcelable>) steps);
        outState.putInt(CURRENT_STEP_POSITION_KEY, currentStepPosition);
        if (mExoPlayer != null) {
            currentPlayerPosition = mExoPlayer.getCurrentPosition();
            outState.putLong(CURRENT_PLAYER_POSITION_KEY, currentPlayerPosition);
            playWhenReady = mExoPlayer.getPlayWhenReady();
            outState.putBoolean(PLAY_WHEN_READY_KEY, playWhenReady);
        }
    }


    private void setupInfo() {
        stepDescription.setText(step.getDescription());
        String thumbnailUrl = step.getThumbnailURL();
        String videoUrl = step.getVideoURL();

        if (thumbnailUrl != null || !thumbnailUrl.equals("")) {
            errorImage.setVisibility(View.GONE);

        } else {
            errorImage.setVisibility(View.VISIBLE);


            if (!thumbnailUrl.equals("")){

                Picasso.get().load(thumbnailUrl).error(R.drawable.ic_image).into(errorImage);
            }else {

                Picasso.get().load(R.drawable.ic_image).into(errorImage);
            }

        }

        if (videoUrl != null || !videoUrl.equals("")){
            playerView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(step.getVideoURL());
            initializePlayer(uri);

        }else {

            playerView.setVisibility(View.GONE);
            mMediaSession.setActive(false);
        }

        if (currentStepPosition == 0) {
            previousStepButton.setEnabled(false);
        } else if (currentStepPosition == steps.size() - 1) {
            nextStepButton.setEnabled(false);
        } else {
            nextStepButton.setEnabled(true);
            previousStepButton.setEnabled(true);
        }
    }

    private void initializePlayer(Uri mediaUri) {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(),
                new DefaultLoadControl()
        );
        playerView.setPlayer(mExoPlayer);
        mExoPlayer.addListener(this);
        MediaSource mediaSource = buildMediaSource(mediaUri);
        mExoPlayer.prepare(mediaSource, true, false);
        mExoPlayer.setPlayWhenReady(playWhenReady);
        mExoPlayer.seekTo(currentPlayerPosition);
    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(context, getString(R.string.app_name));
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent)).
                createMediaSource(uri);
    }


    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(context, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);

        // Do not let MediaButtons restart the mExoPlayer when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the mExoPlayer.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_FAST_FORWARD |
                                PlaybackStateCompat.ACTION_REWIND);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    public static class MediaBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

}
