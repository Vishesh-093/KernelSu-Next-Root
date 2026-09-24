# Porting guide

This repository intentionally treats every supported firmware as an **exact runtime profile**. Do not widen model or kernel matching just because two devices share the same three-part kernel version.

## Current DZDP profile

The active application target is:

```text
Model:          SM-S928B
Build token:    S928BXXU5DZDP
Kernel release: 6.1.145-android14-11-33419968-abS928BXXU5DZDP
```

The application metadata for this exact target lives in:

```text
app/src/main/assets/targets-v3.json
app/src/main/assets/e3q-S928BXXU5DZDP/
```

The KernelSU Next module/daemon pair is built for the exact DZDP vermagic.

## Important: DZDP bootstrap payload provenance

There is currently **no separate `src/targets/e3q-S928BXXU5DZDP/` native exploit source profile** in this repository.

The DZDP app payload is the published S928B payload whose binary was also published under a DZDP tracking branch and is byte-identical to the S928B DZF2 artifact. The exact provenance and SHA-256 are recorded in [`docs/SM-S928B-S928BXXU5DZDP.md`](docs/SM-S928B-S928BXXU5DZDP.md).

That means:

- the repository does not claim that DZDP has a distinct exploit-source port;
- the app still performs exact DZDP model/build/kernel matching;
- the KernelSU Next module and daemon are DZDP-specific and exact-vermagic;
- live end-to-end validation was performed on the exact DZDP device;
- bootstrap reliability remains probabilistic.

## Existing native source profiles

The `src/targets/` tree contains source profiles inherited from the S928 work:

| Profile | Device family | Notes |
| --- | --- | --- |
| `e3q-S928USQS6DZF2` | SM-S928U / U1 | US DZF2 profile |
| `e3q-S928W-S928USQS6DZF2` | SM-S928W | Canadian runtime profile sharing the US kernel banner |
| `e3q-S928BXXS6DZF2` | SM-S928B | International DZF2 profile |

These profiles are not interchangeable. Kernel banners, product identity, runtime layout details, and exploit constants can differ even when the base kernel version is the same.

## Rules for adding another firmware

1. **Capture exact identity**
   - model
   - full build token
   - full `uname -r`
   - Android/SDK level
   - ABI/page size

2. **Recover matching kernel information**
   - kernel image
   - BTF/symbol information where available
   - relevant config and vermagic data

3. **Do not clone a profile by renaming it**
   - create a new `src/targets/<profile>/` only after validating the source constants for that build;
   - never edit an existing DZF2 profile in place to represent another firmware.

4. **Build/audit the bootstrap payload**

```sh
make TARGET=<profile> stable
```

5. **Build the KernelSU Next pair for exact vermagic**
   - module must match the target kernel release;
   - daemon/module provenance and SHA-256 must be recorded.

6. **Add an exact runtime manifest entry**
   - model
   - build token
   - full kernel release
   - payload size/hash
   - KernelSU daemon size/hash

7. **Reject broad matching**
   - do not match only on `6.1.145`;
   - do not treat SM-S928U, SM-S928W, and SM-S928B as equivalent;
   - do not silently fall back to a nearby firmware profile.

8. **Validate on owned/authorized hardware**
   - confirm the exact profile was selected;
   - record SELinux state;
   - record KernelSU driver version;
   - perform repeated root verification;
   - document failed/rebooting attempts as well as successful ones.

9. **Document provenance**
   - upstream repository and commit/tag;
   - local changes;
   - artifact SHA-256;
   - whether the payload was rebuilt or reused byte-for-byte.

## Release rule

A profile should not be presented as generally supported until the exact model/build/kernel combination has been tested end-to-end. Experimental reuse must stay labeled as experimental in the technical record even when live validation succeeds.
