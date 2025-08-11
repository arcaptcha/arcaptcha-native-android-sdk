package co.arcaptcha.arcaptcha_native_sdk.components

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import co.arcaptcha.arcaptcha_native_sdk.R
import co.arcaptcha.arcaptcha_native_sdk.databinding.PuzzleSliderViewBinding
import kotlin.math.roundToInt


@SuppressLint("ClickableViewAccessibility")
class PuzzleSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(context, attrs), View.OnTouchListener {
    protected val binding: PuzzleSliderViewBinding =
        PuzzleSliderViewBinding.inflate(LayoutInflater.from(context), this, true)
    private var sliderContainer: RelativeLayout = binding.sliderContainer
    private var sliderThumb: FrameLayout = binding.sliderThumb
    private var thumbArrow: ImageView = binding.thumbArrow

    public var startTime = 0L
    protected var sliderMaxValue = 0f
    protected var sliderCurrentRealValue = 0f
    protected var sliderCurrentScaledValue = 0
    protected var onInput: ((Float, Int) -> Unit)? = null
    protected var onChange: ((Float, Int, Int, Int) -> Unit)? = null
    lateinit var animatorSet: AnimatorSet

    init {
        sliderThumb.setOnTouchListener(this)
        animatorSet = createAnimatorSet()
    }

    private fun createAnimatorSet(): AnimatorSet {
        val forwardAnim = ObjectAnimator.ofFloat(thumbArrow, "translationX", 0f, -18f).apply {
            duration = 400
        }
        val backwardAnim = ObjectAnimator.ofFloat(thumbArrow, "translationX", -18f, 0f).apply {
            duration = 800
        }

        return AnimatorSet().apply {
            playSequentially(forwardAnim, backwardAnim)
            interpolator = LinearInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    start()
                }
            })
        }
    }

    public fun animateSliderThumb(){
        println("XQQQAnimateSliderThumb")
        animatorSet = createAnimatorSet()
        animatorSet.start()
    }

    public fun stopSliderThumb(){
        println("XQQQStopSliderThumb")
        animatorSet.pause()
        thumbArrow.translationX = 0f
    }

    public fun setOnInputListener(l: (Float, Int) -> Unit){
        onInput = l
    }

    public fun setOnChangeListener(l: (Float, Int, Int, Int) -> Unit){
        onChange = l
    }

    public fun setMaxValue(value: Float){
        val rawSliderWidthDP = resources.getDimension(R.dimen.captcha_slider_width) /
                resources.displayMetrics.density
        this.sliderMaxValue = value - rawSliderWidthDP
    }

    fun resetSlider(){
        startTime = 0L
        sliderThumb.translationX = 0f
        sliderCurrentRealValue = 0f
        sliderCurrentScaledValue = 0
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if(p0 == null || p1 == null || !isEnabled) return false

        return when (p1.action) {
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis()
                sliderContainer.parent.requestDisallowInterceptTouchEvent(true)
                true
            }

            MotionEvent.ACTION_MOVE -> {
                val location = IntArray(2)
                sliderContainer.getLocationOnScreen(location)
                val containerX = location[0]

                val containerWidth = sliderContainer.width
                val maxX = containerWidth - sliderThumb.width

                val newX = (p1.rawX - containerX - sliderThumb.width / 2)
                    .coerceIn(0f, maxX.toFloat())

                sliderThumb.translationX = newX

                sliderCurrentRealValue = newX
                sliderCurrentScaledValue =
                    if(sliderMaxValue > 0) (sliderCurrentRealValue / maxX * sliderMaxValue).roundToInt()
                    else sliderCurrentRealValue.roundToInt()

                onInput?.invoke(sliderCurrentRealValue, sliderCurrentScaledValue)
                true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val dur = (System.currentTimeMillis() - startTime).toInt()
                startTime = 0

                sliderContainer.parent.requestDisallowInterceptTouchEvent(false)
                p0.performClick()
                onChange?.invoke(sliderCurrentRealValue, sliderCurrentScaledValue, dur, dur)
                true
            }

            else -> false
        }
    }
}
