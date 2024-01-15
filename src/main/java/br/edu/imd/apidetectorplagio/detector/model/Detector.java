package br.edu.imd.apidetectorplagio.detector.model;

import java.util.ArrayList;
import java.util.List;

public class Detector {

    private String content;
    private int percentageToDetect;
    private SimilarityCalculator calculator;
    private List<Plagiarism> results;

    public Detector(String content, int percentageToDetect, SimilarityCalculator calculator) {
        this.content = formatInput(content);
        this.percentageToDetect = percentageToDetect;
        this.calculator = calculator;
        this.results = new ArrayList<>();
    }

    public synchronized boolean isPlagiarism(float percentageSimilarity){
        return percentageSimilarity >= percentageToDetect;
    }

    public synchronized int calculateSimilarity(String textCompare){
        return calculator.calculate(content, textCompare);
    }

    public synchronized void addPlagiarism(Plagiarism plagiarism){
        results.add(plagiarism);
    }

    public String getContent() {
        return content;
    }

    private String formatInput(String content){
        return content.replaceAll("\n", "");
    }

    public List<Plagiarism> getResults(){
        return results;
    }
}
