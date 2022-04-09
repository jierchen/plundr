package jier.plundr.mapper;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.customer.ContactDTO;
import jier.plundr.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

    public <T> ReturnPageDTO<T> pageToReturnPageDTO(Page<T> page) {
        ReturnPageDTO<T> returnPageDTO = new ReturnPageDTO<>();

        returnPageDTO.setPageNumber(page.getNumber());
        returnPageDTO.setPageSize(page.getSize());
        returnPageDTO.setPageNumberOfEntities(page.getNumberOfElements());
        returnPageDTO.setTotalPages(page.getTotalPages());
        returnPageDTO.setContent(page.getContent());

        return returnPageDTO;
    }
}
