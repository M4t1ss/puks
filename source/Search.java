import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Search {
	//Puka majina
    private static String START = "0";
    //Sivena majina
    public static String END = "0";
    public static int counter = 0;
    //Saraksts ar visiem celiniem
    private static Map<String, Integer> map = new HashMap<String, Integer>();
    static ValueComparator bvc =  new ValueComparator(map);
    //Saraksts ar visam tacinam no Puka majinas lidz Sivena majinai
    static ArrayList<TreeMap<String,Integer>> listOlists = new ArrayList<TreeMap<String,Integer>>();

    public static void main(String[] args) throws IOException {
    	int virsotnes=0;
		String v="-1";
	    String data;
        Graph graph = new Graph();
		//nolasa no faila
		FileInputStream fstream = new FileInputStream(args[0]);
	    DataInputStream in = new DataInputStream(fstream);
	    @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    while ((data = br.readLine()) != null)   {
	    	String[] tmp = data.split(" ");//atdalitajs
	    	for(String s: tmp){
	    		//nolasa virsotnu skaitu
	    		if (virsotnes==0){
	    			virsotnes=Integer.parseInt(s);
	    			//Sivena majina ir pedeja virsotne
	    			END = Integer.toString(--virsotnes);
	    		}else if (v=="-1"){
	    			v=s;
	    		}else if (v!="-1"){
	                graph.addTwoWayVertex(v, s);
	                v="-1";
	    		}
	    	}
	    }
        LinkedList<String> visited = new LinkedList<String>();
        visited.add(START);
        //kur slept medu
        new Search().breadthFirst(graph, visited);
        Set<String> resultSet = new HashSet<>();
        for (TreeMap<String,Integer> path : listOlists) {
        	//sakarto tacinas celinus pec to izmantosanas biezuma
            TreeMap<String,Integer> pathMapS = new TreeMap<String,Integer>(bvc);
        	for(Map.Entry<String,Integer> entry : path.entrySet()) {
	        	  String key = entry.getKey();
        		  pathMapS.put(key, map.get(key));
        		}
        	//pievieno biezako pie rezultatiem
        	resultSet.add(pathMapS.firstKey());
        }
        //izdruka rezultatu skaitu
        System.out.print(resultSet.size()+" ");
        //izdruka rezultatus
        for (String pods : resultSet){
            System.out.print(pods+" ");
        }
    }
    
    //BFS algoritms
    private void breadthFirst(Graph graph, LinkedList<String> visited) {
        LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
        for (String node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(END)) {
                visited.add(node);
                //izsauc funkciju, kas saglaba tacinu saraksta
                printPath(visited);
                visited.removeLast();
                break;
            }
        }
        for (String node : nodes) {
            if (visited.contains(node) || node.equals(END)) {
                continue;
            }
            visited.addLast(node);
            breadthFirst(graph, visited);
            visited.removeLast();
        }
    }

    private void printPath(LinkedList<String> visited) {
        //tacinas atrasana
        LinkedList<String> sarakstins = new LinkedList<String>(visited);
        int size = sarakstins.size()-1;
        String v,w = "-1";
        TreeMap<String,Integer> pathMap = new TreeMap<String,Integer>(bvc);
        
    	for (int i=0; i<size; i++){
    		if (w=="-1"){
    			v=sarakstins.remove();
    			w=sarakstins.remove();
            	//ieliek skautni saraksta 
                if(map.containsKey(v+" "+w)){
                	int count = map.get(v+" "+w);
                	map.put(v+" "+w, count+1);
                	pathMap.put(v+" "+w, 0);
                }else{
                	map.put(v+" "+w, 1);
                	pathMap.put(v+" "+w, 0);
                }
    		}else{
    			v=w;
    			w=sarakstins.remove();
            	//ieliek skautni saraksta 
                if(map.containsKey(v+" "+w)){
                	int count = map.get(v+" "+w);
                	map.put(v+" "+w, count+1);
                	pathMap.put(v+" "+w, 0);
                }else{
                	map.put(v+" "+w, 1);
                	pathMap.put(v+" "+w, 0);
                }
    		}
    	}
    	//pievieno tacinu sarakstam
    	listOlists.add(pathMap);
    }
}