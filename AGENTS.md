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
