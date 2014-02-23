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

In the following we will describe the [file formats](#file-formats), the available [metrics](#metrics), how to [write a configuration file](#configuration-file) 

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
reader implementing the [AnnotatedSpotReader](src/main/java/it/cnr/isti/hpc/dexter/eval/reader/AnnotatedSpotReader.java)
interface. 

## Metrics



## Configuration File

configuration 





<a name="link1">[1]</a>[bat-framework github](https://github.com/marcocor/bat-framework), [bat-framework website](http://acube.di.unipi.it/bat-framework/)

<a name="link2">[2]</a> A Framework for Benchmarking Entity-Annotation Systems, Marco Cornolti and Paolo Ferragina and Massimiliano Ciaramita, Proceedings of the International World Wide Web Conference (WWW 2013) [[PDF]](http://static.googleusercontent.com/media/research.google.com/en//pubs/archive/40749.pdf). The paper describes several available datasets. 

<a name="link3">[3]</a> [CSAW Dataset](http://www.cse.iitb.ac.in/~soumen/doc/CSAW/Annot/)

 
 
 



 

 

