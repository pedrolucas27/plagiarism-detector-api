package br.edu.imd.apidetectorplagio.detector.process;

import br.edu.imd.apidetectorplagio.detector.model.Detector;
import br.edu.imd.apidetectorplagio.detector.model.MetaDataPage;
import br.edu.imd.apidetectorplagio.detector.model.Plagiarism;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ScrapingProcessor implements Runnable {

    private MetaDataPage metaDataPage;
    private Detector detector;
    private CyclicBarrier barrier;

    public ScrapingProcessor(MetaDataPage metaDataPage, Detector detector, CyclicBarrier barrier) {
        this.metaDataPage = metaDataPage;
        this.detector = detector;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            PageScraping pageScraping = new PageScraping(metaDataPage);
            pageScraping.start();

            if(pageScraping.isSuccessScraping()){
                List<Plagiarism> candidates = catchPlagiarismCandidates(pageScraping);
                Optional<Plagiarism> optional = candidates.stream().max(Plagiarism::compareTo);
                if(optional.isPresent()){
                    detector.addPlagiarism(optional.get());
                }
            }

            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e){
            System.out.println(e.getMessage());
        }
    }


    private float calculateSimilarityPercentage(String paragraphDocument) {
        int absDistance = detector.calculateSimilarity(paragraphDocument);
        return (((float) paragraphDocument.length() - absDistance) / paragraphDocument.length()) * 100;
    }

    private boolean isToCheck(String paragraphDocument, String content) {
        int diff = paragraphDocument.length() - content.length();
        return (diff >= 0 || (Math.abs(diff) <= paragraphDocument.length()));
    }

    private List<Plagiarism> catchPlagiarismCandidates(PageScraping pageScraping){
        String content = detector.getContent();
        List<Plagiarism> plagiarisms = new ArrayList<>();

        float similarityPercentage;
        for (String text : pageScraping.getPageContent()) {
            if(isToCheck(text, content)){
                similarityPercentage = calculateSimilarityPercentage(text);
                if(detector.isPlagiarism(similarityPercentage)){
                    plagiarisms.add(new Plagiarism(pageScraping.getMetaDataPage(), similarityPercentage, text));
                }
            }
        }

        return  plagiarisms;
    }
}
