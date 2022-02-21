alter table patient drop column if exists attempts;
alter table patient add column attempts int default 0;
update patient set attempts = 3