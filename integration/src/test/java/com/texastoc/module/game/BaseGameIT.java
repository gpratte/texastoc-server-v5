package com.texastoc.module.game;

import com.texastoc.BaseIntegrationTest;
import com.texastoc.module.game.model.Game;
import com.texastoc.module.season.model.Season;
import java.time.LocalDate;
import org.springframework.web.client.HttpClientErrorException;

public abstract class BaseGameIT extends BaseIntegrationTest {

  protected Game gameToCreate;
  protected Game gameCreated;
  protected Game gameRetrieved;
  protected Season seasonCreated;
  protected HttpClientErrorException exception;

  protected void before() {
    // Before each scenario
    gameToCreate = null;
    gameCreated = null;
    gameRetrieved = null;
    seasonCreated = null;
    exception = null;
  }

  protected void aSeasonExists() {
    String token = login(ADMIN_EMAIL, ADMIN_PASSWORD);
    seasonCreated = createSeason(token);
  }

  protected void theGameStartsNow() {
    gameToCreate = Game.builder()
        .date(LocalDate.now())
        .hostId(10000)
        .transportRequired(false)
        .build();
  }

  protected void theGameIsCreated() {
    String token = login(USER_EMAIL, USER_PASSWORD);
    gameCreated = createGame(gameToCreate, seasonCreated.getId(), token);
  }

  protected void getGame(int id) {
    String token = login(USER_EMAIL, USER_PASSWORD);
    gameRetrieved = getGame(id, token);
  }

}
