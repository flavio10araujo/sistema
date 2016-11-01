package com.polifono;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.polifono.controller.CustomErrorControllerTest;
import com.polifono.controller.GameControllerTest;
import com.polifono.service.AnswerServiceTest;
import com.polifono.service.ClassPlayerServiceTest;
import com.polifono.service.ClassServiceTest;
import com.polifono.service.ContentServiceTest;
import com.polifono.service.GameServiceTest;
import com.polifono.service.LevelServiceTest;
import com.polifono.service.LoginServiceTest;
import com.polifono.service.MapServiceTest;
import com.polifono.service.PhaseServiceTest;
import com.polifono.service.PlayerGameServiceTest;
import com.polifono.service.PlayerPhaseServiceTest;
import com.polifono.service.PlayerServiceTest;
import com.polifono.service.QuestionServiceTest;
import com.polifono.service.TransactionServiceTest;
import com.polifono.service.currentUser.CurrentUserServiceTest;
import com.polifono.service.currentUser.UserDetailsServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	// Controllers
	CustomErrorControllerTest.class,
	GameControllerTest.class,
	// Services
	AnswerServiceTest.class,
	ClassPlayerServiceTest.class,
	ClassServiceTest.class,
	ContentServiceTest.class,
	GameServiceTest.class,
	LevelServiceTest.class,
	LoginServiceTest.class,
	MapServiceTest.class,
	PhaseServiceTest.class,
	PlayerGameServiceTest.class,
	PlayerPhaseServiceTest.class,
	PlayerServiceTest.class, 
	QuestionServiceTest.class,
	TransactionServiceTest.class,
	CurrentUserServiceTest.class,
	UserDetailsServiceTest.class
})
public class SuiteTest {

}