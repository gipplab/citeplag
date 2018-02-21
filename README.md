# Overview #

The CitePlag backend is mainly written in Java and operates on a MySQL database. The schema and documentation of the database is included in the *doc/database_doc directory* of the repository.

After parsing documents and storing them in the database, CitePlag retrieves document data to pre-compute text-based and citation-based similarities using the algorithms in the directory *backend/src/org/sciplore/cbpd/alg* and stores the results called "patterns" in the database. 

The frontend, which is available at [www.citeplag.org](http://www.citeplag.org), is a CakePHP application that is de-coupled from the backend. The frontend retrieves and visualizes the data in the database.

For detecting text matches, CitePlag uses Encoplot by Christian Grozea, which is a light-weight, fast, and accurate text-based detection tool. Encoplot itself is a small C script. The code for Encoplot is avalaible in this [paper](http://ceur-ws.org/Vol-502/paper2.pdf)

To extract citation and reference data from documents, CitePlag uses [ParsCit](http://aye.comp.nus.edu.sg/parsCit/).

The backend code includes adapter classes to include the functionality of Encoplot and ParsCit.

If you are interested in details on the detection algorithms or the CitePlag system, we suggest taking a look at the [doctoral thesis of Bela Gipp](http://sciplore.org/pub/thesisbelagipp).

# Please cite as: #

N. Meuschke, B. Gipp, and C. Breitinger. CitePlag: A Citation-based Plagiarism Detection System Prototype. In Proceedings of the 5th International Plagiarism Conference, Newcastle upon Tyne, UK, July 16-18 2012.

*BibTeX:*

```
#!tex

@INPROCEEDINGS{Meuschke2012,
   author    = {Meuschke, Norman and Gipp, Bela and Breitinger, Corinna},
   title     = {CitePlag: A Citation-based Plagiarism Detection System Prototype},
   booktitle = {Proceedings of the 5th International Plagiarism Conference},
   year      = {2012},
   location  = {Newcastle upon Tyne, UK}
}
```
