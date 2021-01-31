alter table patient drop column if exists hash;
alter table patient add column hash character varying(255);
drop index if exists patient__hash__idx;
CREATE INDEX IF NOT EXISTS patient__hash__idx ON public.patient(hash);


alter table visit drop column if exists hash;
alter table visit add column hash character varying(255);
drop index if exists visit__hash__idx;
CREATE INDEX IF NOT EXISTS visit__hash__idx ON public.visit(hash);

alter table patient drop column if exists sample_id;
alter table patient add column sample_id int not null default 1;