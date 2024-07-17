package test.tehnic.nexttech.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Task {

    private String name;
    private String command;
    private List<String> requires = new ArrayList<>();

}
