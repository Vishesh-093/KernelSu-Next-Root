# Third-party notices and provenance

This repository contains original packaging/integration work together with code and binary artifacts originating from upstream open-source projects. The repository-level `LICENSE` does **not** relicense third-party components; each upstream component retains its own license and attribution requirements.

## Root My Galaxy

- Upstream: https://github.com/BuSung-dev/Root-My-Galaxy
- License: Apache License 2.0
- Role here: Android launcher lineage, bootstrap workflow, and related integration concepts.

This repository has modified and reworked the upstream application for an exact `SM-S928B / S928BXXU5DZDP` offline KernelSU Next profile. Upstream authorship remains credited.

## Root My Galaxy payload work

- Payload repository lineage: https://github.com/keyarr/Root-My-Galaxy-Payloads
- License: Apache License 2.0
- DZDP tracking commit recorded by this project: `06a9730d6d31ad27407ac9c5680dd907296a91bd`
- Runtime payload SHA-256: `a49b378d654c7e637697a701c3c4c5fd02d22b9b30a7069c03e64ec5844af206`

The DZDP tracking artifact is byte-identical to the published S928B DZF2 payload. This repository therefore describes it as **payload reuse**, not as a distinct DZDP exploit-source port.

The exact-device provenance record is in [`docs/SM-S928B-S928BXXU5DZDP.md`](docs/SM-S928B-S928BXXU5DZDP.md).

## KernelSU Next

- Upstream: https://github.com/KernelSU-Next/KernelSU-Next
- License: GNU General Public License v3.0
- Version used by the current target: KernelSU Next `v3.4.0`
- Source commit recorded by this project: `1a879d6a866f80b1fa1c1009a2ffa747873cbb5e`

This repository distributes an exact-vermagic KernelSU Next module/daemon pair for the documented Samsung kernel and bundles official KernelSU Next manager variants.

Relevant local artifacts include:

```text
kernelsu/android14-6.1_kernelsu-e3q-S928BXXU5DZDP-kdp-v3.4.0.ko
kernelsu/ksud-e3q-S928BXXU5DZDP-kdp-v3.4.0
app/src/standard/assets/manager/KernelSUNextManager.apk
app/src/spoofed/assets/manager/KernelSUNextManager.apk
```

KernelSU Next remains subject to its upstream GPLv3 terms. Corresponding upstream source is available from the project and commit listed above. Local Samsung-specific build/provenance details are recorded in the exact-device validation document.

## AndroidX, Kotlin, Shizuku and build dependencies

The Android application also depends on standard third-party libraries declared in `app/build.gradle.kts`, including AndroidX, Kotlin coroutines, and Shizuku. Those dependencies retain their respective upstream licenses and are resolved through the configured Maven repositories.

## Samsung names and device identifiers

Samsung, Galaxy, Knox, and related product names are trademarks of their respective owners. Their use here is descriptive only and does not imply affiliation or endorsement.

## Maintainer additions

Files and modifications authored specifically for this repository should preserve upstream notices where applicable and clearly distinguish locally authored changes from upstream work.

When adding or updating a third-party component, record at minimum:

1. upstream project URL;
2. license;
3. exact version/tag/commit;
4. whether the artifact was rebuilt or reused byte-for-byte;
5. local modifications;
6. SHA-256 for distributed binary artifacts.
