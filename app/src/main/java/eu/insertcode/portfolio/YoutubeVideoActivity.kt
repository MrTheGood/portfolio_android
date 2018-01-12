package eu.insertcode.portfolio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube_video.*

class YoutubeVideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    companion object {
        val EXTRA_VIDEO = "EXTRA_VIDEO"
        val API_KEY = "AIzaSyD-JqVSPfQfWtPWnADNDm5e4LrJw8adQEI"

        val RECOVERY_REQUEST = 1
    }

    private lateinit var video: String
    private var player: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setWindowFlags()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_video)

        video = intent.extras.getString(EXTRA_VIDEO)

        youtube_view.initialize(API_KEY, this)
    }

    private fun setWindowFlags() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {
            player.loadVideo(video)
            this.player = player
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, errorReason: YouTubeInitializationResult) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show()
        } else {
            Toast.makeText(this, errorReason.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RECOVERY_REQUEST)
            youtube_view.initialize(API_KEY, this)
    }
}
