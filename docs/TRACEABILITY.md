# Traceability Matrix

Each Capstone requirement is linked to the code that fulfils it, the test that
exercises it, and the documentation that describes it.

## Decision on parameter `t`

For the final delivery `t` is the sort direction (`c` ascending, `d`
descending), the meaning approved in the midterm. It is a single reversal of
the sorted list, not a second algorithm.

| Concern | Location |
|---|---|
| Parsing / rejection of invalid values | `SortDirection.fromKey` via `ParameterParser.parse` |
| Behaviour | `SortDirection.apply` (reverse once) |
| Consumer | `LegionApplication.sortTroops` |
| Tests | `ParserTests` (unknown value), `RegressionTests` (both keys resolve, descending is exact reverse), `SortingTests` (ascending and descending agreement across algorithms) |
| Documentation | README "Decision on `t`" section |

## CLI parameters

| Parameter | Parser | Validation | Consumer | Tests |
|---|---|---|---|---|
| `a` | `SortingAlgorithm.fromKey` in `ParameterParser.parse` | catalogue lookup, `InvalidAlgorithmException` on unknown key | `LaunchParameters.getAlgorithm` → `SortingAlgorithm.createStrategy` in `LegionApplication.sortTroops` | `ParserTests` |
| `t` | `SortDirection.fromKey` in `ParameterParser.parse` | `InvalidParameterException` on unknown key | `SortDirection.apply` in `LegionApplication.sortTroops` | `ParserTests`, `RegressionTests` |
| `o` | `Orientation.fromKey` in `ParameterParser.parse` | `InvalidParameterException` on unknown key | `FormationArranger.arrange` | `ParserTests`, `FormationTests` |
| `u` | `ParameterParser.parseCounts` (1..8 comma-separated values) | `ParameterValidator.validateCounts` / `validateCapacity` | `RandomDeployer.deploy`, `LaunchParameters.getTotalTroops` | `ParserTests`, `ValidatorTests` |
| `f` | `ParameterParser.parseFieldSize` (default `Battlefield.DEFAULT_SIZE`) | `ParameterValidator.validateFieldSize`, `Battlefield` constructor | `Battlefield` constructor | `ValidatorTests`, `BattlefieldTests` |
| unknown key | `ParameterParser.readPairs` rejects keys outside `{a,t,o,u,f}` | — | — | `ParserTests` |

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
| NxN field, min 5, max 1000, default 10 | `Battlefield`, `ParameterValidator`, `ParameterParser` | `ValidatorTests` (both bounds, exactly full), `BattlefieldTests` (min, max, below, above) |
| Random collision-free deployment | `RandomDeployer` | `DeploymentTests` (no repeated position over repeated runs), `BattlefieldTests` (exclusive `place`) |
| Every troop deployed once and preserved through sorting and formation | `RandomDeployer`, `FormationArranger` | `DeploymentTests`, `FormationTests` (identity preserved) |
| Capacity and one-line-per-type validation | `ParameterValidator.validateFormationCapacity` | `ValidatorTests` (group wider than line, too many groups, empty config) |
| Sorting by range, one shared criterion, identifier tie-break | `TroopComparator`, `sorting.strategies.*` | `SortingTests` (orders by range not health, all algorithms agree ascending and descending) |
| Four or more working algorithms (eight provided) | `SortingAlgorithm` | `SortingTests` (every strategy on every dataset shape) |
| Orientation N/S/E/W, one type per line | `Orientation`, `FormationArranger` | `FormationTests` (four orientations, no mixed line, single type, exact line) |
| Performance measured around sorting only | `LegionApplication.sortTroops` | manual runs (`Sorting time` line) |
| Centralized exception handling, every exception type in use | `errors.LegionException`, `errors.ExceptionHandler`, `errors.types.*` | `ParserTests`, `ValidatorTests`, `BattlefieldTests`, error runs |
| No regression of midterm behaviour | full pipeline | `RegressionTests` |
| Interactive session decoupled from the core | `commands.GameLoop` | run logs |
| Visualization: configuration, initial field, final field, algorithm, sorting time, row/column indices, legend | `BattlefieldRenderer`, `LegionApplication` | run logs |
