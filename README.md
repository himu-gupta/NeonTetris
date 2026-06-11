# Neon Tetris

Neon Tetris is a polished falling-block puzzle game for Android, built with Kotlin and Jetpack Compose. It combines guideline-inspired gameplay, responsive touch controls, a neon arcade visual system, and purposeful motion without coupling the game rules to the UI.

> This is an independent educational project and is not affiliated with or endorsed by The Tetris Company.

## Product goals

- Make every action feel immediate: movement, rotation, soft drop, hard drop, hold, pause, and restart.
- Keep the board readable under pressure with strong contrast, a restrained glow system, and clear piece silhouettes.
- Use animation to communicate state changes, never to delay input.
- Support short casual sessions and longer score-chasing runs.
- Remain playable with reduced motion, haptics disabled, or sound disabled.
- Keep gameplay deterministic and independently testable.

## Planned gameplay

- Standard 10 x 20 visible playfield with hidden spawn rows.
- Seven tetrominoes generated through a deterministic 7-bag randomizer.
- Clockwise and counter-clockwise rotation with wall-kick support.
- Soft drop, hard drop, ghost piece, hold slot, and three-piece preview queue.
- Lock delay with reset limits to keep movement responsive without allowing infinite stalling.
- Single, double, triple, and four-line clears.
- Level progression, increasing gravity, score multipliers, combos, and back-to-back bonuses.
- Pause, resume, restart confirmation, game-over summary, and quick replay.
- Persisted high score and player preferences.

## UI and motion

- Edge-to-edge, portrait-first Compose layout that adapts to resizable screens.
- Neon-on-midnight design system with distinct, accessible tetromino colors.
- Animated piece translation and rotation with input-first timing.
- Ghost-piece fade, landing pulse, line-clear flash and collapse, score popups, combo treatment, and level-up transition.
- Animated home, pause, and game-over surfaces with consistent button press states.
- Optional haptic feedback for rotate, hard drop, line clear, and game over.
- Reduced-motion mode that replaces spatial effects with opacity and color feedback.

## Controls

| Action | Touch control |
| --- | --- |
| Move | Left and right buttons or horizontal swipe |
| Soft drop | Down button or downward drag |
| Hard drop | Dedicated drop button or quick downward fling |
| Rotate | Clockwise and counter-clockwise buttons or tap gesture |
| Hold | Hold button near the playfield |
| Pause | Pause button in the game HUD |

## Architecture

The app follows a single-activity, unidirectional state-flow architecture.

```text
app/
  core/game/        Pure Kotlin board, pieces, rotation, scoring, timing, and reducer logic
  data/             Settings and high-score persistence
  ui/home/          Start screen and first-run guidance
  ui/game/          Game route, ViewModel, board renderer, HUD, controls, and overlays
  ui/settings/      Motion, sound, haptic, and ghost-piece preferences
  ui/components/    Shared arcade controls and surfaces
  theme/            Color, typography, shape, spacing, and motion tokens
```

The engine receives explicit actions and time steps and returns immutable state. Compose renders that state, while the ViewModel owns the frame loop, lifecycle behavior, persistence coordination, and one-shot feedback events.

## Quality strategy

- **Engine unit tests:** collision, spawn, movement, rotation kicks, line clearing, bag generation, hold rules, scoring, combos, levels, lock delay, and game over.
- **ViewModel tests:** frame progression, pause/resume, settings application, restart, and persisted score updates.
- **Compose tests:** home-to-game flow, controls, pause overlay, settings, and game-over actions.
- **Device verification:** build and install the debug APK, inspect the UI hierarchy, exercise a gameplay journey, rotate/resize where relevant, and visually review captured screenshots.
- **Performance checks:** avoid per-frame allocation in board rendering, keep animation state bounded, and verify input remains responsive during line-clear effects.

## Delivery checkpoints

1. Project scaffold, README, Git initialization, and GitHub repository.
2. Architecture, navigation foundation, and neon design system.
3. Deterministic game engine with focused unit tests.
4. Responsive gameplay UI, HUD, previews, and touch controls.
5. Motion, haptics, visual feedback, and reduced-motion behavior.
6. Persistence, settings, accessibility, and lifecycle handling.
7. Full build, test, emulator, and gameplay verification.
8. Final screenshots, README update, and release-ready repository state.

Each checkpoint is committed and pushed independently so the repository history documents the implementation sequence.

## Screenshots

Emulator captures will be added here after the verified gameplay build is complete.

## Build

```bash
./gradlew :app:assembleDebug
```

The project targets Android 16 (API 36), supports Android 8.0+ (API 26), and uses Java 17.
