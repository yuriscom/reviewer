alter table visitor_fetch_log drop column if exists attempts;
alter table visitor_fetch_log add column attempts int default 0;
update visitor_fetch_log set attempts = 3;

alter table visit drop column if exists attempts;
