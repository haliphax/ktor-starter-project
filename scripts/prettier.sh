#!/usr/bin/env bash
# prettier formatter pre-commit hook

# run prettier against files
# shellcheck disable=SC2068
npx prettier -u -c -w $@
