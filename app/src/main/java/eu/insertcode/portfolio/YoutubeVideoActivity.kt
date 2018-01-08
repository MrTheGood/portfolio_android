package eu.insertcode.portfolio

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_video)

        youtube_view.initialize(API_KEY, this)
    }


    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {
            player.cueVideo("USQ0RdSqwmU")
            //TODO: get youtube url from data
            //TODO: https://www.sitepoint.com/using-the-youtube-api-to-embed-video-in-an-android-app/
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, errorReason: YouTubeInitializationResult) {
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
