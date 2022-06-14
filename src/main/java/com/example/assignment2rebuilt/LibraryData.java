package com.example.assignment2rebuilt;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * LibraryData Servlet - Provides GET method for listing books/authors, POST method for adding books/authors
 * @author Nick
 */
@WebServlet(name = "libraryData", value = "/library-data")
public class LibraryData extends HttpServlet {

    DBConnection dbConnection = new DBConnection();
    private String message;
    public void init() {
        message = "Library Servlet!";
    }

    /**
     * Get method for displaying books and authors
     * @param request gets parameters
     * @param response response from client
     * @throws ServletException Defines a general exception a servlet can throw when it encounters difficulty.
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String view = request.getParameter("view");

        List<Book> bookList = null;
        List<Author> authorList = null;

        if (view.equals("book")){
            bookList = dbConnection.getBookList();
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("viewallbooks.jsp");
            request.setAttribute("booklist", bookList);
            requestDispatcher.forward(request, response);

        } else if (view.equals("author")){
            authorList = dbConnection.getAuthorList();
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("viewallauthors.jsp");
            request.setAttribute("authorList", authorList);
            requestDispatcher.forward(request, response);

        } else {
            System.out.println("ERROR, DIDN'T GET VIEW PARAM!");
        }
    }

    /**
     * Post method for adding books and authors
     * @param request gets parameters
     * @param response on submit, redirects to library it was added to
     * @throws ServletException Defines a general exception a servlet can throw when it encounters difficulty.
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String view = request.getParameter("view");

        if (view.equals("add_book")) {
            String isbn = request.getParameter("isbn");
            String title = request.getParameter("title");
            int editionNumber = Integer.valueOf(request.getParameter("edition_number"));
            String copyright = request.getParameter("copyright");

            Book newBook = new Book(request.getParameter("isbn"), request.getParameter("title"),
                    Integer.valueOf(request.getParameter("edition_number")), request.getParameter("copyright"));

            Author author = new Author(Integer.valueOf(request.getParameter("authorID")), "", "");
            newBook.getAuthorList().add(author);

            try {
                dbConnection.insertBook(newBook);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            response.sendRedirect("library-data?view=book");

        } else if(view.equals("add_author")){

            int author_id = Integer.valueOf(request.getParameter("author_id"));
            String first_name = request.getParameter("first_name");
            String last_name = request.getParameter("last_name");

            try {
                dbConnection.insertAuthor(
                        new Author(
                                Integer.valueOf(request.getParameter("author_id")),
                                request.getParameter("first_name"),
                                request.getParameter("last_name")
                        )
                );

            } catch (SQLException e) {
                e.printStackTrace();
            }

            response.sendRedirect("library-data?view=author");

        } else {
            System.out.println("ERROR, DIDN'T GET VIEW PARAM!");
        }
    }
}