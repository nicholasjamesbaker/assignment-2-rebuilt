<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add an Author to the Database!</title>
</head>
<body>

<h1>Add An Author</h1>

<form action = "library-data" method="POST">
    Author ID: <input type = "text" name = "author_id"> <br/>
    First Name: <input type = "text" name = "first_name"> <br/>
    Last Name: <input type = "text" name = "last_name"> <br/>
    <input type="hidden" id="view" name="view" value="add_author">
    <input type = "submit" value = "Submit" />
</form>
</br>

<a href="index.jsp">Back to Main Menu</a>

</body>
</html>