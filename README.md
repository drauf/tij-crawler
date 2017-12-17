# tij-crawler

## <img src="gb.svg" alt="Flag of Great Britain" height="24px"> Original requirements
The task is to write an online robot that browses resources within a given domain (reasonably large, at least 3000 subpages) belonging to a foreign university and saves copies of documents on the disk and analyzes the graph of connections between them (vertex = page or resource (file), arch = link from one page (resource) to another page (resource)).
  
To obtain max.20 points, you must complete all of the following tasks:
- GUI interface,
- implementation of network communication and I/O operations in a synchronous (blocking) model,
- using a built-in pool of threads to carry out the multithreaded downloading (and saving) of documents,
- adherence to the Robots Exclusion Protocol restrictions,
- analysis of the graph of connections between documents: number of vertices and arcs, distribution of incoming and outgoing steps.
  
To obtain the next 10 points, you MUST accomplish all of the following elements of the task:
- implementation of own thread pool for execution of multithreaded downloading (and saving) documents,
- graph analysis of connections between documents: number of consistency components, average length of the shortest path between vertices (in each SCC), diameter of each graph consistency component.
  
To obtain the next 10 points, you MUST accomplish all of the following elements of the task:
- implementation of network communication and I/O operations in an asynchronous (non-blocking) model.
  
As part of this task, it is MANDATORY to create a report in which the following elements should be included:
- detailed description of the concept at the LOGICAL level (architecture + workflow) of implemented solutions (note: do not describe classes or class diagrams!),
- documenting the implementation of individual elements of the task,
- test results, including measured times of individual stages of application operations for different number of threads (1,2,4,8,16,32) for implemented communication models (synchronous and asynchronous).


## <img src="pl.svg" alt="Flag of Poland" height="24px"> Original requirements
Zadanie polega na napisaniu robota internetowego, który przegląda zasoby w obrębie podanej (rozsądnie dużej, min. 3000
podstron) domeny należącej do zagranicznej uczelni i zapisuje na dysku kopie dokumentów oraz analizuje graf połączeń między
nimi (wierzchołek=strona lub zasób (plik), łuk=link z jednej strony (zasobu) do innej strony (zasobu)).
  
Do uzyskania max.20 punktów NALEŻY zrealizować wszystkie poniższe elementy zadania:
- interfejs GUI,
- realizacja komunikacji sieciowej oraz operacji I/O w modelu synchronicznym (blokującym),
- skorzystanie z gotowej puli wątków w celu realizacji ściągania (i zapisywania) wielowątkowego dokumentów,
- przestrzeganie ograniczeń Robots Exclusion Protocol,
- analiza grafu połączeń między dokumentami: liczba wierzchołków i łuków, rozkład stopni wchodzących i wychodzących.
  
Do uzyskania kolejnych 10 punktów NALEŻY zrealizować wszystkie poniższe elementy zadania:
- implementacja własnej puli wątków w celu realizacji ściągania (i zapisywania) wielowątkowego dokumentów,
- analiza grafu połączeń między dokumentami: liczba składowych spójności, średnia długość najkrótszej ścieżki pomiędzy wierzchołkami (w każdej składowej spójności), średnica każdej składowej spójności grafu.
  
Do uzyskania kolejnych 10 punktów NALEŻY zrealizować wszystkie poniższe elementy zadania:
- realizacja komunikacji sieciowej oraz operacji I/O w modelu asynchronicznym (nieblokującym).
  
W ramach tego zadania należy OBOWIĄZKOWO zrealizować sprawozdanie, w którym NALEŻY zawrzeć następujące elementy:
- szczegółowy opis koncepcji na poziomie LOGICZNYM (architektura + workflow) zrealizowanych rozwiązań (uwaga: nie opisywać
klas ani diagramów klas!),
- udokumentowanie realizacji poszczególnych elementów zadania,
- wyniki testów, w tym zmierzone czasy poszczególnych etapów działania aplikacji dla różnej liczby wątków (1,2,4,8,16,32) dla
zaimplementowanych modeli komunikacji (synchroniczne oraz asynchroniczne).

## Areas for improvement
- Using GUI described in Java was a terrible idea - defining it in an XML document would be much cleaner  
- Custom logger was a bad idea (poor API)  
- Custom thread pool is a joke and could be improved (especially awaiting the execution and mixing Futures/Callables/Tasks)  
- Default thread pool is broken (but was way slower than the custom one anyway)  
- Graph analysis based on streams is a mental masturbation and should be changed to something maintanable  
- Robots exclusion protocol code is never called and is only there to get all the points for the assignment
- After the requirements changed, the code was heavily modified but not refactored (and it should be)  
- IIRC some exceptions were silenced  
