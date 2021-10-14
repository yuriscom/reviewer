-- revert --
ALTER TABLE patient DROP CONSTRAINT if exists patient__client__fk;

alter table visitor_fetch_log drop column if exists client_id;
alter table patient drop column if exists client_id;

drop table if exists client;

DROP index if exists client_unq;
DROP index if exists visitor_fetch_log_unq;
DROP index if exists patient_unq;

CREATE UNIQUE INDEX patient_unq ON patient (phone);
-- --

CREATE TABLE public.client (
	id bigserial NOT NULL,
	uname character varying(255),
	name character varying(255),
	website text,
	link_google text,
	email character varying(255),
	logo text,
	created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT null,
    CONSTRAINT client__pkey PRIMARY KEY (id)
);
CREATE UNIQUE INDEX client_unq ON client (uname);

insert into client (uname, name, website, link_google, email, logo) values
('accuro', 'Wilderman Medical Clinic', 'https://drwilderman.com/dr-wilderman/', 'https://www.google.com/search?q=wilderman+medical+clinic+reviews&oq=wilderman+medical+clinic+reviews#lrd=0x882b2c7d41be60d7:0x2ec07308563270f6,1,,,', 'yuriscom@gmail.com', 'logo.bc3311f1.png'),
('cosmetic', 'Wilderman Cosmetic Clinic', 'https://www.medicalcosmeticclinic.ca/', 'https://www.google.com/search?q=Wilderman+Medical+Cosmetic+Clinic#lrd=0x882b2c62b4a1fb05:0x4cff02cc3b85afa0,3,,,', 'yuriscom@gmail.com', 'logo.543e3158.png');

CREATE UNIQUE INDEX visitor_fetch_log_unq ON visitor_fetch_log (etag);


alter table patient add column client_id bigint;
ALTER TABLE patient
    ADD CONSTRAINT patient__client__fk FOREIGN KEY (client_id) REFERENCES client (id);
update patient set client_id=(select id from client where name='Wilderman Medical Clinic');
alter table patient alter column client_id set not null;
ALTER TABLE patient DROP CONSTRAINT if exists patient_phone_key;
DROP index if exists patient_unq;
CREATE UNIQUE INDEX patient_unq ON patient (client_id, phone);