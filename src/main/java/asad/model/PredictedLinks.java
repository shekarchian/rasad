package asad.model;

import java.util.List;

public class PredictedLinks {
    private List<Link> links;

    public PredictedLinks(List<Link> links) {
        this.links = links;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
