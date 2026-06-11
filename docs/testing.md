# Testing strategy

Neon Tetris keeps game rules and UI state in shared Kotlin. Device tests remain focused on Android navigation, rendering, accessibility semantics, and lifecycle behavior.

## Local tests

`GameEngineTest` covers the deterministic 7-bag, hard drop and locking, hold limits, row clearing, boundary collision, and pause behavior on multiple targets.

```bash
./gradlew :shared:desktopTest :shared:testAndroidHostTest
```

## Instrumented UI tests

`NeonTetrisJourneyTest` launches the production activity and verifies the home-to-game, pause/resume, settings, and back-stack journeys.

```bash
ANDROID_SERIAL=<resizable-serial> ./gradlew :app:connectedDebugAndroidTest
```

## Build verification

```bash
./gradlew :app:assembleDebug
./gradlew :shared:compileKotlinDesktop
./gradlew :shared:compileKotlinIosX64 :shared:compileKotlinIosArm64 :shared:compileKotlinIosSimulatorArm64
```

Linking an iOS framework additionally requires a full Xcode installation and active Xcode developer directory.

## Desktop verification

```bash
./gradlew :shared:run
./gradlew :shared:packageDistributionForCurrentOS
```

Native desktop packaging requires a JDK with `jpackage`.

## Manual emulator journey

1. Confirm the selected serial belongs to `Resizable_Experimental`, then launch the app and verify the home screen remains clear of system bars.
2. Open Settings and toggle ghost, haptics, sound, and reduced motion.
3. Start a game and exercise movement, both rotations, hold, soft drop, hard drop, pause, restart, and Home.
4. Confirm the playfield, preview queue, hold slot, HUD, overlays, and touch targets remain visible.
5. Capture the home, gameplay, pause, and settings states for visual review.
