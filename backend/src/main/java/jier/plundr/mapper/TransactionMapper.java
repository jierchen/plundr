package jier.plundr.mapper;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.transaction.TransactionDTO;
import jier.plundr.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    /**
     * Maps {@code Transaction} to {@code TransactionDTO}.
     * @param transaction {@code Transaction} to map.
     * @return {@code TransactionDTO} mapped.
     */
    public TransactionDTO transactionToTransactionDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setId(transaction.getId());
        transactionDTO.setTransactionType(transaction.getTransactionType());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setOwningAccountId(transaction.getOwningAccount().getId());
        transactionDTO.setRecipientAccountId(transaction.getRecipientAccount() != null
                ? transaction.getRecipientAccount().getId() : null);
        transactionDTO.setCreateDate(transaction.getCreateDate().toLocalDate());

        return transactionDTO;
    }

    public ReturnPageDTO<TransactionDTO> transactionPageToTransactionPageDTO(
            ReturnPageDTO<Transaction> transactionReturnPageDTO) {
        ReturnPageDTO<TransactionDTO> transactionDTOReturnPageDTO = new ReturnPageDTO<>();

        transactionDTOReturnPageDTO.setPageSize(transactionReturnPageDTO.getPageSize());
        transactionDTOReturnPageDTO.setPageNumber(transactionReturnPageDTO.getPageNumber());
        transactionDTOReturnPageDTO.setPageNumberOfEntities(transactionReturnPageDTO.getPageNumberOfEntities());
        transactionDTOReturnPageDTO.setTotalPages(transactionReturnPageDTO.getTotalPages());
        transactionDTOReturnPageDTO.setContent(
                transactionReturnPageDTO.getContent().stream()
                        .map(transaction -> transactionToTransactionDTO(transaction))
                        .collect(Collectors.toList()));

        return transactionDTOReturnPageDTO;
    }
}
