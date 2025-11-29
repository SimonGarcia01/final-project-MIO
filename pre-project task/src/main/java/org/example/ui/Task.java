package org.example.ui;

import org.example.control.GraphController;
import org.example.modelStructures.IGraph;
import org.example.modelStructures.Vertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Task {

    // Attributes, global variables
    private static IGraph<Vertex> graph;
    private static GraphController control = new GraphController();
    private static HashMap<String, String> stopsWithLines = new HashMap<>();
    private static HashMap<String, Integer> stopsWithOrientations = new HashMap<>();
    private static HashMap<String, Integer> stopsWithVariants = new HashMap<>();
    private static HashMap<String, Integer> stopsWithStopSequences = new HashMap<>();
    private List<String[]> input = new ArrayList<>();

    // Scanner
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Task task = new Task();

        // 2111 is the size of the stops that are in linestops. There are 8 stops that are not in linestops.
        graph = control.createGraph(2111);
        //System.out.println(task.readStopsAndCreateVertexes());
        task.readStopsAndCreateVertexes();

        // Group by lines
        //System.out.println(task.sortByConditions());
        //task.sortByConditions();


        // Create and add edges
        //System.out.println(task.createAndConnectEdges());
        task.createAndConnectEdges();


        int option;

        do {
            System.out.println("\n*** BIENVENIDO A NUESTRO GRAFO ***\n");
            System.out.println("-- Menu --\n");
            System.out.println("1. Mostrar lista de paradas ordenadas por ruta, orientación, variante y stopsequence.");
            System.out.println("2. Mostrar lista de paradas ordenadas por ruta, orientación, variante y stopsequence con subgrafo.");
            System.out.println("3. Mostrar matriz de pesos del grafo (por ahora, donde hay una relación se pone 1 y donde no 0). Matriz muy grande, utilizar buscador de consola para encontrar 1.0's");
            System.out.println("4. Salir.");

            System.out.print("Digite su opcion: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println(graph.getEdges());
                    break;
                case 2:
                    System.out.println(graph.getVertices());
                    //var list = graph.getVertices().stream().collect(Collectors.groupingBy(Vertex::getLineId));
                    //list.forEach((lineId, lista) -> {
                      //  System.out.println(ANSI_CYAN + "Ruta: " + lineId + ANSI_RESET);
                        //lista.forEach(System.out::println);
                    //});
                    break;
                case 3:
                    System.out.println(graph.printMatrix());
                    break;
                case 4:
                    System.out.println("¡Chao profe! ¡Esperamos que le haya gustado nuestro avance!");
                    break;
                default:
                    System.out.println("Opcion no valida. Vuelva a intentarlo.");
                    break;
            }

        } while (option != 3);

    }

    public void createMaps() {

        String path = "src/main/resources/linestops-241.csv";
        File csv = new File(path);


        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                // If first line looks like a header, skip it
                if (firstLine) {
                    firstLine = false;
                    String low = line.toLowerCase();
                    if (low.contains("stop_id") || low.contains("stopid") || low.contains("id"))
                        continue;
                }


                // Simple CSV split (assumes stable CSV without quoted commas)
                String[] parts = line.split(",", -1);
                if (parts.length < 1) {
                    // Not enough columns — skip line
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                // We add each line to the arraylist
                input.add(parts);
                //System.out.println(Arrays.stream(Arrays.stream(parts).toArray()).toList());

                // Stops by line
                String stopId = parts[4].trim();
                String lineId = parts[3].trim();

                stopsWithLines.put(stopId, lineId);

                // Add orientation filter. Lines by orientation
                // So, it means that is stops by line and orientation. :)
                int orientation = Integer.parseInt(parts[2].trim());
                stopsWithOrientations.put(stopId, orientation);

                // Stops by line, orientation and variant
                // Add another filter and order
                int variant = Integer.parseInt(parts[6].trim());
                stopsWithVariants.put(stopId, variant);
                
                // Stops by line, orientation, variant and stopsequence
                // Add another filter and order
                int stopsequence = Integer.parseInt(parts[1].trim());
                stopsWithStopSequences.put(stopId, stopsequence);
                //System.out.println(stopsWithStopSequences);

            }

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();

        }

    }

    // This method read stops files and create vertexes
    public String readStopsAndCreateVertexes() {

        createMaps();

        String[] nolinestops = {"4", "5", "6", "9", "10", "21", "22", "41"};

        String path = "src/main/resources/stops-241.csv";
        File csv = new File(path);

        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                // If first line looks like a header, skip it
                if (firstLine) {
                    firstLine = false;
                    String low = line.toLowerCase();
                    if (low.contains("stop_id") || low.contains("stopid") || low.contains("id"))
                        continue;
                }

                // Simple CSV split (assumes stable CSV without quoted commas)
                String[] parts = line.split(",", -1);
                if (parts.length < 1) {
                    // Not enough columns — skip line
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }


                String stopId = parts[0].trim();

                if(Arrays.asList(nolinestops).contains(stopId)) {
                    continue;
                }

                String name = parts[3].trim();
                double x;
                double y;

                try {
                    x = Double.parseDouble(parts[4].trim()) / 1e7;
                    y = Double.parseDouble(parts[5].trim()) / 1e7;
                } catch (NumberFormatException nfe) {
                    System.err.println("Skipping line with invalid coordinates: " + line);
                    continue;
                }

                // We create the stops. Each stop is a vertex.
                //String lineId = stopsWithLines.get(stopId);
                //int orientation = stopsWithOrientations.get(stopId);
                //int variant = stopsWithVariants.get(stopId);
                //int stopsequence = stopsWithStopSequences.get(stopId);
                control.createAndAddVertex(graph, name, stopId, x, y);

            }

            return "Vertexes created successfully.";

        } catch (IOException e) {

            System.err.println("Error reading file '" + csv.getPath() + "': " + e.getMessage());
            e.printStackTrace();
            return "Vertexes not created successfully.";

        }

    }

    // This method read linestops file and group by line
    /*
    public String sortByConditions() {

            // Hago groupBy por ruta
            List<Vertex> list = new ArrayList<>();
            //graph.getVertices().stream().sorted(Comparator.comparing(Vertex::getLineId)
              //              .thenComparing(Vertex::getOrientation)
               //             .thenComparing(Vertex::getVariant)
                //            .thenComparing(Vertex::getStopSequence))
                  //          .forEach(list::add);
            graph.setVertexes(list);
            return "Vertexes sorted ready.";

    }
     */

    // This method create and add edges to the graph
    public String createAndConnectEdges(){
        // We create the edge between two vertexes.
        try {

            // No recorro vertices sino linestops que son las conexiones
            String lineId;
            String stop1Id;
            String stop2Id;
            String orientation;
            String variant;
            String stopSequence;

            for(int i = 0; i < input.size()-1; i++) {

                lineId = input.get(i)[3];
                stop1Id = input.get(i)[4];
                stop2Id = input.get(i+1)[4];
                orientation = input.get(i)[2];
                variant = input.get(i)[6];
                stopSequence = input.get(i)[1];

                control.connectEdge(graph, lineId, stop1Id, stop2Id, orientation, variant, stopSequence, 1.0);

            }


            return "Edges created and added to the graph successfully";

        } catch (Exception e) {

            System.err.println("Error reading file :" + e.getMessage());
            e.printStackTrace();
            return "Edges created and added to the graph unsuccessfully.";

        }
        
    }

}
