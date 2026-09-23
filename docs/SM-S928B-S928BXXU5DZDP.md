# SM-S928B / S928BXXU5DZDP experimental record

This profile exists so the offline app can select the exact firmware currently
reported by the connected phone. It does not broaden S928B matching to every
6.1.145 kernel.

## Exact identity

| Field | Value |
| --- | --- |
| Model | `SM-S928B` |
| Build display | `BP4A.251205.006.S928BXXU5DZDP` |
| Fingerprint | `samsung/e3qxxx/e3q:16/BP4A.251205.006/S928BXXU5DZDP:user/release-keys` |
| Kernel release | `6.1.145-android14-11-33419968-abS928BXXU5DZDP` |
| Android / SDK | Android 16 / 36 |

## Exploit provenance and status

BuSung's tracking issue records 23/23 matching kernel symbols against DZF2,
matching BTF layouts, tracefs event ID 106, and the S928B physical RAM base.
It also records unresolved physical-page reclaim reliability and a pending
production KernelSU build.

The `keyarr/zdp-s928b-support` branch at commit
`06a9730d6d31ad27407ac9c5680dd907296a91bd` publishes a file under the DZDP
profile. Its 104,128 bytes and SHA-256 are identical to the existing BuSung
S928B DZF2 app payload:

```text
a49b378d654c7e637697a701c3c4c5fd02d22b9b30a7069c03e64ec5844af206
```

The branch does not include a distinct DZDP target source tree. Therefore this
profile remains an experimental reuse of the published S928B payload rather
than a distinct DZDP exploit port. End-to-end validation on the exact connected
device succeeded, but reclaim reliability remains probabilistic.

## Exact KernelSU Next 3.4.0 pair

The current build uses official KernelSU Next v3.4.0 commit
`1a879d6a866f80b1fa1c1009a2ffa747873cbb5e`, with the maintained Samsung
KDP/RKP/DEFEX/no-patch-text forward port. The `ksud` binary embeds this exact
module for offline late loading.

| Artifact | Size | SHA-256 |
| --- | ---: | --- |
| `kernelsu/android14-6.1_kernelsu-e3q-S928BXXU5DZDP-kdp-v3.4.0.ko` | 6,264,264 | `5010c298c5f28066df8dca7079adc99a731a3c18f3f544387c2737189bd94a27` |
| `kernelsu/ksud-e3q-S928BXXU5DZDP-kdp-v3.4.0` | 5,999,776 | `59c4ec0daf2242aa9a95f5c429682f23e7bc18d13a12b798c5890b26ea5ce9b1` |

The module reports KernelSU Next version `33294` and exact vermagic:

```text
6.1.145-android14-11-33419968-abS928BXXU5DZDP SMP preempt mod_unload modversions aarch64
```

The standard launcher bundles official manager package `com.rifsxd.ksunext`;
the spoofed launcher bundles official manager package `yhaxhr.birgvn.bmwbne`.
Both manager APKs retain the official KernelSU Next signing certificate.

Live validation on the exact DZDP phone succeeded on the second controlled
attempt after the first probabilistic reclaim attempt rebooted the device. The
spoofed manager reported `Working`, driver `v3.4.0 (33294-4)`, and manager
`v3.4.0-spoofed (33294-4)`. SELinux remained enforcing and ten consecutive
`su -c id -u` checks returned `0`.

## Live validation screenshots

The screenshots below were captured on the exact `SM-S928B / S928BXXU5DZDP`
device used for the validation described above.

### Offline launcher / exact-device match

![KernelSU Next Root launcher active on SM-S928B S928BXXU5DZDP](SM-S928B-S928BXXU5DZDP-Launcher.jpg)

The launcher reports KernelSU Next active and shows the exact firmware,
Android version, kernel release, and `arm64-v8a (4K)` ABI used by this profile.

### KernelSU Next manager

![KernelSU Next v3.4.0 spoofed manager working on SM-S928B S928BXXU5DZDP](SM-S928B-S928BXXU5DZDP-KernelSU-Next.jpg)

The spoofed manager reports `Working`, driver `v3.4.0 (33294-4)`,
`LKM (GKI2)`, `Tracepoint` hook mode, an installed metamodule, and enabled
Zygisk injection.

## Previous KernelSU 3.3.0 pair

The KernelSU module was rebuilt from upstream v3.3.0 commit
`932014ab5b2c9b74a3d11e2ec4d17dd10fc9442e` with the maintained Samsung
KDP/RKP/DEFEX/no-patch-text forward port.

| Artifact | Size | SHA-256 |
| --- | ---: | --- |
| `kernelsu/android14-6.1_kernelsu-e3q-S928BXXU5DZDP-kdp-v3.3.0.ko` | 5,849,640 | `dd677e9e9e6372009a3a8e68ca1ba0ec1c889d23a3547cd7f4c1aa2c07dd7882` |
| `kernelsu/ksud-e3q-S928BXXU5DZDP-kdp-v3.3.0` | 6,647,424 | `5686641a536ee92ab69451e025985c7be01bef2c452582fa338b276a445ee5de` |

The module reports KernelSU version `32601` and exact vermagic:

```text
6.1.145-android14-11-33419968-abS928BXXU5DZDP SMP preempt mod_unload modversions aarch64
```

It has an empty `__versions` section and uses the no-text-patching Samsung
configuration. The build and static checks passed. On the exact DZDP phone,
the clean-boot flow acquired bootstrap root on attempt 5, late-loaded the
module, reported driver `32601-2`, stayed SELinux enforcing, and passed ten
consecutive `su -c id -u` checks.
