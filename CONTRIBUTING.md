# Contributing

Contributions are welcome, especially around documentation, reproducibility, validation, and exact-device porting.

## Ground rules

- Test only on devices you own or are explicitly authorized to test.
- Do not broaden compatibility by model name or three-part kernel version alone.
- Preserve upstream attribution and license notices.
- Do not commit signing keys, tokens, private device identifiers, or unredacted logs.
- Do not add release APKs or other large distributable binaries to the Git tree; use GitHub Releases.

## Before opening a pull request

For normal application/documentation changes, run:

```sh
./gradlew :app:testStandardDebugUnitTest :app:testSpoofedDebugUnitTest
./gradlew :app:assembleStandardDebug :app:assembleSpoofedDebug
```

For target/profile changes, also provide:

1. exact model;
2. full firmware/build token;
3. full kernel release (`uname -r`);
4. artifact SHA-256 values;
5. upstream source/tag/commit provenance;
6. whether each artifact was rebuilt or reused byte-for-byte;
7. hardware validation evidence from an owned/authorized device.

## Adding firmware support

Read [`PORTING.md`](PORTING.md) first. Every firmware must have its own explicit compatibility record. A PR that simply renames another device's profile or widens matching without evidence should not be merged.

## Pull request scope

Prefer small, reviewable PRs. Separate:

- application/UI changes;
- target metadata;
- native exploit profile changes;
- KernelSU module/daemon updates;
- documentation-only changes.

This makes it easier to identify which changes affect privileged code or supported hardware.

## Binary changes

If a tracked runtime binary changes, include:

```text
old SHA-256
new SHA-256
old size
new size
upstream source/commit
reason for change
validation performed
```

Update every manifest/document that pins that artifact.

## Security reports

Do not open a public issue containing a new sensitive vulnerability, secret, or weaponizable private detail. Follow [`SECURITY.md`](SECURITY.md).
