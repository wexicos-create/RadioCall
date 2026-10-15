# Implementation Plan - AFM Secure Offline Voice & Messaging

An advanced offline communication application utilizing AM/FM radio frequency modulation simulation (AFM), ultra-low-bandwidth Opus audio codec optimization, adaptive congestion control, end-to-end encryption, and local SQLite/Room persistence, styled with a premium dark minimalist editorial design.

## User Review Required
> [!IMPORTANT]
> Please review the planned features before we proceed with code generation.
> - **AFM Radio Frequency Transceiver**: Tune frequencies (87.5 - 108.0 MHz) to connect peer devices offline with signal strength and audio telemetry.
> - **Opus Low-Bandwidth & Congestion Control**: Tuned audio parameters (6kbps-32kbps, FEC, DTX) for ultra-low bandwidth radio transmission.
> - **End-to-End Encryption**: AES-256 secure messaging and voice streaming.
> - **Local Room Database & AI**: Fully local persistence with offline AI smart indexing.
> - **Premium Editorial Dark UI**: High-contrast typography, custom waveforms, clean cards, and fluid micro-interactions.

## Proposed Changes

### 1. Project Metadata & Configuration
- Update `metadata.json` and `res/values/strings.xml` with app name "AFM Secure".
- Ensure dependencies (`room`, `coroutines`, `material3`, `compose`) are properly configured.

### 2. Data Layer & Persistence (Room)
- Create Entity models: `MessageEntity`, `ChannelEntity`, `CallLogEntity`, `AiLogEntity`.
- Create Daos and `AppDatabase`.
- Implement local repository for offline sync and data persistence.

### 3. Core Engine & Cryptography
- `AfmRadioEngine`: Manages simulated AM/FM frequency channels, signal interference, packet loss simulation, and adaptive Opus bitrate control.
- `CryptoManager`: Handles end-to-end encryption (AES-256 / RSA key pairs) for secure offline messages and voice streams.
- `AudioTransceiver`: Manages audio recording/playback with low-bandwidth Opus encoding parameters.

### 4. UI & Navigation (Jetpack Compose)
- Theme: `Theme.kt` with dark editorial palette, sleek surfaces, typography, and custom shapes.
- Screens:
  - **Dashboard / Channel Selector**: AFM Frequency dialer (87.5 - 108.0 MHz) with signal strength and active peer nodes.
  - **Secure Chat**: Threaded encrypted messaging with E2EE badge and local AI suggestions.
  - **Voice Call Screen**: Low-bandwidth audio call view with real-time audio waveform, bitrate regulator, FEC toggle, and latency monitor.
  - **Secure Vault & Logs**: Local encrypted database storage inspector & export tool.
  - **Settings & Diagnostics**: Radio bandwidth statistics, encryption keys status, and dark theme toggles.

## Verification Plan
- Run `compile_applet` to ensure zero compilation errors.
- Verify unit & Robolectric tests if applicable.
