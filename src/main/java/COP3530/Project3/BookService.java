package COP3530.Project3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private ArrayList<Book> book;

    @Autowired
    BookService(ArrayList<Book> book){
        this.book = book;
        for(int i = 1;i<10_026;i++) {
            addBook(this.book, "Test"+i);
        }
    }

    private void addBook(ArrayList<Book> book, String test) {
        Book b = new Book();
        b.setTitle("Title"+test);
        b.setDesc("Desc"+test);
        b.setPrice(10.22);
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
        return book.stream().filter(c->c.getTitle().contains(search)).collect(Collectors.toList());
    }
}
