package mm.kso.movieapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide

class LoadingActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val imageView = findViewById<ImageView>(R.id.image_loading)
        Glide.with(baseContext).load(R.raw.marvel).into(imageView)
        startLoading()
    }

    private fun startLoading() {
        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@LoadingActivity, MainActivity::class.java)
            startActivity(i)
            finish()// Your Code
        }, 3000)

    }
}