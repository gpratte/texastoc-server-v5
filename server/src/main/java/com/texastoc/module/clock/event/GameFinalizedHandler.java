package com.texastoc.module.clock.event;

import com.texastoc.common.GameFinalizedEvent;

public class GameFinalizedHandler {

  //;;!! private final ClockService clockService;

  //  public GameFinalizedHandler(ClockService clockService) {
//    this.clockService = clockService;
//  }
  public GameFinalizedHandler() {
  }

  public void handleGameFinalized(GameFinalizedEvent event) {
    // ;;!! clockService.endClock(event.getGameId());
  }
}
