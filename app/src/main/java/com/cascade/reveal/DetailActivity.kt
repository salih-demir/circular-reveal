package com.cascade.reveal

import android.content.Intent
import android.os.Bundle
import android.view.View

class DetailActivity : RevealAnimationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)
        findViewById<View>(R.id.start_button).setOnClickListener {
            val intent = Intent(this@DetailActivity, AboutActivity::class.java)
            startActivity(intent)
        }
        findViewById<View>(R.id.finish_button).setOnClickListener { finish() }
    }
}