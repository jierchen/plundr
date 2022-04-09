package jier.plundr.mapper;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.customer.ContactDTO;
import jier.plundr.model.Customer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public ContactDTO customerToContactDTO(Customer customer) {
        ContactDTO contactDTO = new ContactDTO();

        contactDTO.setFirstName(customer.getFirstName());
        contactDTO.setLastName(customer.getLastName());
        contactDTO.setDateOfBirth(customer.getDateOfBirth());
        contactDTO.setPhoneNumber(customer.getPhoneNumber());
        contactDTO.setEmail(customer.getEmail());

        return contactDTO;
    }

    public ReturnPageDTO<ContactDTO> customerPageDTOToContactPageDTO(ReturnPageDTO<Customer> customerReturnPageDTO) {
        ReturnPageDTO<ContactDTO> contactDTOReturnPageDTO = new ReturnPageDTO<>();

        contactDTOReturnPageDTO.setPageSize(customerReturnPageDTO.getPageSize());
        contactDTOReturnPageDTO.setPageNumber(customerReturnPageDTO.getPageNumber());
        contactDTOReturnPageDTO.setPageNumberOfEntities(customerReturnPageDTO.getPageNumberOfEntities());
        contactDTOReturnPageDTO.setTotalPages(customerReturnPageDTO.getTotalPages());
        contactDTOReturnPageDTO.setContent(
                customerReturnPageDTO.getContent().stream()
                        .map(customer -> customerToContactDTO(customer))
                        .collect(Collectors.toList()));

        return contactDTOReturnPageDTO;
    }
}
