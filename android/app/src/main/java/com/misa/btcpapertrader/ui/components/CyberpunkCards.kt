package com.misa.btcpapertrader.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.misa.btcpapertrader.domain.model.CashFlowModel
import com.misa.btcpapertrader.domain.model.CotacaoModel
import com.misa.btcpapertrader.domain.model.EstadoModel
import com.misa.btcpapertrader.domain.model.PricePoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.misa.btcpapertrader.domain.model.TradeModel
import com.misa.btcpapertrader.domain.util.Formatter
import com.misa.btcpapertrader.domain.util.TradingMetrics
import com.misa.btcpapertrader.ui.theme.CyberpunkColors

/**
 * Card com cotação atual do BTC e origem do preço.
 */
@Composable
fun CardCotacao(
    cotacao: CotacaoModel?,
    isLoading: Boolean,
    onRefreshClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberpunkColors.BgCard),
        border = BorderStroke(1.dp, CyberpunkColors.VerdeEscuro),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "► BTC / BRL (SPOT)",
                    color = CyberpunkColors.TextoSecundario,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                OutlinedButton(
                    onClick = onRefreshClick,
                    enabled = !isLoading,
                    border = BorderStroke(1.dp, CyberpunkColors.VerdeMatrix),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberpunkColors.VerdeMatrix),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (isLoading) "ATUALIZANDO..." else "ATUALIZAR",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (cotacao != null) {
                Text(
                    text = Formatter.formatarMoeda(cotacao.precoBrl),
                    color = CyberpunkColors.VerdeNeon,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Fonte: ${cotacao.fonte} ",
                        color = CyberpunkColors.TextoSecundario,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = if (cotacao.atual) "[ONLINE]" else "[CACHE]",
                        color = if (cotacao.atual) CyberpunkColors.VerdeMatrix else CyberpunkColors.AmareloPastel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            } else {
                Text(
                    text = "Conectando ao mercado...",
                    color = CyberpunkColors.TextoApagado,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * Card de resumo do portfólio e saldos.
 */
@Composable
fun CardResumoPortfolio(
    estado: EstadoModel,
    cotacaoAtual: Double?
) {
    val btcValorBrl = (cotacaoAtual ?: 0.0) * estado.capitalBtc
    val patrimonioTotal = estado.capitalBrl + btcValorBrl
    val lucroTotal = if (estado.patrimonioInicialBrl > 0) patrimonioTotal - estado.patrimonioInicialBrl else 0.0
    val lucroPct = if (estado.patrimonioInicialBrl > 0) (lucroTotal / estado.patrimonioInicialBrl) * 100 else 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberpunkColors.BgSecundario),
        border = BorderStroke(1.dp, CyberpunkColors.VerdeMatrix),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "► PATRIMÔNIO TOTAL",
                color = CyberpunkColors.TextoSecundario,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = Formatter.formatarMoeda(patrimonioTotal),
                color = CyberpunkColors.TextoPrimario,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            if (estado.patrimonioInicialBrl > 0) {
                Text(
                    text = "Resultado: ${Formatter.formatarMoeda(lucroTotal, comSinal = true)} (${Formatter.formatarPorcentagem(lucroPct)})",
                    color = if (lucroTotal >= 0) CyberpunkColors.Lucro else CyberpunkColors.Prejuizo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Caixa (BRL)", color = CyberpunkColors.TextoSecundario, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Text(Formatter.formatarMoeda(estado.capitalBrl), color = CyberpunkColors.TextoPrimario, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Posição BTC", color = CyberpunkColors.TextoSecundario, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Text(Formatter.formatarBtc(estado.capitalBtc), color = CyberpunkColors.TextoPrimario, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }

            if (estado.precoMedioBrl > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Preço Médio BRL:", color = CyberpunkColors.TextoSecundario, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Text(Formatter.formatarMoeda(estado.precoMedioBrl), color = CyberpunkColors.AmareloPastel, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Card informativo das regras da estratégia automática.
 */
@Composable
fun CardEstrategiaStatus(estado: EstadoModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberpunkColors.BgCard),
        border = BorderStroke(1.dp, CyberpunkColors.VerdeEscuro),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "► ESTRATÉGIA AUTOMÁTICA",
                color = CyberpunkColors.VerdeMatrix,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text("• Compra Extra: R$ 5.000 se o preço cair 2.0%", color = CyberpunkColors.TextoSecundario, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            Text("• Take-Profit: Vende 50% se subir 3.0%", color = CyberpunkColors.TextoSecundario, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            Text("• Stop-Loss: Vende 50% se cair 10.0%", color = CyberpunkColors.TextoSecundario, fontSize = 11.sp, fontFamily = FontFamily.Monospace)

            if (estado.takeProfitExecutado || estado.stopLossExecutado) {
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    if (estado.takeProfitExecutado) {
                        Text("[TAKE-PROFIT EXECUTADO] ", color = CyberpunkColors.VerdeNeon, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                    if (estado.stopLossExecutado) {
                        Text("[STOP-LOSS EXECUTADO]", color = CyberpunkColors.Vermelho, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

/**
 * Card com métricas de desempenho e risco (Win Rate, Profit Factor, P&L FIFO).
 */
@Composable
fun CardMetricasRisco(metricas: TradingMetrics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberpunkColors.BgCard),
        border = BorderStroke(1.dp, CyberpunkColors.VerdeEscuro),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "► MÉTRICAS E DESEMPENHO (FIFO)",
                color = CyberpunkColors.VerdePastel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Win Rate", color = CyberpunkColors.TextoSecundario, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = if (metricas.winRatePct != null) Formatter.formatarPorcentagem(metricas.winRatePct, false) else "N/A",
                        color = CyberpunkColors.TextoPrimario,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Column {
                    Text("Profit Factor", color = CyberpunkColors.TextoSecundario, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = when {
                            metricas.profitFactor == null -> "N/A"
                            metricas.profitFactor.isInfinite() -> "∞"
                            else -> String.format("%.2f", metricas.profitFactor)
                        },
                        color = CyberpunkColors.TextoPrimario,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Column {
                    Text("P&L Realizado", color = CyberpunkColors.TextoSecundario, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        text = Formatter.formatarMoeda(metricas.totalPnlRealizado, comSinal = true),
                        color = if (metricas.totalPnlRealizado >= 0) CyberpunkColors.Lucro else CyberpunkColors.Prejuizo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

/**
 * Item individual da lista de trades.
 */
@Composable
fun ItemTrade(trade: TradeModel) {
    val isCompra = trade.tipo.uppercase() == "COMPRA"
    val corTipo = if (isCompra) CyberpunkColors.VerdeNeon else CyberpunkColors.RosaPastel

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CyberpunkColors.BgSecundario),
        border = BorderStroke(1.dp, CyberpunkColors.BgDestaque),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row {
                    Text(
                        text = "[${trade.tipo}] ",
                        color = corTipo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = trade.motivo,
                        color = CyberpunkColors.TextoSecundario,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = Formatter.formatarDataHora(trade.timestamp),
                    color = CyberpunkColors.TextoApagado,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = Formatter.formatarBtc(trade.quantidadeBtc),
                    color = CyberpunkColors.TextoPrimario,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "@ ${Formatter.formatarMoeda(trade.precoBrl)}",
                    color = CyberpunkColors.TextoSecundario,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * Dialog genérico para inserção de valor monetário ou quantidade.
 */
@Composable
fun DialogEntradaValor(
    titulo: String,
    labelCampo: String,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var textValue by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberpunkColors.BgCard,
        titleContentColor = CyberpunkColors.VerdeMatrix,
        title = {
            Text(titulo, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = textValue,
                    onValueChange = {
                        textValue = it
                        errorText = null
                    },
                    label = { Text(labelCampo, color = CyberpunkColors.TextoSecundario) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberpunkColors.VerdeMatrix,
                        unfocusedBorderColor = CyberpunkColors.TextoApagado,
                        focusedTextColor = CyberpunkColors.TextoPrimario
                    )
                )
                if (errorText != null) {
                    Text(
                        text = errorText!!,
                        color = CyberpunkColors.Vermelho,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val doubleValue = textValue.replace(",", ".").toDoubleOrNull()
                    if (doubleValue != null && doubleValue > 0) {
                        onConfirm(doubleValue)
                    } else {
                        errorText = "Digite um valor numérico válido e maior que zero."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberpunkColors.VerdeMatrix, contentColor = CyberpunkColors.BgPrincipal)
            ) {
                Text("CONFIRMAR", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = CyberpunkColors.TextoSecundario, fontFamily = FontFamily.Monospace)
            }
        }
    )
}

/**
 * Card com o gráfico de cotação ao longo do tempo (Estilo Nubank + Cyberpunk).
 */
@Composable
fun CardGraficoCotacao(
    pricePoints: List<PricePoint>,
    timeFrame: String,
    onTimeFrameSelected: (String) -> Unit,
    selectedPoint: PricePoint?,
    onSelectedPointChanged: (PricePoint?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberpunkColors.BgCard),
        border = BorderStroke(1.dp, CyberpunkColors.VerdeEscuro),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "► COTAÇÃO HISTÓRICA",
                    color = CyberpunkColors.TextoSecundario,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )

                // Timeframe Selector Buttons (1D, 1S, 1M, 1A, Tudo)
                val timeFrames = listOf("1D", "1S", "1M", "1A", "Tudo")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    timeFrames.forEach { tf ->
                        val selected = tf == timeFrame
                        Button(
                            onClick = { onTimeFrameSelected(tf) },
                            modifier = Modifier.height(28.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) CyberpunkColors.VerdeEscuro else CyberpunkColors.BgSecundario,
                                contentColor = if (selected) CyberpunkColors.VerdeMatrix else CyberpunkColors.TextoApagado
                            ),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = tf,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val activePrice = selectedPoint?.priceBrl ?: pricePoints.lastOrNull()?.priceBrl ?: 0.0
            val firstPrice = pricePoints.firstOrNull()?.priceBrl ?: activePrice
            val variacao = if (firstPrice > 0) ((activePrice - firstPrice) / firstPrice) * 100 else 0.0
            val isPositive = variacao >= 0
            val dateLabel = selectedPoint?.let { formatChartDate(it.timestamp, timeFrame) } ?: getPeriodLabel(timeFrame)

            // Nubank-style header: Price and Variation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = Formatter.formatarMoeda(activePrice),
                        color = CyberpunkColors.TextoPrimario,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = dateLabel,
                        color = CyberpunkColors.TextoApagado,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (pricePoints.size >= 2) {
                    Text(
                        text = "${if (isPositive) "+" else ""}${Formatter.formatarPorcentagem(variacao)}",
                        color = if (isPositive) CyberpunkColors.Lucro else CyberpunkColors.Prejuizo,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (pricePoints.size < 2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Carregando histórico do mercado...",
                        color = CyberpunkColors.TextoApagado,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            } else {
                val minP = pricePoints.minOfOrNull { it.priceBrl } ?: 0.0
                val maxP = pricePoints.maxOfOrNull { it.priceBrl } ?: 0.0
                val rangeP = if (maxP > minP) maxP - minP else 1.0
                val chartColor = if (isPositive) CyberpunkColors.VerdeNeon else CyberpunkColors.Prejuizo

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Máx: ${Formatter.formatarMoeda(maxP)}",
                        color = CyberpunkColors.TextoSecundario,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Mín: ${Formatter.formatarMoeda(minP)}",
                        color = CyberpunkColors.TextoSecundario,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(CyberpunkColors.BgSecundario, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(134.dp)
                            .pointerInput(pricePoints) {
                                detectTapGestures(
                                    onPress = { offset ->
                                        val index = ((offset.x / size.width) * pricePoints.size).toInt().coerceIn(0, pricePoints.size - 1)
                                        onSelectedPointChanged(pricePoints[index])
                                        tryAwaitRelease()
                                        onSelectedPointChanged(null)
                                    }
                                )
                            }
                            .pointerInput(pricePoints) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        val index = ((offset.x / size.width) * pricePoints.size).toInt().coerceIn(0, pricePoints.size - 1)
                                        onSelectedPointChanged(pricePoints[index])
                                    },
                                    onDrag = { change, _ ->
                                        val index = ((change.position.x / size.width) * pricePoints.size).toInt().coerceIn(0, pricePoints.size - 1)
                                        onSelectedPointChanged(pricePoints[index])
                                    },
                                    onDragEnd = {
                                        onSelectedPointChanged(null)
                                    },
                                    onDragCancel = {
                                        onSelectedPointChanged(null)
                                    }
                                )
                            }
                    ) {
                        val width = size.width
                        val height = size.height
                        val stepX = if (pricePoints.size > 1) width / (pricePoints.size - 1) else width

                        // Draw grid lines
                        val gridLines = 4
                        for (i in 0..gridLines) {
                            val y = (height / gridLines) * i
                            drawLine(
                                color = CyberpunkColors.BgDestaque,
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                strokeWidth = 1f
                            )
                        }

                        val canvasPoints = pricePoints.mapIndexed { index, pt ->
                            val x = index * stepX
                            val normalizedY = ((pt.priceBrl - minP) / rangeP).toFloat()
                            val y = height - (normalizedY * (height * 0.8f) + height * 0.1f)
                            Offset(x, y)
                        }

                        if (canvasPoints.isNotEmpty()) {
                            // Draw area fill gradient
                            val fillPath = Path().apply {
                                moveTo(canvasPoints.first().x, height)
                                canvasPoints.forEach { lineTo(it.x, it.y) }
                                lineTo(canvasPoints.last().x, height)
                                close()
                            }
                            drawPath(
                                path = fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(chartColor.copy(alpha = 0.35f), Color.Transparent),
                                    startY = 0f,
                                    endY = height
                                )
                            )

                            // Draw line path
                            val linePath = Path().apply {
                                moveTo(canvasPoints.first().x, canvasPoints.first().y)
                                for (i in 1 until canvasPoints.size) {
                                    lineTo(canvasPoints[i].x, canvasPoints[i].y)
                                }
                            }
                            drawPath(
                                path = linePath,
                                color = chartColor,
                                style = Stroke(width = 3f)
                            )

                            // If a point is selected, draw crosshair and highlight
                            selectedPoint?.let { sel ->
                                val selIndex = pricePoints.indexOf(sel)
                                if (selIndex in canvasPoints.indices) {
                                    val pt = canvasPoints[selIndex]
                                    // Vertical line
                                    drawLine(
                                        color = CyberpunkColors.TextoSecundario,
                                        start = Offset(pt.x, 0f),
                                        end = Offset(pt.x, height),
                                        strokeWidth = 1.5f
                                    )
                                    // Circle highlight
                                    drawCircle(
                                        color = CyberpunkColors.VerdeMatrix,
                                        radius = 5f,
                                        center = pt
                                    )
                                    drawCircle(
                                        color = CyberpunkColors.TextoPrimario,
                                        radius = 2.5f,
                                        center = pt
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatChartDate(timestamp: Long, timeFrame: String): String {
    val date = Date(timestamp)
    val sdf = when (timeFrame) {
        "1D" -> SimpleDateFormat("HH:mm", Locale("pt", "BR"))
        "1S", "1M" -> SimpleDateFormat("dd/MM HH:mm", Locale("pt", "BR"))
        else -> SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    }
    return sdf.format(date)
}

private fun getPeriodLabel(timeFrame: String): String {
    return when (timeFrame) {
        "1D" -> "Últimas 24 horas"
        "1S" -> "Última semana"
        "1M" -> "Último mês"
        "1A" -> "Último ano"
        "Tudo" -> "Histórico completo"
        else -> ""
    }
}
