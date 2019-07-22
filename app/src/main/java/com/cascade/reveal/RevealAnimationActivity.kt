package com.cascade.reveal

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

import java.util.ArrayList
import java.util.LinkedHashMap
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

@SuppressLint("Registered")
open class RevealAnimationActivity : AppCompatActivity() {
    companion object {
        private val ACTIVITY_KEY_LIST = ArrayList<String>()
        private val ACTIVITY_KEY_SET_TOUCH_MAP = LinkedHashMap<String, Point>()
    }

    private var animationDuration: Int = 0
    private var isExitAnimationRequested: Boolean = false

    private val key: String
        get() = toString()

    private val screenSize: Point
        get() {
            val size = Point()
            val display = windowManager.defaultDisplay
            display.getSize(size)

            return size
        }

    private val callingActivityTouchPoint: Point?
        get() {
            val key = key

            val index = ACTIVITY_KEY_LIST.indexOf(key)
            if (index > 0) {
                val previousActivityKey = ACTIVITY_KEY_LIST[index - 1]
                return ACTIVITY_KEY_SET_TOUCH_MAP[previousActivityKey]
            }

            return null
        }

    //region LIFECYCLE METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        val key = key
        ACTIVITY_KEY_LIST.add(key)

        if (!isTaskRoot) {
            initializeResources()
            initializeBackground()
            requestEnterAnimation()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        val key = key
        ACTIVITY_KEY_LIST.remove(key)
        ACTIVITY_KEY_SET_TOUCH_MAP.remove(key)

        super.onDestroy()
    }
    //endregion

    //region FLOW METHODS
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()

        val key = key
        val point = Point(x, y)
        ACTIVITY_KEY_SET_TOUCH_MAP[key] = point

        return super.dispatchTouchEvent(ev)
    }

    override fun finish() {
        if (isExitAnimationRequested) {
            super.finish()
        } else if (!isTaskRoot) {
            isExitAnimationRequested = true
            requestExitAnimation()
        } else
            super.finish()
    }
    //endregion

    //region INITIALIZATION_METHODS
    private fun initializeResources() {
        animationDuration = resources.getInteger(android.R.integer.config_longAnimTime)
    }

    private fun initializeBackground() {
        val window = window
        val decorView = window.decorView
        val contentView = findViewById<View>(android.R.id.content)

        val decorDrawable = decorView.background
        val contentDrawable = contentView.background

        var newContentDrawable: Drawable? = null
        if (decorDrawable != null && contentDrawable != null) {
            val drawables = arrayOfNulls<Drawable>(2)
            drawables[0] = decorView.background
            drawables[1] = contentView.background
            newContentDrawable = LayerDrawable(drawables)
        } else if (decorDrawable != null) {
            newContentDrawable = decorDrawable
        }

        if (newContentDrawable != null)
            contentView.background = newContentDrawable

        window.setBackgroundDrawableResource(android.R.color.transparent)
    }
    //endregion

    //region STORY METHODS
    private fun createRevealAnimation(reverse: Boolean): Animator {
        val screenSize = screenSize
        val width = screenSize.x
        val height = screenSize.y
        val radiusSize = sqrt((width * width + height * height).toDouble()).toInt()

        val animCenterX: Int
        val animCenterY: Int
        val callingActivityTouchPoint = callingActivityTouchPoint
        if (callingActivityTouchPoint != null) {
            animCenterX = callingActivityTouchPoint.x
            animCenterY = callingActivityTouchPoint.y
        } else {
            animCenterX = width / 2
            animCenterY = height / 2
        }

        val decorView = window.decorView
        val interpolator = getInterpolator(reverse)
        val animator = ViewAnimationUtils.createCircularReveal(decorView, animCenterX, animCenterY, 0f, radiusSize.toFloat())
        animator.interpolator = interpolator
        animator.duration = animationDuration.toLong()
        return animator
    }

    private fun requestEnterAnimation() {
        val decorView = window.decorView
        decorView.alpha = 0f
        decorView.post {
            decorView.alpha = 1f

            val animator = createRevealAnimation(false)
            animator.start()
        }
    }

    private fun requestExitAnimation() {
        val decorView = window.decorView
        decorView.post {
            val animator = createRevealAnimation(true)
            animator.addListener(object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    decorView.alpha = 0f
                    if (!isFinishing)
                        super@RevealAnimationActivity.finish()
                }
            })
            animator.start()
        }
    }

    private fun getInterpolator(reverse: Boolean): Interpolator {
        return if (reverse) {
            object : AccelerateDecelerateInterpolator() {
                override fun getInterpolation(input: Float): Float {
                    return 1 - super.getInterpolation(input)
                }
            }
        } else {
            AccelerateDecelerateInterpolator()
        }
    }
    //endregion
}