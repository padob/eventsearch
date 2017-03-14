package com.pdb.eventsearch.cucumber.stepdefs;

import com.pdb.eventsearch.EventsearchApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = EventsearchApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
