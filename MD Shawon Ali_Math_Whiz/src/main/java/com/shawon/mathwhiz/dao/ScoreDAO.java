package com.shawon.mathwhiz.dao;

import com.shawon.mathwhiz.model.GameScore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    // Save score with MIXED difficulty because the game has
    // questions with different difficulty levels.
    public void save(String playerName, int score) throws SQLException {
        save(playerName, score, "MIXED");
    }

    public void save(String playerName, int score, String difficulty)
            throws SQLException {

        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty.");
        }

        if (score < 0 || score > 21) {
            throw new IllegalArgumentException("Score must be between 0 and 21.");
        }

        String sql = """
                INSERT INTO game_scores (player_name, score, difficulty)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, playerName.trim());
            statement.setInt(2, score);
            statement.setString(3, difficulty);

            statement.executeUpdate();
        }
    }

    // Retrieve the top 10 players.
    public List<GameScore> topTen() throws SQLException {

        List<GameScore> scores = new ArrayList<>();

        String sql = """
                SELECT id, player_name, score, difficulty, played_at
                FROM game_scores
                ORDER BY score DESC, played_at ASC
                LIMIT 10
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                GameScore gameScore = new GameScore(
                        resultSet.getInt("id"),
                        resultSet.getString("player_name"),
                        resultSet.getInt("score"),
                        resultSet.getString("difficulty"),
                        resultSet.getTimestamp("played_at")
                );

                scores.add(gameScore);
            }
        }

        return scores;
    }
}