package br.edu.imd.apidetectorplagio.detector.process;

import br.edu.imd.apidetectorplagio.detector.model.Detector;
import br.edu.imd.apidetectorplagio.detector.model.MetaDataPage;
import br.edu.imd.apidetectorplagio.detector.model.Plagiarism;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Logger;

public class Executor {

    private Detector detector;
    private List<MetaDataPage> metaDataPages;


    private final String API_KEY = "AIzaSyDfy-NgE3sYkRYVEjdt7UwiE2bNqgv_wv4";
    private final String CX = "85c8998830e4f4f25";

    private final Logger LOG = Logger.getLogger(Executor.class.getName());


    public Executor(Detector detector) {
        this.detector = detector;
        this.metaDataPages = new ArrayList<>();
    }

    public void trackData(){
        LOG.info("Inicializando o rastreamendo de dados ...");
        GoogleCustomSearchClient searchClient = new GoogleCustomSearchClient(API_KEY, CX);
        this.metaDataPages = searchClient.searchByContent(detector.getContent());
        LOG.info("Rastreamendo de finalizado! ");
    }

    public List<Plagiarism> process() throws BrokenBarrierException, InterruptedException {
        LOG.info("Inicializando o processo de detecção...");

        CyclicBarrier cyclicBarrier = new CyclicBarrier(metaDataPages.size() + 1, () -> LOG.info("Processamento finalizado!"));

        for(MetaDataPage metaDataPage : metaDataPages){
            ScrapingProcessor processor = new ScrapingProcessor(metaDataPage, detector, cyclicBarrier);
            Thread thread = new Thread(processor);
            thread.start();
        }

        cyclicBarrier.await();
        return detector.getResults();
    }


}
