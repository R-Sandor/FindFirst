package frontend.scripts;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
public class CpEnv { 
  public static void main(String [] args) throws Exception { 
    Files.copy(Path.of(args[0]), Path.of(args[1]), StandardCopyOption.valueOf("REPLACE_EXISTING"));
  }
}
