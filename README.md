# ONTOQA

*Question Answering system with ontology-based natural language processing*

*Coursework in Artificial Intelligence 2016/2017*

Ontoqa is a Question&Answering (Q&A) system for knowledge bases that adhere to the semantic web standards for data representation.
The system accepts questions formulated in natural language (english), translates them in SPARQL queries and submits them to the knowòedge base to show results.
The system has been built on a specific knowledge base, but should support adaptability to different ontologies.


## Build
The app building is provided by Apache Maven. To build the app you need to run

    $app> mvn clean package

If you want to skip tests:

    $app> mvn clean package -P skip-tests

If you want to build with code optimization:

    $app> mvn clean package -P optimize
    
To correctly build the app, the following libraries must be installed in the local Maven repository:
 
* DUDES
* lemon.api


## Usage
The app can be run both with and without Apache Maven.


### Usage with Apache Maven
To run the app with Apache Maven, you need to run

    $app>mvn exec:java -Dargs="YOUR ARGUMENTS HERE"

For example, to print the app version, you need to run

    $app>mvn exec:java -Dargs="--version"

Running the app this way could be useful during development,
because it is repackaged at every execution.


### Usage without Apache Maven    
To run the app without Apache Maven, you need to run

    $>java -jar path/to/ontoqa-1.0-SNAPSHOT-jar-with-dependencies.jar YOUR ARGUMENTS HERE

For example, to print the app version, you need to run

    $>java -jar path/to/ontoqa-1.0-SNAPSHOT-jar-with-dependencies.jar --version


## Authors
Antonella Botte, [abotte@acm.org](mailto:abotte@acm.org)

Giacomo Marciani, [gmarciani@acm.org](mailto:gmarciani@acm.org)

Debora Partigianoni, [dpartigianoni@acm.org](mailto:dpartigianoni@acm.org)


## References
Antonella Botte, Giacomo Marciani and Debora Partigianoni. 2017. *com.acmutv.ontoqa.OntoqaMain, Question-Answering with ontology-based natural language processing*. Courseworks in Artificial Intelligence. University of Rome Tor Vergata, Italy [Read here](https://gmarciani.com)

Philipp Cimiano, Christina Unger, and John McCrae. 2014. *Ontology-based semantics of natural language*. Morgan Claypool Publishers. [Read here](http://www.morganclaypool.com/doi/abs/10.2200/S00561ED1V01Y201401HLT024?journalCode=hlt)

Ronald Brachman and Hector Levesque. 2004. *Knowledge Representation and Reasoning*. Morgan Kaufmann Publishers Inc., San Francisco, CA, USA. [Read here](http://dl.acm.org/citation.cfm?id=975621)


## License
The project is released under the [MIT License](https://opensource.org/licenses/MIT).
