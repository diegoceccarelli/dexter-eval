Dexter Eval
===========

Dexter Eval is an entity linking evaluation framework, inspired by the [bat-framework [1]](#link1).
 
The entity linking task aims at identifying 
all the small text fragments in a document 
referring to an entity contained in a given 
knowledge base. In our setting the KB is Wikipedia.

Given a text T,  an entity linker usually annotates
it producing a list of **spots** (aka mentions, or annotations), 
i.e., substrings of text that refer to an entity. The linker
associates to each spot a Wikipedia/Dbpedia entity 
(represented by the Wikipedia `pageid` [details](http://www.mediawiki.org/wiki/API:Query)).

Each annotation returned by a linker then can be represented
as a tuple `<start, end, wiki-id, score>` where:

 * `start` represents the starting position of the spot in the annotated text;
 * `end` represents the ending position;
 * `wiki-id` the id of the entity associated by the linker;
 * `score` the confidence of the linker on the entity. associated. 
 
Researchers produced [several datasets [2]](#link2) for evaluating
entity linking methods, but **the main problem is how to evaluate?** Depending on your needs, you could only consider the annotated wiki-ids (with no interest for the positions), or you would like to ignore the entities and check if your entity linker is good in detecting the correct positions
of the spots and so on. **Dexter-eval** allows you to 
evaluate your method with the //metrics// you prefer. 
 
## How to evaluate
 
**setup**: just clone the project:

    git clone https://github.com/diegoceccarelli/dexter-eval.git
	cd dexter-eval
 
**evaluate**:
	
	./scripts/evaluate.sh <predictions> <golden-truth> <metric> <conf>
 
where: 
  
* `predictions` is a file containing the predictions of your entity linker (in the next the supported formats will be explained);
* `golden-truth` is a file containing the golden-truth predictions;
* `metric` how to decide if two annotations represent a **match**;
* `conf` a file describing which measures (e.g., Precision, Recall ...) compute and how to output the values.


The folder `example` contains the `iitb` [[3]](#link3) annotated dataset, and the predictions produced by the [Wikiminer](http://wikipedia-miner.cms.waikato.ac.nz/) entity linker, you can benchmark wikiminer running the command: 

    ./scripts/evaluate.sh example/wikiminer-iitb-predictions.json.gz example/iitb-dataset.json.gz Me example/conf-macro-measures.txt

In the example, using the metric `Me`, two annotated spots will be considered the same if they have the same `wikiid`.

Please note that since there could be several occurrences of the same entity (in different positions), a prediction and 
its golden truth are always prefiltered removing multiple occurrences of the same annotation (based on the selected metric.). See the available metrics section for more details.
Moreover invalid annotations (referring to an unknown entity, `wikiid==0`, or referring to a disambiguation page `wikiid< 0`) are currently filtered out (this is reflected by the codes `[-noId][-noDisamb]` in the results). 

In the following we will describe the [file formats](#file-formats), the available [metrics](#comparison-metrics), how to [write a configuration file](#configuration-file) 

## File Formats

dexter-eval accepts predictions and golden-truth predictions in two different formats, i.e., **json** or **tsv**.

###The JSON format

A JSON file contains one [annotated record](src/main/java/it/cnr/isti/hpc/dexter/eval/AssessmentRecord.java) per line encoded in json. A record represents a document with its annotations. An annotated record is identified by its `docid` and can contain the text of the document (but it is not mandatory). It also contains a list of annotations, where
an [annotation](src/main/java/it/cnr/isti/hpc/dexter/eval/AnnotatedSpot.java) is characterized by:

* `docId` the doc-id of the annotated record (not mandatory);
* `spot` the piece of text annotated; 
* `start` the position in the text where the annotation starts;
* `end` the position in the text where the annotation ends;
* `entity` the wiki-id of the entity associated the spot;
* `wikiname` the label associated to the wiki-id (not mandatory);
* `confidenceScore` the confidence score associated by the entity linker to the mapping (it is not mandatory and it is not considered in the golden-truth records).

You can find an example of annotated record [here](src/test/resources/prediction-example.json) (use [this 
service](http://jsonviewer.stack.hu/) to analyze the structure of the JSON).

###The TSV format

dexter-eval also supports Tab Separated Values (tsv) Files, a tsv file is composed by several lines,
and each line represent an [annotated record](src/main/java/it/cnr/isti/hpc/dexter/eval/AssessmentRecord.java). The format of the line 
is: 

	 docid \t spot \t start \t end \t entity (wiki-id) \t wikiname \t confidence score

where:

* `docId` the doc-id of the annotated record (mandatory);
* `spot` the piece of text annotated; 
* `start` the position in the text where the annotation starts;
* `end` the position in the text where the annotation ends;
* `entity` the wiki-id of the entity associated the spot;
* `wikiname` the label associated to the wiki-id (not mandatory);
* `confidenceScore` the confidence score associated by the entity linker to the mapping (it is not mandatory and it is not considered in the golden-truth records).

If a field is not mandatory and you don't want to use it put an empty string or 0 in case of numbers,
You can find an example of annotated record [here](src/test/resources/goldentruth-test.tsv);

### Other formats

If you need to read the golden-truth and/or your predictions in other formats you can add your 
reader implementing the [AnnotatedSpotReader](src/main/java/it/cnr/isti/hpc/dexter/eval/reader/AnnotatedSpotReader.java) interface. 

## Comparison Metrics

As stated in the beginning, depending on the application, there could be different ways to consider
if an annotation in the predictions and an annotation in the golden-truth represent the same annotation
(i.e., a match). dexter-eval provides the following metrics: 

  * `Me` (match entities): two annotations are the same if the are annotated with the same entity;
  * `Mwm` (weak mention comparator): two annotations match if their positions overlap (no checks on the correctness of the associated entity);
  * `Mwa` (weak annotation comparator): two annotations are the same if they are annotated with the same entity and windows of text where the two annotations are performed overlap.

You can add a different comparison metric implementing the interface [AnnotatedSpotComparator](src/main/java/it/cnr/isti/hpc/dexter/eval/cmp/AnnotatedSpotComparator.java). 
We remark that based on the comparison metric used, the golden truth or the prediction could contain 
several occurrences of the same item. For this reason, before the evaluation, both the lists are checked
and if there are multiple occurrences only item only the encountered in the list will be kept. 

## Configuration File

The configuration file allows to define: 
  
  1. **Measures** the benchmark measures to compute for the predictions, with respect of the golden-truth;
  2. **Filters** for each benchmark measure, particular filters to the predictions/golden-truth elements can be added;
  3. **Output writers** for each benchmark measure, you can define how the results should be presented in output.
  
The configuration file is a plain text file where each line represents a measure. The format of a line is:

	Measure[x][y][z][..]

Where `Measure` is the name of a measure, and `x`,`y`,`z` and so on, are names of filters or output writers
(that should be enclosed between brackets). You can find an example of configuration file [here](example/conf-macro-measures.txt).

### Benchmark measures

dexter-eval provides the following benchmark measure: 

* `tp` **true positives**;
* `fp` **false positives**;
* `fn` **false negative**;
* `P`  **macro precision**;
* `R`  **macro recall**;
* `F1`  **macro Fmeasure**;
* `microP` **micro precision**;
* `microR` **micro recall**;
* `microF1` **micro Fmeasure**;

For details on how the measures work and how they are computed, please refer to [[2]](#link2).

### Benchmark filters

You can add [filters](src/main/java/it/cnr/isti/hpc/dexter/eval/filter) that are applied to the predictions before evaluating the performance:

* `[@k]` **Top-k filter**: it filters out only the top-k predictions with the highest confidence score (it expects k to be an integer). Using this filter allows to compute measures @k, for example you can compute precision at 1 adding the line `P[@1]` to the configuration file, or adding `F1[@5]` for the F1 measure at 5. 
* `[tk]` **Threshold filter**:   it filters out only the predictions with confidence greater than k (it expects k to be a double). Using this filter allows to compute measures varying the value of the confidence. For example you can compute precision with confidence greater or equal than 0.5 adding `P[t0.5]` to the configuration file, or adding `F1[@t0.8]` for the F1 measure considering only the predictions with confidence >= 0.8.

### Output Writers

Output can be produced in several different ways, implementing the [OutputResultsAppender](src/main/java/it/cnr/isti/hpc/dexter/eval/output/OutputResultsAppender.java) class.
Currently dexter-eval provides only	a [console appender](src/main/java/it/cnr/isti/hpc/dexter/eval/output/ConsoleResultsAppender.java) which prints the results
on the stdout. By default, each measure defined in the configuration file has its console appender by default,
but you can set up a console appender that prints all the partial evaluations (for debugging purposes) adding the code `[>c+]`.



## References

<a name="link1">[1]</a> The **Bat-framework**: [Code on Github](https://github.com/marcocor/bat-framework), [website](http://acube.di.unipi.it/bat-framework/)

<a name="link2">[2]</a> A Framework for Benchmarking Entity-Annotation Systems, Marco Cornolti and Paolo Ferragina and Massimiliano Ciaramita, Proceedings of the International World Wide Web Conference (WWW 2013) [[PDF]](http://static.googleusercontent.com/media/research.google.com/en//pubs/archive/40749.pdf). The paper describes several available datasets. 

<a name="link3">[3]</a> [CSAW Dataset](http://www.cse.iitb.ac.in/~soumen/doc/CSAW/Annot/)

 
 
 



 

 

