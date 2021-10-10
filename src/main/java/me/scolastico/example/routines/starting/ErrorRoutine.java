package me.scolastico.example.routines.starting;

import java.util.HashMap;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;

public class ErrorRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> hashMap) throws Exception {
    ErrorHandler.enableErrorLogFile();
    ErrorHandler.enableCatchUncaughtException();
    //ErrorHandler.enableSentry("sentry dns url here");
    return new RoutineAnswer(hashMap);
  }

}
