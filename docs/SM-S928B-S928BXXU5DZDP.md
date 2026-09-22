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

## Exact KernelSU 3.3.0 pair

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
