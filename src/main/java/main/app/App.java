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

    /**
     * <h1>Entry point for App</h1>
     *
     * <p>Files must fit the requirements:</p>
     *
     *  <ul>
     *      <li>Table has two columns</li>
     *      <li>Two columns must be separated by a space</li>
     *      <li>No trailing spaces</li>
     *  </ul>
     *
     *  <p>
     *      Preferred format requirements:
     *  </p>
     *  <ul>
     *      <li>Column 1 is a comma separated combined key (tuple)</li>
     *  </ul>
     *
     * @param args
     */
    public static void main(String[] args) {
        FixProblem problem = new FixProblem("/table1.txt", "/table2.txt");
        if (!problem.hasError()) {
            problem.writeTo("Merged-Table.txt");
        } else {
            System.out.println(problem.getMessage());
        }
    }

    /**
     * Class to handle Fixing the problem
     */
    private static class FixProblem {
        /**
         * Variables to handle errors and print messages
         */
        String message;
        boolean error = false;

        /**
         * Set of Unique Keys from each file
         * and Map of each key with volume totals
         */
        Set<String> uniqueKeys = new HashSet<String>();
        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        /**
         * Constructor for merging two files in a groupBy fashion
         * @param file1 First table to merge
         * @param file2 Second table to merge
         */
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

        /**
         * Read the two input files
         * @param tableOne First table to read
         * @param tableTwo Second table to read
         */
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

        /**
         * Process file contents and place into a Set of unique keys
         * and a map containing the sum of volumes for each key
         * @param tableReader Reader for table
         */
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

        /**
         * Write results of process into new file
         * @param outputFile Location of output file
         */
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

        /**
         * Return result Map.
         * @return key, volume Map of result.
         *
         * Not in use.
         */
        Map<String, Integer> getResults() {
            return this.resultMap;
        }

        /**
         * Return HashSet of unique keys
         * @return all unique keys found in both tables.
         *
         * Not in use.
         */
        Set<String> getUniqueKeys() {
            return this.uniqueKeys;
        }

        /**
         * Message when an exception has been thrown
         * @return error message
         */
        String getMessage() {
            return this.message;
        }

        /**
         * Set error message when an exception is thrown and caught
         * @param str error message
         */
        private void setMessage(String str) {
            this.message = str;
        }

        /**
         * Flag to determine whether an error has occurred
         * @return true or false
         */
        boolean hasError() {
            return this.error;
        }


    }
}
