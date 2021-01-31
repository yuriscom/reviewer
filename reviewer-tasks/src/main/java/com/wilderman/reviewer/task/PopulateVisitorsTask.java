package com.wilderman.reviewer.task;

import com.wilderman.reviewer.exception.ServiceException;
import com.wilderman.reviewer.service.VisitorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PopulateVisitorsTask extends BaseTask {
	@Autowired
    VisitorsService visitorsService;


	@Scheduled(cron = "${task.PopulateVisitorsTask.cron:* * * * * *}")
	@Override
	public void run() {
		super.run();
	}

	public void process(LocalDate targetDate) throws Exception {
		String processor = this.getClass().getSimpleName();

		LOG.info("[{}] start ... ", processor);
		LocalDateTime begin = LocalDateTime.now();

		visitorsService.ingestData();


		LocalDateTime end = LocalDateTime.now();
		LOG.info("[{}] end! Time costs: {} seconds", processor, Duration.between(begin, end).getSeconds());
	}




}
