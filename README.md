Dexter Eval
===========

Dexter Eval is an entity linking evaluation framework. 
The entity linking task aims at identifying 
all the small text fragments in a document 
referring to an entity contained in a given 
knowledge base. In our setting the KB is Wikipedia.

Given a text T,  an entity linker usually annotates
it producing a list of **Spots** (aka mentions, or annotations), 
i.e., substrings of text that refer to an entity. The linker
associates to each spot a Wikipedia/Dbpedia entity 
(represented by the Wikipedia `pageid` [details](http://www.mediawiki.org/wiki/API:Query)).

Each annotation returned by a linker then can be represented
as a tuple `<start, end, wikid, score>` where:

 * `start` represents the starting position of the spot in the annotated text;
 * `end` represents the ending position;
 * `wikiid` the id of the entity associated by the linker;
 * `score` the confidence of the linker on the entity. associated. 

 

