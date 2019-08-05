package com.nayanzin.demojasperreports;

import com.nayanzin.demojasperreports.dto.Employee;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class DemoJasperReportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJasperReportsApplication.class, args);
    }

    @Bean
    public CommandLineRunner exportJasper() {
        return args -> {

            String destination = args.length == 1 ? args[0] : "/home/study/Desktop/employeeReport";

            // Prepare report params.
            Map<String, Object> params = new HashMap<>();
            params.put("title", "Title");
            params.put("minSalary", "10.55");

            // Prepare report objects.
            Employee employee = Employee.builder()
                    .firstName("Alex")
                    .lastName("Nayanzin")
                    .salary("100")
                    .build();

            List<Employee> employeeList = new ArrayList<>();
            employeeList.add(employee);
            employeeList.add(employee);

            // Create Bean datasource.
            JRBeanCollectionDataSource object = new JRBeanCollectionDataSource(employeeList);

            // Generate report.
            JasperPrint jasperPrint = JasperFillManager.fillReport(getClass().getClassLoader().getResourceAsStream("jasper/employeeReport.jasper"), params, object);

            // Export report to PDF, XLS, CSV, HTML
            exportToPdf(jasperPrint, destination);
            exportToXls(jasperPrint, destination);
            exportToCsv(jasperPrint, destination);
            exportToHtml(jasperPrint, destination);
        };
    }

    // Export jasper print to PDF file.
    private void exportToPdf(JasperPrint jasperPrint, String destination) throws JRException {

        // Create PDF exporter with input and output.
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destination + ".pdf"));

        // Config PDF file
        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("Nayanzin");
        exportConfig.setMetadataTitle("Employee report");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        // Export to PDF.
        exporter.exportReport();
    }

    // Export jasper print to Excel file.
    private void exportToXls(JasperPrint jasperPrint, String destination) throws JRException {

        // Create excel exporter with input and output.
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destination + ".xls"));

        // Config report.
        SimpleXlsxReportConfiguration reportConfiguration = new SimpleXlsxReportConfiguration();
        reportConfiguration.setSheetNames(new String[]{"Employee Data"});
        exporter.setConfiguration(reportConfiguration);

        // Export to Excel.
        exporter.exportReport();
    }

    // Export jasper print to CSV file.
    private void exportToCsv(JasperPrint jasperPrint, String destination) throws JRException {

        // Create csv exporter with input and output.
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(destination + ".csv"));

        // Config report.
        SimpleCsvMetadataReportConfiguration reportConfiguration = new SimpleCsvMetadataReportConfiguration();
        reportConfiguration.setWriteHeader(false);
        exporter.setConfiguration(reportConfiguration);

        // Export to CSV.
        exporter.exportReport();
    }

    // Export jasper print to HTML file.
    private void exportToHtml(JasperPrint jasperPrint, String destination) throws JRException {

        // Create HTML exporter with input and output.
        HtmlExporter exporter = new HtmlExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(destination + ".html"));

        // Config report.
        SimpleHtmlReportConfiguration reportConfiguration = new SimpleHtmlReportConfiguration();
        reportConfiguration.setAccessibleHtml(true);
        exporter.setConfiguration(reportConfiguration);

        // Export to HTML.
        exporter.exportReport();
    }
}
