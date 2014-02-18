#!/usr/bin/env bash
source ./scripts/config.sh

EXPECTED_ARGS=2

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: `basename $0` predictions[.json,.tsv] goldentruth[.json,.tsv]"
  exit $E_BADARGS
fi

PREDICTIONS=/tmp/prediction.treceval
QRELS=/tmp/gt.qrels
TREC_EVAL=./bin/trec_eval8.1 

echo "convert predictions $1 to treceval format in $PREDICTIONS"
$JAVA $CLI.TracevalConverterCLI  -input $1 -output $PREDICTIONS
echo "convert goldentruth $2 to treceval format in $QRELS"
$JAVA $CLI.QrelConverterCLI  -input $2 -output $QRELS
$TREC_EVAL -a $QRELS $PREDICTIONS
./bin/trec_eval -m set_P -m set_recall  $QRELS $PREDICTIONS


