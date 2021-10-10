package me.scolastico.example.routines.starting;

import com.github.lalyos.jfiglet.FigletFont;
import java.util.HashMap;
import me.scolastico.example.Application;
import me.scolastico.tools.console.ConsoleLoadingAnimation;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import org.fusesource.jansi.AnsiConsole;

public class HeaderRoutine implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> hashMap) throws Exception {
    try {
      System.out.println(FigletFont.convertOneLine("Example"));
      System.out.println("Version: " + Application.getVersion() + " | Commit: " + Application.getBranch() + "/" + Application.getCommit() + " | By: scolastico");
      hashMap.put("startingTime", System.currentTimeMillis());
      AnsiConsole.systemInstall();
      ConsoleLoadingAnimation.setAnimation(new char[]{'|', '/', '-', '\\'});
      ConsoleLoadingAnimation.setSpeed(25);
      System.out.println();
      return new RoutineAnswer(hashMap);
    } catch (Exception e) {
      ErrorHandler.handle(e);
      return new RoutineAnswer(true, "exception");
    }
  }

}
