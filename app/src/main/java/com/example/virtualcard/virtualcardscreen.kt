package com.example.virtualcard



import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VirtualCardScreen() {
    var isRevealed by remember { mutableStateOf(false) }

    val revealProgress by animateFloatAsState(
        targetValue = if (isRevealed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "reveal"
    )

    val cardRotation by animateFloatAsState(
        targetValue = if (isRevealed) 0f else 180f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "rotation"
    )

    val glowAlpha by rememberInfiniteTransition(label = "glow").animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Levitation animation - only active when revealed
    val levitationOffset by rememberInfiniteTransition(label = "levitate").animateFloat(
        initialValue = if (isRevealed) -10f else 0f,
        targetValue = if (isRevealed) 10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "levitate"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0C29),
                        Color(0xFF302B63),
                        Color(0xFF24243e)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated background particles
//        repeat(20) { index ->
//            val offsetX by rememberInfiniteTransition(label = "particle$index").animateFloat(
//                initialValue = (-100..100).random().toFloat(),
//                targetValue = (-150..150).random().toFloat(),
//                animationSpec = infiniteRepeatable(
//                    animation = tween((3000..6000).random(), easing = LinearEasing),
//                    repeatMode = RepeatMode.Reverse
//                ),
//                label = "particleX"
//            )
//
//            Box(
//                modifier = Modifier
//                    .offset(x = offsetX.dp, y = ((-200..200).random()).dp)
//                    .size((4..12).random().dp)
//                    .clip(RoundedCornerShape(50))
//                    .background(Color.White.copy(alpha = 0.1f))
//            )
//        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            // Card Container with 3D effect
            Box(
                modifier = Modifier
                    .offset(y = levitationOffset.dp)
                    .graphicsLayer {
                        rotationY = cardRotation
                        cameraDistance = 12f * density
                    }
            ) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(360.dp, 230.dp)
                        .scale(1.05f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF00F5FF).copy(alpha = glowAlpha),
                                    Color.Transparent
                                )
                            )
                        )
                        .blur(20.dp)
                )

                // Main Card
                Card(
                    modifier = Modifier
                        .size(350.dp, 220.dp)
                        .graphicsLayer {
                            shadowElevation = 24f
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF667eea),
                                        Color(0xFF764ba2),
                                        Color(0xFFf093fb)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        // Card content
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Top section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "VIRTUAL CARD",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f),
                                    letterSpacing = 2.sp
                                )

                                // Chip
                                Box(
                                    modifier = Modifier
                                        .size(50.dp, 40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFFFFD700),
                                                    Color(0xFFFFA500)
                                                )
                                            )
                                        )
                                )
                            }

                            // Card number
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    repeat(4) {
                                        CardNumberGroup(
                                            number = if (isRevealed) "1234" else "••••",
                                            revealProgress = revealProgress
                                        )
                                    }
                                }
                            }

                            // Bottom section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "CARD HOLDER",
                                        fontSize = 8.sp,
                                        color = Color.White.copy(alpha = 0.7f),
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    AnimatedText(
                                        text = if (isRevealed) "REHAN.M" else "•••• •••",
                                        revealProgress = revealProgress,
                                        fontSize = 14.sp
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "EXPIRES",
                                        fontSize = 8.sp,
                                        color = Color.White.copy(alpha = 0.7f),
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    AnimatedText(
                                        text = if (isRevealed) "12/28" else "••/••",
                                        revealProgress = revealProgress,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Decorative circles
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .offset(x = 280.dp, y = (-20).dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .offset(x = 270.dp, y = 150.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.White.copy(alpha = 0.08f))
                        )
                    }
                }
            }

            // Reveal/Hide Button with animation
            val buttonScale by animateFloatAsState(
                targetValue = if (isRevealed) 1.05f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "buttonScale"
            )

            Button(
                onClick = { isRevealed = !isRevealed },
                modifier = Modifier
                    .scale(buttonScale)
                    .width(200.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF00F5FF),
                                    Color(0xFF667eea)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            text = if (isRevealed) "HIDE DETAILS" else "REVEAL CARD",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Security badge
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF00FF00))
                )

            }
        }
    }
}

@Composable
fun CardNumberGroup(number: String, revealProgress: Float) {
    val scale by animateFloatAsState(
        targetValue = 1f + (revealProgress * 0.1f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "numberScale"
    )

    Text(
        text = number,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        letterSpacing = 2.sp,
        modifier = Modifier
            .scale(scale)
            .graphicsLayer {
                alpha = 0.5f + (revealProgress * 0.5f)
            }
    )
}

@Composable
fun AnimatedText(text: String, revealProgress: Float, fontSize: androidx.compose.ui.unit.TextUnit) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.graphicsLayer {
            alpha = 0.5f + (revealProgress * 0.5f)
        }
    )
}