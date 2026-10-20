package com.misa.btcpapertrader.domain.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.misa.btcpapertrader.MainActivity
import com.misa.btcpapertrader.domain.model.TradeModel

object NotificationHelper {
    private const val CHANNEL_ID = "btc_trades_channel"
    private const val CHANNEL_NAME = "BTC Paper Trader - Alertas de Trading"
    private const val NOTIFICATION_ID = 1001

    fun showTradeNotification(context: Context, trade: TradeModel) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alertas de ordens executadas pela estratégia automática"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val title = "🤖 ${trade.tipo} DE BTC EXECUTADA!"
        val message = "Preço: ${Formatter.formatarMoeda(trade.precoBrl)} | Motivo: ${trade.motivo}"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            notificationManager.notify(NOTIFICATION_ID + (System.currentTimeMillis() % 10000).toInt(), notification)
        } catch (e: SecurityException) {
            // Permissão POST_NOTIFICATIONS não concedida
        }
    }
}
