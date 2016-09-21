package com.polifono;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.polifono.service.ContentServiceTest;
import com.polifono.service.GameServiceTest;
import com.polifono.service.LevelServiceTest;
import com.polifono.service.LoginServiceTest;
import com.polifono.service.MapServiceTest;
import com.polifono.service.PhaseServiceTest;
import com.polifono.service.PlayerPhaseServiceTest;
import com.polifono.service.PlayerServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	PlayerServiceTest.class, 
	LoginServiceTest.class,
	GameServiceTest.class,
	LevelServiceTest.class,
	MapServiceTest.class,
	PhaseServiceTest.class,
	PlayerPhaseServiceTest.class,
	ContentServiceTest.class
})
public class SuiteTest {

}