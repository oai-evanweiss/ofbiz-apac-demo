# AGENTS.md

This file explains how to build, test, and work safely in this repo.

---

## Build & Environment
- **Gradle Java** (monorepo style)
- Run commands from the **repo root**
- Java compatibility: source/target **1.8** (builds on newer JDKs)

---

## Common Commands
```bash
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
✅ Always run tests from the repo root.

---

## Tests
- JUnit 4 is supported
- Place new tests under:
```
applications/<component>/src/test/java/...
```
- Run with `./gradlew test`

**Results:**
- XML → `build/test-results/test/`
- HTML → `build/reports/tests/test/index.html`

---

## Code Search
Use ripgrep (`rg`) for exploring the codebase:
```bash
rg -n 'taxInPrice' applications/
rg -n 'VAT_TAX' applications/
```

---

## Workflow (required)
1. **Triage → Search → Guard test → Change → Re-check**
   - Add a small *failing* JUnit test that encodes the intended behavior.
   - Make the **smallest** change to satisfy the test.
   - Re-run tests from the repo root.

2. **Cross-layer consistency**
   - If a behavior/flag/config appears in multiple layers (Java + XML/minilang),
     search all references and keep semantics consistent across those files
     in the same PR.

3. **Tests: keep them simple**
   - Prefer text-based assertions (read files and assert on tokens/branches).
   - Avoid runtime bootstrap in tests (`component://`, `SimpleMethod.runSimpleService`, `OFBizTestCase`)
     unless the environment is explicitly set up.

4. **Post-change audit**
   - After changes, run a short grep audit to confirm no stale branches/hard-coded values remain.
   - Include the commands/output in the PR description.

5. **Gradle**
   - Always run from the repo root:
     ```bash
     ./gradlew test
     ./gradlew test --tests 'fully.qualified.TestClass' --rerun-tasks --info
     ```
   - Do **not** use subproject test tasks.

---

## Guidelines
- Keep changes minimal and scoped
- Every fix should add a test that fails without it
- Prefer text-based assertions for config/XML validation
- Don’t rely on full OFBiz runtime bootstrap in plain unit tests

---

## PR Checklist
- [ ] Build passes (`./gradlew clean build`)
- [ ] Tests pass (`./gradlew test`)
- [ ] New/updated tests included
- [ ] Changes are small and well-described
