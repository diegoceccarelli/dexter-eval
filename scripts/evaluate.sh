#!/usr/bin/env bash
source ./scripts/config.sh

EXPECTED_ARGS=2

METRICS=metrics.txt

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: `basename $0` predictions[.json,.tsv] goldentruth[.json,.tsv]"
  exit $E_BADARGS
fi

echo "evaluate prediction $1 against $2"
$JAVA -Dmetrics=metrics.txt $CLI.EvaluatorCLI -input $1 -gt $2