package br.edu.imd.apidetectorplagio.detector.process;

import br.edu.imd.apidetectorplagio.detector.model.MetaDataPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class GoogleCustomSearchClient {

    private String apiKey;
    private String cx;
    private OkHttpClient client;

    private final String URL_BASE = "https://www.googleapis.com/customsearch/v1";

    private final Logger LOG = Logger.getLogger(GoogleCustomSearchClient.class.getName());

    public GoogleCustomSearchClient(String apiKey, String cx){
        this.apiKey = apiKey;
        this.cx = cx;
        this.client = new OkHttpClient();
    }

    public List<MetaDataPage> searchByContent(String content) {
        LOG.info("Pesquisando conteúdo ...");
        Response response = visit(content);

        try {
            if(response.code() == 200 && Objects.nonNull(response.body())){
                JsonObject jsonResult = new JsonParser().parse(response.body().string()).getAsJsonObject();
                JsonArray jsonElements = jsonResult.getAsJsonArray("items");

                return buildResults(jsonElements);
            }
        }catch (IOException ioException){
            LOG.info("Falha no processamento de conversão dos metadados das páginas referente a pesquisa: "+ ioException.getMessage());
        }

        return Collections.emptyList();
    }


    private Response visit(String content) {
        String url = getSearchUrlEncode(content);

        try{
            LOG.info("Visitando url: "+url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            return client.newCall(request).execute();
        }catch (IOException ioException){
            LOG.info("Falha ao acessar url: "+url);
            LOG.info("Error: "+ioException.getMessage());
        }

        return new Response.Builder()
                .code(400)
                .message("There was an error when searching for the sender content at the following address: "+url)
                .build();
    }

    private String getSearchUrlEncode(String content){
        return String.format("%s?key=%s&cx=%s&q=%s", URL_BASE, apiKey, cx, URLEncoder.encode(content));
    }

    private List<MetaDataPage> buildResults(JsonArray jsonElements) throws JsonProcessingException {
        List<MetaDataPage> results = new ArrayList<>();

        for(int i = 0; i < jsonElements.size(); i++){
            JsonObject asJsonObject = jsonElements.get(i).getAsJsonObject();
            if(Objects.nonNull(asJsonObject)) {
                MetaDataPage metaDataPage = getSearchResult(asJsonObject);
                results.add(metaDataPage);
            }
        }

        return results;
    }

    private MetaDataPage getSearchResult(JsonObject asJsonObject) {
        return new MetaDataPage.Builder()
                .link(asJsonObject.get("link").getAsString())
                .title(asJsonObject.get("title").getAsString())
                .kind(asJsonObject.get("kind").getAsString())
                .snippet(asJsonObject.get("snippet").getAsString())
                .htmlSnippet(asJsonObject.get("htmlSnippet").getAsString())
                .build();
    }

}
