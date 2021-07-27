alter table visitor_fetch_log drop if exists etag, drop if exists event_time;
alter table visitor_fetch_log add column etag varchar(255), add column event_time timestamp;


alter table visitor_fetch_log alter column created_at set default CURRENT_TIMESTAMP;