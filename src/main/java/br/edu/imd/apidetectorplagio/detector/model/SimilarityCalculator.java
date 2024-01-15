package br.edu.imd.apidetectorplagio.detector.model;

@FunctionalInterface
public interface SimilarityCalculator {

    int calculate(String t1, String t2);

}
