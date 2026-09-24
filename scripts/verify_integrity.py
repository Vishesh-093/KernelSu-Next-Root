#!/usr/bin/env python3
"""Offline integrity checks for the exact SM-S928B / DZDP release inputs."""

from __future__ import annotations

import hashlib
import json
import pathlib
import re
import sys

ROOT = pathlib.Path(__file__).resolve().parents[1]
EXPECTED_MODEL = "SM-S928B"
EXPECTED_BUILD = "S928BXXU5DZDP"
EXPECTED_KERNEL = "6.1.145-android14-11-33419968-abS928BXXU5DZDP"


def fail(message: str) -> None:
    raise SystemExit(f"ERROR: {message}")


def sha256(path: pathlib.Path) -> str:
    h = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            h.update(chunk)
    return h.hexdigest()


def verify_runtime_checksums() -> None:
    manifest = ROOT / "checksums/runtime.sha256"
    if not manifest.is_file():
        fail("checksums/runtime.sha256 is missing")

    for line_number, raw in enumerate(manifest.read_text().splitlines(), start=1):
        line = raw.strip()
        if not line or line.startswith("#"):
            continue
        parts = line.split(maxsplit=1)
        if len(parts) != 2 or not re.fullmatch(r"[0-9a-f]{64}", parts[0]):
            fail(f"invalid checksum line {line_number}: {raw!r}")
        expected, relative = parts
        path = ROOT / relative
        if not path.is_file():
            fail(f"tracked runtime artifact is missing: {relative}")
        actual = sha256(path)
        if actual != expected:
            fail(f"SHA-256 mismatch for {relative}: {actual} != {expected}")
        print(f"OK sha256 {relative}")


def verify_target_manifest() -> None:
    path = ROOT / "app/src/main/assets/targets-v3.json"
    data = json.loads(path.read_text())

    if data.get("schemaVersion") != 3:
        fail("targets-v3.json schemaVersion must be 3")

    payloads = data.get("payloads")
    if not isinstance(payloads, list) or len(payloads) != 1:
        fail("exact-device release must expose exactly one active payload")

    payload = payloads[0]
    expected = {
        "payloadId": "e3q-S928BXXU5DZDP",
        "models": [EXPECTED_MODEL],
        "kernelVersions": ["6.1.145"],
        "kernelReleases": [EXPECTED_KERNEL],
        "buildTokens": [EXPECTED_BUILD],
    }
    for key, value in expected.items():
        if payload.get(key) != value:
            fail(f"targets-v3.json {key} is {payload.get(key)!r}, expected {value!r}")

    exploit = payload.get("exploit", {})
    kernelsu = payload.get("kernelsu", {})
    for name, entry in (("exploit", exploit), ("kernelsu", kernelsu)):
        url = entry.get("url", "")
        if not isinstance(url, str) or not url.startswith("asset://"):
            fail(f"{name} must use a bundled asset:// URL")
        digest = entry.get("sha256", "")
        if not re.fullmatch(r"[0-9a-f]{64}", digest):
            fail(f"{name} sha256 is invalid")
        if not isinstance(entry.get("size"), int) or entry["size"] <= 0:
            fail(f"{name} size is invalid")

    print(f"OK target {EXPECTED_MODEL} / {EXPECTED_BUILD}")
    print(f"OK kernel {EXPECTED_KERNEL}")


def verify_release_manifest() -> None:
    path = ROOT / "dist/SHA256SUMS"
    lines = [line.strip() for line in path.read_text().splitlines() if line.strip()]
    if len(lines) != 2:
        fail("dist/SHA256SUMS must contain exactly the two release APK entries")

    expected_names = {
        "KernelSUNextRoot-SM-S928B-DZDP-v3.4.0-standard.apk",
        "KernelSUNextRoot-SM-S928B-DZDP-v3.4.0-spoofed.apk",
    }
    found_names: set[str] = set()
    for line in lines:
        parts = line.split(maxsplit=1)
        if len(parts) != 2 or not re.fullmatch(r"[0-9a-f]{64}", parts[0]):
            fail(f"invalid release checksum line: {line!r}")
        found_names.add(parts[1])

    if found_names != expected_names:
        fail(f"unexpected release checksum filenames: {sorted(found_names)!r}")

    tracked_apks = list((ROOT / "dist").glob("*.apk"))
    if tracked_apks:
        fail("release APKs must live in GitHub Releases, not dist/: " + ", ".join(p.name for p in tracked_apks))

    print("OK release checksum manifest")
    print("OK dist/ contains no tracked release APKs")


def main() -> int:
    verify_runtime_checksums()
    verify_target_manifest()
    verify_release_manifest()
    print("Integrity verification passed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
