# Architecture

## Goal

KernelSU Next Root packages an exact-device temporary-root flow for one validated Samsung firmware without modifying the boot image or widening support to nearby builds.

The design deliberately separates five concerns:

```text
Device identity
    ↓
Exact target selection
    ↓
Bootstrap execution
    ↓
KernelSU Next late-load
    ↓
Root verification / manager handoff
```

## Components

### 1. Android launcher

The Android application is responsible for:

- reading device/build/kernel identity;
- rejecting unsupported targets;
- coordinating the Shizuku-assisted flow;
- selecting only the bundled exact-device payload;
- verifying bundled artifact hashes;
- presenting install state and validation information;
- handing off to the matching KernelSU Next manager variant.

Two launcher flavors exist:

```text
standard  -> io.github.vishesh093.ksunextroot
spoofed   -> io.github.vishesh093.ksunextroot.spoofed
```

The manager package expected by each flavor is fixed at build time.

### 2. Target manifest

`app/src/main/assets/targets-v3.json` is the runtime compatibility gate.

The current entry requires all of the following:

```text
model          = SM-S928B
build token    = S928BXXU5DZDP
kernel release = 6.1.145-android14-11-33419968-abS928BXXU5DZDP
```

The three-part kernel version alone is not considered sufficient compatibility evidence.

The manifest also pins artifact size and SHA-256 values.

### 3. Bootstrap payload

The bundled `cve-2026-43499-app.so` is the userspace bootstrap payload used by the launcher.

For DZDP, the published tracking artifact is byte-identical to the existing S928B DZF2 payload. The repository therefore treats it as a reused published binary with exact-device runtime gating and live DZDP validation, not as a separately rebuilt DZDP exploit-source port.

See [`SM-S928B-S928BXXU5DZDP.md`](SM-S928B-S928BXXU5DZDP.md) for provenance.

### 4. KernelSU Next pair

The project packages a KernelSU Next module and daemon built/audited for the exact DZDP kernel vermagic.

```text
kernelsu/android14-6.1_kernelsu-e3q-S928BXXU5DZDP-kdp-v3.4.0.ko
kernelsu/ksud-e3q-S928BXXU5DZDP-kdp-v3.4.0
```

The module is late-loaded after bootstrap rather than being installed through a persistent boot-image modification.

### 5. Manager handoff

The Standard and Spoofed launcher flavors bundle separate official KernelSU Next manager variants. The launcher verifies the expected manager hash before installation/handoff.

## Root lifecycle

```text
Cold/full boot
   ↓
No project-provided persistent root
   ↓
Android userspace starts
   ↓
Shizuku becomes available
   ↓
Exact target check
   ↓
Bootstrap attempt
   ├─ failure/reboot -> no root; retry later
   └─ success
        ↓
KernelSU Next module late-load
        ↓
Driver verification
        ↓
Manager reports Working
```

Root is intentionally **per boot**. A full reboot returns the phone to the normal non-rooted boot path until bootstrap succeeds again.

## Trust boundaries

### Device identity boundary

The project must never silently fall back to another S928 profile. A mismatch must fail closed.

### Binary integrity boundary

Bundled runtime files are pinned by SHA-256. Release APKs are published with external SHA-256 values in `dist/SHA256SUMS`.

### Persistence boundary

The documented design does not patch `boot.img`, disable verified boot, or claim persistent bootloader-level modification.

### Network boundary

The release launcher is designed to contain the runtime payloads locally. It should not need to download the exploit payload, KernelSU Next module/daemon, or manager during normal operation.

### Privilege boundary

Shizuku access is not equivalent to kernel root. The launcher should treat bootstrap success and KernelSU driver availability as separate states and verify the final root condition explicitly.

## Validation evidence

The exact DZDP validation record documents:

- full model/build/kernel identity;
- payload and KernelSU artifact hashes;
- exact vermagic;
- KernelSU Next driver version;
- SELinux enforcing state;
- repeated `su` verification;
- a failed probabilistic attempt as well as the successful run.

See [`SM-S928B-S928BXXU5DZDP.md`](SM-S928B-S928BXXU5DZDP.md).
