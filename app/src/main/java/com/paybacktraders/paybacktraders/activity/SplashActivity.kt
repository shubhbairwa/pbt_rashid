package com.paybacktraders.paybacktraders.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.paybacktraders.paybacktraders.R
import com.paybacktraders.paybacktraders.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAnimationOfLogo()
    }

    private fun setupAnimationOfLogo() {
        val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_animation)

        // Set animation listener
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                // Animation has ended, clear the activity stack
                Intent(this@SplashActivity, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finishAffinity()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Start animation on the ImageView
        binding.imageView.startAnimation(anim)
    }


}