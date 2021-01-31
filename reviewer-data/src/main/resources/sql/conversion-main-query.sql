select to_char( r.visitedOn, '%DATE_FORMAT%') as %GROUP_COL%, br.brand_id, coalesce(r.weight, %WEIGHT_COALESCE%) as weight, br.respondent_id, q1000.key as q1000, q1020.key as q1020,  q1030.key as q1030
from survey_brand_respondent br
  inner join public.survey_respondent r on br.respondent_id=r.id
  inner join public.data_domain q1000 on q1000.id=br.q1000
  left join public.data_domain q1020 on q1020.id=br.q1020
  left join public.data_domain q1030 on q1030.id=br.q1030
  %EXTRA_JOINS%
where
  br.brand_id=%BRAND_ID% and
  q1000.value is not null and q1000.key between 1 and 5
  %EXTRA_WHERE%