# Samsung KernelSU late-load builds

The original files in this directory are built from KernelSU `v3.2.5`, commit
`b0bc817b4e966aa6aa830834eaf6ef765d821d40`. The versioned S928B files are
built from KernelSU `v3.3.0`, commit
`932014ab5b2c9b74a3d11e2ec4d17dd10fc9442e`. They are not interchangeable
between firmware releases.

## Bundled artifacts

| File | Target | KMI | Size | SHA-256 |
| --- | --- | --- | ---: | --- |
| `android14-6.1_kernelsu-e3q-S928USQS6DZF2-kdp.ko` | `SM-S928U` / `SM-S928U1`, `S928USQS6DZF2` | `android14-6.1` | 400,152 | `ed7afea6cd221d5698739d3a1633264c084ffb77f2df730e5808941e0a555de5` |
| `ksud-e3q-S928USQS6DZF2-kdp` | Same exact U/U1 DZF2 build | `android14-6.1` | 4,998,352 | `10c1bf87f8e475e6ab8c5d1c5a085aa1544ee091f4451ad65141ea75261ab610` |
| `android14-6.1_kernelsu-e3q-S928BXXS6DZF2-kdp.ko` | `SM-S928B`, `S928BXXS6DZF2` | `android14-6.1` | 398,432 | `14f805c6a03123e84f10a252eb5b47f6c65c56c05ad4ccccf1f836c6867f64a9` |
| `ksud-e3q-S928BXXS6DZF2-kdp` | Same exact S928B DZF2 build | `android14-6.1` | 4,748,232 | `43f451313dc111429187f8f93e76c57c42976323782aac936c1c09aa309b76b3` |
| `android14-6.1_kernelsu-e3q-S928BXXS6DZF2-kdp-v3.3.0.ko` | `SM-S928B`, `S928BXXS6DZF2` | `android14-6.1` | 404,864 | `70403cdcd239229a0eec18903002d08c839bc787ab6701ecc7eb31d9ccca5bc1` |
| `ksud-e3q-S928BXXS6DZF2-kdp-v3.3.0` | Same exact S928B DZF2 build | `android14-6.1` | 4,989,824 | `396169ca5729ad1c35b557dab32bb2221d150ddfc0c84140aa19a18b7d061abd` |
| `android14-6.1_kernelsu-e3q-S928BXXU5DZDP-kdp-v3.3.0.ko` | `SM-S928B`, `S928BXXU5DZDP` | `android14-6.1` | 5,849,640 | `dd677e9e9e6372009a3a8e68ca1ba0ec1c889d23a3547cd7f4c1aa2c07dd7882` |
| `ksud-e3q-S928BXXU5DZDP-kdp-v3.3.0` | Same exact S928B DZDP build | `android14-6.1` | 6,647,424 | `5686641a536ee92ab69451e025985c7be01bef2c452582fa338b276a445ee5de` |

The standalone `.ko` files are retained for auditing. The app late-loads the
matching `ksud-*` binary because `ksud late-load` embeds the target
`<kmi>_kernelsu.ko` asset.

Do not reuse the U/U1 pair on `SM-S928B` or `SM-S928W`. W was recorded to
panic when the U/U1 `ksud` live-patched text; it uses the S928B no-patch-text
`ksud` instead. The U/U1 and B modules also have different vermagic strings.

## S928B KernelSU 3.3.0 forward-port

The S928B v3.3.0 pair applies the maintained Samsung KDP/RKP/DEFEX patch to
the clean upstream v3.3.0 tag. It was built with the exact DZF2 release string,
reports KernelSU version `32601`, has an empty `__versions` section, passed
KernelSU's DDK symbol checker, and has no unsafe text-patching imports. The
paired `ksud` was rebuilt with NDK r29 and embeds this exact module.

This new pair is build- and static-audit verified but has not yet been tested
on S928B hardware. The offline app selects it only for the exact
`SM-S928B` / `S928BXXS6DZF2` profile; S928U/U1 and S928W remain on their
previous hardware-tested payloads.

The DZDP pair is a separate v3.3.0 build with exact
`6.1.145-android14-11-33419968-abS928BXXU5DZDP` vermagic. It passed the same
build-time static gates and was validated end-to-end on the exact DZDP phone:
the live driver reported `32601-2`, SELinux remained enforcing, and repeated
root checks passed. It must not be substituted for the DZF2 pair.
