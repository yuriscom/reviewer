package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.dto.FetchLogData;
import com.wilderman.reviewer.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VisitorFetchLogService {

    // visitors/accuro/direct/import_20220131012301.csv
    private static final String FETCH_LOG_KEY_PATTERN = "visitors\\/([^\\/]+)\\/(direct\\/)?import_\\d+\\.(csv|xml)";

    public FetchLogData getFetchLogData(VisitorFetchLog log) throws ServiceException {
        Pattern pattern = Pattern.compile(FETCH_LOG_KEY_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(log.getS3key());
        if (!matcher.find()) {
            throw new ServiceException("Could not parse Log key " + log.getS3key());
        }

        String uname = matcher.group(1);
        String direct  = matcher.group(2);
        return new FetchLogData(uname, !StringUtils.isEmpty(direct));
    }
}