package br.edu.imd.apidetectorplagio.detector.model;

public class Levenshtein implements SimilarityCalculator {

    @Override
    public int calculate(String t1, String t2) {
        int m = t1.length();
        int n = t2.length();
        int[][] T = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            T[i][0] = i;
        }
        for (int j = 1; j <= n; j++) {
            T[0][j] = j;
        }

        int cost;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                cost = t1.charAt(i - 1) == t2.charAt(j - 1) ? 0 : 1;
                T[i][j] = Integer.min(Integer.min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                        T[i - 1][j - 1] + cost);
            }
        }

        return T[m][n];
    }
}
