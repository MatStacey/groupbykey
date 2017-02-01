package main.app;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        Set<String> keys = problem.getUniqueKeys();
        Map<String, Integer> values = problem.getResults();

        if (!problem.hasError()) {
            for (String key : keys) {
                System.out.println(String.format("%s : %s", key, values.get(key)));
            }
        } else {
            System.out.println(problem.getMessage());
        }
    }


    private static class FixProblem {
        String tableKey;
        String message;
        String volume;
        boolean error = false;

        Set<String> uniqueKeys = new HashSet<String>();
        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        private FixProblem(String file1, String file2) {
            InputStream tableOne = FixProblem.class.getResourceAsStream(file1);
            InputStream tableTwo = FixProblem.class.getResourceAsStream(file2);
            if (tableOne == null) {
                this.setMessage("no file");
                this.error = true;
                return;
            }
            try {
                BufferedReader tableOneReader = new BufferedReader(new InputStreamReader(tableOne, "UTF-8"));


                int lastKeySeparator;
                String tableOneStr;
                while ((tableOneStr = tableOneReader.readLine()) != null) {
                    lastKeySeparator = tableOneStr.lastIndexOf(" ");
                    this.tableKey = tableOneStr.substring(0, lastKeySeparator);
                    this.volume = tableOneStr.substring(lastKeySeparator, tableOneStr.length());

                    this.uniqueKeys.add(this.tableKey);
                    Integer currentVolume = this.resultMap.get(this.tableKey);
                    if (currentVolume == null) {
                        currentVolume = 0;
                    }
                    this.resultMap.put(this.tableKey, currentVolume + Integer.parseInt(this.volume.trim()));
                }
            } catch (Exception e) {
                this.error = true;
                this.setMessage("Whoops");
            }

            try {
                String tableTwoStr;
                BufferedReader tableTwoReader = new BufferedReader(new InputStreamReader(tableTwo, "UTF-8"));
                while ((tableTwoStr = tableTwoReader.readLine()) != null) {

                    Integer lastKeySeparator2 = tableTwoStr.lastIndexOf(" ");
                    this.tableKey = tableTwoStr.substring(0, lastKeySeparator2);
                    this.volume = tableTwoStr.substring(lastKeySeparator2, tableTwoStr.length());


                    this.uniqueKeys.add(this.tableKey);
                    Integer currentVolume = this.resultMap.get(this.tableKey);
                    if (currentVolume == null) {
                        currentVolume = 0;
                    }
                    this.resultMap.put(this.tableKey, currentVolume + Integer.parseInt(this.volume.trim()));
                }
            } catch (Exception e) {
                this.error = true;
                this.setMessage("Whoops 2");
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
