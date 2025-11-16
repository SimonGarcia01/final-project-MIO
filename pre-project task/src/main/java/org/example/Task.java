package org.example;

import org.example.model.GraphController;

public class Task {

    public static void main(String[] args) {

        GraphController control = new GraphController();

        System.out.println(control.readCSV());

    }

}
