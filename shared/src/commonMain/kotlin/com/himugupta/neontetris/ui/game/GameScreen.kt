package com.himugupta.neontetris.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.RotateLeft
import androidx.compose.material.icons.automirrored.rounded.RotateRight
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.VerticalAlignBottom
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.himugupta.neontetris.audio.GameAudio
import com.himugupta.neontetris.audio.GameSoundEffect
import com.himugupta.neontetris.audio.SilentGameAudio
import com.himugupta.neontetris.core.game.BoardHeight
import com.himugupta.neontetris.core.game.BoardWidth
import com.himugupta.neontetris.core.game.CellPosition
import com.himugupta.neontetris.core.game.GameAction
import com.himugupta.neontetris.core.game.GameState
import com.himugupta.neontetris.core.game.GameStatus
import com.himugupta.neontetris.core.game.HiddenRows
import com.himugupta.neontetris.core.game.Tetromino
import com.himugupta.neontetris.data.PlayerPreferences
import com.himugupta.neontetris.data.PreferencesRepository
import com.himugupta.neontetris.theme.DeepSpace
import com.himugupta.neontetris.theme.GridLine
import com.himugupta.neontetris.theme.InkMuted
import com.himugupta.neontetris.theme.NeonBlue
import com.himugupta.neontetris.theme.NeonCyan
import com.himugupta.neontetris.theme.NeonGreen
import com.himugupta.neontetris.theme.NeonOrange
import com.himugupta.neontetris.theme.NeonPink
import com.himugupta.neontetris.theme.NeonViolet
import com.himugupta.neontetris.theme.NeonYellow
import com.himugupta.neontetris.theme.Panel
import com.himugupta.neontetris.ui.components.NeonButton

@Composable
fun GameScreen(
  preferences: PlayerPreferences,
  preferencesRepository: PreferencesRepository,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
  gameAudio: GameAudio = SilentGameAudio,
  viewModel: GameViewModel = viewModel { GameViewModel(preferencesRepository) },
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val lifecycleOwner = LocalLifecycleOwner.current
  val hapticFeedback = LocalHapticFeedback.current
  DisposableEffect(gameAudio) { onDispose { gameAudio.setMusicPlaying(false) } }

  LaunchedEffect(preferences.soundEnabled, state.status) {
    if (preferences.soundEnabled && state.status == GameStatus.Playing) {
      gameAudio.setMusicPlaying(true)
    } else {
      gameAudio.setMusicPlaying(false)
    }
  }

  val dispatchWithFeedback: (GameAction) -> Unit = { action ->
    if (preferences.hapticsEnabled && state.status == GameStatus.Playing) {
      when (action) {
        GameAction.HardDrop -> hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        GameAction.RotateClockwise,
        GameAction.RotateCounterClockwise,
        GameAction.Hold -> hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        else -> Unit
      }
    }
    if (preferences.soundEnabled && state.status == GameStatus.Playing) {
      when (action) {
        GameAction.HardDrop -> gameAudio.playEffect(GameSoundEffect.HardDrop)
        GameAction.RotateClockwise,
        GameAction.RotateCounterClockwise -> gameAudio.playEffect(GameSoundEffect.Rotate)
        GameAction.Hold -> gameAudio.playEffect(GameSoundEffect.Hold)
        else -> Unit
      }
    }
    viewModel.dispatch(action)
  }

  LaunchedEffect(state.eventId) {
    if (preferences.hapticsEnabled && state.lastClear.isNotEmpty()) {
      hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }
    if (preferences.soundEnabled && state.lastClear.isNotEmpty()) {
      gameAudio.playEffect(GameSoundEffect.LineClear)
    }
  }

  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_STOP) viewModel.dispatch(GameAction.Pause)
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
  }

  GameContent(
    state = state,
    preferences = preferences,
    onAction = dispatchWithFeedback,
    onBack = onBack,
    modifier = modifier,
  )
}

@Composable
internal fun GameContent(
  state: GameState,
  preferences: PlayerPreferences,
  onAction: (GameAction) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier =
      modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(listOf(DeepSpace, MaterialTheme.colorScheme.background)))
        .safeDrawingPadding()
        .padding(horizontal = 12.dp, vertical = 10.dp),
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      GameHud(state, onPause = { onAction(GameAction.Pause) })
      Row(
        modifier = Modifier.fillMaxWidth().weight(1f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
      ) {
        SidePreview(label = "HOLD", piece = state.holdPiece, modifier = Modifier.width(58.dp))
        Box(
          modifier = Modifier.weight(1f).fillMaxHeight(),
          contentAlignment = Alignment.Center,
        ) {
          GameBoard(
            state = state,
            ghostEnabled = preferences.ghostEnabled,
            reducedMotion = preferences.reducedMotion,
            onAction = onAction,
            modifier = Modifier.fillMaxHeight(),
          )
        }
        Column(
          modifier = Modifier.width(58.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Text(
            "NEXT",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = InkMuted,
            style = MaterialTheme.typography.labelSmall,
          )
          state.nextPieces.take(3).forEach { MiniPiece(it) }
        }
      }
      GameControls(canHold = state.canHold, onAction = onAction)
    }

    GameOverlay(
      state = state,
      onResume = { onAction(GameAction.Resume) },
      onRestart = { onAction(GameAction.Restart) },
      onBack = onBack,
    )
  }
}

@Composable
private fun GameHud(state: GameState, onPause: () -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    HudValue("SCORE", state.score.toString(), Modifier.weight(1.5f))
    HudValue("LEVEL", state.level.toString(), Modifier.weight(1f))
    HudValue("LINES", state.lines.toString(), Modifier.weight(1f))
    IconControlButton(
      icon = Icons.Rounded.Pause,
      description = "Pause game",
      onClick = onPause,
      modifier = Modifier.size(52.dp),
      accent = NeonPink,
    )
  }
}

@Composable
private fun HudValue(label: String, value: String, modifier: Modifier = Modifier) {
  Column(
    modifier =
      modifier
        .background(Panel.copy(alpha = 0.9f), RoundedCornerShape(14.dp))
        .border(1.dp, GridLine, RoundedCornerShape(14.dp))
        .padding(horizontal = 10.dp, vertical = 8.dp),
  ) {
    Text(label, color = InkMuted, style = MaterialTheme.typography.labelSmall)
    AnimatedContent(value, label = "hudValue") { animatedValue ->
      Text(animatedValue, style = MaterialTheme.typography.titleLarge)
    }
  }
}

@Composable
private fun SidePreview(label: String, piece: Tetromino?, modifier: Modifier = Modifier) {
  Column(
    modifier =
      modifier.semantics {
        contentDescription = if (piece == null) "$label slot empty" else "$label slot: ${piece.name}"
      },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(label, color = InkMuted, style = MaterialTheme.typography.labelSmall)
    Spacer(Modifier.height(8.dp))
    AnimatedContent(piece, label = "sidePreviewPiece") { animatedPiece ->
      MiniPiece(animatedPiece)
    }
  }
}

@Composable
private fun MiniPiece(piece: Tetromino?, modifier: Modifier = Modifier) {
  Canvas(
    modifier =
      modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .background(Panel.copy(alpha = 0.78f), RoundedCornerShape(12.dp))
        .border(1.dp, GridLine.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
        .padding(6.dp),
  ) {
    piece ?: return@Canvas
    val cells = piece.cells(0)
    val minX = cells.minOf { it.x }
    val maxX = cells.maxOf { it.x }
    val minY = cells.minOf { it.y }
    val maxY = cells.maxOf { it.y }
    val cell = minOf(size.width / (maxX - minX + 1), size.height / (maxY - minY + 1)) * 0.78f
    val contentWidth = (maxX - minX + 1) * cell
    val contentHeight = (maxY - minY + 1) * cell
    val origin = Offset((size.width - contentWidth) / 2f, (size.height - contentHeight) / 2f)
    cells.forEach { block ->
      drawNeonCell(
        topLeft = origin + Offset((block.x - minX) * cell, (block.y - minY) * cell),
        cellSize = cell,
        color = piece.color(),
      )
    }
  }
}

@Composable
private fun GameBoard(
  state: GameState,
  ghostEnabled: Boolean,
  reducedMotion: Boolean,
  onAction: (GameAction) -> Unit,
  modifier: Modifier = Modifier,
) {
  val active = state.activePiece
  val animatedOrigin by animateOffsetAsState(
    targetValue = Offset(active?.x?.toFloat() ?: 0f, active?.y?.toFloat() ?: 0f),
    animationSpec = tween(if (reducedMotion) 0 else 70, easing = FastOutSlowInEasing),
    label = "piecePosition",
  )
  val clearFlash = remember { Animatable(0f) }
  var dragX by remember { mutableFloatStateOf(0f) }
  var dragY by remember { mutableFloatStateOf(0f) }

  LaunchedEffect(state.eventId) {
    if (state.lastClear.isNotEmpty()) {
      clearFlash.snapTo(1f)
      clearFlash.animateTo(0f, tween(if (reducedMotion) 0 else 260))
    }
  }

  BoxWithConstraints(
    modifier =
      modifier
        .aspectRatio(0.5f, matchHeightConstraintsFirst = true)
        .clip(RoundedCornerShape(14.dp))
        .background(Color(0xFF070C1C))
        .border(1.dp, GridLine, RoundedCornerShape(14.dp))
        .semantics { contentDescription = "Tetris playfield" }
        .pointerInput(state.status) {
          detectTapGestures(onTap = { onAction(GameAction.RotateClockwise) })
        }
        .pointerInput(state.status) {
          detectDragGestures(
            onDragStart = {
              dragX = 0f
              dragY = 0f
            },
            onDragEnd = {
              if (dragY > 120f) onAction(GameAction.HardDrop)
              dragX = 0f
              dragY = 0f
            },
          ) { change, amount ->
            change.consume()
            dragX += amount.x
            dragY += amount.y
            if (dragX > 28f) {
              onAction(GameAction.MoveRight)
              dragX = 0f
            } else if (dragX < -28f) {
              onAction(GameAction.MoveLeft)
              dragX = 0f
            }
            if (dragY > 34f && dragY < 120f) {
              onAction(GameAction.SoftDrop)
              dragY = 0f
            }
          }
        },
  ) {
    Canvas(Modifier.fillMaxSize()) {
      val cellSize = minOf(size.width / BoardWidth, size.height / (BoardHeight - HiddenRows))
      val boardWidth = cellSize * BoardWidth
      val boardHeight = cellSize * (BoardHeight - HiddenRows)
      val origin = Offset((size.width - boardWidth) / 2f, (size.height - boardHeight) / 2f)

      drawBoardGrid(origin, cellSize)
      state.board.cells.drop(HiddenRows).forEachIndexed { y, row ->
        row.forEachIndexed { x, type ->
          if (type != null) drawNeonCell(origin + Offset(x * cellSize, y * cellSize), cellSize, type.color())
        }
      }

      if (ghostEnabled) state.ghostPiece?.let { ghost ->
        ghost.blocks.filterVisible().forEach { block ->
          drawNeonCell(
            origin + Offset(block.x * cellSize, (block.y - HiddenRows) * cellSize),
            cellSize,
            ghost.type.color(),
            ghost = true,
          )
        }
      }

      active?.let { piece ->
        val localCells = piece.type.cells(piece.rotation)
        localCells.forEach { block ->
          val y = animatedOrigin.y + block.y - HiddenRows
          if (y >= 0f) {
            drawNeonCell(
              origin + Offset((animatedOrigin.x + block.x) * cellSize, y * cellSize),
              cellSize,
              piece.type.color(),
            )
          }
        }
      }

      if (clearFlash.value > 0f) {
        state.lastClear.forEach { row ->
          if (row >= HiddenRows) {
            drawRect(
              Color.White.copy(alpha = clearFlash.value * 0.8f),
              topLeft = origin + Offset(0f, (row - HiddenRows) * cellSize),
              size = Size(boardWidth, cellSize),
            )
          }
        }
      }
    }
  }
}

private fun DrawScope.drawBoardGrid(origin: Offset, cellSize: Float) {
  for (x in 0..BoardWidth) {
    val lineX = origin.x + x * cellSize
    drawLine(GridLine.copy(alpha = 0.34f), Offset(lineX, origin.y), Offset(lineX, origin.y + cellSize * 20), 1f)
  }
  for (y in 0..20) {
    val lineY = origin.y + y * cellSize
    drawLine(GridLine.copy(alpha = 0.34f), Offset(origin.x, lineY), Offset(origin.x + cellSize * BoardWidth, lineY), 1f)
  }
}

private fun DrawScope.drawNeonCell(
  topLeft: Offset,
  cellSize: Float,
  color: Color,
  ghost: Boolean = false,
) {
  val gap = cellSize * 0.1f
  val rectSize = Size(cellSize - gap * 2, cellSize - gap * 2)
  if (ghost) {
    drawRoundRect(
      color.copy(alpha = 0.5f),
      topLeft = topLeft + Offset(gap, gap),
      size = rectSize,
      cornerRadius = androidx.compose.ui.geometry.CornerRadius(cellSize * 0.14f),
      style = Stroke(width = maxOf(1.5f, cellSize * 0.07f)),
    )
    return
  }
  drawRoundRect(
    color.copy(alpha = 0.18f),
    topLeft = topLeft + Offset(gap * 0.2f, gap * 0.2f),
    size = Size(cellSize - gap * 0.4f, cellSize - gap * 0.4f),
    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cellSize * 0.2f),
  )
  drawRoundRect(
    brush = Brush.linearGradient(listOf(color.copy(alpha = 0.72f), color)),
    topLeft = topLeft + Offset(gap, gap),
    size = rectSize,
    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cellSize * 0.14f),
  )
  drawLine(
    Color.White.copy(alpha = 0.35f),
    topLeft + Offset(gap * 1.6f, gap * 1.4f),
    topLeft + Offset(cellSize - gap * 1.6f, gap * 1.4f),
    maxOf(1f, cellSize * 0.035f),
  )
}

@Composable
private fun GameControls(canHold: Boolean, onAction: (GameAction) -> Unit) {
  Column(
    modifier =
      Modifier.fillMaxWidth()
        .background(Panel.copy(alpha = 0.82f), RoundedCornerShape(24.dp))
        .border(1.dp, GridLine, RoundedCornerShape(24.dp))
        .padding(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      TextControlButton(
        label = if (canHold) "HOLD" else "USED",
        description = if (canHold) "Hold piece" else "Hold used; available after piece locks",
        onClick = { onAction(GameAction.Hold) },
        modifier = Modifier.weight(0.9f),
        accent = NeonBlue,
        height = 54.dp,
        enabled = canHold,
      )
      IconControlButton(
        icon = Icons.AutoMirrored.Rounded.RotateLeft,
        description = "Rotate counter-clockwise",
        onClick = { onAction(GameAction.RotateCounterClockwise) },
        modifier = Modifier.weight(0.72f),
        accent = NeonViolet,
        height = 54.dp,
      )
      IconControlButton(
        icon = Icons.AutoMirrored.Rounded.RotateRight,
        description = "Rotate clockwise",
        onClick = { onAction(GameAction.RotateClockwise) },
        modifier = Modifier.weight(0.72f),
        accent = NeonViolet,
        height = 54.dp,
      )
      IconControlButton(
        icon = Icons.Rounded.VerticalAlignBottom,
        label = "DROP",
        description = "Hard drop",
        onClick = { onAction(GameAction.HardDrop) },
        modifier = Modifier.weight(1.2f),
        accent = NeonPink,
        height = 54.dp,
      )
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      IconControlButton(
        icon = Icons.AutoMirrored.Rounded.ArrowBack,
        description = "Move left",
        onClick = { onAction(GameAction.MoveLeft) },
        modifier = Modifier.weight(1f),
        height = 66.dp,
      )
      IconControlButton(
        icon = Icons.Rounded.ArrowDownward,
        description = "Soft drop",
        onClick = { onAction(GameAction.SoftDrop) },
        modifier = Modifier.weight(1.25f),
        accent = NeonGreen,
        height = 66.dp,
      )
      IconControlButton(
        icon = Icons.AutoMirrored.Rounded.ArrowForward,
        description = "Move right",
        onClick = { onAction(GameAction.MoveRight) },
        modifier = Modifier.weight(1f),
        height = 66.dp,
      )
    }
  }
}

@Composable
private fun TextControlButton(
  label: String,
  description: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  accent: Color = MaterialTheme.colorScheme.primary,
  height: androidx.compose.ui.unit.Dp = 58.dp,
  enabled: Boolean = true,
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier.height(height).semantics { contentDescription = description },
    shape = RoundedCornerShape(18.dp),
    colors =
      ButtonDefaults.buttonColors(
        containerColor = accent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = accent.copy(alpha = 0.24f),
        disabledContentColor = InkMuted,
      ),
    border = BorderStroke(1.dp, accent.copy(alpha = 0.86f)),
    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp),
  ) {
    Text(label, style = MaterialTheme.typography.labelLarge, maxLines = 1, softWrap = false)
  }
}

@Composable
private fun IconControlButton(
  icon: ImageVector,
  description: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  label: String? = null,
  accent: Color = MaterialTheme.colorScheme.primary,
  height: androidx.compose.ui.unit.Dp? = null,
) {
  Button(
    onClick = onClick,
    modifier = modifier.then(if (height != null) Modifier.height(height) else Modifier).semantics {
      contentDescription = description
    },
    shape = RoundedCornerShape(18.dp),
    colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = MaterialTheme.colorScheme.onPrimary),
    border = BorderStroke(1.dp, accent.copy(alpha = 0.86f)),
    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp),
  ) {
    Icon(icon, contentDescription = null, modifier = Modifier.size(if (label == null) 30.dp else 25.dp))
    if (label != null) {
      Spacer(Modifier.width(5.dp))
      Text(label, style = MaterialTheme.typography.labelLarge)
    }
  }
}

@Composable
private fun GameOverlay(
  state: GameState,
  onResume: () -> Unit,
  onRestart: () -> Unit,
  onBack: () -> Unit,
) {
  val visible = state.status == GameStatus.Paused || state.status == GameStatus.GameOver
  AnimatedVisibility(
    visible = visible,
    enter = fadeIn() + scaleIn(initialScale = 0.94f),
    exit = fadeOut() + scaleOut(targetScale = 0.96f),
  ) {
    Box(
      Modifier.fillMaxSize().background(Color(0xD9050816)),
      contentAlignment = Alignment.Center,
    ) {
      Column(
        modifier =
          Modifier.fillMaxWidth(0.82f)
            .background(Panel, RoundedCornerShape(28.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), RoundedCornerShape(28.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Text(
          if (state.status == GameStatus.GameOver) "GAME OVER" else "PAUSED",
          style = MaterialTheme.typography.headlineMedium,
        )
        if (state.status == GameStatus.GameOver) {
          Text("Score ${state.score}  /  Lines ${state.lines}", color = InkMuted)
        }
        if (state.status == GameStatus.Paused) NeonButton("Resume", onResume)
        NeonButton("Restart", onRestart, accent = NeonViolet)
        NeonButton("Home", onBack, accent = NeonPink)
      }
    }
  }
}

private fun List<CellPosition>.filterVisible(): List<CellPosition> = filter { it.y >= HiddenRows }

private fun Tetromino.color(): Color =
  when (this) {
    Tetromino.I -> NeonCyan
    Tetromino.O -> NeonYellow
    Tetromino.T -> NeonViolet
    Tetromino.S -> NeonGreen
    Tetromino.Z -> NeonPink
    Tetromino.J -> NeonBlue
    Tetromino.L -> NeonOrange
  }
