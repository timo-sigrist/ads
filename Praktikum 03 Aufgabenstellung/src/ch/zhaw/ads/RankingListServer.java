package ch.zhaw.ads;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RankingListServer implements CommandExecutor {

    private List<Competitor> list;

    public RankingListServer() {
        this.list = new LinkedList<>();
    }

    public List<Competitor> createList(String rankingText) {
        String [] rankingTextParts = rankingText.split("\n");
        for (String text : rankingTextParts){
            String[] textParts = text.split(";");
            this.list.add(new Competitor(0, textParts[0], textParts[1]));
        }
        return this.list;
    }

    public String createSortedText(List<Competitor> competitorList) {
        StringBuilder sortedText = new StringBuilder();
        Collections.sort(competitorList);
        int ranked = 1;
        for (Competitor c : competitorList) {
            c.setRank(ranked);
            ranked++;
            sortedText.append(c).append(System.lineSeparator());
        }
        return sortedText.toString();
    }

    public String createNameList(List<Competitor> competitorList) {
        StringBuilder nameList = new StringBuilder();
        CompetitorNameListComperator nameListComperator = new CompetitorNameListComperator();
        Collections.sort(competitorList, nameListComperator);
        for (Competitor c : competitorList) {
            c.setRank(0);
            nameList.append(c).append(System.lineSeparator());
        }
        return nameList.toString();
    }

    public String execute(String rankingList) {
        List<Competitor> competitorList = createList(rankingList);
        return "Rangliste\n" + createSortedText(competitorList);
    }
}
 class CompetitorNameListComperator implements Comparator<Competitor> {

     @Override
     public int compare(Competitor o1, Competitor o2) {
         int dif = 0;
         if (o1.getName().equals(o2.getName())) {
             SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
             try {
                 Date datethis = sdf.parse(o1.getTime());
                 Date dateToComp = sdf.parse(o2.getTime());
                 dif = datethis.compareTo(dateToComp);
             } catch (ParseException e) {
                 e.printStackTrace();
             }
         } else {
             dif = o1.getName().compareTo(o2.getName());
         }
         return dif;
     }
 }