package com.polifono;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.polifono.service.AnswerServiceTest;
import com.polifono.service.ContentServiceTest;
import com.polifono.service.GameServiceTest;
import com.polifono.service.LevelServiceTest;
import com.polifono.service.LoginServiceTest;
import com.polifono.service.MapServiceTest;
import com.polifono.service.PhaseServiceTest;
import com.polifono.service.PlayerPhaseServiceTest;
import com.polifono.service.PlayerServiceTest;
import com.polifono.service.QuestionServiceTest;
import com.polifono.service.TransactionServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	PlayerServiceTest.class, 
	LoginServiceTest.class,
	GameServiceTest.class,
	LevelServiceTest.class,
	MapServiceTest.class,
	PhaseServiceTest.class,
	PlayerPhaseServiceTest.class,
	ContentServiceTest.class,
	QuestionServiceTest.class,
	AnswerServiceTest.class,
	TransactionServiceTest.class
})
public class SuiteTest {

}