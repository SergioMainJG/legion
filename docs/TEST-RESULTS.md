# Test Results

Exhaustive run of the automated suite plus a manual CLI battery.
Date: 2026-09-06. `javac` (Java 17), fresh `./build.sh` (exit 0).

- Automated: **133 checks, 133 passed, 0 failed** (`./test.sh`).
- Manual CLI battery: **44 scenarios, all as expected** (8 algorithms, 4 orientations,
  2 directions, field bounds, capacity, case-insensitivity, variable `u`, the
  interactive session, the guided setup, and 23 error cases — 18 CLI + 5 interactive).

---

## 1. Automated suite (`./test.sh`)

```
--------------------------------------------------
Total: 133  Passed: 133  Failed: 0
```

| Group | Checks | Covers |
|---|---:|---|
| parser | 14 | valid line, missing / unknown / duplicated parameter, malformed `key=value`, invalid algorithm / direction / orientation, non-numeric and negative quantity, non-numeric `f`, case-insensitive keys and values |
| validator | 10 | field size `[5,1000]` incl. both bounds, exactly-full battlefield, capacity exceeded, group wider than a line, too many groups, empty configuration |
| sorting (8 × 10) | 80 | each strategy on mixed / empty / single / sorted / reversed / equal-range / min-max-range / large datasets; orders by range not health; does not mutate input |
| every algorithm agrees | 3 | all 8 produce the same ascending order, the same descending order, and agree on repeated ranges |
| battlefield | 8 | inside / outside bounds, exclusive placement, capacity, min / max / below / above size |
| formation | 5 (+ inside "sorting") | four orientations, no mixed line, single type, exact line capacity, identity preserved |
| deployment | 5 | every troop placed once, no repeated position over repeated runs, small & large fields, nothing lost or renamed through sorting + formation |
| regression | 5 | midterm-style command runs the full pipeline; bubble/insertion agree with the rest; four orientation keys and both directions resolve; descending is the exact reverse of ascending |

First 12 lines of the run:

```
[PASS] parser accepts a valid argument line
[PASS] parser defaults the field size to 10 when f is absent
[PASS] parser maps a short u list and pads the rest with zero
[PASS] parser rejects a missing mandatory parameter
[PASS] parser rejects an argument without key=value shape
[PASS] parser rejects a duplicated key
[PASS] parser rejects an unknown algorithm key
[PASS] parser rejects a non numeric count
[PASS] parser rejects a negative count
[PASS] parser rejects an unknown parameter
[PASS] parser rejects an unknown sort direction value
[PASS] parser rejects an unknown orientation value
```

---

## 2. The eight algorithms produce one identical order

**Input (same for all):** `a=<k> t=c o=s u=1,1,1,1,1,1,1,1 f=12` — one of every troop type.

| `a` | Strategy | `Result` |
|---|---|---|
| `b` | Bubble Sort | `[E-1(range 1), M-1(range 1), I-1(range 2), T-1(range 2), C-1(range 3), R-1(range 5), S-1(range 6), A-1(range 8)]` |
| `i` | Insertion Sort | *(identical)* |
| `s` | Selection Sort | *(identical)* |
| `m` | Merge Sort | *(identical)* |
| `q` | Quick Sort | *(identical)* |
| `h` | Heap Sort | *(identical)* |
| `c` | Counting Sort | *(identical)* |
| `r` | Radix Sort | *(identical)* |

Order is by `range` ascending, ties broken by identifier (`E-1` before `M-1`,
`I-1` before `T-1`). No strategy uses `health`.

---

## 3. The four orientations

**Input:** `a=b t=c o=<k> u=1,1,1 f=6` → sorted `[M-1(range 1), T-1(range 2), C-1(range 3)]`.
Final formation (`M` = first / lowest-range group):

```
o=n  (South -> North)        o=s  (North -> South)
  3 |  C . . . . .             0 |  M . . . . .
  4 |  T . . . . .             1 |  T . . . . .
  5 |  M . . . . .             2 |  C . . . . .

o=e  (West -> East)          o=w  (East -> West)
  0 |  M T C . . .             0 |  . . . C T M
```

- `n` builds rows from the south edge upward; `s` from the north edge downward.
- `e` builds columns from the west edge rightward; `w` from the east edge leftward.
- Each troop type occupies its own line; no line mixes types.

---

## 4. Sort direction `t`

**Input:** `a=m o=s u=2,1,2,1 f=8`

| `t` | `Result` | Final formation (top rows) |
|---|---|---|
| `c` ascending | `[M-1(1), T-1(2), T-2(2), C-1(3), C-2(3), S-1(6)]` | `row0 M` / `row1 T T` / `row2 C C` / `row3 S` |
| `d` descending | `[S-1(6), C-2(3), C-1(3), T-2(2), T-1(2), M-1(1)]` | `row0 S` / `row1 C C` / `row2 T T` / `row3 M` |

`d` is the exact reversal of `c` (identifier tie-break reverses consistently:
`C-1,C-2` → `C-2,C-1`).

---

## 5. Edge cases

| Scenario | Input | Result |
|---|---|---|
| Minimum field | `a=b t=c o=s u=1,1,1 f=5` | Runs; `Field: 5x5`, formation in column 0 |
| Default field (no `f`) | `a=b t=c o=s u=1,1,1` | Runs; `Field: 10x10` |
| Maximum field | `a=q t=c o=s u=2,2,2 f=1000` | Runs to completion; `Field: 1000x1000`, sorting + formation OK |
| Battlefield exactly full | `a=s t=c o=e u=5,5,5,5,5 f=5` | 25 troops fill every cell; final formation is 5 clean columns `M I T C S` |
| Short `u` list | `a=b t=c o=s u=3 f=6` | `Troops: 3` (3 Commanders, other types 0) |
| Full `u` list | `a=b t=c o=s u=1,1,1,1,1,1,1,1 f=10` | `Troops: 8`, one of each type |
| Case-insensitive | `A=B T=D O=N U=1,1,1 F=6` | `Algorithm: Bubble Sort / Order: descending / Orientation: north` |

**Exactly-full 5×5 final formation:**

```
        0   1   2   3   4
  0 |   M   I   T   C   S
  1 |   M   I   T   C   S
  2 |   M   I   T   C   S
  3 |   M   I   T   C   S
  4 |   M   I   T   C   S
```

---

## 6. Interactive session (game loop)

**Input:** `a=b t=c o=s u=2,1,1,1,1 f=8` then
`status` · `status C-1` · `move I-1 2` · `attack S-1 M-1` · `heal M-1 S-1` ·
`attack M-1 S-1` · `heal T-1 C-1` · `badcmd` · `exit`

```
legion> status
        0   1   2   3   4   5   6   7
  0 |   M   *   *   *   *   *   *   *
  1 |   I   *   *   *   *   *   *   *
  2 |   T   *   *   *   *   *   *   *
  3 |   C   C   *   *   *   *   *   *
  4 |   S   *   *   *   *   *   *   *
  ...
M-1 Medic health=121/121 range=1 movement=3 pattern=lateral
I-1 Infantry health=160/160 range=2 movement=2 pattern=straight
T-1 Tank health=228/228 range=2 movement=1 pattern=straight
C-1 Commander health=202/202 range=3 movement=3 pattern=diagonal
C-2 Commander health=180/180 range=3 movement=3 pattern=diagonal
S-1 Sniper health=152/152 range=6 movement=2 pattern=straight
legion> status C-1
C-1 Commander health=202/202 range=3 movement=3 pattern=diagonal
legion> move I-1 2                 -> ERROR E-CMD: Destination (0, 3) is already occupied.
legion> attack S-1 M-1             -> Action executed: S-1 attacks M-1
legion> heal M-1 S-1               -> Action executed: M-1 heals S-1
legion> attack M-1 S-1             -> ERROR E-CMD: M-1 cannot attack.
legion> heal T-1 C-1               -> ERROR E-CMD: T-1 cannot heal.
legion> badcmd                     -> ERROR E-CMD: Unknown command: badcmd. Write help to list the available commands.
legion> exit                       -> Session closed.
```

Ability gating works (Medic has no `Attackable`, Tank has no `Healable`),
collision is detected on `move`, and a bad command does not end the session.

**Guided setup (`./run.sh` with no arguments):** answering
`b, c, n, 1, 1, 1, 0, 0, 0, 0, 0, 6` produces `Bubble Sort / ascending / north /
6x6 / Troops: 3` and then runs the full pipeline. (Prompts print correctly one
per line on a real terminal; they collapse onto one line only when stdin is piped.)

---

## 7. Error handling (controlled, no stack traces)

| # | Input | Code | Message |
|---|---|---|---|
| 1 | `a=b t=c o=s u=1,1,1 x=9 f=6` | `E-PARAM` | `Unknown parameter: x. Expected a, t, o, u or f.` |
| 2 | `a=b a=i t=c o=s u=1,1,1 f=6` | `E-PARAM` | `Duplicated parameter: a. Each parameter must appear once.` |
| 3 | `bogus a=b t=c o=s u=1,1,1` | `E-PARAM` | `Malformed parameter: bogus. Expected key=value.` |
| 4 | `a=b t=c o=s f=6` | `E-PARAM` | `Missing required parameter: u.` |
| 5 | `a=b t=c u=1,1,1 f=6` | `E-PARAM` | `Missing required parameter: o.` |
| 6 | `a=z t=c o=s u=1,1,1 f=6` | `E-ALG` | `Unknown sorting algorithm: z` |
| 7 | `a=b t=x o=s u=1,1,1 f=6` | `E-PARAM` | `Unknown sort direction: x. Expected c or d.` |
| 8 | `a=b t=c o=ajs u=1,1,1 f=6` | `E-PARAM` | `Unknown orientation: ajs. Expected n, s, e or w.` |
| 9 | `a=b t=c o=s u=1,x f=6` | `E-PARAM` | `Amount of Medic must be a whole number, received x.` |
| 10 | `a=b t=c o=s u=1,-3 f=6` | `E-PARAM` | `Amount of Medic must be a whole number, received -3.` |
| 11 | `a=b t=c o=s u=1,1,1 f=big` | `E-PARAM` | `Parameter f must be a whole number, received big.` |
| 12 | `a=b t=c o=s u=1,1,1,1,1,1,1,1,1 f=10` | `E-PARAM` | `Parameter u expects between 1 and 8 values separated by commas, received 9.` |
| 13 | `a=b t=c o=s u=0,0,0 f=6` | `E-PARAM` | `At least 1 troop must be deployed.` |
| 14 | `a=b t=c o=s u=1,1,1 f=4` | `E-FIELD` | `Field size must be between 5 and 1000, received 4.` |
| 15 | `a=b t=c o=s u=1,1,1 f=1001` | `E-FIELD` | `Field size must be between 5 and 1000, received 1001.` |
| 16 | `a=b t=c o=s u=20,20 f=5` | `E-FIELD` | `The battlefield holds 25 cells and 40 troops were requested.` |
| 17 | `a=c t=c o=e u=1,2,5,5,13 f=6` | `E-FIELD` | `A line holds 6 units and 13 Infantry were requested.` |
| 18 | `a=b t=c o=s u=1,1,1,1,1,1 f=5` | `E-FIELD` | `The final formation needs 6 lines and the battlefield only has 5.` |

In the interactive session:

| Input | Code | Message |
|---|---|---|
| `move I-1 2` (destination taken) | `E-CMD` | `Destination (0, 3) is already occupied.` |
| `attack M-1 S-1` (Medic can't attack) | `E-CMD` | `M-1 cannot attack.` |
| `heal T-1 C-1` (Tank can't heal) | `E-CMD` | `T-1 cannot heal.` |
| `badcmd` | `E-CMD` | `Unknown command: badcmd. Write help to list the available commands.` |
| `move I-1 1` / `heal I-1 C-1` (no such unit) | `E-CMD` | `There is no troop with identifier I-1.` |

Every error is reported through `ExceptionHandler` in the uniform `ERROR <code>`
block; none produced a Java stack trace.

---

## 8. Final-validation checklist

| Phase | Result |
|---|---|
| 1. Compile from scratch | Pass (`./build.sh` exit 0) |
| 2. Run all automated tests | Pass (133/133) |
| 3. Valid specification examples | Pass (§2–§6) |
| 4. Invalid examples | Pass (§7, 22 cases) |
| 5. Four orientations | Pass (§3) |
| 6. Eight algorithms | Pass (§2) |
| 7. All sort by range | Pass (identical output, `Criterion: attack range`) |
| 8. Formation never mixes types in a line | Pass (§3, §5, automated `no line mixes two troop types`) |
| 9. README / UML / traceability vs. code | Pass (updated this session) |
| 10. No midterm regression | Pass (`RegressionTests`, §4 direction behaviour preserved) |
