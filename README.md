## Amazon Books Scavenger ##
A web browser-based search tool that allows a user to search a library database of books (a CSV file sourced from Amazon) by author, title, or description.

Search results are sorted using a mergesort algorithm. Books can be loaded in either a B+ tree or a hash map structure. A primary goal of this project was to analyze and compare the performance of both data structures when conducting a search using the tool.

Co-developed with David Dexter (OrganomagnesiumHalide) and Sufia Rashid (SufiaRashid).

I was primarily responsible for implementing the backend functionality of loading books into a hash map (Hash.java) and the mergesort algorithm for sorting search results (MergeSort.java). I also assisted with functionality for filtering books by certain parameters (BookService.java).

## Usage
Run ```make``` to start the server (must have JDK 17 installed), then navigate to http://localhost:8080/.
