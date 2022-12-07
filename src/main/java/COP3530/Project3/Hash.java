/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COP3530.Project3;
import java.util.*;
import java.io.*;

public class Hash {
    HashMap<String, Book> books = new HashMap<>();

    public void read() throws Exception {
        // TODO code application logic here
        
        try {
            // Open books file
            File file = new File("books_data.csv");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            
            // Discard first line
            br.readLine();

            String str;

            while((str = br.readLine()) != null) {
               // Get next line
               StringBuilder line = new StringBuilder(str); 
               Book newBook = new Book();
               
               // Read amountFound
               String title = parseLine(line);
               newBook.setTitle(title);

               // Read description
               String desc = parseLine(line);
               newBook.setDesc(desc);

               // Read author(s)
               StringBuilder tempAuthors = new StringBuilder(parseLine(line));

               // Replace any double quotes w/single quotes
               String tempAuthsStr = tempAuthors.toString().replaceAll("\"", "'");
               tempAuthors = new StringBuilder(tempAuthsStr);

               if (tempAuthors.length() >= 2)
                   tempAuthors.delete(0, 2);

               while (tempAuthors.length() > 1) {
                  // Read in each author individually
                   String authorName = tempAuthors.substring(0, tempAuthors.indexOf("'"));
                   newBook.addAuthor(authorName);
                   tempAuthors.delete(0, tempAuthors.indexOf("'") + 1);

                   if (tempAuthors.charAt(0) == ',') {
                       tempAuthors.delete(0, 3);
                   }
               }

               // Read image link
               String image = parseLine(line);
               newBook.setImage(image);

               // Read preview link
               String previewLink = parseLine(line);
               newBook.setPrevLink(previewLink);

               // Read publisher name
               String publisher = parseLine(line);
               newBook.setPub(publisher);
               
               // Read published date
               String publishedDate = parseLine(line);
               newBook.setPubDate(publishedDate);

               // Read info link
               String infoLink = parseLine(line);
               newBook.setInfoLink(infoLink);
               
               // Read categories
               StringBuilder tempCats = new StringBuilder(parseLine(line));
               
               // Replace any double quotes w/single quotes
               String tempCatsStr = tempCats.toString().replaceAll("\"", "'");
               tempCats = new StringBuilder(tempCatsStr);

               if (tempCats.length() >= 2)
                   tempCats.delete(0, 2);

               while (tempCats.length() > 1) {
                   // Read each category individually
                   String catName = tempCats.substring(0, tempCats.indexOf("'"));
                   newBook.addCategory(catName);
                   tempCats.delete(0, tempCats.indexOf("'") + 1);

                   if (tempCats.charAt(0) == ',') {
                       tempCats.delete(0, 3);
                   }
               }
               
               // Read ratings count
               String tempRatings = parseLine(line);
               double ratingsCount = 0;
               if (tempRatings.length() != 0)
                   ratingsCount = Double.parseDouble(tempRatings);
               newBook.setRatingsCount(ratingsCount);

               books.put(title, newBook);
            }
            fr.close();
         }
         catch (Exception e) { 
             System.out.println("File read error"); 
         }
        
    }
    
    public static String parseLine(StringBuilder line) {
        String result = "";
        if (line.length() == 0)
            return result;
        // If data is encapsulated in double quotes,
        // find and read to end quote
        if (line.charAt(0) == '"') {
                line.deleteCharAt(0);
                int endIndex = line.indexOf("\"");
                // If index is still within data, skip to next double quote
                if (line.toString().contains("\"\"")) {
                    while (endIndex < (line.length() - 1) && line.charAt(endIndex + 1) == '"') {
                        endIndex = line.indexOf("\"", endIndex + 2);
                    }
                }
                
                // Get result, remove from line
                result = line.substring(0, endIndex);
                line.delete(0, endIndex + 2);
                
                result = result.replaceAll("\"\"", "\"");
        }
        else { // Read to next comma (or end of line)
               int endIndex;
               if (line.indexOf(",") != -1)
                    endIndex = line.indexOf(",");
               else
                    endIndex = line.length();
               
               // Get result, remove from line
               result = line.substring(0, endIndex);
               line.delete(0, endIndex + 1);
        }
        return result;
    }
}
