package com.itunestracksearch.presentation.ui.album

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import dagger.hilt.android.AndroidEntryPoint

private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

@AndroidEntryPoint
class MediaPlaybackService : MediaBrowserServiceCompat()  {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private lateinit var currentPlayer: Player
    private lateinit var audioFocusRequest: AudioFocusRequest
    private val playerListener = PlayerEventListener()

    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    private val playPlaybackState: PlaybackStateCompat = PlaybackStateCompat.Builder()
        .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0f)
        .build()

    private val pausePlaybackState: PlaybackStateCompat = PlaybackStateCompat.Builder()
        .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0f)
        .build()

    private val stoppedPlaybackState: PlaybackStateCompat = PlaybackStateCompat.Builder()
        .setState(PlaybackStateCompat.STATE_STOPPED, 0, 0f)
        .build()

    private val callback = object: MediaSessionCompat.Callback() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
            super.onPlayFromUri(uri, extras)
            val am = baseContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                setAudioAttributes(AudioAttributes.Builder().run {
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    build()
                })
                build()
            }

            val result = am.requestAudioFocus(audioFocusRequest)
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                startService(Intent(baseContext, MediaBrowserService::class.java))
                mediaSession.isActive = true
                mediaSession.setPlaybackState(playPlaybackState)
                prepareList(uri!!)
                exoPlayer.playWhenReady = true
            }
        }

        fun prepareList(song: Uri) {
            val item = MediaItem.fromUri(song)
            val playbackPosition = 0L
            val currentWindow = 0
            exoPlayer.stop()
            exoPlayer.setMediaItem(item)
            exoPlayer.prepare()
            exoPlayer.seekTo(currentWindow, playbackPosition)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onStop() {
            stopSelf()
            mediaSession.let{
                it.isActive = false
                currentPlayer.stop()
                mediaSession.setPlaybackState(stoppedPlaybackState)
            }
            stopService(Intent(baseContext, MediaBrowserService::class.java))
        }

        override fun onPause() {
            mediaSession.setPlaybackState(pausePlaybackState)
            currentPlayer.pause()
        }
    }

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(baseContext, com.itunestracksearch.util.TAG).apply {
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())
            setCallback(callback)
            currentPlayer = exoPlayer
            isActive = true
        }

        sessionToken = mediaSession.sessionToken
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        currentPlayer.stop()
    }
    override fun onDestroy() {
        mediaSession.run {
            isActive = false
            release()
        }

        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        //onGetRoot는 사용하지 않는다.
        return BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        //onLoadChildren 사용하지 않는다.
    }

    private inner class PlayerEventListener : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        }

        override fun onPlayerError(error: ExoPlaybackException) {
        }
    }
}