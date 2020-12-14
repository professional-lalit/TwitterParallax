package com.parallax.app

import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.lifecycle.MutableLiveData
import com.parallax.app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var scrollAmount = MutableLiveData<Int>()
    private var lastScrollAmount = 0
    private var appbarHeight = 0
    private var appbarOriginalY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbar.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.appbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                appbarHeight = binding.appbar.height
                appbarOriginalY = binding.appbar.y
                binding.linBody.setPadding(0, appbarHeight, 0, 0)
            }
        })

        binding.scroll.viewTreeObserver.addOnScrollChangedListener {
            scrollAmount.postValue(binding.scroll.scrollY)
        }

        scrollAmount.observe(this, { scroll ->
            if (lastScrollAmount > scroll && lastScrollAmount != binding.scroll.maxScrollAmount) {
//                Log.d(localClassName, " scroll up: $scroll appbar height: $appbarHeight")
                val deltaY = (binding.appbar.y + (lastScrollAmount - scroll))
                Log.d(localClassName, " scroll up delta Y: $deltaY")

                if (deltaY < appbarOriginalY) {
                    setAppbarMargin(deltaY)
                }
            } else {
                Log.d(localClassName, " scroll down: $scroll appbar height: $appbarHeight")
                if (scroll < appbarHeight) {
                    setAppbarMargin((-scroll).toFloat())
                    binding.scroll.elevation = 5f
                } else {
                    setAppbarMargin(-appbarHeight.toFloat())
                }
            }
            lastScrollAmount = scroll
        })
    }

    private fun setAppbarMargin(margin: Float) {
        binding.appbar.y = margin
    }

}