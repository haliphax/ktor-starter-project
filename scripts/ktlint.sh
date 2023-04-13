#!/usr/bin/env bash
# ktlint formatter pre-commit hook
KTLINT_VERSION="0.48.2"
KTLINT_PATH=".cache/ktlint.${KTLINT_VERSION}"

# ensure .cache directory exists
if [[ ! -d ".cache" ]]; then
  mkdir .cache
  chmod a+r .cache
fi

# check for/download ktlint
if [[ ! -f "$KTLINT_PATH" ]]; then
  # clean out old versions
  rm ".cache/ktlint*" || true

  # download executable to cache folder
  curl -sSLO \
    "https://github.com/pinterest/ktlint/releases/download/${KTLINT_VERSION}/ktlint"
  chmod a+x ktlint
  mv ktlint "$KTLINT_PATH"
fi

# run ktlint against files; exclude generated source
# shellcheck disable=SC2068
"$KTLINT_PATH" -F --relative "!**/generated/**" "!**/proto/**" $@
