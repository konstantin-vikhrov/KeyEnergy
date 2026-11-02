import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ListToCSV {

    // Writes a list of string arrays to a CSV file
    public static void writeListToCSV(List<String[]> data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
                // Escape and join values with commas
                String csvRow = String.join(",", escapeValues(row));
                writer.write(csvRow + System.lineSeparator());
            }
            System.out.println("CSV file created successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }

    // Escapes values containing commas or quotes
    private static String[] escapeValues(String[] values) {
        String[] escaped = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (value.contains(",") || value.contains("\"")) {
                value = "\"" + value.replace("\"", "\"\"") + "\"";
            }
            escaped[i] = value;
        }
        return escaped;
    }

    public static void main(String[] args) {
        // Example data: each String[] is a row
        List<String[]> data = Arrays.asList(
                new String[]{"Name", "Age", "City"},
                new String[]{"Alice", "30", "New York"},
                new String[]{"Bob", "25", "Los Angeles"},
                new String[]{"Charlie", "35", "Chicago, IL"} // contains comma
        );

        writeListToCSV(data, "output.csv");
    }
}
