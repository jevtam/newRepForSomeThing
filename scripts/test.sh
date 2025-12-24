#!/usr/bin/env bash
set -euo pipefail

pass=0
total=9

run() {
  expected=$1; shift
  set +e
  ./scripts/run.sh "$@" >/dev/null 2>&1
  rc=$?
  set -e
  if [ $rc -eq $expected ]; then
    echo "OK  ($expected) $*"
    pass=$((pass+1))
  else
    echo "FAIL(exp:$expected got:$rc) $*"
  fi
}

set +e; ./scripts/run.sh --help >/dev/null 2>&1; rc=$?; set -e
if [ $rc -eq 1 ]; then
  echo "OK  (1) --help";
  pass=$((pass+1));
else
  echo "FAIL(exp:1 got:$rc) --help";
fi

run 0 --login alice --password qwerty --action read --resource A.B.C --volume 10
run 2 --login alice --password wrong --action read --resource A.B.C --volume 10
run 3 --login charlie --password qwerty --action read --resource A.B.C --volume 10
run 4 --login alice --password qwerty --action remove --resource A.B.C --volume 10
run 5 --login alice --password qwerty --action exec --resource A.B.C --volume 1
run 6 --login alice --password qwerty --action read --resource A.X.Y --volume 1
run 7 --login alice --password qwerty --action read --resource A.B.C --volume notint
run 8 --login alice --password qwerty --action read --resource A.B.C --volume 999

echo
echo "$pass/$total passed"
