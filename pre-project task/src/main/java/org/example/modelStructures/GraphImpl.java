package org.example.modelStructures;

import org.example.exceptions.GraphException;

import java.util.*;


public class GraphImpl implements IGraph<Vertex> {

    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BACKGROUND_BLACK = "\u001B[40;1m";
    public static final String ANSI_MAGENTA = "\u001B[35m";
    public static final String ANSI_YELLOW = "\u001B[33;1m";

    //Attributes
    private List<Vertex> vertices;
    // This data structure is ordered by default by keys
    private SortedMap<String, List<String>> edges;
    private double[][] matrix;
    private int maxSize;
    private List<String> infoByLine = new ArrayList<>();
    private HashMap<String, String> lines = new HashMap<>();
    private List<String> temporalLines = new ArrayList<>();
    private List<String> temporal2 = new ArrayList<>();


    
    //Constructor
    public GraphImpl(int maxSize) {
        this.maxSize = maxSize;
        this.vertices = new ArrayList<>();
        this.edges = new TreeMap<>();
        // 0.0 is the default number for double.
        this.matrix = new double[maxSize][maxSize];


    }

    //Add a vertex
    @Override
    public void add(Vertex value) {
        if (vertices.size() >= maxSize)
            throw new IllegalStateException("Max graph size reached.");

        vertices.add(value);
    }

    @Override
    public void setVertexes(List<Vertex> vertexes) {

        this.vertices = vertexes;
    }

    @Override
    public String getEdges() {

        //return edges;
        StringBuilder st = new StringBuilder();
        edges.forEach((key, value) -> st.append("\n" + ANSI_BACKGROUND_BLACK + "Ruta " +  ANSI_MAGENTA + findNameByLineId(key) + ANSI_YELLOW + " ➜ id: " + ANSI_CYAN + key + ANSI_RESET + "\n" + value));
        return String.valueOf(st);

    }

    //Add an edge between two vertices
    int cnt = 0;

    @Override
    public void addEdge(String lineId, String stop1Id, String orientation, String variant, String stopSequence, double weight) throws GraphException {

        // Positions in adjacency matriz are in the form: (row, colum)

        // Row
        int i = findStopIndexById(stop1Id); // -> x
        //System.out.println("Index: " + i);

        // Column
        //int j = findStopIndexById(stop2Id); // -> y


        //System.out.println("Coordenadas: (" + i + ", " + j + ")");

        // Tengo que buscar la posicion en la matriz de stop1Id y de Stop2Id
        // Para poder hacer esto, debo encontrar el vertice

        // Los vertices son PARADAS



        // AUN ESTA MAL PORQUE EFECTIVAMENTE ESTA TOMANDO VALORES IGUALES
        // TODO ES PORQUE SE ESTA UNIENDO DE SEGUIDO PERO EL HASHMAP NO TIENE ORDEN
        //if (i == j)
            // verificar variante
            //System.out.println(stop1Id);
            //System.out.println(stop2Id);
            //System.out.println(i);
            //System.out.println(j);
            //throw new GraphException("Self-loops are not allowed.");

        /*
        if (matrix[i][j] != 0.0)
            throw new GraphException("Parallel edges are not allowed.");
         */

        // ESTO NO LO ESTA HACIENDO BIEN. OJO
        //matrix[i][j] = 1.0;
        cnt++;

        //System.out.println("LineId: " + lineId);

        String infoEdge = makeInfoEdge(lineId, stop1Id, orientation, variant, stopSequence, weight);
        infoByLine.add(infoEdge);

    }

    // Auxiliar method
    public void group() {

        for(int k = 0; k < temporalLines.size(); k++) {
            List<String> filtered = new ArrayList<>();
            for(int m = 0; m < infoByLine.size(); m++) {

                if(infoByLine.get(m).split(", ")[0].split(": ")[1].equals(temporalLines.get(k))) {
                    filtered.add(infoByLine.get(m));

                }

            }

            filtered = sortOrientation(filtered);
            edges.put(temporalLines.get(k), filtered);

        }

        temporalLines.clear();

    }

    // We sort each list associated to a key
    public List<String> sortOrientation (List<String> list) {

        List<String> temporal3 = new ArrayList<>();
        List<String> temporal4 = new ArrayList<>();
        List<String> temporal5 = new ArrayList<>();
        HashSet<Integer> variants0 = new HashSet<>();
        HashSet<Integer> variants1 = new HashSet<>();

        for(String elem :  list) {
            var orientation = Integer.parseInt(elem.split(", ")[2].split(": ")[1]);
            if(orientation == 0) {
                temporal4.add(elem);
                variants0.add( Integer.parseInt(elem.split(", ")[3].split(": ")[1]));
            }
            else {
                temporal5.add(elem);
                variants1.add( Integer.parseInt(elem.split(", ")[3].split(": ")[1]));
            }
        }

        temporal4 = sortVariant(temporal4);
        temporal5 = sortVariant(temporal5);

        for(Integer i : variants0) {
            temporal4 = sortStopSequence(temporal4, i);
        }

        for(Integer i : variants1) {
            temporal5 = sortStopSequence(temporal5, i);
        }

        temporal3.addAll(temporal4);
        temporal3.addAll(temporal5);

        return temporal3;

    }

    // We sort each list associated to a key
    public List<String> sortVariant (List<String> list) {

        for(int i = 0; i < list.size()-1; i++) {

            for(int j=0; j < (list.size()-i)-1; j++) {
                var elem2 = list.get(j);
                var variant2 = Integer.parseInt(elem2.split(", ")[3].split(": ")[1]);
                var elem3 = list.get(j+1);
                var variant3 = Integer.parseInt(elem3.split(", ")[3].split(": ")[1]);

                if(variant2 > variant3) {
                    list.set(j, elem3);
                    list.set(j+1, elem2);
                }
            }

        }

        return list;

    }

    public List<String> sortStopSequence (List<String> list, int variant) {

        for (int j = 0; j < (list.size() - 1); j++) {
            var elem2 = list.get(j);
            var sequence2 = Integer.parseInt(elem2.split(", ")[4].split(": ")[1]);
            var variant2 = Integer.parseInt(elem2.split(", ")[3].split(": ")[1]);
            var elem3 = list.get(j + 1);
            var sequence3 = Integer.parseInt(elem3.split(", ")[4].split(": ")[1]);
            var variant3 = Integer.parseInt(elem3.split(", ")[3].split(": ")[1]);

            if (variant2 == variant) {
                if (variant3 == variant) {
                    if (sequence2 > sequence3) {
                        list.set(j, elem3);
                        list.set(j + 1, elem2);
                    }
                } else {
                    break;
                }
            }
        }

        return list;

    }


    // Auxiliar function for avoid duplicity
    public String makeInfoEdge(String lineId, String stop1Id, String orientation, String variant, String stopSequence, double weight) {

        String infoEdge = "\nLineId: " + lineId;
        infoEdge += ", Parada: " + findNameByStopId(stop1Id);
        //infoEdge += ", Destino: " + findNameByStopId(stop2Id);
        infoEdge += ", Orientación: " + orientation;
        infoEdge += ", Variante: " + variant;
        infoEdge += ", StopSequence: " + stopSequence;
        infoEdge += ", Peso: " + weight + ";\n";

        return infoEdge;

    }

    public void createAdjacencyMatrix(String stop1Id, String stopId) {

    }

    //Remove an edge between two vertices
    @Override
    public void removeEdge(String Stop1Id, String Stop2Id) throws GraphException {

        int i = findStopIndexById(Stop1Id);
        int j = findStopIndexById(Stop2Id);

        if (matrix[i][j] == 0.0)
            throw new GraphException("Edge does not exist.");

        matrix[i][j] = 0.0;
    }

    //Remove a vertex
    @Override
    public void removeVertex(String StopId) throws GraphException {

        int idx = findStopIndexById(StopId);

        vertices.remove(idx);

        // Shift rows upward
        for (int r = idx; r < vertices.size(); r++) {
            matrix[r] = matrix[r + 1];
        }

        // Shift columns left
        for (int r = 0; r < vertices.size() + 1; r++) {
            for (int c = idx; c < vertices.size(); c++) {
                matrix[r][c] = matrix[r][c + 1];
            }
        }

        // Cleanup last row and column
        int n = vertices.size();
        for (int i = 0; i < maxSize; i++) {
            matrix[n][i] = 0.0;
            matrix[i][n] = 0.0;
        }

    }

    @Override
    public String printMatrix() {

        StringBuilder text = new StringBuilder();
        int n = vertices.size();

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                text.append(matrix[i][j]);
                text.append("\t");
            }
            text.append("\n");
        }

        return String.valueOf(text);

    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    // DESPUES ACOMODO ESTA, PRIMERO ACOMODO LA MATRIZ DE ADYACENCIA. OJO
    public int findStopIndexById(String id) throws GraphException {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getStopId().equals(id)) {
                return i;
            }
        }
        throw new GraphException("Stop with ID " + id + " not found.");
    }

    // Auxiliar method for find the name given a stopId
    public String findNameByStopId(String stopId) {

        for(Vertex v :  vertices) {
            if(v.getStopId().equals(stopId)) {
                return v.getName();
            }
        }

        return "No name found for this stopId.";

    }

    // Auxiliar method for find the name given a stopId
    public String findNameByLineId(String lineId) {

        return  lines.get(lineId) != null ? lines.get(lineId) : "No line found for this lineId.";

    }

    @Override
    public void createLine(String lineId, String lineName) {
        lines.put(lineId, lineName);
        temporalLines.add(lineId);
    }

}