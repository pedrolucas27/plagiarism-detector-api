package br.edu.imd.apidetectorplagio.detector.model;

import org.jetbrains.annotations.NotNull;

public record Plagiarism (MetaDataPage metaDataPage, float similarityPercentage, String similarOccurrence) implements Comparable<Plagiarism>{

    @Override
    public int compareTo(@NotNull Plagiarism o) {
        return Float.compare(o.similarityPercentage, this.similarityPercentage);
    }
}
