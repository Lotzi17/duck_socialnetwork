package lab2_map.util;


import java.util.List;

public class Page<E> {

    private final List<E> content;
    private final int totalCount;

    public Page(List<E> content, int totalCount) {
        this.content = content;
        this.totalCount = totalCount;
    }

    public List<E> getContent() {
        return content;
    }

    public int getTotalCount() {
        return totalCount;
    }
}

