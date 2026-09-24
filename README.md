# KernelSU Next Root — Galaxy S24 Ultra SM-S928B / DZDP

[![CI](https://github.com/Vishesh-093/KernelSu-Next-Root/actions/workflows/ci.yml/badge.svg)](https://github.com/Vishesh-093/KernelSu-Next-Root/actions/workflows/ci.yml)
[![Release](https://img.shields.io/github/v/release/Vishesh-093/KernelSu-Next-Root)](https://github.com/Vishesh-093/KernelSu-Next-Root/releases)
[![License](https://img.shields.io/github/license/Vishesh-093/KernelSu-Next-Root)](LICENSE)

An exact-device, fully offline **KernelSU Next 3.4.0 temporary-root launcher** for the Samsung Galaxy S24 Ultra `SM-S928B` running `S928BXXU5DZDP`.

> **Status:** device-tested on the exact DZDP target. Root is per boot, SELinux remains enforcing, and the boot image is not modified. The underlying bootstrap exploit is probabilistic and may require retries.

## Supported target

| Field | Required value |
| --- | --- |
| Model | `SM-S928B` |
| Build | `S928BXXU5DZDP` |
| Kernel | `6.1.145-android14-11-33419968-abS928BXXU5DZDP` |
| Android | 16 / SDK 36 |
| KernelSU Next | `3.4.0` / version code `33294` |

Matching is intentionally strict. Other models, firmware builds, and kernel releases are rejected.

## What this project does

```text
Stock boot
   ↓
Android starts normally
   ↓
Shizuku-assisted bootstrap
   ↓
CVE-2026-43499 userspace payload
   ↓
KernelSU Next module late-load
   ↓
Root verification
```

The project does **not** unlock the bootloader, patch `boot.img`, disable verified boot, or make root persistent across a full reboot.

## Release downloads

Use the GitHub Release assets rather than APK copies committed into the repository:

- [Standard launcher](https://github.com/Vishesh-093/KernelSu-Next-Root/releases/download/v3.4.0/KernelSUNextRoot-SM-S928B-DZDP-v3.4.0-standard.apk)
- [Spoofed-manager launcher](https://github.com/Vishesh-093/KernelSu-Next-Root/releases/download/v3.4.0/KernelSUNextRoot-SM-S928B-DZDP-v3.4.0-spoofed.apk)
- [SHA256SUMS](https://github.com/Vishesh-093/KernelSu-Next-Root/releases/download/v3.4.0/SHA256SUMS)

### Release hashes

```text
3cb7849e101728a7fe597cdb1da007e82ccb4ee41203a99155b09d7417f01895  KernelSUNextRoot-SM-S928B-DZDP-v3.4.0-standard.apk
52ee2b5465605d24a20e785cf811abf743a8bad4e316f6f3f9c18f0b8a3379ca  KernelSUNextRoot-SM-S928B-DZDP-v3.4.0-spoofed.apk
```

Verify before installing:

```sh
sha256sum -c SHA256SUMS
```

## Launcher variants

| Variant | Launcher package | Bundled KernelSU Next manager |
| --- | --- | --- |
| Standard | `io.github.vishesh093.ksunextroot` | `com.rifsxd.ksunext` |
| Spoofed | `io.github.vishesh093.ksunextroot.spoofed` | `yhaxhr.birgvn.bmwbne` |

Both launchers verify the bundled manager APK SHA-256 before installation. Use the manager variant that matches the launcher used for that test run.

## Usage

1. Confirm the phone is exactly `SM-S928B / S928BXXU5DZDP` with the kernel listed above.
2. Enable Developer options, USB debugging, and **Disable child process restrictions**.
3. Start Shizuku in ADB mode.
4. Open one launcher, grant Shizuku access, and run the DZDP profile.
5. Install/open the matching bundled KernelSU Next manager when prompted.
6. Treat a reboot during bootstrap as a failed probabilistic attempt; do not assume root survived a full reboot.

Use only on a device you own or are explicitly authorized to test.

## Validation

Live validation on the exact DZDP phone succeeded after a controlled retry. The validated run reported:

- KernelSU Next `Working`
- driver `v3.4.0 (33294-4)`
- SELinux `Enforcing`
- ten consecutive `su -c id -u` checks returning `0`

Screenshots, exact firmware identity, artifact hashes, vermagic, exploit provenance, and prior KernelSU 3.3.0 validation are documented in [`docs/SM-S928B-S928BXXU5DZDP.md`](docs/SM-S928B-S928BXXU5DZDP.md).

## Important implementation detail

The DZDP bootstrap payload currently reuses the published S928B DZF2 exploit binary because the published DZDP branch artifact is byte-identical to that S928B payload. This repository does **not** claim a distinct DZDP exploit-source port. Device matching, the KernelSU Next module, daemon, vermagic, packaging, and live validation are DZDP-specific.

See [`PORTING.md`](PORTING.md) before adding another firmware build.

## Build

With the Android/Gradle caches already prepared:

```sh
./gradlew --offline --no-configuration-cache \
  :app:testStandardDebugUnitTest \
  :app:testSpoofedDebugUnitTest \
  :app:assembleStandardRelease \
  :app:assembleSpoofedRelease
```

Public CI uses a normal online dependency-resolution environment and validates unit tests, debug builds, target metadata, and checked release hashes.

See [`docs/BUILD-REPRODUCIBILITY.md`](docs/BUILD-REPRODUCIBILITY.md) for reproducibility notes.

## Repository map

```text
app/                         Android launcher application
app/src/main/assets/         Exact DZDP runtime payload metadata
kernelsu/                    Exact-vermagic KernelSU Next module/daemon pair
src/targets/                 Native exploit source profiles from upstream work
artifacts/                   Published bootstrap payload provenance copies
docs/                        Validation and architecture documentation
dist/SHA256SUMS              Release checksum manifest
```

## Security and provenance

- Security policy: [`SECURITY.md`](SECURITY.md)
- Architecture: [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)
- Third-party components: [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md)
- Exact-device validation: [`docs/SM-S928B-S928BXXU5DZDP.md`](docs/SM-S928B-S928BXXU5DZDP.md)

## Credits

This project is derived from [BuSung-dev/Root-My-Galaxy](https://github.com/BuSung-dev/Root-My-Galaxy) and uses KernelSU Next from [KernelSU-Next/KernelSU-Next](https://github.com/KernelSU-Next/KernelSU-Next). Upstream authorship, licensing, artifact provenance, and local changes are documented in [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md).

Maintained by **Vishesh ([Vishesh-093](https://github.com/Vishesh-093))**.
