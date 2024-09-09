package ch.zhaw.ads;

import java.util.*;

public class FuzzySearchServer implements CommandExecutor {
    public static List<String> names = new ArrayList<>(); // List of all names
    public static Map<String, List<Integer>> trigrams = new HashMap<>(); // List of all Trigrams
    public static Map<Integer, Integer> counts = new HashMap<>(); // Key: index of

    // load all names into names List
    // each name only once (i.e. no doublettes allowed
    public static void loadNames(String nameString) {
        String[] rows = nameString.split("\n");
        for (String row : rows) {
            String[] data = row.split(";");
            if (!names.contains(data[0])){
                names.add(data[0]);
            }
        }
    }

    // add a single trigram to 'trigrams' index
    public static void addToTrigrams(int nameIdx, String trig) {
        List<Integer> foundTrigrams = trigrams.get(trig);
        if (foundTrigrams == null) {
            foundTrigrams = new ArrayList<>();
        }
        foundTrigrams.add(nameIdx);
        trigrams.putIfAbsent(trig, foundTrigrams);
    }

    // works better for flipped and short names if " " added and lowercase
    private static String nomalize(String name) {
        return " " + name.toLowerCase().trim() + " ";
    }

    // construct a list of trigrams for a name
    public static List<String> trigramForName(String name) {
        name = nomalize(name);
        int pointer = 0;
        List<String> trigrams = new ArrayList<>();
        while (name.length() > pointer + 2) {
            trigrams.add(name.substring(pointer, pointer + 3));
            pointer++;
        }
        return trigrams;
    }

    public static void constructTrigramIndex(List<String> names) {
        for (int nameIdx = 0; nameIdx < names.size(); nameIdx++) {
            List<String> trigs = trigramForName(names.get(nameIdx));
            for (String trig : trigs) {
                addToTrigrams(nameIdx, trig);
            }
        }
    }

    private static void incCount(int cntIdx) {
        Integer c = counts.get(cntIdx);
        c = (c == null)?  1 : c + 1;
        counts.put(cntIdx, c);
    }

    // find name index with most corresponding trigrams
    // if no trigram/name matches at all then return -1
    public static int findIdx(String name) {
        counts.clear();
        List<String> foundTrigs = trigramForName(name);
        for (String trig : foundTrigs) {
            List<Integer> hits = trigrams.get(trig);
            if (hits != null){
                hits.forEach(FuzzySearchServer::incCount);
            }
        }

        int maxIdx = Collections.max(counts.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        return maxIdx>0?maxIdx:-1;
    }
    // finde Namen gebe "" zurück wenn gefundener Name nicht grösser als verlangter score ist.
    public static String find(String searchName, int scoreRequired) {
        int found = findIdx(searchName);
        String foundName = "";
        if (found >= 0 && score(found) >= scoreRequired) {
            foundName = names.get(found);
        }
        return foundName;
    }

    private static int score(int found) {
        String foundName = names.get(found);
        return (int) (100.0 * Math.min(counts.get(found), foundName.length()) / foundName.length());
    }

    public String execute(String searchName)  {
        int found = findIdx(searchName);
        if (found >= 0) {
            int score = score(found);
            String foundName = names.get(found);
            return searchName + " -> " + foundName + " " + score + "%\n";
        } else {
            return "nothing found\n";
        }
    }

    public static void main(String[] args)  {
        FuzzySearchServer fs = new FuzzySearchServer();
        System.out.println(fs.execute("Kiptum Daniel"));
        System.out.println(fs.execute("Daniel Kiptum"));
        System.out.println(fs.execute("Kip Dan"));
        System.out.println(fs.execute("Dan Kip"));
    }

    static {
        String rangliste = "Mueller Stefan;02:31:14\n" +
                "Marti Adrian;02:30:09\n" +
                "Kiptum Daniel;02:11:31\n" +
                "Ancay Tarcis;02:20:02\n" +
                "Kreibuhl Christian;02:21:47\n" +
                "Ott Michael;02:33:48\n" +
                "Menzi Christoph;02:27:26\n" +
                "Oliver Ruben;02:32:12\n" +
                "Elmer Beat;02:33:53\n" +
                "Kuehni Martin;02:33:36\n";
        loadNames(rangliste);
        constructTrigramIndex(names);
    }
}
