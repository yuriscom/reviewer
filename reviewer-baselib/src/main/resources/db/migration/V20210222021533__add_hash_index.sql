drop index if exists review__hash__idx;
CREATE INDEX IF NOT EXISTS review__hash__idx ON public.review(hash);