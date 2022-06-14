<%@ page import="java.net.http.HttpRequest" %><%--
  Created by IntelliJ IDEA.
  User: Josh
  Date: 2022-05-30
  Time: 9:09 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JSP Examples</title>
</head>
<body>

<%-- Declare a variable in a Scriplet --%>
<%! String string ="Hello World!"; %>


<p>
    <%-- Expression --%>
    <b>Example of an Expression </b>
    Current time is: <%= new java.util.Date() %>
</p>

<p>
    <%-- Scriplet with reference to a variable --%>
    An Example of a Scriplet:
    <% out.print(string + "... Sample JSP code"); %>
</p>

<%
    if(string.equals("Hello Cruel World!")){
%>Strings match!<%
    }
    else {
        out.print("strings don't match");
    }
%>

<%-- JSP Implicit Objects --%>

<%
    out.println("out is an implicit Object <br>");
%>




</body>
</html>