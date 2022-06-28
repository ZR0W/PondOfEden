import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class StatRecord {
    HashMap<Creature, History> creatureHistories = new HashMap<Creature, History>();

    public StatRecord(Collection<Creature> creatures) {
        for(Creature creature : creatures) {
            creatureHistories.put(creature, new History());
        }
    }

    public boolean addEntry(Creature c, Creature approached, boolean isSuccessful, Double MV, Double SPMV) {
        if(creatureHistories.containsKey(c)) {
            History h = creatureHistories.get(c);
            h.addRecord(c, isSuccessful, MV, SPMV);
            creatureHistories.put(c, h);
            return true;
        }else {
            return false;
        }
    }

    public String getSPMVHistory(Creature c) {
        History h = creatureHistories.get(c);
        Double[] d = h.getSPMVLog();
        String[] s = new String[d.length];
        for(int i = 0; i < d.length; i++) {
            s[i] = String.valueOf(d[i]);
        }
        return String.join(", ", s);
    }

    private class History {

        ArrayList<Record> allRecords;
        public History() {
            allRecords = new ArrayList<Record>();
        }

        public void addRecord(Creature approached, boolean isSuccessful, Double MV, Double SPMV) {
            allRecords.add(new Record(approached, isSuccessful, MV, SPMV));
        }

        public Double[] getSPMVLog() {
            Double[] out = new Double[allRecords.size()];
            for(int i = 0; i < allRecords.size(); i++) {
                Double spmv = allRecords.get(i).SPMV;
                out[i] = spmv;
            }
            return out;
        }
    }

    private class Record {
        //composed of interaction and result
        Creature approached;
        boolean isSuccessful;
        Double MV;
        Double SPMV;
        public Record(Creature approached, boolean isSuccessful, Double MV, Double SPMV) {
            this.approached = approached;
            this.isSuccessful = isSuccessful;
            this.MV = MV;
            this.SPMV = SPMV;
        }
    }
}
