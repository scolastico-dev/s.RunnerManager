package me.scolastico.example.routines.starting;

import java.util.HashMap;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.ebean.DatabaseConfig;
import me.scolastico.tools.ebean.DatabaseConnector;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.Ansi;

public class DatabaseRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> hashMap) throws Exception {
    try {
      DatabaseConfig config = (DatabaseConfig) hashMap.get("dbConfig");
      DatabaseConnector connector = new DatabaseConnector(true);

      System.out.print("Loading database drivers... ");
      ConsoleLoadingAnimation.enable();
      DatabaseConnector.loadDatabaseDrivers();
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      System.out.print("Connecting to database... ");
      ConsoleLoadingAnimation.enable();
      connector.connectToDatabase(config);
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      System.out.print("Migrate database... ");
      ConsoleLoadingAnimation.enable();
      connector.runMigrations();
      ConsoleLoadingAnimation.disable();
      System.out.println(Ansi.ansi().fgGreen().a("[OK]").reset());

      return new RoutineAnswer(hashMap);
    } catch (Exception e) {
      try {
        ConsoleLoadingAnimation.disable();
      } catch (Exception ignored) {}
      System.out.println(Ansi.ansi().fgRed().a("[FAIL]").reset());
      ErrorHandler.handle(e);
      return new RoutineAnswer(true, "exception");
    }
  }

}
