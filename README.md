# tij-crawler

## Original requirements (in polish)
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
