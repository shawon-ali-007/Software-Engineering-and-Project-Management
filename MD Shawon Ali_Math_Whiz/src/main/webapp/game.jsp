
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String playerName = request.getParameter("playerName");

    if (playerName == null || playerName.trim().isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    String safePlayerName = playerName
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Math Whiz - Game</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

    <header class="main-header">
        <h1>Math Whiz</h1>
        <p>Good luck, <%= safePlayerName %>!</p>
    </header>

    <main class="card">

        <h2>Answer the Questions</h2>
        <p class="score-info">Maximum Score: 21</p>

        <% if (request.getAttribute("error") != null) { %>
        <p class="error">
            <%= request.getAttribute("error") %>
        </p>
        <% } %>

        <form action="${pageContext.request.contextPath}/game"
              method="post">

            <input type="hidden"
                   name="playerName"
                   value="<%= safePlayerName %>">

            <!-- Question 1 -->
            <section class="question">
                <h3>Question 1 - Prime Number</h3>
                <p>Is 7 a prime number?</p>
                <p class="marks">Difficulty: EASY | Marks: 2</p>

                <label>
                    <input type="radio" name="q1" value="1" required>
                    Yes
                </label>

                <label>
                    <input type="radio" name="q1" value="0">
                    No
                </label>
            </section>

            <!-- Question 2 -->
            <section class="question">
                <h3>Question 2 - Modulus</h3>
                <p>What is the result of 17 % 5?</p>
                <p class="marks">Difficulty: NORMAL | Marks: 4</p>

                <input type="number"
                       name="q2"
                       placeholder="Enter your answer"
                       required>
            </section>

            <!-- Question 3 -->
            <section class="question">
                <h3>Question 3 - Trigonometry</h3>
                <p>What is the value of sin(30°)?</p>
                <p class="marks">Difficulty: HARD | Marks: 9</p>

                <input type="number"
                       name="q3"
                       step="any"
                       placeholder="Enter your answer (e.g. 0.5)"
                       required>
            </section>

            <!-- Question 4 -->
            <section class="question">
                <h3>Question 4 - Trigonometry</h3>
                <p>What is the value of tan(45°)?</p>
                <p class="marks">Difficulty: NORMAL | Marks: 6</p>

                <input type="number"
                       name="q4"
                       step="any"
                       placeholder="Enter your answer (e.g. 1)"
                       required>
            </section>

            <button type="submit" class="btn">
                Submit Answers
            </button>

        </form>

        <a class="secondary-link"
           href="${pageContext.request.contextPath}/index.jsp">
            Back to Home
        </a>

    </main>

    <footer>
        <p>&copy; 2026 Math Whiz</p>
    </footer>

</div>

</body>
</html>
