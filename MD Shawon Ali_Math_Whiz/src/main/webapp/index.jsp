
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Math Whiz - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">

    <header class="main-header">
        <h1>Math Whiz</h1>
        <p>Challenge your mathematical skills!</p>
    </header>

    <main class="card">
        <h2>Welcome to Math Whiz!</h2>

        <p>
            Answer four mathematics questions and test your knowledge
            of prime numbers, modulus and trigonometry.
        </p>

        <div class="game-info">
            <p><strong>Total Questions:</strong> 4</p>
            <p><strong>Maximum Score:</strong> 21</p>
            <p><strong>Difficulty Levels:</strong> Easy, Normal and Hard</p>
        </div>

        <% if (request.getAttribute("error") != null) { %>
        <p class="error">
            <%= request.getAttribute("error") %>
        </p>
        <% } %>

        <form action="${pageContext.request.contextPath}/game.jsp"
              method="get">

            <label for="playerName">Enter Your Name</label>

            <input type="text"
                   id="playerName"
                   name="playerName"
                   placeholder="Your name"
                   maxlength="100"
                   required>

            <button type="submit" class="btn">
                Start Game
            </button>
        </form>

        <a class="secondary-link"
           href="${pageContext.request.contextPath}/leaderboard">
            View Top 10 Leaderboard
        </a>
    </main>

    <footer>
        <p>&copy; 2026 Math Whiz</p>
    </footer>

</div>

</body>
</html>
