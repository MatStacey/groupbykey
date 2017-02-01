package main.app;


import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * FixLionel's Problem yo
 */
public class App {
    public static void main(String[] args) {
        FixProblem problem = new FixProblem("/table1.txt", "/table2.txt");
        if (!problem.hasError()) {
            problem.writeTo("Merged-Table.txt");
        } else {
            System.out.println(problem.getMessage());
        }
    }


    private static class FixProblem {
        String message;
        boolean error = false;

        Set<String> uniqueKeys = new HashSet<String>();
        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        private FixProblem(String file1, String file2) {
            InputStream tableOne = FixProblem.class.getResourceAsStream(file1);
            InputStream tableTwo = FixProblem.class.getResourceAsStream(file2);
            if (tableOne == null || tableTwo == null) {
                this.setMessage("no file");
                this.error = true;
                return;
            }
            this.readFiles(tableOne, tableTwo);
        }

        private void readFiles(InputStream tableOne, InputStream tableTwo) {
            try {
                BufferedReader tableOneReader = new BufferedReader(new InputStreamReader(tableOne, "UTF-8"));
                BufferedReader tableTwoReader = new BufferedReader(new InputStreamReader(tableTwo, "UTF-8"));
                this.processFile(tableOneReader);
                this.processFile(tableTwoReader);
            } catch (Exception e) {
                this.setMessage("Whoops");
                this.error = true;
            }
        }

        private void processFile(BufferedReader tableReader) {
            try {
                String strLine;
                while ((strLine = tableReader.readLine()) != null) {
                    int lastKeySeparator = strLine.lastIndexOf(" ");
                    String tableKey = strLine.substring(0, lastKeySeparator);
                    if(tableKey.contains(",")) {
                        tableKey = tableKey.replace(",", "");
                    }
                    String volume = strLine.substring(lastKeySeparator, strLine.length());

                    this.uniqueKeys.add(tableKey);
                    Integer currentVolume = this.resultMap.get(tableKey);
                    if (currentVolume == null) {
                        currentVolume = 0;
                    }
                    this.resultMap.put(tableKey, currentVolume + Integer.parseInt(volume.trim()));
                }
            } catch (Exception e) {
                this.error = true;
                this.setMessage("Whoops");
            }
        }

        private void writeTo(String outputFile) {
            try {
                PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
                for (String key : uniqueKeys) {
                    writer.println(String.format("%s, %s", key, resultMap.get(key)));
                }
                writer.close();
            } catch (IOException e) {
                this.setMessage("Can't Write");
                this.error = true;
            }
        }

        Map<String, Integer> getResults() {
            return this.resultMap;
        }

        Set<String> getUniqueKeys() {
            return this.uniqueKeys;
        }

        String getMessage() {
            return this.message;
        }

        void setMessage(String str) {
            this.message = str;
        }

        boolean hasError() {
            return this.error;
        }


    }
}
