package com.cascade.reveal

import android.content.Intent
import android.os.Bundle
import android.view.View

class AboutActivity : RevealAnimationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)
        findViewById<View>(R.id.start_button).setOnClickListener {
            val intent = Intent(this@AboutActivity, DetailActivity::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.finish_button).setOnClickListener { finish() }
    }
}