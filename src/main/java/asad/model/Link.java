package asad.model;

public class Link {
    private Node node1;
    private Node node2;
    private Double score;

    public Link(Node node1, Node node2, Double score) {
        this.node1 = node1;
        this.node2 = node2;
        this.score = score;
    }

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
