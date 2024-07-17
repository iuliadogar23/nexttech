package test.tehnic.nexttech.service;

import org.springframework.stereotype.Service;
import test.tehnic.nexttech.model.Job;
import test.tehnic.nexttech.model.Task;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobProcessingService {

    public Job orderJob(Job job) {
        return processJob(job);
    }

    public String getScriptJob(Job job) {
        final String firstCommand = "#!/usr/bin/env bash";

        Job orderJob = processJob(job);

        StringBuilder script = new StringBuilder();

        script.append(firstCommand)
                .append('\n');
        for (Task task : orderJob.getTasks()) {
            script.append(task.getCommand())
                    .append('\n');
        }

        return script.toString();
    }

    private Job processJob(Job job) {
        List<String> required = job.getTasks().stream().map(t-> t.getRequires()).flatMap(List::stream).collect(Collectors.toList());
        List<String> tasks = job.getTasks().stream().map(t->t.getName()).collect(Collectors.toList());

        tasks.removeAll(required);

        if (tasks.size()>1)
            throw new RuntimeException("Too many tasks; Format not allowed");
        if (tasks.size() == 0)
            throw new RuntimeException("Format not allowed; circular reference detected");

        Task lastTask = job.getTasks().stream().filter(t -> t.getName().equals(tasks.get(0))).findAny().get();
        Set<Task> result = reccursiveTest(new HashSet<>(), job, lastTask, new ArrayList<>());
        result.add(lastTask);


        return new Job(result.stream().toList());
    }

    private Set<Task> reccursiveTest(Set<Task> tasks, Job job, Task current, List<String> executionTask)
    {
        if (current.getRequires().isEmpty())
            return tasks;
        else {
            executionTask.addAll(current.getRequires());
            for (String req: executionTask)
            {
                for (Task task : job.getTasks())
                    if (task.getName().equals(req)) {
                        executionTask.remove(req);
                        tasks.add(task);
                        return reccursiveTest(tasks, job, task, executionTask);
                    }
            }
        }
        throw new RuntimeException("Entity not found");
    }

    //old implementations
//    private Set<Task> reccursiveTest(Set<Task> tasks, Job job, Task current)
//    {
//        if (current.getRequires().isEmpty())
//            return tasks;
//        else {
//            for (String req: current.getRequires())
//            {
//                for (Task task : job.getTasks())
//                    if (task.getName().equals(req)) {
//                        tasks.add(task);
//                        return reccursiveTest(tasks, job, task);
//                    }
//            }
//        }
//        throw new RuntimeException("Entity not found");
//    }

    //    private Job processJobOrder(Job job) {
//        Job copiedJob = job;
//        List<Task> orderedTasks = new LinkedList<Task>();
//        List<String> nextOrderedTasks = new ArrayList<>();
//
//        do {
//            Task upcomingTask = copiedJob.getTasks().stream().filter(task -> task.getRequires() == null || task.getRequires().isEmpty()).findFirst().get();
//            orderedTasks.add(upcomingTask);
//            copiedJob.getTasks().remove(upcomingTask);
//            nextOrderedTasks = orderedTasks.stream().map(t -> t.getName()).toList();
//            for (int i = 0; i < copiedJob.getTasks().size(); i++) {
//                List<String> finalNextOrderedTasks = nextOrderedTasks;
//                copiedJob.getTasks().get(i).setRequires(copiedJob.getTasks().get(i).getRequires().stream().filter(r -> !finalNextOrderedTasks.contains(r)).toList());
//            }
//
//        } while (!copiedJob.getTasks().isEmpty());
//        return new Job(orderedTasks);
//    }


}
