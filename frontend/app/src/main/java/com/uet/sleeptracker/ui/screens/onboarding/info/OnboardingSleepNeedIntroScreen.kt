package com.uet.sleeptracker.ui.screens.onboarding.info

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uet.sleeptracker.R
import com.uet.sleeptracker.ui.theme.SleepBackground
import com.uet.sleeptracker.ui.theme.SleepPrimary
import com.uet.sleeptracker.ui.theme.SleeptrackerTheme

@Composable
fun OnboardingSleepNeedIntroScreen(
    onNavigateNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SleepBackground)
    ) {
        // Top Graphic: Purple Mountain/Wave with Bed wireframe
        SleepNeedGraphic(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .align(Alignment.TopCenter)
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1.8f))

            Text(
                text = stringResource(id = R.string.sleep_need_intro_text),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1.2f))

            Button(
                onClick = onNavigateNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleepPrimary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.sleep_need_intro_button),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun SleepNeedGraphic(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Wave curve control points
        // Curve starts mid-left, peaks high around 55% x, ends mid-right
        // The curve crosses through the upper portion of the bed
        val curveStartX = 0f
        val curveStartY = height * 0.62f
        val cp1x = width * 0.28f;  val cp1y = height * 0.50f
        val cp2x = width * 0.52f;  val cp2y = height * 0.06f
        val curveEndX = width;     val curveEndY = height * 0.30f

        // ── 1. Purple gradient fill BELOW the curve ──────────────────────────
        // Shape: curve line → right edge down → bottom → left edge up → back
        val fillPath = Path()
        fillPath.moveTo(curveStartX, curveStartY)
        fillPath.cubicTo(cp1x, cp1y, cp2x, cp2y, curveEndX, curveEndY)
        fillPath.lineTo(width, height)
        fillPath.lineTo(0f, height)
        fillPath.close()

        // Gradient: purple just below the curve, fading to transparent at bottom
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f  to Color(0xFF3B0FA0).copy(alpha = 0.75f),  // rich purple at top of fill
                    0.30f to Color(0xFF1E0660).copy(alpha = 0.50f),
                    0.65f to Color(0xFF0D0230).copy(alpha = 0.25f),
                    1.0f  to Color.Transparent
                ),
                startY = height * 0.06f,   // near the peak of the curve
                endY   = height            // bottom of canvas
            )
        )

        // ── 2. Wave border — glow + sharp line ───────────────────────────────
        val wavePath = Path()
        wavePath.moveTo(curveStartX, curveStartY)
        wavePath.cubicTo(cp1x, cp1y, cp2x, cp2y, curveEndX, curveEndY)

        // Soft glow underneath the line
        drawPath(
            path = wavePath,
            color = Color(0xFF5522EE).copy(alpha = 0.25f),
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )
        // Sharp bright line on top
        drawPath(
            path = wavePath,
            color = Color(0xFF6633FF),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // ── 3. Isometric Bed ─────────────────────────────────────────────────
        // Position the bed so the wave cuts through its upper portion
        // Bed anchor = center of mattress top surface
        val cx = width * 0.40f
        val cy = height * 0.56f   // raised up so the wave crosses through it

        val bedColor  = Color(0xFF6633FF).copy(alpha = 0.90f)
        val glowColor = Color(0xFF6633FF).copy(alpha = 0.20f)
        val strokeSharp = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val strokeGlow  = Stroke(width = 5.dp.toPx(),   cap = StrokeCap.Round, join = StrokeJoin.Round)

        val bedL   = 115.dp.toPx()   // length  (foot → head)
        val bedW   = 72.dp.toPx()    // width   (right → left)
        val frameH = 12.dp.toPx()    // frame/mattress thickness
        val headH  = 46.dp.toPx()    // headboard full height
        val headMid = headH * 0.50f  // mid-rail position on headboard
        val legH   = 11.dp.toPx()    // leg drop below frame

        // True isometric projection (30° axes):
        //   u = bed-length axis  → (+cos30, +sin30) = (+0.866, +0.5)
        //   v = bed-width  axis  → (-cos30, +sin30) = (-0.866, +0.5)
        //   w = vertical up      → (0, -1)
        fun px(u: Float, v: Float, w: Float = 0f) = cx + u * 0.866f - v * 0.866f
        fun py(u: Float, v: Float, w: Float = 0f) = cy + u * 0.5f   + v * 0.5f   - w
        fun pt(u: Float, v: Float, w: Float = 0f) = Offset(px(u,v,w), py(u,v,w))

        // ── Mattress top corners ──
        val ftR = pt(0f,   0f,   frameH)
        val ftL = pt(0f,   bedW, frameH)
        val hdR = pt(bedL, 0f,   frameH)
        val hdL = pt(bedL, bedW, frameH)

        // ── Frame bottom corners ──
        val ftRb = pt(0f,   0f,   0f)
        val ftLb = pt(0f,   bedW, 0f)
        val hdRb = pt(bedL, 0f,   0f)
        val hdLb = pt(bedL, bedW, 0f)

        // ── Headboard corners ──
        val hbBR = hdR
        val hbBL = hdL
        val hbTR = pt(bedL, 0f,   frameH + headH)
        val hbTL = pt(bedL, bedW, frameH + headH)
        val hbMR = pt(bedL, 0f,   frameH + headMid)
        val hbML = pt(bedL, bedW, frameH + headMid)

        // ── Legs ──
        val legFtR = Offset(ftRb.x, ftRb.y + legH)
        val legFtL = Offset(ftLb.x, ftLb.y + legH)
        val legHdR = Offset(hdRb.x, hdRb.y + legH)
        val legHdL = Offset(hdLb.x, hdLb.y + legH)

        // ── Center divider on mattress ──
        val midFt = Offset((ftR.x + ftL.x) / 2f, (ftR.y + ftL.y) / 2f)
        val midHd = Offset((hdR.x + hdL.x) / 2f, (hdR.y + hdL.y) / 2f)

        val bedPath = Path().apply {
            // Mattress top surface
            moveTo(ftR.x, ftR.y); lineTo(hdR.x, hdR.y)
            lineTo(hdL.x, hdL.y); lineTo(ftL.x, ftL.y); close()

            // Right face of frame
            moveTo(ftR.x, ftR.y);   lineTo(ftRb.x, ftRb.y)
            lineTo(hdRb.x, hdRb.y); lineTo(hdR.x, hdR.y)

            // Front (foot) face of frame
            moveTo(ftR.x, ftR.y);   lineTo(ftRb.x, ftRb.y)
            lineTo(ftLb.x, ftLb.y); lineTo(ftL.x, ftL.y)

            // Headboard rectangle
            moveTo(hbBR.x, hbBR.y); lineTo(hbTR.x, hbTR.y)
            lineTo(hbTL.x, hbTL.y); lineTo(hbBL.x, hbBL.y); close()

            // Headboard mid horizontal rail
            moveTo(hbMR.x, hbMR.y); lineTo(hbML.x, hbML.y)

            // Legs (4 corners)
            moveTo(ftRb.x, ftRb.y); lineTo(legFtR.x, legFtR.y)
            moveTo(ftLb.x, ftLb.y); lineTo(legFtL.x, legFtL.y)
            moveTo(hdRb.x, hdRb.y); lineTo(legHdR.x, legHdR.y)
            moveTo(hdLb.x, hdLb.y); lineTo(legHdL.x, legHdL.y)

            // Center divider line on mattress
            moveTo(midFt.x, midFt.y); lineTo(midHd.x, midHd.y)
        }

        drawPath(path = bedPath, color = glowColor, style = strokeGlow)
        drawPath(path = bedPath, color = bedColor,  style = strokeSharp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun OnboardingSleepNeedIntroScreenPreview() {
    SleeptrackerTheme {
        OnboardingSleepNeedIntroScreen(onNavigateNext = {})
    }
}
