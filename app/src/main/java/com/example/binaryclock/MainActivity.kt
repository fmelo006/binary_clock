package com.example.binaryclock

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.binaryclock.ui.theme.BinaryClockTheme
import kotlinx.coroutines.delay
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 3. Keep Screen On
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            BinaryClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black // Deep black background
                ) {
                    BinaryClockApp()
                }
            }
        }
    }
}

@Composable
fun BinaryClockApp() {
    val context = LocalContext.current
    // 4. Persistence
    val prefs = remember { context.getSharedPreferences("binary_clock_prefs", Context.MODE_PRIVATE) }
    
    var isNightMode by remember { 
        mutableStateOf(prefs.getBoolean("night_mode", false)) 
    }

    fun setNightMode(enabled: Boolean) {
        isNightMode = enabled
        prefs.edit().putBoolean("night_mode", enabled).apply()
    }

    BinaryClockScreen(
        isNightMode = isNightMode,
        onModeChange = { setNightMode(it) }
    )
}

@Composable
fun BinaryClockScreen(
    isNightMode: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }
    var showMenu by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            delay(1000)
        }
    }

    // 2. Menu Auto-hide
    LaunchedEffect(showMenu) {
        if (showMenu) {
            delay(5000)
            showMenu = false
        }
    }

    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
    val minute = currentTime.get(Calendar.MINUTE)
    val second = currentTime.get(Calendar.SECOND)

    Box(modifier = Modifier.fillMaxSize()) {
        
        // Main Clock Content
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isNightMode) {
                Text(
                    text = "Binary Clock",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // 1. Lógica do Relógio (BCD) - 6 Columns
                // Hours
                BinaryDigitColumn(digit = hour / 10, isNightMode = isNightMode)
                BinaryDigitColumn(digit = hour % 10, isNightMode = isNightMode)

                Spacer(modifier = Modifier.width(16.dp))

                // Minutes
                BinaryDigitColumn(digit = minute / 10, isNightMode = isNightMode)
                BinaryDigitColumn(digit = minute % 10, isNightMode = isNightMode)

                Spacer(modifier = Modifier.width(16.dp))

                // Seconds
                BinaryDigitColumn(digit = second / 10, isNightMode = isNightMode)
                BinaryDigitColumn(digit = second % 10, isNightMode = isNightMode)

                Spacer(modifier = Modifier.width(16.dp))

                // Row Labels (Powers of 2)
                if (!isNightMode) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PowerOfTwoLabel(text = "8")
                        PowerOfTwoLabel(text = "4")
                        PowerOfTwoLabel(text = "2")
                        PowerOfTwoLabel(text = "1")
                        Spacer(modifier = Modifier.height(30.dp)) 
                    }
                }
            }
        }

        // 2. Invisible Clickable Area (Top 20%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { showMenu = true }
        )

        // 2. Menu Overlay
        AnimatedVisibility(
            visible = showMenu,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF222222)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Modo Normal",
                        color = if (!isNightMode) Color.Cyan else Color.Gray,
                        modifier = Modifier
                            .clickable { 
                                onModeChange(false)
                                showMenu = false // Optional: close on select
                            }
                            .padding(8.dp)
                    )
                    Text(
                        text = "Modo Noturno",
                        color = if (isNightMode) Color.Cyan else Color.Gray,
                        modifier = Modifier
                            .clickable { 
                                onModeChange(true)
                                showMenu = false 
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BinaryDigitColumn(digit: Int, isNightMode: Boolean) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bits: 8, 4, 2, 1 (Top to Bottom)
        // Prompt: "leitura é feita de baixo para cima (valores: 1, 2, 4, 8)"
        // This means Bottom is 1, Top is 8.
        // In a Column, the first item is Top. So Top=8.
        BinaryCircle(isOn = (digit and 8) != 0, isNightMode = isNightMode)
        BinaryCircle(isOn = (digit and 4) != 0, isNightMode = isNightMode)
        BinaryCircle(isOn = (digit and 2) != 0, isNightMode = isNightMode)
        BinaryCircle(isOn = (digit and 1) != 0, isNightMode = isNightMode)

        // Decimal Digit Label
        if (!isNightMode) {
            Text(
                text = digit.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.height(30.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun BinaryCircle(isOn: Boolean, isNightMode: Boolean) {
    val size = 40.dp
    // 5. Visuals: Neon Blue
    val neonBlue = Color(0xFF00E5FF) 
    val offColor = Color(0xFF111111) // Very dark gray for off state

    Box(
        modifier = Modifier
            .size(size)
            .background(
                color = if (isOn) neonBlue else offColor,
                shape = CircleShape
            )
            .border(
                width = if (isOn) 0.dp else 1.dp,
                color = if (isOn) Color.Transparent else Color.DarkGray,
                shape = CircleShape
            )
    )
}

@Composable
fun PowerOfTwoLabel(text: String) {
    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}



