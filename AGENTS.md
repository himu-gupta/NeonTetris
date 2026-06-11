# Repository guidance

- Keep gameplay rules Android-free under `core/game`.
- Render immutable `GameState` in Compose and route player input through `GameAction`.
- Preserve edge-to-edge inset handling on every screen.
- Add focused engine tests for rule changes and an instrumented journey test for navigation or interaction changes.
- See [docs/testing.md](docs/testing.md) for verification commands and the emulator checklist.
