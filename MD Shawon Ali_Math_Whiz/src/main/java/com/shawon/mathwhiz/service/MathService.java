
package com.shawon.mathwhiz.service;

public class MathService {

    // Prime Number
    public static int isPrime(int number) {

        if (number < 2) {
            return 0;
        }

        for (int i = 2; i <= number / i; i++) {
            if (number % i == 0) {
                return 0;
            }
        }

        return 1;
    }

    // Modulus
    public static int calculateMod(int a, int b) {

        if (b == 0) {
            throw new IllegalArgumentException(
                    "Divisor cannot be zero"
            );
        }

        return a % b;
    }

    // Sine
    public static double calculateSin(double degree) {

        return Math.sin(Math.toRadians(degree));
    }

    // Cosine
    public static double calculateCos(double degree) {

        return Math.cos(Math.toRadians(degree));
    }

    // Tangent
    public static double calculateTan(double degree) {

        double normalized = degree % 180.0;

        if (Math.abs(Math.abs(normalized) - 90.0) < 1e-10) {
            throw new IllegalArgumentException(
                    "Tangent is undefined at this angle"
            );
        }

        return Math.tan(Math.toRadians(degree));
    }

    // Decimal Answer Comparison
    public static boolean isClose(double actual, double answer) {

        return Math.abs(actual - answer) <= 0.0001;
    }

    // Difficulty Multiplier
    public static int getMultiplier(String difficulty) {

        switch (difficulty.toUpperCase()) {

            case "EASY":
                return 1;

            case "NORMAL":
                return 2;

            case "HARD":
                return 3;

            default:
                throw new IllegalArgumentException(
                        "Invalid difficulty level"
                );
        }
    }

    // Final Marks Calculation
    public static int calculateFinalMarks(
            int baseMarks, String difficulty) {

        return baseMarks * getMultiplier(difficulty);
    }
}
