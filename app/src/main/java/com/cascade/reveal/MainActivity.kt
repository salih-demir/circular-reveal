package com.cascade.reveal

import android.content.Intent
import android.os.Bundle
import android.view.View

class MainActivity : RevealAnimationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.start_detail_button).setOnClickListener {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.start_about_button).setOnClickListener {
            val intent = Intent(this@MainActivity, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}