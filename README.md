# Neon Tetris

Neon Tetris is a polished Kotlin Multiplatform falling-block puzzle game for Android, desktop, and iOS. Its game engine, state management, Compose Multiplatform UI, procedural soundtrack, and preferences contract are shared while each target keeps a thin native host.

> This is an independent educational project and is not affiliated with or endorsed by The Tetris Company.

## Product goals

- Make every action feel immediate: movement, rotation, soft drop, hard drop, hold, pause, and restart.
- Keep the board readable under pressure with strong contrast, a restrained glow system, and clear piece silhouettes.
- Use animation to communicate state changes, never to delay input.
- Support short casual sessions and longer score-chasing runs.
- Remain playable with reduced motion, haptics disabled, or sound disabled.
- Keep gameplay deterministic and independently testable.

## Gameplay

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
- Looping procedural neon chiptune with lightweight game effects.
- Reduced-motion mode that replaces spatial effects with opacity and color feedback.

## Controls

| Action | Touch control |
| --- | --- |
| Move | Large left and right arrow buttons or horizontal swipe |
| Soft drop | Large down-arrow button or downward drag |
| Hard drop | Pink drop action or quick downward fling |
| Rotate | Rotate-left and rotate-right icon buttons or tap gesture |
| Hold | Hold button near the playfield |
| Pause | Pause button in the game HUD |

## Targets

- **Android:** production APK with native `SharedPreferences`, `AudioTrack`, effects, haptics, and lifecycle integration.
- **Desktop:** runnable Compose desktop application with persistent preferences, native Java Sound audio, and a macOS DMG distribution.
- **iOS:** shared framework target with a Compose `MainViewController`, `NSUserDefaults`, AVAudioEngine music, and native effects. Full framework linking requires Xcode.

## Architecture

The app follows a unidirectional state-flow architecture with platform services injected at the host boundary.

```text
app/
  Android activity, preferences, and audio implementations

shared/src/commonMain/
  core/game/        Pure Kotlin rules, board, pieces, scoring, timing, and reducer logic
  data/             Shared preferences state and storage contract
  audio/            Shared soundtrack synthesis and audio contract
  ui/               Compose Multiplatform screens, controls, renderer, and ViewModel
  theme/            Shared color and typography system

shared/src/androidMain/
  Android system-back integration

shared/src/desktopMain/
  Desktop window, preferences, and audio implementation

shared/src/iosMain/
  Compose UIViewController, NSUserDefaults, AVAudioEngine, and native effects
```

The engine receives explicit actions and time steps and returns immutable state. Compose renders that state, while the ViewModel owns the frame loop, lifecycle behavior, persistence coordination, and one-shot feedback events.

## Quality strategy

- **Common engine tests:** collision, spawn, movement, line clearing, bag generation, hold rules, and pause behavior on Android host and desktop targets.
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

<table>
  <tr>
    <th>Home</th>
    <th>Gameplay</th>
  </tr>
  <tr>
    <td><img src="docs/screenshots/home.png" width="280" alt="Neon Tetris home screen"></td>
    <td><img src="docs/screenshots/gameplay.png" width="280" alt="Neon Tetris gameplay screen"></td>
  </tr>
  <tr>
    <th>Pause</th>
    <th>Settings</th>
  </tr>
  <tr>
    <td><img src="docs/screenshots/paused.png" width="280" alt="Neon Tetris pause overlay"></td>
    <td><img src="docs/screenshots/settings.png" width="280" alt="Neon Tetris settings screen"></td>
  </tr>
</table>

Captured from the `Resizable_Experimental` Android emulator at 1080 x 2400 after visual and accessibility-hierarchy review.

## Verification

- Android debug APK and instrumented-test APK build successfully with Android Gradle Plugin 9.0.1.
- Six deterministic shared engine tests pass on Android host and desktop targets.
- Three production-activity Compose journeys pass on `Resizable_Experimental`.
- Shared UI and platform code compile for iOS x64, arm64, and simulator arm64 targets.
- Desktop application launches and packages as `shared/build/compose/binaries/main/dmg/Neon Tetris-1.0.0.dmg`.
- iOS framework linking is not available on this machine because full Xcode is not installed.
- The Android home screen was visually rechecked at 1080 x 2400 after migration.

## Build

```bash
./gradlew :app:assembleDebug
./gradlew :shared:desktopTest :shared:testAndroidHostTest
./gradlew :shared:run
./gradlew :shared:compileKotlinIosSimulatorArm64
```

Create the macOS DMG with a full JDK 17 that includes `jpackage`:

```bash
./gradlew :shared:packageDistributionForCurrentOS
```

The Android target uses API 36, supports Android 8.0+ (API 26), and all JVM targets use Java 17.
