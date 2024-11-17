package com.polifono.service.impl.transaction;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.model.entity.Player;
import com.polifono.model.entity.Transaction;
import com.polifono.repository.ITransactionRepository;

/**
 * Unit test methods for the TransactionService.
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService service;

    @Mock
    private ITransactionRepository repository;

    private final Integer PLAYER_ID_EXISTENT = 1;

    private final Integer TRANSACTION_ID_EXISTENT = 1;
    private final Integer TRANSACTION_ID_INEXISTENT = Integer.MAX_VALUE;

    private final String CODE_EXISTENT = "F5456E04-2474-4DC5-B188-319183075F04";
    private final String CODE_INEXISTENT = "-1";

    /* save - begin */
    @Test
    public void save_WhenSaveTransaction_ReturnTransactionSaved() {
        Optional<Transaction> entity = getEntityStubData();

        when(repository.save(entity.get())).thenReturn(entity.get());

        Transaction entityReturned = service.save(entity.get());

        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertNotEquals(0, entityReturned.getId(), "failure - expected id attribute bigger than 0");

        verify(repository, times(1)).save(entity.get());
        verifyNoMoreInteractions(repository);
    }
    /* save - end */

    /* findOne - begin */
    @Test
    public void findOne_WhenTransactionIsExistent_ReturnTransaction() {
        Optional<Transaction> entity = getEntityStubData();

        when(repository.findById(TRANSACTION_ID_EXISTENT)).thenReturn(entity);

        Optional<Transaction> entityReturned = service.findById(TRANSACTION_ID_EXISTENT);
        Assertions.assertNotNull(entityReturned, "failure - expected not null");
        Assertions.assertEquals(TRANSACTION_ID_EXISTENT.intValue(), entityReturned.get().getId(), "failure - expected id attribute match");

        verify(repository, times(1)).findById(TRANSACTION_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenTransactionIsInexistent_ReturnNull() {
        when(repository.findById(TRANSACTION_ID_INEXISTENT)).thenReturn(null);

        Optional<Transaction> entityReturned = service.findById(TRANSACTION_ID_INEXISTENT);
        Assertions.assertNull(entityReturned, "failure - expected null");

        verify(repository, times(1)).findById(TRANSACTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */

    /* findByCode - begin */
    @Test
    public void findByCode_WhenSearchByCodeExistent_ReturnList() {
        List<Transaction> list = getEntityListStubData();

        when(repository.findByCode(CODE_EXISTENT)).thenReturn(list);

        List<Transaction> listReturned = service.findByCode(CODE_EXISTENT);
        Assertions.assertNotNull(listReturned, "failure - not expected null");
        Assertions.assertNotEquals(0, listReturned.size(), "failure - list size not expected 0");

        verify(repository, times(1)).findByCode(CODE_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findByCode_WhenSearchByCodeInexistent_ReturnEmptyList() {
        when(repository.findByCode(CODE_INEXISTENT)).thenReturn(new ArrayList<>());

        List<Transaction> listReturned = service.findByCode(CODE_INEXISTENT);
        Assertions.assertEquals(0, listReturned.size(), "failure - expected empty list");

        verify(repository, times(1)).findByCode(CODE_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findByCode - end */

    /* stubs - begin */
    private Optional<Transaction> getEntityStubData() {
        Player player = new Player();
        player.setId(PLAYER_ID_EXISTENT);

        Transaction entity = new Transaction();
        entity.setId(TRANSACTION_ID_EXISTENT);
        entity.setPlayer(player);
        entity.setQuantity(27);
        entity.setDtInc(new Date());
        entity.setClosed(false);

        return Optional.of(entity);
    }

    private List<Transaction> getEntityListStubData() {
        List<Transaction> list = new ArrayList<>();

        Transaction entity1 = getEntityStubData().get();
        Transaction entity2 = getEntityStubData().get();

        list.add(entity1);
        list.add(entity2);

        return list;
    }
    /* stubs - end */
}
