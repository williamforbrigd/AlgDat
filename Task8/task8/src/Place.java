public class Place {
    int nodeNr;
    int code;
    String name;

    public Place(int nodeNr, int code, String name) {
        this.nodeNr = nodeNr;
        this.code= code;
        this.name= name;
    }

    public Place(int nodeNr, int code) {
        this.nodeNr = nodeNr;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this == o) return true;
        if(!(o instanceof Place)) return false;
        Place place = (Place)o;
        return this.nodeNr == place.nodeNr && this.code == place.code && this.name.equals(place.name);
    }
    @Override
    public String toString() {
        return nodeNr + " " + code + " " + name;
    }
}
