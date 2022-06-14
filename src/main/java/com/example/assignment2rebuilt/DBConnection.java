package com.example.assignment2rebuilt;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * DBConnection Class - Loads books/authors into lists, joins relationships, creates connection
 * @author Nick
 */
public class DBConnection {

    private List<Book> bookList = new LinkedList<>();
    private List<Author> authorList = new LinkedList<>();
    private HashMap<String, String> library = new HashMap<>();

    /**
     * Constructor for DBConnection Class
     * Loads books, authors and relationships when creating the object
     */
    public DBConnection() {
        loadBooks();
        loadAuthors();
        loadDatabase();
    }

    /**
     * Returns book list of database
     * @return book list
     */
    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * Returns author list of database
     * @return author list
     */
    public List<Author> getAuthorList() {
        return authorList;
    }

    /**
     * Retrieve all the books from the database into a LinkedList.
     */
    private void loadBooks(){
        try (
                Connection connection = getBooksDBConnection();
                Statement statement = connection.createStatement();
        ) {
            String sqlQuery = "SELECT * from " + BooksDatabaseSQL.BOOK_TABLE_NAME;
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while(resultSet.next()) {
                bookList.add(
                        new Book(
                                resultSet.getString(BooksDatabaseSQL.BOOK_COL_NAME_ISBN),
                                resultSet.getString(BooksDatabaseSQL.BOOK_COL_NAME_TITLE),
                                resultSet.getInt(BooksDatabaseSQL.BOOK_COL_NAME_EDITION_NUMBER),
                                resultSet.getString(BooksDatabaseSQL.BOOK_COL_NAME_COPYRIGHT)
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve all the authors from the database into a LinkedList.
     */
    private void loadAuthors(){
        try(
                Connection connection = getBooksDBConnection();
                Statement statement = connection.createStatement();
        ){
            String sqlQuery = "SELECT * from " + AuthorsDatabaseSQL.AUTHOR_TABLE_NAME;
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                authorList.add(
                        new Author(
                                resultSet.getInt(AuthorsDatabaseSQL.AUTHOR_COL_NAME_AUTHORID),
                                resultSet.getString(AuthorsDatabaseSQL.AUTHOR_COL_NAME_FIRST_NAME),
                                resultSet.getString(AuthorsDatabaseSQL.AUTHOR_COL_NAME_LAST_NAME)
                        )
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Inserts new book to database for an existing author
     * @param book book values to be added
     */
    public void insertBook(Book book) throws SQLException {
        Connection connection = getBooksDBConnection();

        String sqlQuery = "INSERT INTO " + BooksDatabaseSQL.BOOK_TABLE_NAME +
                " VALUES (?,?,?,?)";

        String SQLAuthorISBN = "INSERT into authorISBN (authorID, isbn)" +
                "Values (?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1, book.getIsbn());
        preparedStatement.setString(2, book.getTitle());
        preparedStatement.setInt(3, book.getEditionNumber());
        preparedStatement.setString(4, book.getCopyright());
        preparedStatement.execute();

        PreparedStatement preparedStatement2 = connection.prepareStatement(SQLAuthorISBN);
        preparedStatement2.setString(2, book.getIsbn());
        for(Author author: book.getAuthorList()) {
            preparedStatement2.setInt(1, author.getAuthorID());
            preparedStatement2.execute();
        }
    }


    /**
     * Inserts new author to database
     * @param author author values to be added
     */
    public void insertAuthor(Author author) throws SQLException{
        Connection connection = getBooksDBConnection();

        String sqlQuery = "INSERT INTO " + AuthorsDatabaseSQL.AUTHOR_TABLE_NAME +
                " VALUES (?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setInt(1, author.getAuthorID());
        preparedStatement.setString(2, author.getFirstName());
        preparedStatement.setString(3, author.getLastName());

        preparedStatement.executeQuery();
    }

    /**
     * Loads database, establishes query that links relationships together
     */
    private void loadDatabase(){
        try(
                Connection connection = getBooksDBConnection();
                Statement statement = connection.createStatement();
        ) {

            String sqlQuery = "SELECT " + BooksDatabaseSQL.BOOK_TABLE_NAME + "." + BooksDatabaseSQL.BOOK_COL_NAME_TITLE + "," +
                    AuthorsDatabaseSQL.AUTHOR_TABLE_NAME + "." + AuthorsDatabaseSQL.AUTHOR_COL_NAME_AUTHORID +
                    " FROM " + BooksDatabaseSQL.BOOK_TABLE_NAME +
                    " JOIN " + AuthorISBNDatabaseSQL.AUTHOR_ISBN_TABLE_NAME +
                    " ON " + BooksDatabaseSQL.BOOK_TABLE_NAME + "." + BooksDatabaseSQL.BOOK_COL_NAME_ISBN + "=" +
                    AuthorISBNDatabaseSQL.AUTHOR_ISBN_TABLE_NAME + "." + AuthorISBNDatabaseSQL.AUTHOR_ISBN_COL_NAME_ISBN +
                    " JOIN " + AuthorsDatabaseSQL.AUTHOR_TABLE_NAME +
                    " ON "  + AuthorsDatabaseSQL.AUTHOR_TABLE_NAME + "." + AuthorsDatabaseSQL.AUTHOR_COL_NAME_AUTHORID + "=" +
                    AuthorISBNDatabaseSQL.AUTHOR_ISBN_TABLE_NAME + "." + AuthorsDatabaseSQL.AUTHOR_COL_NAME_AUTHORID;

            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String authorID = resultSet.getString(2);
                if (library.containsKey(title)) {
                    library.put(title, library.get(title) + ", " + authorID);
                } else {
                    library.put(title, authorID);
                }
            }
            for (Map.Entry<String, String> entry : library.entrySet()) {
                for (Book book : getBookList()) {
                    if (entry.getKey().equals(book.getTitle())) {
                        for (Author author : getAuthorList()) {
                            if (entry.getValue().contains(Integer.toString(author.getAuthorID()))) {
                                book.addAuthorList(author);
                                author.addBookList(book);
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Get a connection to the Books Database - details in the inner class Books Database SQL
     * @return connection
     * @throws SQLException
     */
    private Connection getBooksDBConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            System.out.println("Option 1: Find the class worked!");
        } catch (ClassNotFoundException ex) {
            System.err.println("Error: unable to load driver class!");
        } catch(IllegalAccessException ex) {
            System.err.println("Error: access problem while loading!");
        } catch(InstantiationException ex){
            System.err.println("Error: unable to instantiate driver!");
        }

        return DriverManager.getConnection(BooksDatabaseSQL.DB_URL, BooksDatabaseSQL.USER, BooksDatabaseSQL.PASS);
    }

    /**
     * Simple inner class to abstract all the specific SQL Information
     */
    private class BooksDatabaseSQL{

        //Login information
        static final String DB_URL = "jdbc:mariadb://localhost:3304/books";
        static final String USER = "root";
        static final String PASS = "Password1";

        //Book Table Information
        public static final String BOOK_TABLE_NAME = "titles";
        public static final String BOOK_COL_NAME_ISBN = "isbn";
        public static final String BOOK_COL_NAME_TITLE = "title";
        public static final String BOOK_COL_NAME_EDITION_NUMBER = "editionNumber";
        public static final String BOOK_COL_NAME_COPYRIGHT = "copyright";

    }

    /**
     * Stores terms for Authors SQL database
     */
    private static class AuthorsDatabaseSQL{
        //login info
        static final String DB_URL = "jdbc:mariadb://localhost:3304/books";
        static final String USER = "root";
        static final String PASS = "Password1";

        //author table info
        public static final String AUTHOR_TABLE_NAME = "authors";
        public static final String AUTHOR_COL_NAME_AUTHORID = "authorID";
        public static final String AUTHOR_COL_NAME_FIRST_NAME = "firstName";
        public static final String AUTHOR_COL_NAME_LAST_NAME = "lastName";
    }

    /**
     * Stores terms for AuthorISBN SQL database
     */
    private static class AuthorISBNDatabaseSQL{
        //login info
        static final String DATABASE_URL = "jdbc:mariadb://localhost:3304/books";
        static final String USER = "root";
        static final String PASS = "Password1";

        //author table info
        public static final String AUTHOR_ISBN_TABLE_NAME = "authorisbn";
        public static final String AUTHOR_ISBN_COL_NAME_AUTHORID = "authorID";
        public static final String AUTHOR_ISBN_COL_NAME_ISBN = "isbn";

    }
}