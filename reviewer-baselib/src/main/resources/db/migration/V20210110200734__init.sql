drop table if exists review;
drop table if exists visit;
drop table if exists patient;
drop table if exists visitor_fetch_log;

CREATE TABLE public.patient (
	id bigserial NOT NULL,
	ohip character varying(255),
	birthdate timestamp,
	fname character varying(255),
	lname character varying(255),
	phone character varying(255),
	email character varying(255),
	status character varying(255) not null default 'NEW',
	created_at timestamp without time zone DEFAULT now() NOT NULL,
  updated_at timestamp without time zone DEFAULT now() NOT null,
  CONSTRAINT patient__pkey PRIMARY KEY (id),
  unique(phone)
);

CREATE INDEX IF NOT EXISTS patient__ohip__idx ON public.patient(ohip);
CREATE INDEX IF NOT EXISTS patient__email__idx ON public.patient(email);
CREATE INDEX IF NOT EXISTS patient__phone__idx ON public.patient(phone);


CREATE TABLE public.visitor_fetch_log (
	id bigserial NOT NULL,
	num_records int4 NULL DEFAULT 0,
	s3key varchar(255) NOT NULL,
	status varchar(255) NOT NULL DEFAULT 'PENDING'::character varying,
	created_at timestamp NOT NULL,
	CONSTRAINT decipher_fetch_log_pkey PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS visitor_fetch_log__status__idx ON public.visitor_fetch_log(status);

CREATE TABLE public.visit (
	id bigserial NOT NULL,
	patient_id bigint NOT null,
	log_id bigint not null,
	visited_on timestamp,
	status character varying(255) not null default 'NEW',
	attempts int default 0,
	data jsonb DEFAULT '{}'::jsonb,
	created_at timestamp without time zone DEFAULT now() NOT NULL,
  updated_at timestamp without time zone DEFAULT now() NOT null,
	CONSTRAINT visit__pkey PRIMARY KEY (id),
	CONSTRAINT visit__patient__fk FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
	CONSTRAINT visit__visitor_fetch_log__fk FOREIGN KEY (log_id) REFERENCES visitor_fetch_log(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS visit__appointment_date__idx ON public.visit(visited_on);

CREATE TABLE public.review (
	id bigserial NOT NULL,
	patient_id bigint NOT null,
	visit_id bigint not null,
	rating int not null,
	hash character varying(255) not null,
	data jsonb DEFAULT '{}'::jsonb,
	created_at timestamp without time zone DEFAULT now() NOT NULL,
  updated_at timestamp without time zone DEFAULT now() NOT null,
	CONSTRAINT review__pkey PRIMARY KEY (id),
	CONSTRAINT review__patient__fk FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE cascade,
	CONSTRAINT review__visit__fk FOREIGN KEY (visit_id) REFERENCES visit(id) ON DELETE cascade,
	unique(patient_id, visit_id)
);
CREATE INDEX IF NOT EXISTS review__rating__idx ON public.review(rating);


insert into visitor_fetch_log(num_records, s3key, status, created_at) values
(0, 'test', 'PROCESSED', '2020-12-06 17:53:40.561'),
(1, 'reportreviewmanagement.csv', 'PENDING', NOW());

INSERT INTO patient (id,ohip,birthdate,phone,email,status,created_at,updated_at) VALUES
(1,'111222333','1980-01-18','+16479661556',NULL,'RATED','2020-12-06 17:53:40.561','2020-12-06 17:53:40.561')
;
INSERT INTO visit (id,patient_id,log_id, visited_on,status,attempts,"data",created_at,updated_at) VALUES
(1,1,1,'2020-12-06','RATED',0,'{}','2020-12-06 17:54:30.692','2020-12-06 17:54:30.692')
;
INSERT INTO review (id,patient_id,visit_id,rating,hash,"data",created_at,updated_at) VALUES
(6,1,1,1,'R6WMCS','{}','2020-12-07 00:12:15.404','2020-12-07 00:12:15.404')
;

SELECT setval('patient_id_seq', (SELECT max(id) FROM "patient"));
SELECT setval('visitor_fetch_log_id_seq', (SELECT max(id) FROM "visitor_fetch_log"));
SELECT setval('visit_id_seq', (SELECT max(id) FROM "visit"));
SELECT setval('review_id_seq', (SELECT max(id) FROM "review"));