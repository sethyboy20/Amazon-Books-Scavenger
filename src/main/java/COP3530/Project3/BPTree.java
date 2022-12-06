package COP3530.Project3;
import java.util.Map.Entry;
import java.util.*;
import java.util.Locale;

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

    public class LeafNode extends Node {
        protected ArrayList<Book> bookData;
        protected LeafNode right;
        protected LeafNode left;
        public LeafNode() {
            titleK = new ArrayList<String>();
            bookData = new ArrayList<Book>();
            is_Leaf = false;
        }

        public void setListTitle(List<String> multTitles)
        {
            titleK = new ArrayList<String>(multTitles);
        }

        public void setListBook(List<Book> multBooks)
        {
            bookData = new ArrayList<Book>(multBooks);
            is_Leaf = true;
        }

        public void setBookData(String title, Book book)
        {
            titleK = new ArrayList<String>();
            titleK.add(title);
            bookData = new ArrayList<Book>();
            bookData.add(book);
            is_Leaf = true;
        }

        public void leafSort(String title1, Book book1)
        {
            int n = titleK.size() - 1;
            int leftN = titleK.get(0).compareTo(title1);
            int rightN = titleK.get(n).compareTo(title1);

            if (leftN > 0)
            {
                titleK.add(0, title1);
                bookData.add(0, book1);
            }
            else if (rightN < 0)
            {
                titleK.add(title1);
                bookData.add(book1);
            }
            else if (!(leftN > 0) && !(rightN < 0))
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

    public class InternalNode extends Node {

        // m nodes
        protected ArrayList<Node> child_ptr; // m+1 children

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


        public void setListTitleI(List<String> multTitles)
        {
            titleK = new ArrayList<String>(multTitles);
        }

        public void setListChildNode(List<Node> childNode)
        {
            child_ptr = new ArrayList<Node>(childNode);
            is_Leaf = false;
        }

        public void internalSort(int pos, Entry<String, Node> childP)
        {
            int n = titleK.size();
            if (pos < n)
            {
                titleK.add(pos, childP.getKey());
                child_ptr.add(pos+1, childP.getValue());
            }
            else if (pos > titleK.size() || pos == titleK.size()) {
                titleK.add(childP.getKey());
                child_ptr.add(childP.getValue());
            }

        }

    }

    public Node rootNode;

    public void insert(String title1, Book book1)
    {
        bookAll.add(book1);
        boolean checkFirst = true;
        Entry<String, Node> childNodePlace = helperInsert(rootNode, title1, book1, null, checkFirst);
        sizeBPT++;

        if(childNodePlace != null) {
            String tempTitle = childNodePlace.getKey();
            Node tempB = childNodePlace.getValue();
            InternalNode tempR = new InternalNode();
            tempR.setChild_ptr(tempTitle, tempB, rootNode);
            rootNode = tempR;
        }
    }

    private Entry<String, Node> helperInsert(Node tempN, String title1, Book book1, Entry<String, Node> addChild, boolean check)
    {
        LeafNode newL = new LeafNode();
        newL.setBookData(title1, book1);

        Node tem1 = newL;

        if (check == true)
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

        if (tempN.is_Leaf == true)
        {
            LeafNode firstLeaf = (LeafNode)tempN;
            LeafNode newLeaf = newL;

            firstLeaf.leafSort(title1, newLeaf.bookData.get(0));

            if (firstLeaf.leaks() == true)
            {
                ArrayList<String> tempTitle1 = new ArrayList<String>();
                ArrayList<Book> tempBook1 = new ArrayList<Book>();
                int add = max_data;
                int rem = max_data;
                int k = 2*max_data;

                while (add <=k) {
                    String t = firstLeaf.titleK.get(add);
                    Book b = firstLeaf.bookData.get(add);
                    tempTitle1.add(t);
                    tempBook1.add(b);
                    add++;
                }

                while (rem <=k) {
                    int s = firstLeaf.titleK.size()-1;
                    int bS = firstLeaf.bookData.size()-1;
                    firstLeaf.titleK.remove(s);
                    firstLeaf.bookData.remove(bS);
                    rem++;
                }

                String seperateTitle = tempTitle1.get(0);
                LeafNode nextLeaf = new LeafNode();
                nextLeaf.setListTitle(tempTitle1);
                nextLeaf.setListBook(tempBook1);

                LeafNode tmp = firstLeaf.right;
                firstLeaf.right = nextLeaf;
                firstLeaf.right.left = nextLeaf;
                nextLeaf.left = firstLeaf;
                nextLeaf.right = tmp;

                if(firstLeaf == rootNode)
                {
                    InternalNode tempR = new InternalNode();
                    tempR.setChild_ptr(seperateTitle, nextLeaf, firstLeaf);
                    rootNode = tempR;
                    return null;
                }
                return new AbstractMap.SimpleEntry<>(seperateTitle, nextLeaf);
            }
            else {
                return null;
            }
        }
        else if(tempN.is_Leaf == false)
        {
            int pos = 0;
            InternalNode inNodePos = (InternalNode) tempN;

            for (pos = 0; pos < inNodePos.titleK.size(); pos++)
            {
                String bName = inNodePos.titleK.get(pos);
                if(bName.compareTo(title1) >= 0)
                {
                    break;
                }
            }

            addChild = helperInsert(inNodePos.child_ptr.get(pos), title1,book1, addChild, check);

            if(addChild != null)
            {
                int pos2 = 0;
                for (pos2 = 0;pos2 < inNodePos.titleK.size();pos2++)
                {
                    String x = inNodePos.titleK.get(pos2);
                    String y = addChild.getKey();
                    if(x.compareTo(y) >= 0)
                    {
                        break;
                    }
                }

                inNodePos.internalSort(pos2, addChild);

                if(inNodePos.leaks() == true)
                {
                    ArrayList<String> tempTitle2 = new ArrayList<String>();
                    ArrayList<Node> tempNode2 = new ArrayList<Node>();

                    String separateTitle = inNodePos.titleK.get(max_data);
                    inNodePos.titleK.remove(max_data);

                    tempNode2.add(inNodePos.child_ptr.get(max_data+1));
                    inNodePos.child_ptr.remove(max_data+1);
                    int s = inNodePos.titleK.size();

                    while(max_data < s) {
                        tempTitle2.add(inNodePos.titleK.get(max_data));
                        tempNode2.add(inNodePos.child_ptr.get(max_data+1));
                        inNodePos.titleK.remove(max_data);
                        inNodePos.child_ptr.remove(max_data+1);
                        s = inNodePos.titleK.size();
                    }

                    InternalNode tempRN = new InternalNode();
                    tempRN.setListTitleI(tempTitle2);
                    tempRN.setListChildNode(tempNode2);

                    if(inNodePos == rootNode)
                    {
                        InternalNode tempR = new InternalNode();
                        tempR.setChild_ptr(separateTitle, tempRN, rootNode);
                        rootNode = tempR;
                        return null;
                    }
                    return new AbstractMap.SimpleEntry<>(separateTitle, tempRN);
                }
            }
        }
        return null;
    }

    private Node helperSearchTitle(Node nodeBook, String title) {
        if(nodeBook.is_Leaf == false) {
            InternalNode position = (InternalNode) nodeBook;
            int size1 = nodeBook.titleK.size()-1;
            int size2 = position.child_ptr.size()-1;
            int size3 = position.titleK.size()-1;

            String titleComparison = position.titleK.get(0);
            if(title.compareTo(titleComparison) < 0) {
                return helperSearchTitle(position.child_ptr.get(0), title);
            }

            titleComparison = position.titleK.get(size1);
            if(title.compareTo(titleComparison) >= 0) {
                return helperSearchTitle(position.child_ptr.get(size2), title);
            }

            else {
                for(int i=0; i<size3; i++) {
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

    public boolean searchTitle(String text) {
        if (text != null)
        {
            if (rootNode != null)
            {
                LeafNode correctL = (LeafNode) helperSearchTitle(rootNode, text);

                for (int i = 0; i < correctL.titleK.size(); i++) {
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

    public void traverse()
    {
        for (int i = 0; i < 200; i++)
        {
            System.out.print("Book Num: " + i+1 + " ");
            System.out.println(bookAll.get(i).toString());
        }
    }


    public ArrayList<Book> traverseTitle(String text)
    {
        int m = 1;
        ArrayList<String> similar = new ArrayList<>();
        ArrayList<Book> results = new ArrayList<>();
        for (int i = 0; i < bookAll.size() && m <= 200; i++)
        {
            if (text.compareTo(bookAll.get(i).getTitle()) == 0)
            {
                similar.add(0,bookAll.get(i).toString());
                results.add(bookAll.get(i));
                m++;
                continue;
            }
            StringBuilder builder = new StringBuilder(text);
            for (int j = 0;  j < text.length()/2 || text.length() == 1;j++) {
                if (bookAll.get(i).getTitle().toLowerCase(Locale.US).contains(builder.toString().toLowerCase(Locale.US))) {
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



    /*public static void main(String[] args) {
        int i;
        int n;
        String t;
        Book books = new Book();
        Scanner in = new Scanner(System.in);
        BPTree b = new BPTree();
        System.out.print("enter the no of books to be inserted\n");
        n = in.nextInt();
        for (i = 0; i < n; i++) {
            t = in.next();
            books = new Book();
            books.setTitle(t);
            b.insert(t, books);
        }
        System.out.print("SIZE OF TREE + " + b.sizeBPT + "\n");
        System.out.print("traversal of constructed tree\n");
        for (i = 0; i < n; i++) {
            //        System.out.print("SEARCH\n");
            t = in.next();
            b.searchTitle(t);
        }
    }*/


}