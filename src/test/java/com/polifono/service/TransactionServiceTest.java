package com.polifono.service;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.polifono.AbstractTest;
import com.polifono.domain.Player;
import com.polifono.domain.Transaction;

/**
 * Unit test methods for the TransactionService.
 * 
 */
@Transactional
public class TransactionServiceTest extends AbstractTest {

	@Autowired
    private ITransactionService service;
	
	private final Integer PLAYER_ID_EXISTENT = 1;
	
	private final Integer TRANSACTION_ID_EXISTENT = 1;
	private final Integer TRANSACTION_ID_INEXISTENT = Integer.MAX_VALUE;
	
	private final String CODE_EXISTENT = "F5456E04-2474-4DC5-B188-319183075F04";
	private final String CODE_INEXISTENT = "-1";

    @Before
    public void setUp() {
        // Do something before each test method.
    }

    @After
    public void tearDown() {
        // Clean up after each test method.
    }
    
    /* save - begin */
    @Test
    public void save() {
    	Player player = new Player();
    	player.setId(PLAYER_ID_EXISTENT);
    	
    	Transaction transaction = new Transaction();
    	transaction.setPlayer(player);
    	transaction.setQuantity(27);
    	transaction.setDtInc(new Date());
    	transaction.setClosed(false);

    	Transaction entitySaved = service.save(transaction);
    	
    	Assert.assertNotNull("failure - expected not null", entitySaved);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entitySaved.getId());
    	
    	Transaction entity = service.findOne(entitySaved.getId());

    	Assert.assertNotNull("failure - expected not null", entity);
    	Assert.assertNotEquals("failure - expected id attribute bigger than 0", 0, entity.getId());
    	
    	/*
    	// Changing all possible fields.
    	//content.setId(content.getId() + 1);
    	entity.setContent(content);
    	entity.setName(entity.getName() + " CHANGED");
    	entity.setOrder(entity.getOrder() + 1);
    	
    	transaction.setPlayer(player);
    	transaction.setQuantity(27);
    	transaction.setDtInc(new Date());
    	transaction.setClosed(false);
    	transaction.setCode("12345");
    	transaction.setReference("10");
    	transaction.setDate(new Date());
    	transaction.setLastEventDate(new Date());
    	transaction.setType(1);
    	transaction.setStatus(1);
    	transaction.setPaymentMethodType(1);
    	transaction.setPaymentMethodCode(1);
    	transaction.setGrossAmount(100);
    	transaction.setDiscountAmount(10);
    	transaction.setFeeAmount(15);
    	transaction.setNetAmount(90);
    	transaction.setExtraAmount(10);
    	transaction.setInstallmentCount(2);
    	transaction.setItemCount(1);
    	transaction.setEscrowEndDate(new Date());
    	transaction.setCancellationSource("");
    	transaction.setPaymentLink("");

    	Transaction updatedEntity = service.save(entity);

    	Assert.assertNotNull("failure - expected not null", updatedEntity);
    	Assert.assertEquals("failure - expected id attribute match", entity.getId(), updatedEntity.getId());
    	Assert.assertEquals("failure - expected content attribute match", entity.getContent().getId(), updatedEntity.getContent().getId());
    	Assert.assertEquals("failure - expected name attribute match", entity.getName(), updatedEntity.getName());
    	Assert.assertEquals("failure - expected order attribute match", entity.getOrder(), updatedEntity.getOrder());
    	*/
    }
    /* save - end */
    
    /* findOne - begin */
    @Test
    public void findOne_TransactionExistentButReturnNull_ExceptionThrown() {
    	Transaction entity = service.findOne(TRANSACTION_ID_EXISTENT);
        Assert.assertNotNull("failure - expected not null", entity);
    }

    @Test
    public void findOne_TransactionExistentWithWrongId_ExceptionThrown() {
        Integer id = new Integer(TRANSACTION_ID_EXISTENT);
        Transaction entity = service.findOne(id);
        Assert.assertEquals("failure - expected id attribute match", id.intValue(), entity.getId());
    }

    @Test
    public void findOne_TransactionInexistent_ReturnNull() {
    	Transaction entity = service.findOne(TRANSACTION_ID_INEXISTENT);
        Assert.assertNull("failure - expected null", entity);
    }
    /* findOne - end */
    
    /* findTransactionsByCode - begin */
    @Test
    public void findTransactionsByCode_SearchCodeExistent_ReturnList() {
    	List<Transaction> list = service.findTransactionsByCode(CODE_EXISTENT);
        Assert.assertNotNull("failure - not expected null", list);
        Assert.assertNotEquals("failure - list size not expected 0", 0, list.size());
    }

    @Test
    public void findTransactionsByCode_SearchCodeInexistent_ReturnListEmpty() {
    	List<Transaction> list = service.findTransactionsByCode(CODE_INEXISTENT);
    	Assert.assertEquals("failure - expected empty list", 0, list.size());
    }
    /* findTransactionsByCode - end */
}