import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyLogger {
    private ArrayList<String[]> buffer = new ArrayList<String[]>();

    private int max_buffer_size;

    private FileWriter file_writer;
    private BufferedWriter bw;

    String file_path;

    public MyLogger() {

    }

    public void initiateLogger(String file_name, int max_buffer_size) {
        this.file_path = "C:/Users/Bartek/Desktop/Metahurystyki/EA csvs/" + file_name;
        try {
            Path path = Paths.get(file_path+ ".csv");
            int counter = 0;
            while (Files.exists(path)) {
                counter++;
                path = Paths.get(file_path + "(" + counter + ')' + ".csv");
            }
            if(counter != 0) {
                file_path = file_path + "(" + counter + ").csv";
            }
            else {
                file_path = file_path + ".csv";
            }
            Files.createFile(path);
            this.max_buffer_size = max_buffer_size;
            file_writer = new FileWriter(file_path);
            bw = new BufferedWriter(file_writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String[] message) {
        this.buffer.add(message);
//        try {
//            StringBuilder line = new StringBuilder();
//            for (int i = 0; i < message.length; i++) {
//                line.append(message[i]);
//                if (i != message.length - 1) {
//                    line.append(',');
//                }
//            }
//            line.append("\n");
//            file_writer.write(line.toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
                if (this.buffer.size() > this.max_buffer_size) {
            int test = 0;
            test ++;
            try {
                this.writeToCsvFile(this.buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.buffer.clear();
        }
    }

    private static final String COMMA = ",";
    private static final String DEFAULT_SEPARATOR = COMMA;
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
    private static final String NEW_LINE_UNIX = "\n";
    private static final String NEW_LINE_WINDOWS = "\r\n";

    public String convertToCsvFormat(final String[] line) {
        return convertToCsvFormat(line, DEFAULT_SEPARATOR);
    }

    public String convertToCsvFormat(final String[] line, final String separator) {
        return convertToCsvFormat(line, separator, true);
    }

    // if quote = true, all fields are enclosed in double quotes
    public String convertToCsvFormat(
            final String[] line,
            final String separator,
            final boolean quote) {

        return Stream.of(line)                              // convert String[] to stream
                .map(l -> formatCsvField(l, quote))         // format CSV field
                .collect(Collectors.joining(separator));    // join with a separator

    }

    // put your extra login here
    private String formatCsvField(final String field, final boolean quote) {

        String result = field;

        if (result.contains(COMMA)
                || result.contains(DOUBLE_QUOTES)
                || result.contains(NEW_LINE_UNIX)
                || result.contains(NEW_LINE_WINDOWS)) {

            // if field contains double quotes, replace it with two double quotes \"\"
            result = result.replace(DOUBLE_QUOTES, EMBEDDED_DOUBLE_QUOTES);

            // must wrap by or enclosed with double quotes
            result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;

        } else {
            // should all fields enclosed in double quotes
            if (quote) {
                result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
            }
        }

        return result;

    }

    // a standard FileWriter, CSV is a normal text file
    private void writeToCsvFile(List<String[]> list) throws IOException {

        List<String> collect = list.stream()
                .map(this::convertToCsvFormat)
                .collect(Collectors.toList());
        this.buffer.clear();

        // CSV is a normal text file, need a writer
        try {
            for (String line : collect) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeAll() {
        try {
            writeToCsvFile(this.buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            bw.close();
            file_writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
