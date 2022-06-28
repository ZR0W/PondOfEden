public class Creature {

    private Double SPMV; //self-perceived mate value
    private Double MV; //mate value
    int x = 0;
    int y = 0;

    public Creature(Double MV) {
        this.MV = MV;
        //TODO: how should initial SPMV be generated
        this.SPMV = 5.0;
    }

    public void setLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int[] getLocation() {
        return new int[]{x, y};
    }

    public Double getSPMV() {
        return SPMV;
    }

    public Double getMV() {
        return MV;
    }

    /*
    Creature's SPMV changes as a function of the difference between the MV of the rejecter and the SPMV of the creaure itself
     */
    public void getRejected(Creature c) {
        SPMV -= (getSPMV()-c.getMV())*0.2;
    }

    /*
    will reject, return false, if approacher MV is lower than own SPMV
    what interaction should occur to SPMV when approached by a creature of a MV lower than the creature's own SPMV?
     */
    public boolean beApproachedBy(Creature c) {
        if(c.getMV() > getSPMV()) {
            //accept
            mateSuccessfully(c);
            return true;
        }else{
            //reject
            return false;
        }
    }

    /*
    call this function to mate successfully with creature
     */
    public void mateSuccessfully(Creature c) {
        SPMV += (c.getMV()-getSPMV())*0.1;
    }


    public static void main(String[] args) {
        Creature c = new Creature(6.4);
    }
}
