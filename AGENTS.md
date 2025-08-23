# AGENTS.md

Think of this file as a *README for agents*. It tells you how to build,
test, and validate fixes in this repo, plus the exact commands we expect
you to run.

------------------------------------------------------------------------

## Project at a glance

-   Monorepo-style Gradle build (single root project; many components
    under `applications/`, `framework/`, etc.)
-   Java 8 source/target compatibility (build runs fine on newer JVMs)
-   Tests are launched **from the root** (there is **no**
    `:applications:accounting:test` task)

Directory highlights: - `applications/accounting/src/main/java/...` ---
tax calculation code (e.g., `TaxAuthorityServices.java`, helpers) -
`applications/product/minilang/.../PriceServices.xml` --- price display
logic (minilang) - `applications/accounting/src/test/java/...` --- JUnit
tests you add for fast validation

------------------------------------------------------------------------

## Setup

``` bash
# from repo root
./gradlew --version        # sanity check Gradle/JDK
./gradlew clean build      # compile everything
```

If you need Java, use the system JDK (project sets
`sourceCompatibility = '1.8'`).

------------------------------------------------------------------------

## How to run tests (the right way)

> ✅ Always run tests from the **repo root**.

``` bash
# run all tests
./gradlew test

# re-run tests verbosely for a single class
./gradlew test --tests 'org.apache.ofbiz.accounting.tax.TaxPreferencesTest' --rerun-tasks --info
```

**Do NOT** run `:applications:accounting:test` --- that task does not
exist here.

------------------------------------------------------------------------

## Fast validation checklist (before making a PR)

1)  **Build & unit tests**

``` bash
./gradlew clean test
```

Expect `BUILD SUCCESSFUL`. If you added a test, verify the XML/HTML
reports under: - `build/test-results/test/TEST-*.xml` -
`build/reports/tests/test/index.html`

2)  **Targeted smoke checks (shell)** Use `rg` (ripgrep) to confirm you
    removed obvious regressions:

-   Hard-coded tax rate not present:

``` bash
rg -n '0\.25' applications/accounting/src/main/java/org/apache/ofbiz/accounting/tax/TaxAuthorityServices.java
rg -n 'set field="parameters\.taxPercentage" value="0\.25"' applications/product/minilang/product/PriceServices.xml
```

-   Tax-inclusive flag checks are correct:

``` bash
rg -n '"taxInPrice"' applications/accounting/src/main/java/org/apache/ofbiz/accounting/tax/TaxAuthorityServices.java
rg -n 'parameters\.taxInPrice' applications/product/minilang/product/price/PriceServices.xml
# Expect inclusive paths to use "Y" (not "N") where appropriate
```

3)  **Minimal business test (logic sanity)** For a JP item priced
    **¥5,000** with **10%** tax:

-   If **tax-inclusive**: total should remain **¥5,000**; extracted tax
    ≈ **¥455** (5000 × 0.10 / 1.10), rounded per rules.
-   If **exclusive**: tax should be **¥500**, total **¥5,500**.

Automate this with a small JUnit test when you change core tax or price
paths.

------------------------------------------------------------------------

## Typical agent workflows

### A) Investigate a "tax added on top of inclusive price" bug

1.  Grep likely sources:

``` bash
rg -n 'taxInPrice|VAT_TAX|SALES_TAX|taxPercentage' applications/
```

2.  Inspect:
    -   `TaxAuthorityServices.java` for inclusive vs. exclusive math
    -   `PriceServices.xml` for minilang gates around
        `parameters.taxInPrice`
3.  Add/adjust a focused unit test (e.g., `TaxPreferencesTest`) to lock
    behavior.
4.  Run:

``` bash
./gradlew test --rerun-tasks
```

### B) Add a unit test

Create under:

    applications/accounting/src/test/java/org/apache/ofbiz/accounting/tax/YourTestName.java

Then:

``` bash
./gradlew test --tests 'org.apache.ofbiz.accounting.tax.*' --rerun-tasks
```

------------------------------------------------------------------------

## Coding & PR expectations (short)

-   Keep changes minimal and well-scoped.
-   Include **at least one** failing test before the fix (if practical),
    then make it pass.
-   In PR description, include:
    -   **Problem** (1--2 lines)
    -   **What changed**
    -   **How you validated** (commands + test names)
    -   **Risk** (if any)

Example PR checklist: - \[ \] Built with `./gradlew clean build` - \[ \]
Tests: `./gradlew test` (list specific tests) - \[ \] `rg` smoke checks
(attach outputs if relevant)

------------------------------------------------------------------------

## Common pitfalls

-   Running subproject tasks like `:applications:accounting:test` → not
    defined here. Use root `./gradlew test`.
-   Fixing downstream invoice adjustments (`InvoiceServices`) when the
    issue is upstream in **tax** or **price** services.
-   Leaving hard-coded literals (e.g., `0.25`) or inverted
    `"taxInPrice"` gates (`"N"` vs `"Y"`).

------------------------------------------------------------------------

## Tools you may use

-   Gradle CLI (`./gradlew …`)
-   Ripgrep (`rg`) for code search
-   JUnit 4 for unit tests

No external services are required to validate fixes.

------------------------------------------------------------------------
