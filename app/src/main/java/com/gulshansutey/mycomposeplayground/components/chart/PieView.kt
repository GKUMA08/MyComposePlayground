package com.gulshansutey.mycomposeplayground.components.chart


import android.content.res.Resources
import android.util.TypedValue
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.gulshansutey.mycomposeplayground.ultis.LogCompositions

@Stable
@Composable
fun PieChart(
    pies: List<Pie>,
    modifier: Modifier = Modifier,
    trackThickness: Float = 10f,
    animDuration: Int = 1000
) {
    LogCompositions(tag = "Pie Chart")
    val pieView = PieView(trackThickness.toPx)
    var tracksValue = 0f
    pies.forEach { tracksValue += it.value }
    val animSpecs = FloatTweenSpec(animDuration, 0, FastOutSlowInEasing)
    val animProgress = remember(pies) { Animatable(initialValue = 0f) }
    LaunchedEffect(pies) {
        animProgress.animateTo(1f, animSpecs)
    }
    Canvas(modifier = modifier) {
        drawIntoCanvas {
            var startAngle = 0f
            pies.forEach {
                val angle = 360.0f * (it.value * animProgress.value) / tracksValue
                pieView.draw(
                    it,
                    size,
                    drawContext.canvas,
                    startAngle,
                    angle
                )
                startAngle += angle
            }
        }
    }
}

private class PieView(private val thickness: Float) {

    private val trackPaint = Paint()

    init {
        trackPaint.isAntiAlias = true
        trackPaint.style = PaintingStyle.Stroke
    }

    fun draw(
        pie: Pie,
        size: Size,
        canvas: Canvas,
        startAngle: Float,
        sweepAngle: Float
    ) {
        val rectContainer = prepareContainerWithBounds(size, thickness)

        trackPaint.color = pie.color
        trackPaint.strokeWidth = thickness

        canvas.drawArc(
            rect = rectContainer,
            paint = trackPaint,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false
        )

    }

    private fun prepareContainerWithBounds(size: Size, trackSize: Float): Rect {
        val trackBounds = trackSize.asHalf()
        val containerBounds = (size.width - size.height).asHalf()
        return Rect(
            left = trackBounds + containerBounds,
            top = trackBounds,
            right = size.width - trackBounds - containerBounds,
            bottom = size.height - trackBounds
        )
    }
}

fun Float.asHalf() = this / 2
val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)
data class Pie(
    val value: Float,
    val color: Color
)
