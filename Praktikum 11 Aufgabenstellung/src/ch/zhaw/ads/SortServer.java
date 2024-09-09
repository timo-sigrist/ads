package ch.zhaw.ads;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SortServer implements CommandExecutor {
    private final int DATARANGE = 10000000;
    public int dataElems; // number of data

    public void swap(int[] a, int i, int j) {
        int h = a[i];
        a[i] = a[j];
        a[j] = h;
    }

    public void bubbleSort(int[] a) {
        // k geht von hinten an den String, i durch den String
        for (int k = a.length - 1; k > 0; k--) {
            boolean noSwap = true;
            for (int i = 0; i < k; i++) {
                if (a[i] > a[i + 1]) {
                    swap(a, i, i + 1);
                    noSwap = false;
                }
            }
            if (noSwap) break;
        }
    }

    public void insertionSort(int[] a) {
        for (int k = 0; k < a.length; k++) {
            int x = a[k];
            int i;
            for (i = k; ((i > 0) && a[i - 1] > x); i--) {
                a[i] = a[i - 1];
            }
            a[i] = x;
        }
    }

    public void selectionSort(int[] a) {
        for (int k = 0; k < a.length; k++) {
            int min = k;
            for (int i = k + 1; i < a.length; i++) {
                if (a[i] < a[min]) min = i;
            }
            if (min != k) swap(a, min, k);
        }
    }

    public void streamSort(int[] a) {
        // zum Vergleichen (falls Sie Zeit und Lust haben)
        int[] b = Arrays.stream(a).sorted().toArray();
        System.arraycopy(b, 0, a, 0, a.length);
    }

    public boolean isSorted(int[] a) {
        boolean isSorted = true;
        for (int i = 0; i< a.length-1; i++) {
            if (a[i] > a[i+1]){
                isSorted = false;
                break;
            }
        }
        return isSorted;
    }

    public int[] randomData() {
        int[] a = new int[dataElems];
        Random rdm = new Random();
        for (int i = 0; i < a.length; i++) {
            a[i] = rdm.nextInt(DATARANGE);
        }
        return a;
    }

    public int[] ascendingData() {
        int[] a = randomData();
        Arrays.sort(a);
        return a;
    }

    public int[] descendingData() {
        int[] a = ascendingData();
        // Für primitive Datentypen geht Collection.inreverseOrder nicht
        int[] desc = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            desc[a.length - (i + 1)] = a[i];
        }
        return desc;
    }

    // measure time of sorting algorithm
    // generator to generate the data
    // consumer sorts the data
    // return elapsed time in ms
    // if data is not sorted an exception is thrown
    public double measureTime(Supplier<int[]> generator, Consumer<int[]> sorter) throws Exception {
        double elapsed = 0;

        int[] a = generator.get();
        int[] b = new int[dataElems];

        long startTime = System.currentTimeMillis();
        long endTime = startTime;
        int count = 0;

        while(endTime< startTime+1000){
            b = Arrays.copyOf(a,dataElems);
            sorter.accept(b);
            count++;
            endTime = System.currentTimeMillis();
        }

        elapsed = (double)(endTime - startTime) / count;
        if (!isSorted(b)) throw new Exception ("ERROR not sorted");
        return elapsed;
    }

    public String execute(String arg)  {
        Map<String, Consumer<int[]>> sorter = Map.of(
                "BUBBLE", this::bubbleSort,
                "INSERTION", this::insertionSort,
                "SELECTION", this::selectionSort,
                "STREAM", this::streamSort
        );
        Map<String, Supplier<int[]>> generator =  Map.of(
                "RANDOM", this::randomData,
                "ASC", this::ascendingData,
                "DESC", this::descendingData
        );

        String[] args = arg.toUpperCase().split(" ");
        dataElems = Integer.parseInt(args[2]);
        try {
            double time = measureTime(generator.get(args[1]), sorter.get(args[0]));
            return arg + " " + time + " ms\n";
        } catch (Exception ex) {
            return arg + " " + ex.getMessage();
        }
    }

    public static void main(String[] args) {
        SortServer sorter = new SortServer();
        String sort;
        sort = "BUBBLE RANDOM 10000"; System.out.println(sorter.execute(sort));
        sort = "SELECTION RANDOM 10000"; System.out.println(sorter.execute(sort));
        sort = "INSERTION RANDOM 10000"; System.out.println(sorter.execute(sort));

        sort = "BUBBLE ASC 10000"; System.out.println(sorter.execute(sort));
        sort = "SELECTION ASC 10000"; System.out.println(sorter.execute(sort));
        sort = "INSERTION ASC 10000"; System.out.println(sorter.execute(sort));
    }
}
