package test.tehnic.nexttech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import test.tehnic.nexttech.model.Job;
import test.tehnic.nexttech.service.JobProcessingService;

@RestController
public class JobProcessingController {

    @Autowired
    private JobProcessingService jobService;

    @PostMapping("/job")
    public ResponseEntity<Job> getTasksInOrder(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.orderJob(job));
    }

    @PostMapping("/script")
    public ResponseEntity<String> getScript(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.getScriptJob(job));
    }

}
