package jier.plundr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReturnPageDTO<T> {

    private int pageNumber;
    private int pageSize;
    private int pageNumberOfEntities;
    private int totalPages;
    private List<T> content;
}
