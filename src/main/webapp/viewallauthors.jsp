<%@ page import="java.util.List" %>
<%@ page import="com.example.assignment2rebuilt.Author" %>
<%@ page import="com.example.assignment2rebuilt.Book" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>View all Authors</title>
</head>
<body>

<!-- book list attribute is available --->

<% List<Author> authorList =  (List<Author>) request.getAttribute("authorList"); %>

<table>
    <tr>
        <th>Author ID</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Books</th>
    </tr>

    <%
        for (Author author: authorList) {
            out.println("<tr>");
            out.println("<td>" + author.getAuthorID() + "</td>");
            out.println("<td>" + author.getFirstName() + "</td>");
            out.println("<td>" + author.getLastName() + "</td>");
            for (Book book : author.getBookList()) {
                out.println("<td>" + book.getTitle() + " Edition: " + book.getEditionNumber() + " | ISBN: " + book.getIsbn() + " - Copyright: " + book.getCopyright());
            }
            out.println("</tr>");
        }
    %>

</table>

<a href="index.jsp">Back to Main Menu</a>

</body>
</html>