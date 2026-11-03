package BoomiPages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.BufferedWriter;

public class ListProcessesWithNoLog {
    final static String url = "https://platform.boomi.com/AtomSphere.html";
    final static boolean exportarCSV = true;

    private static String getUrl(String page, String process) {
        String params = ";accountId=keyenergyservicesllc-UA2H59;";
        DateTimeFormatter formatter;

        if (Objects.equals(page, "reporting")) {
            formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return url + "#" + page + params + "autoRefresh=false;dateRange=" +
                    "range;fromDate=01/01/2026;fromHour=00;fromMinute=00;toDate=" +
                    formatter.format(LocalDate.now()) + ";toHour=23;toMinute=59;tz=-0600" +
                    ";containers=a6e34453-c0ad-4282-8ea9-6204b7342bcd,8a366500-ba22-463c-a6de-1ef424940268;processes=" + process;
        }
        else {
            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return url + "#" + page + params + "processId=" + process +
                    ";dateType=RANGE;dateStart=202601010000UTC-6;dateEnd=" +
                    formatter.format(LocalDate.now()) + "2359UTC-6;autoRefresh=false";
        }
    }

    private static List<String> readFileToLines(String filePath) {
        Path path = Paths.get(filePath);
        try {
            // Reads all lines into memory as a List
            return Files.readAllLines(path);
        } catch (IOException e) {
            // Handle or log the exception according to your application needs
            System.err.println("Error reading the file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private static void appendProcessData(BufferedWriter writer, String id, long min, long max, boolean maxCeiled, long total, int exceededTotal, String exceededDetail) throws IOException {
        String minSLA = String.valueOf(min);
        String maxSLA = String.valueOf(max);
        if (exportarCSV)
            writer.write("\"" + id + "\",\"" + minSLA + "\",\"" + maxSLA + (maxCeiled ? " (ceiled)\",\"" : "\",\"") +
                    total + "\",\"" + (exceededTotal == -1 ? "?\",\"" : exceededTotal + "\",\"") + exceededDetail + "\"");
        else
            writer.write(id + "\t\t" +
                    minSLA + (minSLA.length() > 3 ? "\t\t" : "\t\t\t") +
                    maxSLA + (maxSLA.length() > 3 ? "\t\t" : (maxCeiled ? " (ceiled)\t" : "\t\t\t")) +
                    total + (total > 999 ? "\t\t" : "\t\t\t") +
                    (exceededTotal == -1 ? "?\t\t\t" : exceededTotal + (exceededTotal > 999 ? "\t\t" : "\t\t\t")) +
                    exceededDetail);
        writer.newLine();
        writer.flush();
    }

    public static void main(String[] args) throws IOException {
        final String user = "kvikhrov@keyenergy.com",
                     pass = "KVEnroute001!",
                     folder = "C:\\Users\\konstantin.vikhrov\\OneDrive - Enroute\\Documents\\Boomi\\",
                     inputFile = "BoomiProcesses.txt",
                     outputFile = "BoomiProcessSLA" + (exportarCSV ? ".csv" : ".txt"),
                     dashboardAvg = "(//div[@class='sources_entry hide_usage'])//td"; // Locator

        final By email = By.name("loginfmt"),
                 password = By.name("passwd"),
                 reportingExecutions = By.xpath("//tr[@__gwt_row]/td[10]"),
                 reportingDropDown = By.xpath("//div[@class='header reporting-type-menu']"),
                 emptyReport = By.xpath("//div[@class='gwt-Label boomi_standard_empty_label']"),
                 emptyDashboard = By.xpath("//div[text()='There is no data available with the current filter setting for this gadget']"),
                 nextButton = By.xpath("(//button[@data-locator='button-next'])[1]"),
                 signinNumber = By.id("idRichContext_DisplaySign");

        /*
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        WebDriver driver = new EdgeDriver(options);
         */
        WebDriver driver = new EdgeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        // Login

        driver.get(url + "#reporting;accountId=keyenergyservicesllc-UA2H59");
        wait.until(ExpectedConditions.visibilityOfElementLocated(email)).sendKeys(user + Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(password)).sendKeys(pass + Keys.ENTER);
        //System.out.println( "Sign in number: " + wait.until(ExpectedConditions.visibilityOfElementLocated(signinNumber)).getAttribute("textContent"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(reportingDropDown));

        // Query the process durations

        List<String> processes = readFileToLines(folder + inputFile);
        List<String> processesWithOutExecutions = new ArrayList<>();
        List<WebElement> executions;
        Path filePath  = Paths.get(folder, outputFile);
        long avg, max;

        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            if(exportarCSV)
                writer.write("\"Component ID\",\"Min (sec)\",\"Max (sec)\",\"Executions\",\"Exceeded\",\"Durations (sec) that exceeded Max (in descendent order)\"");
            else {
                writer.write("Component ID\t\t\t\t\t\t\t\tMin (sec)\tMax (sec)\tExecutions\tExceeded\tDurations (sec) that exceeded Max (in descendent order)");
                writer.newLine();
                writer.write("------------------------------------\t\t----------\t----------\t----------\t----------\t-------------------------------------------------------");
            }
            writer.newLine();
            writer.flush();
        }
        catch (IOException e) {
            System.err.println("Error writing data: " + e.getMessage());
        }

        for(int i = 0; i < processes.size(); ++i) {
            driver.get(getUrl("reporting", processes.get(i)));
            wait.until(ExpectedConditions.or(
                            ExpectedConditions.visibilityOfElementLocated(reportingExecutions),
                            ExpectedConditions.visibilityOfElementLocated(emptyReport))
                      );
            executions = driver.findElements(reportingExecutions);
            if(executions.isEmpty()) {
                driver.get(getUrl("soaDashboard", processes.get(i)));
                wait.until(ExpectedConditions.or(
                                ExpectedConditions.visibilityOfElementLocated(By.xpath(dashboardAvg)),
                                ExpectedConditions.visibilityOfElementLocated(emptyDashboard))
                          );
                executions = driver.findElements(By.xpath(dashboardAvg + "[3]"));
                if(executions.isEmpty())
                    processesWithOutExecutions.add(processes.get(i));
                else {
                    String standardDev = driver.findElement(By.xpath(dashboardAvg + "[4]")).getAttribute("textContent"); // Standard Deviation is retrieved.
                    String totalExec = driver.findElement(By.xpath(dashboardAvg + "[5]")).getAttribute("textContent");
                    avg = Math.round(Double.parseDouble(executions.get(0).getAttribute("textContent")) / 1000); // Dashboard displays only 1 row per process.
                    max = avg + (long) Math.ceil(Double.parseDouble(standardDev) / 1000);
                    appendProcessData(writer, processes.get(i), avg, max, false, Long.parseLong(totalExec), -1, "See Real-Time Dashboard for details");
                }
            }
            else {
                // Check if the execution report is paged (to include all the records)
                List<Long> durations = new ArrayList<>();
                while (driver.findElement(nextButton).isEnabled()) {
                    for(WebElement execution : executions)
                        durations.add(convertToSeconds(execution.getAttribute("textContent")));
                    driver.findElement(nextButton).click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(reportingExecutions));
                    executions = driver.findElements(reportingExecutions);
                }
                for(WebElement execution : executions)
                    durations.add(convertToSeconds(execution.getAttribute("textContent")));

                long totalCount = durations.size();
                avg = getAverage(durations);
                max = (long) Math.ceil(avg + getStandardDeviation(durations, avg));
                durations = listExceededDurations(durations, max); //durations returned in descendent order.
                appendProcessData(writer, processes.get(i), avg, (max == 0 ? 1 : max), (max == 0), totalCount, durations.size(), summarizeDurations(durations));
            }
            System.out.println((i + 1) + " out of " +  processes.size() + " processes checked (" + processesWithOutExecutions.size() + " without logs)");
        }

        if (!processesWithOutExecutions.isEmpty()) {
            System.out.println("The following processes have no execution logs in Boomi:");
            for(String process : processesWithOutExecutions)
                System.out.println(process);
            System.out.println(processesWithOutExecutions.size() + " out of " + processes.size() + " processes in total");
        }
        driver.quit();
   }

   private static String summarizeDurations(List<Long> durations) {
        if(durations.isEmpty())
            return "";
        else {
            LinkedHashMap<Long, Long> durationsMap = new LinkedHashMap<>();
            long counter = 0;
            long currentDuration = durations.get(0);
            for (Long duration : durations) {
                if(currentDuration == duration)
                    ++counter;
                else {
                    durationsMap.put(currentDuration, counter);
                    counter = 1;
                    currentDuration = duration;
                }
            }
            durationsMap.put(currentDuration, counter);
            String dm = durationsMap.toString();
            StringBuilder sb = new StringBuilder(dm);
            sb.deleteCharAt(dm.length() - 1).deleteCharAt(0);
            dm = sb.toString();
            return dm.replace("=", " (x").replace(",", "),") + ")";
        }
   }

   private static List<Long> listExceededDurations(List<Long> durations, double max) {
        List<Long> exceededDurations = new ArrayList<>();
        for(Long duration : durations)
            if(duration > max)
                exceededDurations.add(duration);
        exceededDurations.sort(Comparator.reverseOrder());
        return exceededDurations;
   }

   private static double getStandardDeviation(List<Long> durations, double avg) {
       double max = 0;
       for(Long execTime : durations)
           max += Math.pow(execTime - avg, 2);
       if (durations.size() == 1)
           return Math.sqrt(max);
       else
           return Math.sqrt(max / (durations.size() - 1));
   }

   private static long getAverage(List<Long> durations) {
        double avg = 0;
        for(Long execTime : durations)
            avg += execTime;
        return (long) Math.floor(avg / durations.size());
   }

   private static long convertToSeconds(String duration) {
       int hours = 0, minutes, seconds;

       String[] parts = duration.split(":");
       if(parts.length < 3) {
           minutes = Integer.parseInt(parts[0]);
           seconds = Integer.parseInt(parts[1]);
       }
       else {
           hours = Integer.parseInt(parts[0]);
           minutes = Integer.parseInt(parts[1]);
           seconds = Integer.parseInt(parts[2]);
       }
       return ((hours * 3600) + (minutes * 60) + seconds);
   }
}
