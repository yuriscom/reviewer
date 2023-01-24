package com.wilderman.reviewer.task.VisitorsReportIngestion.Imaging;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class ImagingContentNormalization {

    private final static String titleLocateString = "first name";
    private final static String cellPhoneLocateString = "cell phone";
    private final static String dateLocateString = "service date";
    private final static String endLocateString = "total";

    public static String normalize(String content) {
        content = content.replaceAll("^\\ufeff", "");
        List<String> rows = Arrays.asList(content.split("(\\r|\\n|\\r\\n)"))
                .stream()
                .filter(e -> StringUtils.isNotEmpty(e))
                .collect(Collectors.toList());
        List<String> resultRows = new ArrayList<>();
        List<Section> sections = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Integer> activeHeaderColumns = new ArrayList<>();

        sections.add(new Section());
        int curSectionIdx = 0;
//        int i = 0;
        for (int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);
            String rowFormatted = formatRow(row);
            if (rowFormatted.toLowerCase().startsWith(titleLocateString)) {
                String nextRow = getRowString(rows, i + 1);
                String nextRowFormatted = formatRow(nextRow);
                boolean multipleLinesHeaders = false;
                if (nextRowFormatted.toLowerCase().startsWith(cellPhoneLocateString)) {
                    multipleLinesHeaders = true;
                }
                if (sections.get(curSectionIdx).getStartIdx() != null) {
                    continue;
                }

                if (curSectionIdx == 0) {
                    generateHeaders(headers, activeHeaderColumns, row);
                    if (multipleLinesHeaders) {
                        generateHeaders(headers, activeHeaderColumns, nextRow);
                    }
                }

                int startIdx = i + 1;
                if (multipleLinesHeaders) {
                    startIdx += 1;
                }
                sections.get(curSectionIdx).setStartIdx(startIdx);
            }

            if (rowFormatted.toLowerCase().startsWith(endLocateString)) {
                sections.get(curSectionIdx).setEndIdx(i - 1);
                sections.add(new Section());
                curSectionIdx++;
            }
        }

        if (sections.get(sections.size() - 1).isEmpty()) {
            sections.remove(sections.get(sections.size() - 1));
        }

        if (activeHeaderColumns.size() != headers.size()) {
            log.error("Internal Error");
            return "";
        }

        if (headers.size() == 0) {
            log.error("Invalid Format");
            return "";
        }

        String cleanHeaders = headers.stream().filter(e -> !StringUtils.isEmpty(e)).collect(Collectors.joining(","));
        resultRows.add(String.join(",", headers));
        int maxActiveHeaderColumnIdx = Collections.max(activeHeaderColumns);
        int dateHeaderIdx = -1;
        for (int i=0; i<headers.size();i++) {
            if (headers.get(i).equalsIgnoreCase(dateLocateString)) {
                dateHeaderIdx = activeHeaderColumns.get(i);
            }
        }

        for (Section section : sections) {
            if (section.getEndIdx() == null) {
                section.setEndIdx(rows.size());
            }

            for (int i = section.getStartIdx(); i < section.getEndIdx(); i++) {
                String row = rows.get(i);
                if (!StringUtils.isEmpty(formatRow(row))) {
                    List<String> resultElements = new ArrayList<>();
                    String[] elements = row.split(",");
                    if (elements.length < maxActiveHeaderColumnIdx) {
                        continue;
                    }
                    log.info(row);
                    for (int j = 0; j <= maxActiveHeaderColumnIdx; j++) {
                        if (activeHeaderColumns.contains(j)) {
                            resultElements.add(sanitizeCellValue(elements[j], dateHeaderIdx==j));
                        }
                    }
                    resultRows.add(String.join(",", resultElements));
                }
            }
        }

        return String.join("\r\n", resultRows);
    }

    private static void generateHeaders(List<String> headers, List<Integer> activeHeaderColumns, String row) {
        String[] elements = row.split(",");
        for (int j = 0; j < elements.length; j++) {
            if (!StringUtils.isEmpty(elements[j])) {
                headers.add(elements[j]);
                activeHeaderColumns.add(j);
            }
        }
    }

    private static String sanitizeCellValue(String val, boolean isDate) {
        if (isDate) {
            return getSqlDate(val);
        } else {
            return val.replaceAll("[\"]", "");
        }
    }

    private static String getSqlDate(String date) {
        Pattern pattern = Pattern.compile("^([\\d]{1,2}\\/[\\d]{1,2}\\/[\\d]{4})");
        Matcher matcher = pattern.matcher(date);

        String sqlDate = "";
        if (matcher.find()) {
            sqlDate = matcher.group(1);
        }
        return sqlDate;
    }

    private static String getNextRowHeader(List<String> rows, int i, int j) {
        if (rows.size() <= i + 1) {
            return "";
        }

        String[] elementsNextRow = rows.get(i + 1).split(",");

        // only relevant if first cell is empty, and current location is NOT empty
        if (StringUtils.isEmpty(elementsNextRow[0]) && elementsNextRow.length > j && !StringUtils.isEmpty(elementsNextRow[j])) {
            return elementsNextRow[j];
        }

        return "";
    }

    private static String getRowString(List<String> rows, int i) {
        if (rows.size() <= i) {
            return "";
        }

        return rows.get(i);
    }

    private static String formatRow(String row) {
        return row.replaceAll(",", " ").trim();
    }

    @Data
    @NoArgsConstructor
    public static class Section {
        private Integer startIdx = null;
        private Integer endIdx = null;
        private boolean skipLine = false;

        public boolean isEmpty() {
            return startIdx == null && endIdx == null;
        }

        public Integer getStartIdx() {
            return startIdx == null ? null :
                    isSkipLine() ? startIdx + 1 : startIdx;
        }
    }
}
