# cloud-itonami-isco-2144

Open Occupation Blueprint for **ISCO-08 2144**: Mechanical Engineers.

This repository designs a forkable OSS business for an independent mechanical engineer: a field-inspection robot performs equipment measurement and vibration sensing under a governor-gated actor, so the practice keeps its own design and sign-off records instead of renting a closed engineering-management SaaS.

## Robotics premise

All cloud-itonami verticals are designed on the premise that a **robot performs
the physical domain work**. Here a field-inspection robot performs equipment measurement, vibration sensing and physical component inspection under an actor that proposes
actions and an independent **Mechanical Engineering Governor** that gates them. The governor never
dispatches hardware itself; `:high`/`:safety-critical` actions (such as
signing off a load-bearing design change, or certifying pressure-vessel safety) require human sign-off.

A live sample of the operator console (robotics safety console, shared template) is rendered in [docs/samples/operator-console.html](docs/samples/operator-console.html) — pure-data HTML output of `kotoba.robotics.ui`.

## Core Contract

```text
client brief + design spec + safety code
        |
        v
Design Advisor -> Mechanical Engineering Governor -> design/review, or human sign-off
        |
        v
robot actions (gated) + operating records + audit ledger
```

No automated advice can dispatch a robot action the governor refuses, suppress
an operating record, or disclose sensitive data without governor approval and
audit evidence.

## Capability layer

Resolves via [`kotoba-lang/occupation`](https://github.com/kotoba-lang/occupation)
(ISCO-08 `2144`). Required capabilities:

- :robotics
- :identity
- :forms
- :dmn
- :bpmn
- :audit-ledger

See [`docs/business-model.md`](docs/business-model.md) and
[`docs/operator-guide.md`](docs/operator-guide.md).

## License

AGPL-3.0-or-later.
