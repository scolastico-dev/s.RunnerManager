package me.scolastico.example.routines.starting;

import java.util.HashMap;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class FinishRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> hashMap) throws Exception {
    try {
      long startingTime = (long) hashMap.get("startingTime");
      System.out.println();
      System.out.println("Done! Starting took " + ((System.currentTimeMillis()-startingTime)/1000D) + " seconds.");
      System.out.println();
      return new RoutineAnswer(hashMap);
    } catch (Exception e) {
      try {
        ConsoleLoadingAnimation.disable();
      } catch (Exception ignored) {}
      ErrorHandler.handle(e);
      return new RoutineAnswer(true, "exception");
    }
  }

}
