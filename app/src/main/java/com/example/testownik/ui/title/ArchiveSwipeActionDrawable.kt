package com.example.testownik.ui.title

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import com.example.testownik.R
import com.example.testownik.util.lerp
import com.example.testownik.util.lerpArgb
import com.google.android.material.color.MaterialColors
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sin

class ArchiveSwipeActionDrawable(context: Context): Drawable() {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = MaterialColors.getColor(context,R.attr.colorSecondary,">:(")
        style = Paint.Style.FILL
    }

    // Rect to represent the circle used for the background/circular reveal animation.
    private val circle = RectF()
    private var cx = 0F
    private var cr = 0F

    private val icon = AppCompatResources.getDrawable(
        context,
        R.drawable.ic_baseline_delete_24
    )!!
    private val iconMargin = context.resources.getDimension(R.dimen.grid_4)
    private val iconIntrinsicWidth = icon.intrinsicWidth
    private val iconIntrinsicHeight = icon.intrinsicHeight

    @ColorInt
    private val iconTint = MaterialColors.getColor(context,R.attr.colorOnBackground,">:(")
    @ColorInt
    private val iconTintActive = MaterialColors.getColor(context,R.attr.colorOnSecondary,">:(")

    // Amount that we should 'overshoot' the icon's scale by when animating.
    private val iconMaxScaleAddition = 0.5F

    private var progress = 0F
        set(value) {
            val constrained = value.coerceIn(0F, 1F)
            if (constrained != field) {
                field = constrained
                callback?.invalidateDrawable(this)
            }
        }
    private var progressAnim: ValueAnimator? = null
    private val dur = context.resources.getInteger(R.integer.reply_motion_duration_medium)
//    private val interp = context.themeInterpolator(R.attr.motionInterpolatorPersistent)

    override fun onBoundsChange(bounds: Rect?) {
        if (bounds == null)  return
        update()
    }

    private fun update() {
        circle.set(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )

        val sideToIconCenter = iconMargin + (iconIntrinsicWidth / 2F)
        cx = circle.right - iconMargin - (iconIntrinsicWidth / 2F)
        // Get the longest visible distance at which the circle will be displayed (the hypotenuse of
        // the triangle from the center of the icon, to the furthest side of the rect, to the top
        // corner of the rect.
        cr = hypot(circle.right - sideToIconCenter, (circle.height() / 2F))

        callback?.invalidateDrawable(this)
    }

    override fun onStateChange(state: IntArray?): Boolean {
        val initialProgress = progress
        val newProgress = if (state?.contains(android.R.attr.state_activated) == true) {
            1F
        } else {
            0F
        }
        progressAnim?.cancel()
        progressAnim = ValueAnimator.ofFloat(initialProgress, newProgress).apply {
            addUpdateListener {
                progress = animatedValue as Float
            }
//            interpolator = interp
            duration = (abs(newProgress - initialProgress) * dur).toLong()
        }
        progressAnim?.start()
        return newProgress == initialProgress
    }

    override fun isStateful(): Boolean = true

    override fun draw(canvas: Canvas) {
        // Draw the circular reveal background.
        canvas.drawCircle(
            cx,
            circle.centerY(),
            cr * progress,
            circlePaint
        )

        // Map our progress range from 0-1 to 0-PI
        val range = lerp(
            0F,
            Math.PI.toFloat(),
            progress
        )
        // Take the sin of our ranged progress * our maxScaleAddition as what we should
        // increase the icon's scale by.
        val additive = (sin(range.toDouble()) * iconMaxScaleAddition).coerceIn(0.0, 1.0)
        val scaleFactor = 1 + additive
        icon.setBounds(
            (cx - (iconIntrinsicWidth / 2F) * scaleFactor).toInt(),
            (circle.centerY() - (iconIntrinsicHeight / 2F) * scaleFactor).toInt(),
            (cx + (iconIntrinsicWidth / 2F) * scaleFactor).toInt(),
            (circle.centerY() + (iconIntrinsicHeight / 2F) * scaleFactor).toInt()
        )

        // Draw/animate the color of the icon
        icon.setTint(
            lerpArgb(iconTint, iconTintActive, 0F, 0.15F, progress)
        )

        // Draw the icon
        icon.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        circlePaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        circlePaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}