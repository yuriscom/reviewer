select to_char( r.visitedOn, '%DATE_FORMAT%') as %GROUP_COL%, br.brand_id, coalesce(r.weight, %WEIGHT_COALESCE%) as weight, q1000.key as q1000,
  q1040.key as q1040, q3000.key as q3000
from survey_brand_respondent br
  inner join public.survey_respondent r on br.respondent_id=r.id
  inner join public.data_domain q1000 on q1000.id=br.q1000
  left join public.data_domain q1040 on q1040.id=br.q1040
  left join public.data_domain q3000 on q3000.id=br.q3000
  %EXTRA_JOINS%
where
  br.brand_id=%BRAND_ID% and
  q1000.value is not null and q1000.key between 1 and 5
  %EXTRA_WHERE%