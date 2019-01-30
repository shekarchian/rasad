package asad.model;

public class TopicProbability {
    private String id;
    private Double probability;

    public TopicProbability(String id, Double probability) {
        this.id = id;
        this.probability = probability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }
}
