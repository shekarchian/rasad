package asad.model;

import java.util.List;

public class Node {
    private List<WordProbability> words;

    public Node(List<WordProbability> words) {
        this.words = words;
    }

    public List<WordProbability> getWords() {
        return words;
    }

    public void setWords(List<WordProbability> words) {
        this.words = words;
    }
}
