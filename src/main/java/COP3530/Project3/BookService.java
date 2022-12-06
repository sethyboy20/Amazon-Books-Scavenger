package COP3530.Project3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Locale;
import java.util.function.Consumer;

@Service
public class BookService {
    private ArrayList<Book> book;
    Hash hashBooks = new Hash();

    @Autowired
    BookService(ArrayList<Book> book) throws Exception{
        hashBooks.read();
        /*this.book = book;
        for(int i = 1;i<10_026;i++) {
            addBook(this.book, "Test"+i);
        }*/
    }

    private void addBook(ArrayList<Book> book, String test) {
        Book b = new Book();
        b.setTitle("Title"+test);
        b.setDesc("Desc"+test);
        b.setRatingsCount(44.4);
        book.add(b);
    }

    public ArrayList<Book> getBook() {
        return book;
    }

    public void setBook(ArrayList<Book> book) {
        this.book = book;
    }

    public List<Book> findBooksByTitle(String search) {
        //return book.stream().filter(c->c.getTitle().toLowerCase(Locale.US).contains(search.toLowerCase(Locale.US))).collect(Collectors.toList());
        return hashBooks.books.values().stream().filter(c->c.getTitle().toLowerCase(Locale.US).contains(search.toLowerCase(Locale.US))).collect(Collectors.toList());
    }
    
    public List<Book> findBooksByDesc(String search) {
        return hashBooks.books.values().stream().filter(c->c.getDesc().toLowerCase(Locale.US).contains(search.toLowerCase(Locale.US))).collect(Collectors.toList());
    }
    
    public List<Book> findBooksByAuthor(String search) {
        return hashBooks.books.values().stream().filter(c->authorSearchHelper(c, search)).collect(Collectors.toList());
    }
    
    private boolean authorSearchHelper(Book book, String search) {
        for (int i = 0; i < book.getAuthors().size(); i++) {
            if (book.getAuthors().get(i).toLowerCase(Locale.US).contains(search.toLowerCase(Locale.US)))
                return true;
        }
        
        return false;
    }
}
