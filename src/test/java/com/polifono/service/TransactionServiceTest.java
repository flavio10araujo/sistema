package com.polifono.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.polifono.AbstractTest;
import com.polifono.domain.Player;
import com.polifono.domain.Transaction;
import com.polifono.repository.ITransactionRepository;
import com.polifono.service.impl.TransactionServiceImpl;

/**
 * Unit test methods for the TransactionService.
 * 
 */
public class TransactionServiceTest extends AbstractTest {

    private ITransactionService service;
    
    @Mock
    private ITransactionRepository repository;
	
	private final Integer PLAYER_ID_EXISTENT = 1;
	
	private final Integer TRANSACTION_ID_EXISTENT = 1;
	private final Integer TRANSACTION_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final String CODE_EXISTENT = "F5456E04-2474-4DC5-B188-319183075F04";
	private final String CODE_INEXISTENT = "-1";

    @Before
    public void setUp() {
        // Do something before each test method.
    	MockitoAnnotations.initMocks(this);
    	service = new TransactionServiceImpl(repository);
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* stubs - begin */
    private Transaction getEntityStubData() {
    	Player player = new Player();
    	player.setId(PLAYER_ID_EXISTENT);
    	
    	Transaction entity = new Transaction();
    	entity.setId(TRANSACTION_ID_EXISTENT);
    	entity.setPlayer(player);
    	entity.setQuantity(27);
    	entity.setDtInc(new Date());
    	entity.setClosed(false);
    	
    	return entity;
    }
    
    private List<Transaction> getEntityListStubData() {
    	List<Transaction> list = new ArrayList<Transaction>();
    	
    	Transaction entity1 = getEntityStubData();
    	Transaction entity2 = getEntityStubData();
    	
    	list.add(entity1);
    	list.add(entity2);
    	
    	return list;
    }
    /* stubs - end */
    
    /* save - begin */
    @Test
    public void save_WhenSaveTransaction_ReturnTransactionSaved() {
    	Transaction entity = getEntityStubData();
    	
    	when(repository.save(entity)).thenReturn(entity);
    	
    	Transaction entityReturned = service.save(entity);
    	
    	Assert.assertNotNull("failure - expected not null", entityReturned);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entityReturned.getId());
    	
    	verify(repository, times(1)).save(entity);
        verifyNoMoreInteractions(repository);
    }
    /* save - end */
    
    /* findOne - begin */
    @Test
    public void findOne_WhenTransactionIsExistent_ReturnTransaction() {
    	Transaction entity = getEntityStubData();
    	
    	when(repository.findOne(TRANSACTION_ID_EXISTENT)).thenReturn(entity);
    	
    	Transaction entityReturned = service.findOne(TRANSACTION_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entityReturned);
        Assert.assertEquals("failure - expected id attribute match", TRANSACTION_ID_EXISTENT.intValue(), entityReturned.getId());
        
        verify(repository, times(1)).findOne(TRANSACTION_ID_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findOne_WhenTransactionIsInexistent_ReturnNull() {
    	when(repository.findOne(TRANSACTION_ID_INEXISTENT)).thenReturn(null);
    	
    	Transaction entityReturned = service.findOne(TRANSACTION_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entityReturned);
        
        verify(repository, times(1)).findOne(TRANSACTION_ID_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findOne - end */
    
    /* findTransactionsByCode - begin */
    @Test
    public void findTransactionsByCode_WhenSearchByCodeExistent_ReturnList() {
    	List<Transaction> list = getEntityListStubData();
    	
    	when(repository.findTransactionsByCode(CODE_EXISTENT)).thenReturn(list);
    	
    	List<Transaction> listReturned = service.findTransactionsByCode(CODE_EXISTENT);
        Assert.assertNotNull("failure - not expected null", listReturned);
        Assert.assertNotEquals("failure - list size not expected 0", 0, listReturned.size());
        
        verify(repository, times(1)).findTransactionsByCode(CODE_EXISTENT);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findTransactionsByCode_WhenSearchByCodeInexistent_ReturnEmptyList() {
    	when(repository.findTransactionsByCode(CODE_INEXISTENT)).thenReturn(new ArrayList<Transaction>());
    	
    	List<Transaction> listReturned = service.findTransactionsByCode(CODE_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, listReturned.size());
    	
    	verify(repository, times(1)).findTransactionsByCode(CODE_INEXISTENT);
        verifyNoMoreInteractions(repository);
    }
    /* findTransactionsByCode - end */
}