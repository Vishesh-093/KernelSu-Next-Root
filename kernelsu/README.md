# Samsung KernelSU late-load artifacts

This directory stores standalone KernelSU/KernelSU Next module and daemon artifacts used for auditing and exact-firmware packaging. Files from different firmware builds are **not interchangeable**.

## Current active DZDP pair

The current application release uses **KernelSU Next v3.4.0** for:

```text
Model:          SM-S928B
Build:          S928BXXU5DZDP
Kernel release: 6.1.145-android14-11-33419968-abS928BXXU5DZDP
KMI:            android14-6.1
```

| File | Size | SHA-256 |
| --- | ---: | --- |
| `android14-6.1_kernelsu-e3q-S928BXXU5DZDP-kdp-v3.4.0.ko` | 6,264,264 | `5010c298c5f28066df8dca7079adc99a731a3c18f3f544387c2737189bd94a27` |
| `ksud-e3q-S928BXXU5DZDP-kdp-v3.4.0` | 5,999,776 | `59c4ec0daf2242aa9a95f5c429682f23e7bc18d13a12b798c5890b26ea5ce9b1` |

The module reports KernelSU Next version `33294` and exact vermagic:

```text
6.1.145-android14-11-33419968-abS928BXXU5DZDP SMP preempt mod_unload modversions aarch64
```

The paired `ksud` embeds the matching module for late loading. The app uses the daemon from `app/src/main/assets/e3q-S928BXXU5DZDP/` and retains the standalone `.ko` here for auditability.

The pair is based on KernelSU Next v3.4.0 source commit:

```text
1a879d6a866f80b1fa1c1009a2ffa747873cbb5e
```

with the maintained Samsung KDP/RKP/DEFEX/no-patch-text forward-port described in the exact-device validation record.

## Live validation

On the exact DZDP device, the successful validation run reported:

- KernelSU Next `Working`
- driver `v3.4.0 (33294-4)`
- SELinux `Enforcing`
- ten consecutive root checks returning uid `0`

The bootstrap stage remains probabilistic; a prior controlled attempt rebooted before the later successful run.

See [`../docs/SM-S928B-S928BXXU5DZDP.md`](../docs/SM-S928B-S928BXXU5DZDP.md).

## Legacy artifacts retained for provenance

Older files remain in this directory for comparison and historical audit:

| Target / generation | Notes |
| --- | --- |
| S928U/U1 DZF2 pair | Earlier hardware-tested KernelSU build |
| S928B DZF2 pair | International DZF2 no-patch-text build |
| S928B DZF2 v3.3.0 | Versioned KernelSU 3.3.0 forward-port |
| S928B DZDP v3.3.0 | Previous exact-vermagic DZDP pair, live validated |

The current app target manifest does not select these legacy pairs for the v3.4.0 DZDP release.

## Safety rule

Never substitute a module or daemon because two devices share `android14-6.1` or the same three-part kernel version. Match the complete model, firmware token, kernel release, vermagic, and artifact hash.

## Licensing

KernelSU Next is upstream GPLv3 software. See [`../THIRD_PARTY_NOTICES.md`](../THIRD_PARTY_NOTICES.md) for provenance and licensing information.
