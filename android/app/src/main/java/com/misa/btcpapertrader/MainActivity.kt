package com.misa.btcpapertrader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.misa.btcpapertrader.ui.screens.MainScreen
import com.misa.btcpapertrader.ui.theme.BTCPaperTraderTheme
import com.misa.btcpapertrader.ui.viewmodel.MainViewModel

/**
 * Ponto de entrada da aplicação Android BTC Paper Trader.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Resultado da permissão de notificação
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            BTCPaperTraderTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
