# ONTOQA

*Question Answering system with ontology-based natural language processing*

*Coursework in Artificial Intelligence 2016/2017*

Natural language processing (NLP) is a family of technologies that can disruptively reshape human-machine interaction,
data-driven decision making and daily exploration of knowledge by humans. One of the most representative and widely
studied NLP application is question-answering (Q&A). With the fast growing diffusion of semantic web and advancement in
NLP technologies, Q&A systems will be one of the most important interface to knowledge.
Since it is not possible to develop a single ontology to effectively capture the whole knowledge, it is necessary to develop
Q&A systems that can easily adapt to distinct ontologies and lexicons.
In this work we describe Ontoqa, a Q&A web and standalone application which aims to achieve this ambitious goal. The
proposed solution leverages ontology-driven NLP through the use of the LTAG/DUDES model and a greedy parsing algorithm
aiming to reduce both the syntactic and semantic search space. The experimental results show that our system can answer the
benchmark questions, with good performance with respect to both response-time. Our work shows that the grammar to the
ontology through the LTAG/DUDES model permits high modularization and generalization of the NLP process. Furthermore,
such a model suits well to the design of parsing algorithms that can effectively limit both the syntactic and semantic search
space.

## Requirements
To correctly build the app, the following libraries must be installed in the local Maven repository:
 
* DUDES
* lemon.api


## Build
The app building is provided by Apache Maven. To build the app you need to run

    $app> mvn clean package

If you want to skip tests:

    $app> mvn clean package -P skip-tests


## Usage 
To run the app, you need to run

    $ontoqa>java -jar target/ontoqa-1.0.jar [YOUR_ARGUMENTS]

For example, to specify a non default configuration file, you need to run

    $ontoqa>java -jar target/ontoqa-1.0.jar --config path/to/config.yaml


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
