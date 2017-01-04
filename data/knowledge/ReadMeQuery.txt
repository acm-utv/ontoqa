Prefissi a cui fanno riferimento le query:
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX org: <http://www.semanticweb.org/debby/ontologies/2016/11/organization-ontology#>


***WHO FOUNDED MICROSOFT?
***WHO ARE THE FOUNDERS OF MICROSOFT?
***WHO IS THE CEO OF APPLE?
***WHAT IS THE NAME OF THE CEO OF APPLE?
***WHO ARE THE CORPORATE OFFICERS OF APPLE?
***WHO IS THE CHAIRMAN OF APPLE?

	" SELECT ?result
		WHERE { ?result org:isChairmanOf org:Microsoft}
	"	

	oppure
	"
	SELECT ?organization ?role ?people 
	WHERE { ?people rdf:type ?role.
		?people ?property ?organization.
		?organization rdf:type org:Organization.
		FILTER(?organization = org:Microsoft && ?property = org:isFounderOf && ?role=org:Founder)
		}
		GROUP BY ?organization ?role ?people
	"
	In quest'ultimo caso viene restituito qualcosa del tipo:
	|		organization		|		role		|		people				|
	|	:Microsoft				|	:Founder		|		:Paul_Allen			|
	|	:Microsoft				| 	:Founder		|		:Bill_Gates			|	

	Per modificare l' "Organization" soggetta a query ed il "role", basta modificare il contenuto del FILTER nel seguente modo:
	- sostituire "Microsoft" con l'organizzazione richiesta, attualmente a scelta tra "Google", "IBM" e "Apple".
	- sostituire "isFounderOf" con una proprietà a scelta tra "isCEOOf", "isCFOOf", "isCorporateOfficerOf", "isChairmanOf".
	- sostituire "Founder" con un ruolo a scelta tra "Chairman", "CEO", "CFO" e "CorporateOfficer". 
	
	Se si vuole far restituire soltanto il risultato della query, senza memorizzare nome dell'organizzazione e ruolo, basta eliminare dalla select "?organization ?role" oppure utilizzare la prima select proposta.
	
	
	
***HOW MANY PEOPLE FOUNDED MICROSOFT?

		"	SELECT ?organization ?role (str(count (distinct ?people)) as ?peopleNum)
				WHERE {?people rdf:type ?role.
					?people ?property ?organization.
					?organization rdf:type org:Organization.
					FILTER(?organization = org:Microsoft && ?property = org:isFounderOf && ?role=org:Founder)
					}
				GROUP BY ?organization ?role
		"

	Fornisce un risultato del tipo:
	|		organization		|		role		|		peopleNum		|
	|	:Google					|	:Founder		|		"2"				|
	
	Per modificare l' "Organization" soggetta a query ed il "role", basta modificare il contenuto del FILTER nel seguente modo:
	- sostituire "Microsoft" con l'organizzazione richiesta, attualmente a scelta tra "Google", "IBM" e "Apple".
	- sostituire "isFounderOf" con una proprietà a scelta tra "isCEOOf", "isCFOOf", "isCorporateOfficerOf", "isChairmanOf".
	- sostituire "Founder" con un ruolo a scelta tra "Chairman", "CEO", "CFO" e "CorporateOfficer". 
	
	Il numero di persone è restituito come "String"
	
	Se si vuole far restituire soltanto il numero richiesto, senza memorizzare nome dell'organizzazione e ruolo, basta eliminare dalla select "?organization ?role".



***WHAT IS THE NET INCOME OF MICROSOFT?
	
	" SELECT ?netIncome
		WHERE { org:Microsoft org:netIncome ?netIncome
		} 
	"


***IS SATYA NADELLA THE CEO OF MICROSOFT?

	"
		ASK { org:Satya_Nadella org:isCEOOf org:Microsoft} 
	"
	- Restituisce un valore booleano, true o false.
	- Il nome da sottoporre a query deve essere scritto sempre nella forma "org:Nome_Cognome". Se il nome o il cognome sono composti da più elementi, separarli da '_'.
	- "isCEOOf" può essere sostituito con "isCFOOf", "isCorporateOfficerOf", "isChairmanOf" o "isFounderOf".
	- "Microsoft" può essere sostituito con "Google", "IBM" o "Apple".
	
	
***WHERE IS MICROSOFT HEADQUARTERED?
	
	"
	SELECT ?headquarter
	WHERE{ org:Microsoft org:isHeadquartered ?headquarter} 
	"	
	
	oppure
	
	"
	SELECT ?organization ?headquarter 
	WHERE {	?organization a org:Organization.
		?headquartes a org:Nation.
		?organization ?property ?headquarter
		FILTER(?organization = org:Apple && ?property = org:isHeadquartered)
		}
	GROUP BY ?organization ?headquarter
	"
	
	
	
***DID MICROSOFT ACQUIRE AN ITALIAN COMPANY?
***DID MICROSOFT ACQUIRE A COMPANY HEADQUARTERED IN ITALY?

	"
		ASK { 	org:Microsoft org:acquireCompany ?company.
				?company org:isHeadquartered org:Italy
			}
	"
	oppure
	"
		ASK { 	?organization org:acquireCompany ?company.
				?company org:isHeadquartered ?headquarter
				FILTER(?organization = org:Microsoft && ?headquarter = org:Italy)
			}
	"
	
	
***IS SATYA NADELLA ITALIAN?

	"
	ASK {	org:Satya_Nadella org:nationality org:Italy		}
	"
	oppure
	
	"
		ASK	{	?person org:nationality ?nation
				FILTER(?person = org:Satya_Nadella && ?nation = org:Italy)
			}
	"


***WHAT IS THE MOST VALUABLE COMPANY?

	"
		SELECT ?company ?organization ?value
			WHERE {	?company a org:Company .
					?organization a org:Organization .
					?company org:companyValue ?value.
					?organization org:acquireCompany ?company .
				  }
			ORDER BY DESC(?value)
			LIMIT 1
	"
	Tale formulazione restituisce la compagnia più costosa con relativo valore (in billion U.S. dollars) e quale organizzazione l'ha acquisita.
	Se si vuole restituire solo ed esclusivamente il nome della compagnia più costosa, la query SPARQL diventa la seguente:
	
	"
		SELECT ?company
			WHERE {	?company a org:Company .
					?company org:companyValue ?value.
				  }
			ORDER BY DESC(?value)
			LIMIT 1
	" 


Dubbi da chiedere a Fiorelli:	

***WHO IS THE CHIEF FINANCIAL OFFICER OF APPLE?
	Da gestire l'equivalenza tra CEO e Chief Financial Officer.
***WHO IS THE PRESIDENT OF GOOGLE? 
	Da gestire l'equivalenza tra president e chairman.

- Se chiedo tutti i corporateOfficer, non mi restituisce in automatico, con un'inferenza, tutti i CEO e i CFO. L'ho gestito manualmente.

- Martin Schroeter ---> nazionalità non chiara. Attualmente è impostata negli Stati Uniti.
	