package eu.insertcode.portfolio

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import eu.insertcode.portfolio.data.Item


class ProjectsActivity : AppCompatActivity() {
    companion object {
        var items: List<Item>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        val imageView = findViewById<ImageView>(R.id.project_image)
        Glide.with(this).asBitmap().load("https://portfolio.insertcode.eu/media/game_unity_mgfm1.png").into(imageView)
    }


}