package ch.zhaw.ads;
@SuppressWarnings("unchecked")
public class MySortedList extends MyList {

    public boolean add(Object o) {
        int i = 0;
        ListNode node = super.head.next;
        while (node.next != null) {
            boolean oisLarger;
            if (o instanceof String) {
                oisLarger = ((String) node.data).compareTo((String) o) >= 0;
            } else {
                oisLarger = Character.compare((char) node.data, (char) o) >= 0;
            }
            if (oisLarger) {
                super.add(o, i);
                return true;
            }
            node = node.next;
            i++;
        }
        super.add(o);
        return true;
    }
}