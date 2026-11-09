
package com.shawon.mathwhiz.controller;

import com.shawon.mathwhiz.dao.ScoreDAO;
import com.shawon.mathwhiz.service.MathService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/game")
public class GameServlet extends HttpServlet {

    private static final int MAX_SCORE = 21;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String playerName = request.getParameter("playerName");

        if (playerName == null || playerName.trim().isEmpty()) {
            request.setAttribute("error", "Please enter your name.");
            request.getRequestDispatcher("/index.jsp")
                    .forward(request, response);
            return;
        }

        try {
            int q1 = Integer.parseInt(request.getParameter("q1"));
            int q2 = Integer.parseInt(request.getParameter("q2"));
            double q3 = Double.parseDouble(request.getParameter("q3"));
            double q4 = Double.parseDouble(request.getParameter("q4"));

            int score = 0;

            // Q1: Prime number - EASY (2 marks)
            if (q1 == MathService.isPrime(7)) {
                score += MathService.calculateFinalMarks(2, "EASY");
            }

            // Q2: Modulus - NORMAL (4 marks)
            if (q2 == MathService.calculateMod(17, 5)) {
                score += MathService.calculateFinalMarks(2, "NORMAL");
            }

            // Q3: Sine - HARD (9 marks)
            if (MathService.isClose(
                    q3, MathService.calculateSin(30))) {
                score += MathService.calculateFinalMarks(3, "HARD");
            }

            // Q4: Tangent - NORMAL (6 marks)
            if (MathService.isClose(
                    q4, MathService.calculateTan(45))) {
                score += MathService.calculateFinalMarks(3, "NORMAL");
            }

            // Save result in database
            ScoreDAO scoreDAO = new ScoreDAO();
            scoreDAO.save(playerName.trim(), score);

            request.setAttribute("playerName", playerName.trim());
            request.setAttribute("score", score);
            request.setAttribute("maxScore", MAX_SCORE);

            request.getRequestDispatcher("/result.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute(
                    "error",
                    "Please enter valid answers for all questions."
            );

            request.getRequestDispatcher("/game.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to save the game result.", e
            );
        }
    }
}
