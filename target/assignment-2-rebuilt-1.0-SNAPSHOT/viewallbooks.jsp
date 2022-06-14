<%@ page import="java.util.List" %>
<%@ page import="com.example.assignment2rebuilt.Book" %>
<%@ page import="com.example.assignment2rebuilt.Author" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>View all Books</title>
</head>
<body>

<% List<Book> bookList =  (List<Book>) request.getAttribute("booklist"); %>

<table>
    <tr>
        <th>ISBN</th>
        <th>Title</th>
        <th>Edition</th>
        <th>Copyright</th>
        <th>Author(s)</th>
    </tr>

    <%
        for (Book book: bookList) {
            out.println("<tr>");
            out.println("<td>" + book.getIsbn() + "</td>");
            out.println("<td>" + book.getTitle() + "</td>");
            out.println("<td>" + book.getEditionNumber() + "</td>");
            out.println("<td>" + book.getCopyright() + "</td>");
            for (Author author : book.getAuthorList()) {
                out.println("<td>" + author.getFirstName() + " " + author.getLastName() + " ");
            }
            out.println("</tr>");
        }
    %>

</table>

<a href="index.jsp">Back to Main Menu</a>

</body>
</html>