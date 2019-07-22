package com.cascade.reveal

import android.animation.Animator

internal abstract class SimpleAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator, isReverse: Boolean) {}

    override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {}

    override fun onAnimationStart(animation: Animator) {}

    override fun onAnimationEnd(animation: Animator) {}

    override fun onAnimationCancel(animation: Animator) {}

    override fun onAnimationRepeat(animation: Animator) {}
}