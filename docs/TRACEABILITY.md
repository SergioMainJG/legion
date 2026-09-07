# Traceability Matrix

Each Capstone requirement is linked to the code that fulfils it and to the
check that exercises it.

## CLI parameters

| Parameter | Parser | Validation | Consumer |
|---|---|---|---|
| `a` | `SortingAlgorithm.fromKey` in `ParameterParser.parse` | catalogue lookup, `InvalidAlgorithmException` on unknown key | `LaunchParameters.getAlgorithm` → `SortingAlgorithm.createStrategy` in `LegionApplication.sortTroops` |
| `t` | `SortDirection.fromKey` in `ParameterParser.parse` | `InvalidParameterException` on unknown key | `SortDirection.apply` in `LegionApplication.sortTroops` |
| `o` | `Orientation.fromKey` in `ParameterParser.parse` | `InvalidParameterException` on unknown key | `FormationArranger.arrange` |
| `u` | `ParameterParser.parseCounts` (1..8 comma-separated values) | `ParameterValidator.validateCounts` / `validateCapacity` | `RandomDeployer.deploy`, `LaunchParameters.getTotalTroops` |
| `f` | `ParameterParser.parseFieldSize` (default `Battlefield.DEFAULT_SIZE`) | `ParameterValidator.validateFieldSize`, `Battlefield` constructor | `Battlefield` constructor |

## Sorting algorithms

| Key | Strategy | Criterion |
|---|---|---|
| `b` | `BubbleSortStrategy` | `TroopComparator.BY_RANGE` |
| `i` | `InsertionSortStrategy` | `TroopComparator.BY_RANGE` |
| `s` | `SelectionSortStrategy` | `TroopComparator.BY_RANGE` |
| `m` | `MergeSortStrategy` | `TroopComparator.BY_RANGE` |
| `q` | `QuickSortStrategy` | `TroopComparator.BY_RANGE` |
| `h` | `HeapSortStrategy` | `TroopComparator.BY_RANGE` |
| `c` | `CountingSortStrategy` | `Troop.getRange` (counting on the range key) |
| `r` | `RadixSortStrategy` | `Troop.getRange` (LSD passes on the range key) |

## Orientations

| Key | Orientation | Line kind | Start edge | `FormationArranger` path |
|---|---|---|---|---|
| `n` | `NORTH` | rows (`isVertical=false`) | south (`isReversed=true`) | `resolveLine` reverses, `resolvePosition` uses `(index, line)` |
| `s` | `SOUTH` | rows | north (`isReversed=false`) | `resolvePosition` uses `(index, line)` |
| `e` | `EAST` | columns (`isVertical=true`) | west (`isReversed=false`) | `resolvePosition` uses `(line, index)` |
| `w` | `WEST` | columns | east (`isReversed=true`) | `resolveLine` reverses, `resolvePosition` uses `(line, index)` |

## Troop hierarchy

| Type | Class | Symbol | Range | Abilities |
|---|---|---|---|---|
| Commander | `troops.units.Commander` | `C` | 3 | `Attackable`, `Movable` |
| Medic | `troops.units.Medic` | `M` | 1 | `Healable`, `Movable` |
| Tank | `troops.units.Tank` | `T` | 2 | `Attackable`, `Movable` |
| Sniper | `troops.units.Sniper` | `S` | 6 | `Attackable`, `Movable` |
| Infantry | `troops.units.Infantry` | `I` | 2 | `Attackable`, `Movable` |
| Engineer | `troops.units.Engineer` | `E` | 1 | `Healable`, `Movable` |
| Artillery | `troops.units.Artillery` | `A` | 8 | `Attackable`, `Movable` (range 0) |
| AntiAircraft | `troops.units.AntiAircraft` | `R` | 5 | `Attackable`, `Movable` |

## Functional requirements

| Requirement | Code | Check |
|---|---|---|
| NxN field, min 5, max 1000, default 10 | `Battlefield`, `ParameterValidator`, `ParameterParser` | `ValidatorTests`, `BattlefieldTests` |
| Random collision-free deployment | `RandomDeployer` | `BattlefieldTests` (exclusive `place`) |
| Capacity and one-line-per-type validation | `ParameterValidator.validateFormationCapacity` | `ValidatorTests` |
| Sorting by range, one shared criterion | `TroopComparator`, `sorting.strategies.*` | `SortingTests` |
| Four or more working algorithms | `SortingAlgorithm` (eight entries) | `SortingTests` |
| Orientation N/S/E/W, one type per line | `Orientation`, `FormationArranger` | `FormationTests` |
| Performance measured around sorting only | `LegionApplication.sortTroops` | manual runs |
| Centralized exception handling | `errors.LegionException`, `errors.ExceptionHandler` | error runs |
| Interactive session decoupled from the core | `commands.GameLoop` | run logs |
| Visualization with row/column indices and legend | `BattlefieldRenderer` | run logs |
