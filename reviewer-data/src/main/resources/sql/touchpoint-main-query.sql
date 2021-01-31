select to_char( r.visitedOn, '%DATE_FORMAT%') as %GROUP_COL%, br.brand_id, coalesce(r.weight, %WEIGHT_COALESCE%) as weight,
br.Q2010r1, br.Q2010r2, br.Q2010r3, br.Q2010r4, br.Q2010r5, br.Q2010r6, br.Q2010r7, br.Q2010r8, br.Q2010r9
, br.Q2010r10, br.Q2010r11, br.Q2010r12, br.Q2010r13, br.Q2010r14, br.Q2010r15, br.Q2010r96, q1000.key as q1000
from survey_brand_respondent br
  inner join public.survey_respondent r on br.respondent_id=r.id
  inner join public.data_domain q1000 on q1000.id=br.q1000
  %EXTRA_JOINS%
where
  br.brand_id=%BRAND_ID% and
  q1000.value is not null and q1000.key between 1 and 5
  %EXTRA_WHERE%