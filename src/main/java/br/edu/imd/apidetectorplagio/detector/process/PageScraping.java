package br.edu.imd.apidetectorplagio.detector.process;

import br.edu.imd.apidetectorplagio.detector.model.MetaDataPage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PageScraping {

    private final MetaDataPage metaDataPage;
    private Set<String> pageContent;
    private boolean successScraping;

    public PageScraping(MetaDataPage metaDataPage){
        this.metaDataPage = metaDataPage;
        this.successScraping = false;
    }

    public void start() {
        this.pageContent = new HashSet<>();
        try {
            Connection connection = Jsoup.connect(metaDataPage.getLink());
            if(connection.execute().statusCode() == 200){
                Document document = connection.get();

                this.pageContent = new HashSet<>(document.getElementsByTag("p").eachText());
                this.successScraping = true;
            }
        }catch (IOException ioException){
            System.err.println("[SCRAPING ERROR]: "+ioException.getMessage());
        }
    }

    public Set<String> getPageContent() {
        return pageContent;
    }

    public boolean isSuccessScraping() {
        return successScraping;
    }

    public MetaDataPage getMetaDataPage() {
        return metaDataPage;
    }
}
