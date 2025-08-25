# AGENTS.md

This file explains how to build and test code in this repo.

------------------------------------------------------------------------

## Build & Environment

-   This is a **Gradle Java project** (monorepo style).
-   Run commands from the **repo root**.
-   Java compatibility: source/target 1.8 (compiles fine on newer JDKs).

------------------------------------------------------------------------

## Common Commands

``` bash
# Clean & build everything
./gradlew clean build

# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests 'org.apache.ofbiz.accounting.tax.SomeTest' --rerun-tasks --info

# See available tasks
./gradlew tasks
```

❌ Do **not** run subproject tasks like `:applications:accounting:test`
--- those don't exist.\
✅ Always run tests from the root.

------------------------------------------------------------------------

## Tests

-   JUnit 4 is supported.

-   Place new tests under:

        applications/<component>/src/test/java/...

-   Use `./gradlew test` to execute them.\

-   Test results:

    -   XML → `build/test-results/test/`\
    -   HTML → `build/reports/tests/test/index.html`

------------------------------------------------------------------------

## Code Search

Ripgrep (`rg`) is available for exploring the codebase. Examples:

``` bash
rg -n 'taxInPrice' applications/
rg -n 'VAT_TAX' applications/
```

------------------------------------------------------------------------

## Workflow (required)

1) Triage → Search → Guard test → Change → Re-check.
   - Add a small **failing** JUnit guard test that encodes the intended behavior.
   - Make the **smallest** change to satisfy the test.
   - Re-run tests from the repo root.

2) Cross-layer consistency
   - If a behavior/flag/config appears in multiple layers (e.g., Java + XML/minilang),
     search all references and keep semantics **consistent** across those files
     in the same PR.

3) Tests: keep them simple
   - Prefer text-based assertions (read files and assert on tokens/branches).
   - Avoid runtime bootstrap in unit tests (no `component://…`, `SimpleMethod.runSimpleService`,
     `OFBizTestCase`) unless the environment is explicitly set up.

4) Post-change audit
   - After changes, run a short grep audit to confirm no stale branches/hard-coded values remain.
   - Include the commands/output in the PR description.

5) Gradle
   - Always run from the repo root:
     ```
     ./gradlew test
     ./gradlew test --tests 'fully.qualified.TestClass' --rerun-tasks --info
     ```
   - Do **not** use subproject test tasks.

------------------------------------------------------------------------

## Guidelines

-   Keep changes minimal and scoped.
-   When adding a fix, also add a test that would fail without it.
-   Use text-based assertions in tests if you only need to validate
    configuration or XML content.
-   Avoid relying on OFBiz runtime bootstrap (`component://`,
    `SimpleMethod.runSimpleService`, `OFBizTestCase`) in plain JUnit
    tests; these require full environment setup.

------------------------------------------------------------------------

## PR Checklist

-   [ ] Build passes (`./gradlew clean build`)
-   [ ] Tests pass (`./gradlew test`)
-   [ ] New/updated tests included
-   [ ] Changes are small and well-described
