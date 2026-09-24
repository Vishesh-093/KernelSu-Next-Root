# Build and reproducibility

This document separates what can currently be rebuilt from this repository from what is only provenance-pinned as a binary input.

## Android launcher

The Android application can be built from this repository with:

- JDK 21
- Android Gradle Plugin 9.2.1
- Kotlin Compose plugin 2.4.0
- compile SDK 37
- target SDK 36
- Android NDK `29.0.14206865`
- `arm64-v8a`

With dependencies already cached, the intended offline build is:

```sh
./gradlew --offline --no-configuration-cache \
  :app:testStandardDebugUnitTest \
  :app:testSpoofedDebugUnitTest \
  :app:assembleStandardRelease \
  :app:assembleSpoofedRelease
```

A clean public CI environment uses normal online dependency resolution instead of `--offline`.

## Release signing

Release APKs are signed locally. Rebuilding the same source without the same signing key will not produce a byte-for-byte identical APK, even when the code and bundled payloads are unchanged.

For that reason, users should verify published release APKs against `dist/SHA256SUMS` rather than assuming a locally rebuilt APK will have the same whole-file hash.

## Bundled manager APKs

The Standard and Spoofed flavors intentionally include official KernelSU Next manager APKs as build inputs:

```text
app/src/standard/assets/manager/KernelSUNextManager.apk
app/src/spoofed/assets/manager/KernelSUNextManager.apk
```

Their expected SHA-256 values are pinned in `app/build.gradle.kts` and checked by the application before manager installation.

## Bootstrap payload

The DZDP bootstrap payload is pinned by size and SHA-256 in `app/src/main/assets/targets-v3.json`.

Current value:

```text
SHA-256: a49b378d654c7e637697a701c3c4c5fd02d22b9b30a7069c03e64ec5844af206
Size:    104128 bytes
```

The technical record explains why this is treated as a published S928B payload reuse rather than a distinct DZDP source rebuild.

## KernelSU Next module/daemon pair

The current exact-DZDP pair is provenance-pinned:

```text
KernelSU Next source commit:
1a879d6a866f80b1fa1c1009a2ffa747873cbb5e

Module SHA-256:
5010c298c5f28066df8dca7079adc99a731a3c18f3f544387c2737189bd94a27

Daemon SHA-256:
59c4ec0daf2242aa9a95f5c429682f23e7bc18d13a12b798c5890b26ea5ce9b1
```

The module has exact DZDP vermagic:

```text
6.1.145-android14-11-33419968-abS928BXXU5DZDP SMP preempt mod_unload modversions aarch64
```

### Current limitation

The repository contains the resulting KernelSU Next module/daemon artifacts and records the upstream source commit plus Samsung-specific forward-port provenance, but it does not yet capture a complete hermetic toolchain + patchset recipe sufficient to promise byte-for-byte reproduction of those binaries from a clean machine.

Therefore the project currently claims:

- **launcher source reproducibility:** yes, subject to normal dependency/signing differences;
- **runtime artifact integrity:** pinned and verifiable by SHA-256;
- **KernelSU Next binary provenance:** documented;
- **bit-for-bit KernelSU Next module/daemon reproduction:** not yet guaranteed.

A future improvement should preserve the complete Samsung patchset, compiler/toolchain identity, build command, kernel/DDK inputs, and environment metadata needed to rebuild the pair independently.

## CI scope

CI should verify at minimum:

1. `targets-v3.json` parses and contains only the intended exact target;
2. bundled exploit and daemon hashes match the target manifest;
3. the standalone current KernelSU Next module hash matches the documented value;
4. manager APK hashes match the values embedded in the build configuration;
5. Standard and Spoofed unit tests pass;
6. both debug launcher flavors assemble successfully.

CI success is not evidence that the bootstrap exploit will succeed on hardware. Device validation remains a separate requirement.
