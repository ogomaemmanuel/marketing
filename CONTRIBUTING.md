# Contributing to marketing

Thanks for your interest in contributing! By participating, you agree to follow this guide.

## Getting started
- Read this file.
- Look through open issues for something you can help with or open a new issue describing your proposal.
- If you're making a large or breaking change, open an issue first to discuss design.

## Reporting bugs
1. Search existing issues to avoid duplicates.
2. Create a new issue with:
   - Title: short descriptive summary
   - Steps to reproduce
   - Expected vs actual behavior
   - Environment: Java version, OS, branch/commit
   - Minimal reproducible example if possible

## Requesting features
- Open an issue labeled `enhancement` describing the use-case and proposed API or behavior.
- For larger features, include a short design note or RFC in the issue before implementing.

## Development setup
1. Install Java 17+ (or the version declared in `pom.xml`).
2. Install Maven (optional if using the included Maven wrapper).
3. Clone and build:
   - git clone https://github.com/ogomaemmanuel/marketing.git
   - cd marketing
   - ./mvnw clean package

## Branching & Pull Requests
- Create a focused topic branch from the default branch:
  - git checkout -b feature/short-description
- Keep PRs small and focused.
- Use clear, descriptive commit messages.
- Push your branch and open a PR targeting the default branch.

## Commit message conventions
- Use an imperative, brief summary line (50 chars max), a blank line, then a body if needed.
- Optional prefixes: feat:, fix:, docs:, test:, chore:, refactor:
Example:
```
feat: add message router with dispatch strategy

This adds a MessageRouter implementation to centralize dispatch logic.
```

## Tests
- Add or update unit tests for changes to behavior.
- Run tests locally: ./mvnw test
- PRs should include tests for important/new behavior where feasible.

## Code style & formatting
- Follow existing project style.
- Run any formatters or linters configured in the repo.
- Keep imports tidy and avoid trailing whitespace.

## PR checklist (fill before requesting review)
- [ ] Code builds locally
- [ ] Tests added/updated & passing
- [ ] Documentation updated (README, Javadocs)
- [ ] No secrets committed
- [ ] Commit history is tidy (squash/fixup as needed)

## Review & process
- Maintainers will review PRs and request changes if needed.
- Address review comments and update the PR until approved.

## Communication
- Use GitHub issues/PR comments for discussion.
- For larger proposals, discuss on an issue before implementing.

## License
- Contributions are covered by the project's license. (No license set yet — add LICENSE at repo root when ready.)

Thank you for improving the project!
