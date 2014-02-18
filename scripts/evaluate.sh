#!/usr/bin/env bash
source ./scripts/config.sh

EXPECTED_ARGS=2

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: `basename $0` predictions[.json,.tsv] goldentruth[.json,.tsv]"
  exit $E_BADARGS
fi

echo "evaluate prediction $1 against $2"
$JAVA $CLI.EvaluatorCLI  -input $1 -gt $2
