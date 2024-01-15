package br.edu.imd.apidetectorplagio.controller;



import br.edu.imd.apidetectorplagio.detector.model.Detector;
import br.edu.imd.apidetectorplagio.detector.model.Levenshtein;
import br.edu.imd.apidetectorplagio.detector.model.Plagiarism;
import br.edu.imd.apidetectorplagio.detector.process.Executor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

@RestController
@CrossOrigin(origins = "*")
public class TextPlagiarismDetector {

    public record Body(String content, int plagiarismPercentage){}

    @PostMapping("/check")
    public ResponseEntity<?> detectPlagiarism(@RequestBody Body request) {
        try {
            Detector detector = new Detector(request.content(), request.plagiarismPercentage(), new Levenshtein());
            Executor executor = new Executor(detector);

            executor.trackData();
            List<Plagiarism> plagiarisms = executor.process();
            return new ResponseEntity<>(plagiarisms, HttpStatusCode.valueOf(200));
        }catch (BrokenBarrierException | InterruptedException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatusCode.valueOf(400)) ;
        }
    }

}
