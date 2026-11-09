
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shawon.mathwhiz.model.GameScore" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    List<GameScore> topScores =
            (List<GameScore>) request.getAttribute("topScores");

    SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMM yyyy, hh:mm a");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Math Whiz - Leaderboard</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

    <header class="main-header">
        <h1>Math Whiz</h1>
        <p>Top 10 Players</p>
    </header>

    <main class="card">

        <h2>Leaderboard</h2>
        <p class="score-info">Maximum Possible Score: 21</p>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Rank</th>
                    <th>Player Name</th>
                    <th>Score</th>
                    <th>Difficulty</th>
                    <th>Played At</th>
                </tr>
                </thead>

                <tbody>

                <%
                    if (topScores != null && !topScores.isEmpty()) {
                        int rank = 1;

                        for (GameScore gameScore : topScores) {

                            String safeName = gameScore.getPlayerName()
                                    .replace("&", "&amp;")
                                    .replace("\"", "&quot;")
                                    .replace("<", "&lt;")
                                    .replace(">", "&gt;");
                %>

                <tr>
                    <td><%= rank++ %></td>
                    <td><%= safeName %></td>
                    <td><strong><%= gameScore.getScore() %> / 21</strong></td>
                    <td><%= gameScore.getDifficulty() %></td>
                    <td>
                        <%= gameScore.getPlayedAt() != null
                                ? dateFormat.format(gameScore.getPlayedAt())
                                : "N/A" %>
                    </td>
                </tr>

                <%
                    }
                } else {
                %>

                <tr>
                    <td colspan="5">No scores available yet. Be the first player!</td>
                </tr>

                <%
                    }
                %>

                </tbody>
            </table>
        </div>

        <a class="btn"
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
