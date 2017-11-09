package eu.insertcode.portfolio

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import eu.insertcode.portfolio.comms.GetDataAsync
import java.lang.ref.WeakReference

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //todo: Prevent back button going here
    }

    override fun onStart() {
        super.onStart()

        if (isNetworkConnected()) {
            GetDataAsync(WeakReference(this)).execute(resources.getString(R.string.url_getData))
        } else {
            AlertDialog.Builder(this)
                    .setTitle(R.string.error_noInternet_title)
                    .setMessage(R.string.error_noInternet_msg)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        finish()
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }
    }


    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
