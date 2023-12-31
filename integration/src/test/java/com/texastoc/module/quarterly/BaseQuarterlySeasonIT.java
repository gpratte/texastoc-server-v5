package com.texastoc.module.quarterly;

import com.texastoc.BaseIntegrationTest;
import com.texastoc.module.game.model.Game;
import com.texastoc.module.quarterly.model.QuarterlySeason;
import com.texastoc.module.season.model.Season;
import java.time.LocalDate;
import java.util.List;

public abstract class BaseQuarterlySeasonIT extends BaseIntegrationTest {

  protected Integer startYear;
  protected Season seasonCreated;
  protected QuarterlySeason qSeasonRetrieved;
  protected Game gameCreated;

  public void before() {
    startYear = null;
    seasonCreated = null;
    qSeasonRetrieved = null;
    gameCreated = null;
  }

  protected void aSeasonExists() {
    // Arrange
    startYear = getSeasonStart().getYear();
    String token = login(ADMIN_EMAIL, ADMIN_PASSWORD);
    seasonCreated = createSeason(startYear, token);
  }

  protected QuarterlySeason getCurrentQuarterlySeason(int seasonId, String token) {
    List<QuarterlySeason> qSeasons = super.getQuarterlySeasons(seasonCreated.getId(), token);
    return getCurrentQuarterlySeason(qSeasons);
  }

  protected QuarterlySeason getCurrentQuarterlySeason(List<QuarterlySeason> qSeasons) {
    LocalDate now = LocalDate.now();
    for (QuarterlySeason qSeason : qSeasons) {
      if (!qSeason.getStart().isAfter(now) && !qSeason.getEnded().isBefore(now)) {
        return qSeason;
      }
    }
    throw new RuntimeException("could not find the quarterly season encompassing " + now);
  }
}

