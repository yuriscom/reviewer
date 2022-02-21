drop table if exists push_history;
CREATE TABLE public.push_history (
                               id bigserial NOT NULL,
                               log_id bigint not null,
                               created_at timestamp without time zone DEFAULT now() NOT NULL,
                               CONSTRAINT push_history__pkey PRIMARY KEY (id)
);