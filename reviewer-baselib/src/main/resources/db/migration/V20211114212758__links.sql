-- delete from flyway_schema_history where version='20211114212758';
-- alter table client RENAME COLUMN link_google_mobile TO link_google;
-- alter table client drop column link_google_desktop;
alter table client RENAME COLUMN link_google TO link_google_mobile;
alter table client add column link_google_desktop text;

update client set link_google_desktop='https://g.page/r/CfZwMlYIc8AuEAg/review', link_google_mobile='https://g.page/r/CfZwMlYIc8AuEAo/review' where uname='accuro';
update client set link_google_desktop='https://g.page/r/CaCvhTvMAv9MEAg/review', link_google_mobile='https://g.page/r/CaCvhTvMAv9MEAo/review' where uname='cosmetic';
