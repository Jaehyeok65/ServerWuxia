package net.skhu.dto;

public class Pagination {
    private int page;
    private int size;

    // 생성자 정의
    public Pagination(int page, int size) {
        this.page = page;
        this.size = size;
    }

    // Getter, Setter 생략
}
