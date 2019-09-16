package com.example.lottiepractice

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.airbnb.lottie.LottieDrawable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Animator.AnimatorListener {
    override fun onAnimationRepeat(p0: Animator?) {
        Log.d("mTest", "repeat")
    }

    override fun onAnimationEnd(p0: Animator?) {
        Log.d("mTest", "End")
    }

    override fun onAnimationCancel(p0: Animator?) {
        Log.d("mTest", "Cancel")
    }

    override fun onAnimationStart(p0: Animator?) {
        Log.d("mTest", "Start")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lottie_view.apply {
            setAnimation("loading.json")
            addAnimatorListener(this@MainActivity)
            repeatCount = LottieDrawable.INFINITE
//            speed = 0.5f
//            repeatMode = LottieDrawable.REVERSE
//            progress = 0.5f
//            playAnimation()
        }

        btn_start.setOnClickListener {
            lottie_view.playAnimation()
        }

        btn_stop.setOnClickListener {
//            lottie_view.cancelAnimation()
            lottie_view.pauseAnimation()
        }

        btn_progress.setOnClickListener {
//            lottie_view.progress = 0.2f
            lottie_view.resumeAnimation()
        }


    }
}
