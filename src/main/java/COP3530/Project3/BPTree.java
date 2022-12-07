package COP3530.Project3;
import java.util.Map.Entry;
import java.util.*;
import java.util.Locale;

public class BPTree {

    public static final int max_data = 5; //num of childNodes
    public int sizeBPT = 0; //size of tree
    public ArrayList<Book> bookAll = new ArrayList<>(); //arraylsit of all books

    public class Node {  //Node class, checks for leaks in tree

public class BPTree {

    public static final int max_data = 5;
    public int sizeBPT = 0;
    public ArrayList<Book> bookAll = new ArrayList<>();

    public class Node {
        protected boolean is_Leaf;
        protected ArrayList<String> titleK;

        public boolean leaks()
        {
            boolean leak = false;
            if (titleK.size() < 2 * BPTree.max_data)
            {
                leak = false;
            }
            if (titleK.size() > 2 * BPTree.max_data)
            {
                leak = true;
            }
            return leak;
        }

    }

    public class LeafNode extends Node {  //leafnode extends nodeclass
        protected ArrayList<Book> bookData; //array list for books
        protected LeafNode right; //traverse left
        protected LeafNode left;   //traverse right
        public LeafNode() {
            titleK = new ArrayList<String>();
            bookData = new ArrayList<Book>();
            is_Leaf = false;
        }

        public void setListTitle(List<String> multTitles) //set title list
        {
            titleK = new ArrayList<String>(multTitles);
        }


        public void setListBook(List<Book> multBooks)  //set book list
        {
            bookData = new ArrayList<Book>(multBooks);
            is_Leaf = true;
        }

        public void setBookData(String title, Book book) //set one title and book
        {
            titleK = new ArrayList<String>();
            titleK.add(title);
            bookData = new ArrayList<Book>();
            bookData.add(book);
            is_Leaf = true;
        }

        public void leafSort(String title1, Book book1) //sort out the leaf nodes in order
        {
            int n = titleK.size() - 1;
            int leftN = titleK.get(0).compareTo(title1);
            int rightN = titleK.get(n).compareTo(title1);

            if (leftN > 0)  //if smallest, insert at begining
            {
                titleK.add(0, title1);
                bookData.add(0, book1);
            }
            else if (rightN < 0) //if largest insert at end
            {
                titleK.add(title1);
                bookData.add(book1);
            }
            else if (!(leftN > 0) && !(rightN < 0)) //traverse to see index placement
            {
                for (int i = 0; i < titleK.size();i++)
                {
                    int index = titleK.get(i).compareTo(title1);
                    if (index > 0)
                    {
                        titleK.add(i, title1);
                        bookData.add(i, book1);
                        break;
                    }
                }

            }
        }

    }

    public class InternalNode extends Node { //extends node class

        // m nodes
        protected ArrayList<Node> child_ptr; // children of node class

        public InternalNode()
        {
            titleK = new ArrayList<String>();
            child_ptr = new ArrayList<Node>();
            is_Leaf = false;
        }

        public void setChild_ptr(String title, Node bookC, Node rootN) {
            titleK = new ArrayList<String>();
            child_ptr = new ArrayList<Node>();
            titleK.add(title);
            child_ptr.add(rootN);
            child_ptr.add(bookC);
        }


        public void setListTitleI(List<String> multTitles) //set list title
        {
            titleK = new ArrayList<String>(multTitles);
        }

        public void setListChildNode(List<Node> childNode) //set list node
        {
            child_ptr = new ArrayList<Node>(childNode);
            is_Leaf = false;
        }


        public void internalSort(int pos, Entry<String, Node> childP) //sort children by title
        {
            int n = titleK.size();
            if (pos < n) //insert front
            {
                titleK.add(pos, childP.getKey());
                child_ptr.add(pos+1, childP.getValue());
            }
            else if (pos > titleK.size() || pos == titleK.size()) { //add new spot
                titleK.add(childP.getKey());
                child_ptr.add(childP.getValue());
            }

        }

    }

    public Node rootNode;

    public void insert(String title1, Book book1) //insert function
    {
        bookAll.add(book1);
        boolean checkFirst = true;
        Entry<String, Node> childNodePlace = helperInsert(rootNode, title1, book1, null, checkFirst);
        sizeBPT++;

        if(childNodePlace != null) { //if node is not null
            String tempTitle = childNodePlace.getKey();
            Node tempB = childNodePlace.getValue();
            InternalNode tempR = new InternalNode();  //make internal node
            tempR.setChild_ptr(tempTitle, tempB, rootNode); //set node inside of tree
            rootNode = tempR; //return root
        }
    }

    private Entry<String, Node> helperInsert(Node tempN, String title1, Book book1, Entry<String, Node> addChild, boolean check)
    {
        LeafNode newL = new LeafNode();
        newL.setBookData(title1, book1); //create new leaf node

        Node tem1 = newL;

        if (check == true) //will only be true once
        {
            check = false;
            if (rootNode == null) {
                rootNode = tem1;
            }
            if (rootNode.titleK.size() == 0) {
                rootNode = tem1;
            }
            tempN = rootNode;
        }

        if (tempN.is_Leaf == true) //if node is leaf, insert right away
        {
            LeafNode firstLeaf = (LeafNode)tempN;
            LeafNode newLeaf = newL;

            firstLeaf.leafSort(title1, newLeaf.bookData.get(0));

            if (firstLeaf.leaks() == true) //if bp tree has leaks, needs to become larger
            {
                ArrayList<String> tempTitle1 = new ArrayList<String>(); //create new title arraylist
                ArrayList<Book> tempBook1 = new ArrayList<Book>(); //create new book arraylist
                int add = max_data;
                int rem = max_data;
                int k = 2*max_data;

                while (add <=k) { //add previous data to new arrays
                    String t = firstLeaf.titleK.get(add);
                    Book b = firstLeaf.bookData.get(add);
                    tempTitle1.add(t);
                    tempBook1.add(b);
                    add++;
                }
                
                while (rem <=k) {  //erase old data so that you can build new leaf
                    int s = firstLeaf.titleK.size()-1;
                    int bS = firstLeaf.bookData.size()-1;
                    firstLeaf.titleK.remove(s);
                    firstLeaf.bookData.remove(bS);
                    rem++;
                }

                String seperateTitle = tempTitle1.get(0);
                LeafNode nextLeaf = new LeafNode();
                nextLeaf.setListTitle(tempTitle1); //set new leaf title
                nextLeaf.setListBook(tempBook1); //set new leaf book data

                LeafNode tmp = firstLeaf.right;
                firstLeaf.right = nextLeaf;
                firstLeaf.right.left = nextLeaf;  //sort them to rearrange tree
                nextLeaf.left = firstLeaf;
                nextLeaf.right = tmp;

                if(firstLeaf == rootNode)  // if leaf is equal to root
                {
                    InternalNode tempR = new InternalNode();
                    tempR.setChild_ptr(seperateTitle, nextLeaf, firstLeaf); // create new internal node
                    rootNode = tempR; //rootnode is equal to tempR
                    return null;
                }
                return new AbstractMap.SimpleEntry<>(seperateTitle, nextLeaf);
            }
            else {
                return null;
            }
        }
        else if(tempN.is_Leaf == false)  //if node is internal node
        {
            int pos = 0;
            InternalNode inNodePos = (InternalNode) tempN; //create new internal node

            for (pos = 0; pos < inNodePos.titleK.size(); pos++)  //find position of title in internal node
            {
                String bName = inNodePos.titleK.get(pos);
                if(bName.compareTo(title1) >= 0)
                {
                    break;
                }
            }

            addChild = helperInsert(inNodePos.child_ptr.get(pos), title1,book1, addChild, check); //new entry map to contain title node combination

            if(addChild != null)  //if it is not null
            {
                int pos2 = 0;
                for (pos2 = 0;pos2 < inNodePos.titleK.size();pos2++) //check to see where it needs to be placed inside of tree
                {
                    String x = inNodePos.titleK.get(pos2);
                    String y = addChild.getKey();
                    if(x.compareTo(y) >= 0)
                    {
                        break;
                    }
                }

                inNodePos.internalSort(pos2, addChild); //sort the tree internal children if needed

                if(inNodePos.leaks() == true) //if tree is leaking over, create new internal nodes
                {
                    ArrayList<String> tempTitle2 = new ArrayList<String>(); //new title keys
                    ArrayList<Node> tempNode2 = new ArrayList<Node>();  //new Nodes

                    String separateTitle = inNodePos.titleK.get(max_data);
                    inNodePos.titleK.remove(max_data);

                    tempNode2.add(inNodePos.child_ptr.get(max_data+1)); //add data to new array lists
                    inNodePos.child_ptr.remove(max_data+1);  //remove old data to build new tree
                    int s = inNodePos.titleK.size();

                    while(max_data < s) {  //add new data with new node and key points
                        tempTitle2.add(inNodePos.titleK.get(max_data));
                        tempNode2.add(inNodePos.child_ptr.get(max_data+1));
                        inNodePos.titleK.remove(max_data);
                        inNodePos.child_ptr.remove(max_data+1);
                        s = inNodePos.titleK.size();
                    }

                    InternalNode tempRN = new InternalNode();
                    tempRN.setListTitleI(tempTitle2);
                    tempRN.setListChildNode(tempNode2);

                    if(inNodePos == rootNode) //if node is equal to root
                    {
                        InternalNode tempR = new InternalNode();
                        tempR.setChild_ptr(separateTitle, tempRN, rootNode); //create new node
                        rootNode = tempR; //add to rootnode and return
                        return null;
                    }
                    return new AbstractMap.SimpleEntry<>(separateTitle, tempRN);
                }
            }
        }
        return null;
    }

    private Node helperSearchTitle(Node nodeBook, String title) {  //traverses through internal nodes to find title
        if(nodeBook.is_Leaf == false) {
            InternalNode position = (InternalNode) nodeBook; //set node to internal node
            int size1 = nodeBook.titleK.size()-1;
            int size2 = position.child_ptr.size()-1;
            int size3 = position.titleK.size()-1;

            String titleComparison = position.titleK.get(0);
            if(title.compareTo(titleComparison) < 0) {  //if title is less than beginning, check left
                return helperSearchTitle(position.child_ptr.get(0), title); //recurse left to find correct leaf node
            }

            titleComparison = position.titleK.get(size1);
            if(title.compareTo(titleComparison) >= 0) { //if title more than end, go to end to find correct leaf
                return helperSearchTitle(position.child_ptr.get(size2), title);
            }

            else {
                for(int i=0; i<size3; i++) { //else traverse through all nodes to check and see what title matches
                    titleComparison = position.titleK.get(i);
                    if(title.compareTo(titleComparison) >= 0) {
                        titleComparison = position.titleK.get(i+1);
                        if (title.compareTo(titleComparison) < 0) {
                            return helperSearchTitle(position.child_ptr.get(i + 1), title);
                        }
                    }
                }
            }
            return null;
        }
        return nodeBook;
    }

    public boolean searchTitle(String text) {  //searching for one title
        if (text != null)
        {
            if (rootNode != null) //if root is not equal to null
            {
                LeafNode correctL = (LeafNode) helperSearchTitle(rootNode, text); //gets correct leaf node

                for (int i = 0; i < correctL.titleK.size(); i++) { //traverse through leaf node to find correct book string
                    String k = correctL.titleK.get(i);
                    if (k.compareTo(text) == 0) {
                        System.out.println(correctL.bookData.get(i).toString());
                        return true;
                    }
                }
            }
        }
        System.out.println("NOT FOUND");
        return false;
    }

    public void traverse()  //traverses and prints out entire tree
    {
        for (int i = 0; i < 200; i++)
        {
            System.out.print("Book Num: " + i+1 + " ");
            System.out.println(bookAll.get(i).toString());
        }
    }


    public ArrayList<Book> traverseTitle(String text) //traverse titles to print out multiple titles for a broader search
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>(); //stores books in result array to be returned
        for (int i = 0; i < bookAll.size() && m <= 200; i++)  //as long as array is less then 200 books
        {
            if (text.compareTo(bookAll.get(i).getTitle()) == 0) //compare if the search is perfect match
            {
                similar.add(0,bookAll.get(i).toString());  //add to front
                results.add(bookAll.get(i));
                m++;
                continue;
            }
            StringBuilder builder = new StringBuilder(text);  //if search is not perfect match but similar
            for (int j = 0;  j < text.length()/2 || text.length() == 1;j++) {
                if (bookAll.get(i).getTitle().toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                    if (similar.contains(bookAll.get(i).toString())) //add to arraylist in back
                    {
                        continue;
                    }
                    similar.add(similar.size(), bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    builder.deleteCharAt(builder.length()-1);
                }
            }
        }
        return results;
    }


    public ArrayList<Book> traverseDesc(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            if (text.compareTo(bookAll.get(i).getDesc()) == 0)
            {
                similar.add(0,bookAll.get(i).toString());
                results.add(bookAll.get(i));
                m++;
                continue;
            }
            StringBuilder builder = new StringBuilder(text);
            for (int j = 0;  j < text.length()/2 || text.length() == 1;j++) {
                if (bookAll.get(i).getDesc().toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                    if (similar.contains(bookAll.get(i).toString()))
                    {
                        continue;
                    }
                    similar.add(similar.size(), bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    builder.deleteCharAt(builder.length()-1);
                }
            }
        }
        return results;
    }

    public ArrayList<Book> traversePreview(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            if (text.compareTo(bookAll.get(i).getPrevLink()) == 0)
            {
                similar.add(0,bookAll.get(i).toString());
                results.add(bookAll.get(i));
                m++;
                continue;
            }
            StringBuilder builder = new StringBuilder(text);
            for (int j = 0;  j < text.length()/2 || text.length() == 1;j++) {
                if (bookAll.get(i).getPrevLink().toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                    if (similar.contains(bookAll.get(i).toString()))
                    {
                        continue;
                    }
                    similar.add(similar.size(), bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    builder.deleteCharAt(builder.length()-1);
                }
            }
        }
        return results;
    }

    public ArrayList<Book> traverseAuthor(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            for (int a = 0; a < bookAll.get(i).getAuthors().size();a++) {
                if (text.compareTo(bookAll.get(i).getAuthors().get(a)) == 0) {
                    similar.add(0, bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    continue;
                }
            }
            for (int a = 0; a < bookAll.get(i).getAuthors().size();a++) {
                StringBuilder builder = new StringBuilder(text);
                for (int j = 0;  j < text.length()/2 || text.length() == 1; j++) {
                    if (bookAll.get(i).getAuthors().get(a).toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                        if (similar.contains(bookAll.get(i).toString()))
                        {
                            continue;
                        }
                        similar.add(similar.size(), bookAll.get(i).toString());
                        results.add(bookAll.get(i));
                        m++;
                        builder.deleteCharAt(builder.length()-1);
                    }
                }
            }
        }
        return results;
    }


    //EXTRA FUNCTIONS FOR EXTRA TYPES OF SEARCHES:
    public ArrayList<Book> traverseImage(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            if (text.compareTo(bookAll.get(i).getImage()) == 0)
            {
                similar.add(0,bookAll.get(i).toString());
                results.add(bookAll.get(i));
                m++;
                continue;
            }
            StringBuilder builder = new StringBuilder(text);
            for (int j = 0;  j < text.length()/2 || text.length() == 1;j++) {
                if (bookAll.get(i).getImage().toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                    if (similar.contains(bookAll.get(i).toString()))
                    {
                        continue;
                    }
                    similar.add(similar.size(), bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    builder.deleteCharAt(builder.length()-1);
                }
            }
        }
        return results;
    }

    public ArrayList<Book> traversePublished(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            if (text.compareTo(bookAll.get(i).getPubDate()) == 0)
            {
                similar.add(0,bookAll.get(i).toString());
                results.add(bookAll.get(i));
                m++;
                continue;
            }
            StringBuilder builder = new StringBuilder(text);
            for (int j = 0;  j < text.length()/2 || text.length() == 1;j++) {
                if (bookAll.get(i).getPubDate().toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                    if (similar.contains(bookAll.get(i).toString()))
                    {
                        continue;
                    }
                    similar.add(similar.size(), bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    builder.deleteCharAt(builder.length()-1);
                }
            }
        }
        return results;
    }

    public ArrayList<Book> traversePub(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            if (text.compareTo(bookAll.get(i).getPub()) == 0)
            {
                similar.add(0,bookAll.get(i).toString());
                results.add(bookAll.get(i));
                m++;
                continue;
            }
            StringBuilder builder = new StringBuilder(text);
            for (int j = 0;  j < text.length()/2 || text.length() == 1;j++) {
                if (bookAll.get(i).getPub().toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                    if (similar.contains(bookAll.get(i).toString()))
                    {
                        continue;
                    }
                    similar.add(similar.size(), bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    builder.deleteCharAt(builder.length()-1);
                }
            }
        }
        return results;
    }

    public ArrayList<Book> traverseInfo(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            if (text.compareTo(bookAll.get(i).getInfoLink()) == 0)
            {
                similar.add(0,bookAll.get(i).toString());
                results.add(bookAll.get(i));
                m++;
                continue;
            }
            StringBuilder builder = new StringBuilder(text);
            for (int j = 0;  j < text.length()/2 || text.length() == 1;j++) {
                if (bookAll.get(i).getInfoLink().toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                    if (similar.contains(bookAll.get(i).toString()))
                    {
                        continue;
                    }
                    similar.add(similar.size(), bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    builder.deleteCharAt(builder.length()-1);
                }
            }
        }
        return results;
    }

    public ArrayList<Book> traverseCat(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            for (int a = 0; a < bookAll.get(i).getCategories().size();a++) {
                if (text.compareTo(bookAll.get(i).getCategories().get(a)) == 0) {
                    similar.add(0, bookAll.get(i).toString());
                    results.add(bookAll.get(i));
                    m++;
                    continue;
                }
            }
            for (int a = 0; a < bookAll.get(i).getCategories().size();a++) {
                StringBuilder builder = new StringBuilder(text);
                for (int j = 0;  j < text.length()/2 || text.length() == 1; j++) {
                    if (bookAll.get(i).getCategories().get(a).toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
                        if (similar.contains(bookAll.get(i).toString()))
                        {
                            continue;
                        }
                        similar.add(similar.size(), bookAll.get(i).toString());
                        results.add(bookAll.get(i));
                        m++;
                        builder.deleteCharAt(builder.length()-1);
                    }
                }
            }
        }
        return results;
    }


    public ArrayList<Book> traverseRatingC(double text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            if (text == bookAll.get(i).getRatingsCount())
            {
                similar.add(0,bookAll.get(i).toString());
                results.add(bookAll.get(i));
                m++;
                continue;
            }
        }
        return results;
    }

}
