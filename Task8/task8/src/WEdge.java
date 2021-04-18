class WEdge extends Edge {

    //Kjøretiden kan brukes kantvekt i grafen.
    int drivingTime;
    //Trenger egentlig ikke lengde og fartsgrense for å løse oppgaven, er med for å vise datagrunnlaget.
    int lengde;
    int fartsgrense;

    public WEdge(int from, int to, WEdge nextEdge, Node toNode, int drivingTime, int lengde, int fartsgrense) {
        super(from, to, nextEdge, toNode);
        this.drivingTime = drivingTime;
        this.lengde = lengde;
        this.fartsgrense = fartsgrense;    }


   @Override
    public String toString() {
        return super.toString() + " " + drivingTime + " " + lengde + " " + fartsgrense;
   }
}
