
package com.shawon.mathwhiz.controller;

import com.shawon.mathwhiz.dao.ScoreDAO;
import com.shawon.mathwhiz.model.GameScore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            ScoreDAO scoreDAO = new ScoreDAO();

            List<GameScore> topScores = scoreDAO.topTen();

            request.setAttribute("topScores", topScores);

            request.getRequestDispatcher("/leaderboard.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load the leaderboard.", e
            );
        }
    }
}
