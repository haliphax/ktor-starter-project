#!/usr/bin/env bash
# bootstrap developer toolchain

# fail early
set -eo pipefail

# change context to root folder of repository
cd "$(realpath "$(dirname "$0")")/.." || (echo "EPIC FAIL"; exit 1)

# prerequisites
prereqs=("npm" "npx" "pre-commit")

# check for prerequisites
missing_prereqs=0

for prereq in "${prereqs[@]}"; do
  if [[ -z "$(which "$prereq")" ]]; then
    if [[ $missing_prereqs -eq 0 ]]; then
      echo "The following prerequisites are missing:" >&2
      missing_prereqs=1
    fi

    echo " - $prereq" >&2
  fi
done

# fail if any prerequisites are missing
if [[ $missing_prereqs -ne 0 ]]; then
  exit 1
fi

# install dev dependencies
npm install

# install pre-commit system and hooks
pre-commit install --install-hooks

# install gitmoji hook
scripts/install-gitmoji-hook.sh

# prepare gradle wrapper, resolve dependencies
echo "Preparing Gradle environment"
./gradlew tasks --refresh-dependencies >/dev/null
