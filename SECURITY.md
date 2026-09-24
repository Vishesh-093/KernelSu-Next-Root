# Security policy

## Scope

This repository contains Android kernel-root research and device-specific packaging. Reports are useful when they concern this repository's own code, release artifacts, supply-chain handling, device matching, privilege boundaries, or documentation that could cause unsafe use.

Examples include:

- a launcher accepting a model/build/kernel outside the documented exact target;
- bundled artifact hash verification being bypassed or incorrect;
- a release asset not matching the published checksum;
- unexpected network activity in the offline build;
- secrets, signing material, or private data committed to the repository;
- a local privilege or file-handling bug introduced by this project.

Unsupported-device exploit failures, expected temporary-root loss after reboot, and the probabilistic behavior of the bootstrap primitive are not by themselves vulnerabilities in this repository.

## Reporting a vulnerability

Prefer GitHub's **private security advisory** flow for this repository when available. Include:

- affected commit/tag;
- exact device/build/kernel if device-specific;
- concise reproduction steps;
- expected vs. observed behavior;
- logs with personal identifiers removed;
- whether the issue can cause persistent modification, data exposure, or execution outside the documented target.

If private advisories are unavailable, open a minimal issue asking for a private contact path and do **not** publish exploit details, secrets, tokens, signing keys, or sensitive device data in the issue.

## Safety boundary

Testing should be limited to devices you own or are explicitly authorized to test. This project does not claim support for bypassing carrier, enterprise, MDM, Knox Guard, or other ownership controls on third-party devices.

## Release verification

Release APK hashes are published in `dist/SHA256SUMS` and on the GitHub release page. Verify the downloaded file before installation:

```sh
sha256sum -c SHA256SUMS
```

The application also verifies bundled manager artifacts before installation.

## Supported security target

The current validated profile is intentionally narrow:

```text
SM-S928B
S928BXXU5DZDP
6.1.145-android14-11-33419968-abS928BXXU5DZDP
```

Do not report the app rejecting another firmware as a bug unless that firmware has been explicitly added as a supported profile.
