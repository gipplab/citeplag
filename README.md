# Overview #

The CitePlag backend is mainly written in Java and operates on a MySQL database. The schema and documentation of the database is included in the *doc/database_doc directory* of the repository.

After parsing documents and storing them in the database, CitePlag retrieves document data to pre-compute text-based and citation-based similarities using the algorithms in the directory *backend/src/org/sciplore/cbpd/alg* and stores the results called "patterns" in the database. 

The frontend, which is available at [www.citeplag.org](http://www.citeplag.org), is a CakePHP application that is de-coupled from the backend. The frontend retrieves and visualizes the data in the database.

For detecting text matches, CitePlag uses Encoplot by Christian Grozea, which is a light-weight, fast, and accurate text-based detection tool. Encoplot itself is a small C script. The code for Encoplot is avalaible in this [paper](http://ceur-ws.org/Vol-502/paper2.pdf)

To extract citation and reference data from documents, CitePlag uses [ParsCit](http://aye.comp.nus.edu.sg/parsCit/).

The backend code includes adapter classes to include the functionality of Encoplot and ParsCit.

If you are interested in details on the detection algorithms or the CitePlag system, we suggest taking a look at the [doctoral thesis of Bela Gipp](http://sciplore.org/pub/thesisbelagipp).

## Please cite as: ##

N. Meuschke, B. Gipp, and C. Breitinger. CitePlag: A Citation-based Plagiarism Detection System Prototype. In Proceedings of the 5th International Plagiarism Conference, Newcastle upon Tyne, UK, July 16-18 2012.

*BibTeX:*

```tex

@INPROCEEDINGS{Meuschke2012,
   author    = {Meuschke, Norman and Gipp, Bela and Breitinger, Corinna},
   title     = {CitePlag: A Citation-based Plagiarism Detection System Prototype},
   booktitle = {Proceedings of the 5th International Plagiarism Conference},
   year      = {2012},
   location  = {Newcastle upon Tyne, UK}
}
```
## Related Publications ##
- N. Meuschke, M. Schubotz, F. Hamborg, T. Skopal, and B. Gipp, “Analyzing Mathematical Content to Detect Academic Plagiarism,” in Proceedings of the International Conference on Information and Knowledge Management (CIKM), Singapore, 2017. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/meuschke2017b.pdf)

- N. Meuschke, N. Siebeck, M. Schubotz, B. Gipp, “Analyzing Semantic Concept Patterns to Detect Academic Plagiarism,” in Proceedings of the 6th International Workshop on Mining Scientific Publications (WOSP) held in conjunction with the ACM/IEEE-CS Joint Conference on Digital Libraries (JCDL), 2017. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/meuschke2017a.pdf) 

- Gipp, Citation-based Plagiarism Detection – Detecting Disguised and Cross-language Plagiarism using Citation Pattern Analysis, Springer Vieweg Research, 2014. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/thesisbelagipp.pdf) 

- N. Meuschke and B. Gipp, “Reducing Computational Effort for Plagiarism Detection by using Citation Characteristics to Limit Retrieval Space”, in Proceedings of the IEEE-CS/ACM International Conference on Digital Libraries (DL), 2014, pp. 197-200. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/meuschke14.pdf)

- B. Gipp, N. Meuschke, and C. Breitinger, “Citation-based Plagiarism Detection: Practicability on a Large-scale Scientific Corpus”, Journal of the American Society for Information Science and Technology, vol. 65, iss. 2, pp. 1527-1540, 2014. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/gipp13b.pdf)

- B. Gipp, N. Meuschke, C. Breitinger, J. Pitman, and A. Nuernberger, “Web-based Demonstration of Semantic Similarity Detection using Citation Pattern Visualization for a Cross Language Plagiarism Case”, in Proceedings International Conference on Enterprise Information Systems (ICEIS), Special Session on Information Systems Security, Lisbon, Portugal, 2014, pp. 677-683. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/gipp14a.pdf)

- N. Meuschke and B. Gipp, “State of the Art in Detecting Academic Plagiarism”, International Journal for Educational Integrity, vol. 9, iss. 1, pp. 50-71, 2013. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/meuschke13.pdf)

- B. Gipp, N. Meuschke, C. Breitinger, M. Lipinski, and A. Nuernberger, “Demonstration of Citation Pattern Analysis for Plagiarism Detection”, in Proceedings International ACM SIGIR Conference on Research and Development in Information Retrieval (SIGIR), 2013. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/gipp13.pdf)

- B. Gipp and N. Meuschke, “Citation Pattern Matching Algorithms for Citation-based Plagiarism Detection: Greedy Citation Tiling, Citation Chunking and Longest Common Citation Sequence”, in Proceedings ACM Symposium on Document Engineering (DocEng), 2011. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/gipp11c.pdf)

- B. Gipp, N. Meuschke, and J. Beel, “Comparative Evaluation of Text- and Citation-based Plagiarism Detection Approaches using GuttenPlag”, in Proceedings ACM/IEEE-CS Joint Conference on Digital Libraries (JCDL), 2011. [(PDF)](https://www.gipp.com/wp-content/papercite-data/pdf/gipp11.pdf)
