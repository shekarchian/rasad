package asad.model;

public class PredictedLinksRequest {
    public enum GraphType {author, article}

    public enum Method {topic_modeling, ccs, keyword}

    private GraphType graph_type;
    private Method method;
    private Integer links_number;
    private Integer topic_size;

    public GraphType getGraph_type() {
        return graph_type;
    }

    public void setGraph_type(GraphType graph_type) {
        this.graph_type = graph_type;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Integer getLinks_number() {
        return links_number;
    }

    public void setLinks_number(Integer links_number) {
        this.links_number = links_number;
    }

    public Integer getTopic_size() {
        return topic_size;
    }

    public void setTopic_size(Integer topic_size) {
        this.topic_size = topic_size;
    }
}
