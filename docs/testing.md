# Testing strategy

Neon Tetris keeps game rules in pure Kotlin and reserves device tests for navigation, rendering, accessibility semantics, and lifecycle behavior.

## Local tests

`GameEngineTest` covers the deterministic 7-bag, hard drop and locking, hold limits, row clearing, boundary collision, and pause behavior.

```bash
./gradlew :app:testDebugUnitTest
```

## Instrumented UI tests

`NeonTetrisJourneyTest` launches the production activity and verifies the home-to-game, pause/resume, settings, and back-stack journeys.

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Build verification

```bash
./gradlew :app:assembleDebug
```

## Manual emulator journey

1. Launch the app and verify the home screen remains clear of system bars.
2. Open Settings and toggle ghost, haptics, sound, and reduced motion.
3. Start a game and exercise movement, both rotations, hold, soft drop, hard drop, pause, restart, and Home.
4. Confirm the playfield, preview queue, hold slot, HUD, overlays, and touch targets remain visible.
5. Capture the home, gameplay, pause, and settings states for visual review.
