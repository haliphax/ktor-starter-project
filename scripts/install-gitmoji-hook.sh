#!/usr/bin/env bash
# install npx-driven gitmoji prepare-commit-msg hook with skip check

# fail early
set -eo pipefail

hook_path="$(realpath "$(dirname "$0")")/../.git/hooks/prepare-commit-msg"

# write script file
cat >"$hook_path" <<EOF
#!/bin/sh
if [ "\$SKIP" = prepare-commit-msg ]; then
  exit 0
fi
exec < /dev/tty
npx --package=gitmoji-cli gitmoji --hook \$1 \$2
EOF

# mark as executable
chmod a+x "$hook_path"
