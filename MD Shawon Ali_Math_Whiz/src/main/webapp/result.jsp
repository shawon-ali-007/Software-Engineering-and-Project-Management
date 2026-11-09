
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String playerName = (String) request.getAttribute("playerName");
    Integer score = (Integer) request.getAttribute("score");
    Integer maxScore = (Integer) request.getAttribute("maxScore");

    if (playerName == null || score == null || maxScore == null) {
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
    <title>Math Whiz - Result</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

    <header class="main-header">
        <h1>Math Whiz</h1>
        <p>Game Completed!</p>
    </header>

    <main class="card result-card">

        <h2>Congratulations, <%= safePlayerName %>!</h2>

        <p>Your final score is:</p>

        <div class="final-score">
            <%= score %> / <%= maxScore %>
        </div>

        <% if (score.equals(maxScore)) { %>
        <h3>Perfect Score! Excellent work!</h3>
        <% } else if (score >= 10) { %>
        <h3>Great job! Keep practicing!</h3>
        <% } else { %>
        <h3>Good effort! Try again to improve your score.</h3>
        <% } %>

        <p>
            Your result has been saved successfully.
        </p>

        <a class="btn"
           href="${pageContext.request.contextPath}/index.jsp">
            Play Again
        </a>

        <a class="secondary-link"
           href="${pageContext.request.contextPath}/leaderboard">
            View Leaderboard
        </a>

    </main>

    <footer>
        <p>&copy; 2026 Math Whiz</p>
    </footer>

</div>

</body>
</html>
