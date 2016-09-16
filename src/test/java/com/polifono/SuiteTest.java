package com.polifono;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.polifono.service.GameServiceTest;
import com.polifono.service.PlayerServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ GameServiceTest.class, PlayerServiceTest.class })
public class SuiteTest {

}