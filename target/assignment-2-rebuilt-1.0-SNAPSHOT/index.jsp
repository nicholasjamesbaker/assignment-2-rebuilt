<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Nick's Book Database</title>
</head>
<body>

<h1>Nick's Book Database</h1>

<li><a href="addbook.jsp">Add a Book</a></li>
<li><a href="addauthor.jsp">Add an Author</a></li>

<!-- //TODO Add in a param to differentiate the books vs authors -->
<li><a href="library-data?view=book">View all Books</a></li>
<li><a href="library-data?view=author">View all Authors</a></li>


</body>
</html>