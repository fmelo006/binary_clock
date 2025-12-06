package com.example.binaryclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import java.util.Calendar

class BinaryClockWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widgets
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        scheduleNextUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_AUTO_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget = ComponentName(context.packageName, BinaryClockWidget::class.java.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
            onUpdate(context, appWidgetManager, appWidgetIds)
            
            scheduleNextUpdate(context)
        }
    }

    private fun scheduleNextUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BinaryClockWidget::class.java).apply {
            action = ACTION_AUTO_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule for the start of the next minute
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MINUTE, 1)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun cancelUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BinaryClockWidget::class.java).apply {
            action = ACTION_AUTO_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        private const val ACTION_AUTO_UPDATE = "com.example.binaryclock.ACTION_AUTO_UPDATE"

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // 1. Get Preferences (Night Mode)
            val prefs = context.getSharedPreferences("binary_clock_prefs", Context.MODE_PRIVATE)
            val isNightMode = prefs.getBoolean("night_mode", false)

            // 2. Get Current Time
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            // Seconds are static/zeroed for battery saving as per requirements
            val second = 0 

            // 3. Prepare RemoteViews
            val views = RemoteViews(context.packageName, R.layout.widget_binary_clock)

            // 4. Click Intent -> Open MainActivity
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                .let { intent ->
                    PendingIntent.getActivity(
                        context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                }
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            
            // 5. Update Views
            val onRes = if (isNightMode) R.drawable.widget_bulb_on_neon else R.drawable.widget_bulb_on_normal
            val offRes = if (isNightMode) R.drawable.widget_bulb_off_neon else R.drawable.widget_bulb_off_normal

            // Helper to set column
            fun setColumn(digit: Int, ids: List<Int>) {
                views.setImageViewResource(ids[0], if ((digit and 8) != 0) onRes else offRes)
                views.setImageViewResource(ids[1], if ((digit and 4) != 0) onRes else offRes)
                views.setImageViewResource(ids[2], if ((digit and 2) != 0) onRes else offRes)
                views.setImageViewResource(ids[3], if ((digit and 1) != 0) onRes else offRes)
            }

            setColumn(hour / 10, listOf(R.id.h1_8, R.id.h1_4, R.id.h1_2, R.id.h1_1))
            setColumn(hour % 10, listOf(R.id.h2_8, R.id.h2_4, R.id.h2_2, R.id.h2_1))
            setColumn(minute / 10, listOf(R.id.m1_8, R.id.m1_4, R.id.m1_2, R.id.m1_1))
            setColumn(minute % 10, listOf(R.id.m2_8, R.id.m2_4, R.id.m2_2, R.id.m2_1))
            setColumn(second / 10, listOf(R.id.s1_8, R.id.s1_4, R.id.s1_2, R.id.s1_1))
            setColumn(second % 10, listOf(R.id.s2_8, R.id.s2_4, R.id.s2_2, R.id.s2_1))
            
            // Set click listener on the whole widget (using a trick if root has no ID: set on all columns)
            // But I really should give the root an ID. I'll do a quick replace on the XML first.
            // Actually, I can just use `views.setOnClickPendingIntent(android.R.id.content, pendingIntent)`? No, that's for Activity.
            // I will update the XML to add an ID to the root.
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
