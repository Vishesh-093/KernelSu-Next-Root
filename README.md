# KernelSU Next Root — SM-S928B DZDP Offline

An exact-device, fully offline KernelSU Next 3.4.0 build for the Galaxy S24
Ultra `SM-S928B` running `S928BXXU5DZDP`.

This `kernelsu-next` branch was derived from Root My Galaxy and rebranded with
a red Material 3 theme. It contains two launchers that can coexist:

| Variant | Launcher package | Bundled KernelSU Next manager |
| --- | --- | --- |
| Standard | `io.github.vishesh093.ksunextroot` | `com.rifsxd.ksunext` |
| Spoofed | `io.github.vishesh093.ksunextroot.spoofed` | `yhaxhr.birgvn.bmwbne` |

Both manager APKs are the official KernelSU Next v3.4.0 release variants and
retain the official signing certificate. The launchers verify the bundled
manager SHA-256 before installation.

## Exact target

| Field | Required value |
| --- | --- |
| Model | `SM-S928B` |
| Build | `S928BXXU5DZDP` |
| Kernel | `6.1.145-android14-11-33419968-abS928BXXU5DZDP` |
| KernelSU Next | `3.4.0` / version code `33294` |

Matching is intentionally strict. Other models, firmware builds, and kernel
releases are rejected. Root is per boot; this does not modify the boot image.
The exploit remains probabilistic because its physical-page reclaim step can
require retries.

Use this only on a device you own or are explicitly authorized to test.

## Offline release APKs

```text
dist/KernelSUNextRoot-SM-S928B-DZDP-v3.4.0-standard.apk
dist/KernelSUNextRoot-SM-S928B-DZDP-v3.4.0-spoofed.apk
```

The two release launchers are signed locally and include all runtime payloads;
they do not download the exploit, module, daemon, or manager.

The spoofed flavor has been validated live on the exact phone: KernelSU Next
reported `Working`, driver `v3.4.0 (33294-4)`, SELinux stayed enforcing, and
ten consecutive root checks passed.

## Build offline

With the Android/Gradle caches already prepared:

```sh
./gradlew --offline --no-configuration-cache :app:testStandardDebugUnitTest :app:testSpoofedDebugUnitTest
./gradlew --offline --no-configuration-cache :app:assembleStandardRelease :app:assembleSpoofedRelease
```

Release lint is disabled because its standalone Gradle artifact is not present
in the offline cache. Runtime unit tests and release assembly are still run.

## Important files

```text
app/src/main/assets/targets-v3.json
app/src/main/assets/e3q-S928BXXU5DZDP/cve-2026-43499-app.so
app/src/main/assets/e3q-S928BXXU5DZDP/ksud-e3q-S928BXXU5DZDP-kdp-v3.4.0
app/src/standard/assets/manager/KernelSUNextManager.apk
app/src/spoofed/assets/manager/KernelSUNextManager.apk
kernelsu/android14-6.1_kernelsu-e3q-S928BXXU5DZDP-kdp-v3.4.0.ko
kernelsu/ksud-e3q-S928BXXU5DZDP-kdp-v3.4.0
docs/SM-S928B-S928BXXU5DZDP.md
```

## Usage

1. Enable Developer options, USB debugging, and **Disable child process
   restrictions**.
2. Start Shizuku in ADB mode.
3. Open either launcher, grant Shizuku access, and run the exact DZDP profile.
4. Install/open the matching bundled manager when prompted.

Do not install both manager variants for the same test run. Keep both launcher
APKs if desired, but use the standard manager with the standard launcher or
the spoofed manager with the spoofed launcher.

## Credits

Derived from [BuSung-dev/Root-My-Galaxy](https://github.com/BuSung-dev/Root-My-Galaxy).
Kernel root support comes from
[KernelSU-Next/KernelSU-Next](https://github.com/KernelSU-Next/KernelSU-Next).
The exact DZDP profile provenance and validation record are documented in
[`docs/SM-S928B-S928BXXU5DZDP.md`](docs/SM-S928B-S928BXXU5DZDP.md).
