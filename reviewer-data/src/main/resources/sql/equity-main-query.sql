select to_char( r.visitedOn, '%DATE_FORMAT%') as %GROUP_COL%, br.brand_id, br.respondent_id, coalesce(r.weight, %WEIGHT_COALESCE%) as weight,
  q1000.key as q1000,
  q1005.key as q1005,
  q1010.key as q1010,
  q1030.key as q1030,
  q3010.key as q3010
from survey_brand_respondent br
  inner join public.survey_respondent r on br.respondent_id=r.id
  inner join public.data_domain q1000 on q1000.id=br.q1000
  left join public.data_domain q1005 on q1005.id=br.q1005
  left join public.data_domain q1010 on q1010.id=br.q1010
  left join public.data_domain q1030 on q1030.id=br.q1030
  left join public.data_domain q3010 on q3010.id=br.q3010
  %EXTRA_JOINS%
where
  br.brand_id=%BRAND_ID% and
  q1000.value is not null and q1000.key between 1 and 5
  %EXTRA_WHERE%