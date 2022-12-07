package COP3530.Project3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
public class MainController {
    final Logger logger = LoggerFactory.getLogger(MainController.class);
    final long resultsPerPage = 50L;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("isSearching", false);
        model.addAttribute("searchByTitle", true);
        model.addAttribute("searchByDesc", false);
        model.addAttribute("searchByAuthor", false);
        model.addAttribute("isSearching", false);
        model.addAttribute("bPlus", true);
        model.addAttribute("asc", true);
        return "searchResults";
    }

    private final BookService bookService;

    @Autowired
    MainController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public String searchResults(Model model, @RequestParam Optional<String> search, @RequestParam String sortType, @RequestParam String struc,
                                @RequestParam String sortOrder, @RequestParam Optional<Integer> index) {
        int indexInt = index.orElse(0);
        if (search.isPresent()) {
	    String searchString = search.get();
            addMemoryUsage(model);
            long origTime = System.currentTimeMillis();
            boolean asc = sortOrder.equals("ascending");
	    logger.error("asc="+sortOrder);
            model.addAttribute("isSearching", true);
            List<Book> resultsFound;
            if (sortType.equals("title")) {
                resultsFound = bookService.findBooksByTitle(searchString, struc, sortType, asc);
            } else if (sortType.equals("desc")) {
                 resultsFound = bookService.findBooksByDesc(searchString, struc, sortType, asc);
            } else if (sortType.equals("author")) {
                resultsFound = bookService.findBooksByAuthor(searchString, struc, sortType, asc);
            } else {
                throw new AssertionError("Wrong search by");
            }
            var booksFound = resultsFound.stream().skip(indexInt * resultsPerPage).limit(resultsPerPage).toList();

            model.addAttribute("amountResults", 0);
            long curTime = System.currentTimeMillis();
            model.addAttribute("searchFor", searchString);
            model.addAttribute("amountFound", resultsFound.size());
            model.addAttribute("booksFound", booksFound);
            model.addAttribute("timeSpent", curTime - origTime);
            model.addAttribute("struc", struc);
            model.addAttribute("sortOrder", sortOrder);
            model.addAttribute("sortType", sortType);
            model.addAttribute("bPlus", struc.equals("bTree"));
            model.addAttribute("asc", asc);
            model.addAttribute("searchByTitle", sortType.equals("title"));
            model.addAttribute("searchByDesc", sortType.equals("desc"));
            model.addAttribute("searchByAuthor", sortType.equals("author"));
            model.addAttribute("resultsFrom", indexInt * resultsPerPage + 1);
            model.addAttribute("resultsTo", (indexInt) * resultsPerPage + booksFound.size());
            model.addAttribute("prevIndex", indexInt - 1);
            if (resultsFound.size() > (indexInt + 1) * resultsPerPage) {
                model.addAttribute("nextIndex", indexInt + 1);
            } else {
                model.addAttribute("nextIndex", -1);
            }

        } else {
            model.addAttribute("searchByTitle", true);
            model.addAttribute("searchByDesc", false);
            model.addAttribute("searchByAuthor", false);
            model.addAttribute("isSearching", false);
            model.addAttribute("bPlus", true);
            model.addAttribute("asc", true);

        }
        return "searchResults";
    }

    void addMemoryUsage(Model model) {
        Runtime runtime = Runtime.getRuntime();
        double totalMemory = runtime.totalMemory();
        double freeMemory = runtime.freeMemory();
        var memUsage = totalMemory - freeMemory;
        if (memUsage < 1_000) {
            model.addAttribute("memUsage", "" + memUsage + " bytes");
        } else if (memUsage < 1_000_000) {
            model.addAttribute("memUsage", "" + String.format("%.2f", memUsage / 1000) + " kb");
        } else if (memUsage < 1_000_000_000) {
            model.addAttribute("memUsage", "" + String.format("%.2f", memUsage / 1_000_000) + " mb");
        } else {
            model.addAttribute("memUsage", "" + String.format("%.2f", memUsage / 1_000_000_000) + " gb");
        }
    }
}
