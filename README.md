Dexter Eval
===========

Dexter Eval is an entity linking evaluation framework, inspired by the **bat-framework** [1].
 
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
 
Researchers produced several datasets[2] for evaluating
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


The folder `example` contains the `iitb` [3] annotated dataset, and the predictions produced by the [Wikiminer](http://wikipedia-miner.cms.waikato.ac.nz/) entity linker, you can benchmark wikiminer running the command: 

    ./scripts/evaluate.sh example/wikiminer-iitb-predictions.json.gz example/iitb-dataset.json.gz Me example/conf-macro-measures.txt

In this example, using the metric `Me`, two annotated spots will be considered the same if they have the same `wikiid`.

Please note that since there could be several occurrences of the same entity (in different positions), a prediction and 
its golden truth are always prefiltered removing multiple occurrences of the same annotation (based on the selected metric.). See the available metrics section for more details.
Moreover invalid annotations (referring to an unknown entity, `wikiid==0`, or referring to a disambiguation page `wikiid< 0`) are currently filtered out (this is reflected by the code `[-noId][-noDisamb]` in the results). 



 
 
[1] [bat-framework github](https://github.com/marcocor/bat-framework), [bat-framework website](http://acube.di.unipi.it/bat-framework/)
 
[2] A Framework for Benchmarking Entity-Annotation Systems, Marco Cornolti and Paolo Ferragina and Massimiliano Ciaramita, Proceedings of the International World Wide Web Conference (WWW 2013) [[PDF]](http://static.googleusercontent.com/media/research.google.com/en//pubs/archive/40749.pdf). The paper describes several available datasets. 

[3] [CSAW Dataset](http://www.cse.iitb.ac.in/~soumen/doc/CSAW/Annot/)

 
 
 



 

 

