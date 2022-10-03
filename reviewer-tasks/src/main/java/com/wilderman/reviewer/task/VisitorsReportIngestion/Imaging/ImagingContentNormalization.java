package com.wilderman.reviewer.task.VisitorsReportIngestion.Imaging;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ImagingContentNormalization {

    private final static String titleLocateString = "first name";
    private final static String endLocateString = "total";

    public static String normalize(String content) {
        List<String> rows = Arrays.asList(content.split("(\\r|\\n|\\r\\n)"));
        List<String> resultRows = new ArrayList<>();
        List<Section> sections = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Integer> activeHeaderColumns = new ArrayList<>();

        sections.add(new Section());
        int curSectionIdx = 0;
//        int i = 0;
        for (int i = 0; i<rows.size(); i++) {
            String row = rows.get(i);
            String rowFormatted = row.replaceAll(",", "");
            if (rowFormatted.toLowerCase().startsWith(titleLocateString)) {

                if (sections.get(curSectionIdx).getStartIdx() != null) {
                    continue;
                }

                String[] elements = row.split(",");
                for (int j=0; j<elements.length; j++) {
                    if (StringUtils.isEmpty(elements[j])) {
                        String nextRowHeader = getNextRowHeader(rows, i, j);
                        if (!StringUtils.isEmpty(nextRowHeader)) {
                            sections.get(curSectionIdx).setSkipLine(true);
                            if (curSectionIdx == 0) {
                                headers.add(nextRowHeader);
                                activeHeaderColumns.add(j);
                            }
                        }
                    } else {
                        if (curSectionIdx == 0) {
                            headers.add(elements[j]);
                            activeHeaderColumns.add(j);
                        }
                    }
                }

                sections.get(curSectionIdx).setStartIdx(i + 1);
            }

            if (rowFormatted.toLowerCase().startsWith(endLocateString)) {
                sections.get(curSectionIdx).setEndIdx(i-1);
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

        resultRows.add(String.join(",",headers));
        for (Section section : sections) {
            if (section.getEndIdx() == null) {
                section.setEndIdx(rows.size());
            }

            for (int i = section.getStartIdx(); i<section.getEndIdx(); i++) {
                String row = rows.get(i);
                if (!StringUtils.isEmpty(row.replaceAll(",", ""))) {
                    List<String> resultElements = new ArrayList<>();
                    String[] elements = row.split(",");
                    for (int idx : activeHeaderColumns) {
                        if (elements.length > idx) {
                            resultElements.add(elements[idx]);
                        } else {
                            resultElements.add("");
                        }
                    }
                    resultRows.add(String.join(",",resultElements));
                }
            }
        }

        return String.join("\r\n", resultRows);
    }

    private static String getNextRowHeader(List<String> rows, int i, int j) {
        if (rows.size() <= i+1) {
            return "";
        }

        String[] elementsNextRow = rows.get(i+1).split(",");

        if (StringUtils.isEmpty(elementsNextRow[0]) && !StringUtils.isEmpty(elementsNextRow[j])) {
            return elementsNextRow[j];
        }

        return "";
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
