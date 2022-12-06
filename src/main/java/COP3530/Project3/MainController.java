package COP3530.Project3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class MainController {
    final Logger logger = LoggerFactory.getLogger(MainController.class);
    final long resultsPerPage = 50L;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("isSearching", false);
        return "searchResults";
    }

    private final BookService bookService;

    @Autowired
    MainController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public String searchResults(Model model, @RequestParam Optional<String> search, @RequestParam Optional<String> sortType, @RequestParam Optional<String> struc,
                                @RequestParam Optional<Boolean> sortOrder, @RequestParam Optional<Integer> index) {
        int indexInt = index.orElse(0);
        if (search.isPresent()) {
            addMemoryUsage(model);
            long origTime = System.currentTimeMillis();
            model.addAttribute("isSearching", true);
            var resultsFound = bookService.findBooksByTitle(search.get(), struc.get(), sortType.get(), sortOrder.get());
            var booksByTitle = resultsFound.stream().skip(indexInt * resultsPerPage).limit(resultsPerPage).toList();
            model.addAttribute("amountResults", 0);
            long curTime = System.currentTimeMillis();
            model.addAttribute("searchFor", search.get());
            model.addAttribute("amountFound", resultsFound.size());
            model.addAttribute("booksByTitle", booksByTitle);
            model.addAttribute("timeSpent", curTime - origTime);
            model.addAttribute("resultsFrom", indexInt * resultsPerPage+1);
            model.addAttribute("resultsTo", (indexInt) * resultsPerPage + booksByTitle.size());
            model.addAttribute("prevIndex", indexInt - 1);
            if (resultsFound.size() > (indexInt + 1) * resultsPerPage) {
                model.addAttribute("nextIndex", indexInt + 1);
            } else {
                model.addAttribute("nextIndex", -1);
            }

        } else {
            model.addAttribute("isSearching", false);
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
