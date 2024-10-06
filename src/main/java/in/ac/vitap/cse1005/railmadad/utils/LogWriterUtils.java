package in.ac.vitap.cse1005.railmadad.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class LogWriterUtils {

  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * Writes a log entry to a file. If the file exceeds the specified size, a new file is created
   * with an incremented suffix.
   *
   * @param path The directory path where the log files will be created.
   * @param logEntry The log entry to write to the file.
   */
  public static void writeLog(String path, String logEntry) {
    BufferedWriter writer = null;
    try {
      // Normalize the file path
      File logFile = getAvailableLogFile(new File(path));

      writer = new BufferedWriter(new FileWriter(logFile, true));
      String timestampedEntry =
          String.format("%s - %s", LocalDateTime.now().format(DATE_TIME_FORMATTER), logEntry);
      writer.write(timestampedEntry);
      writer.newLine();
    } catch (IOException e) {
      log.error("Failed to write log entry.", e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          log.error("Failed to close log writer.", e);
        }
      }
    }
  }

  /**
   * Returns the first available log file under the size limit, creating a new one with an
   * incremented suffix if necessary.
   *
   * @param dir The directory where the log files are stored.
   * @return The log file to write to.
   * @throws IOException If an I/O error occurs.
   */
  private static File getAvailableLogFile(File dir) throws IOException {
    // Check if directory exists or create it
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        throw new IOException("Failed to create directory: " + dir.getAbsolutePath());
      }
    }

    int counter = 1;
    File logFile;
    do {
      logFile = new File(dir, "log_" + counter + ".log");
      counter++;
    } while (logFile.exists() && logFile.length() >= MAX_FILE_SIZE);

    if (!logFile.exists()) {
      if (!logFile.createNewFile()) {
        throw new IOException("Failed to create log file: " + logFile.getAbsolutePath());
      }
    }

    return logFile;
  }
}
