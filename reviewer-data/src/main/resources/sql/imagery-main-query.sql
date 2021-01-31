select to_char( r.visitedOn, '%DATE_FORMAT%') as %GROUP_COL%, br.brand_id, coalesce(r.weight, %WEIGHT_COALESCE%) as weight,
br.Q2000r1, br.Q2000r2, br.Q2000r3, br.Q2000r4, br.Q2000r5, br.Q2000r6, br.Q2000r7, br.Q2000r8, br.Q2000r9, br.Q2000r10,
br.Q2000r11, br.Q2000r12, br.Q2000r13, br.Q2000r14, br.Q2000r15, br.Q2000r16, br.Q2000r17, br.Q2000r18, br.Q2000r19,
br.Q2000r20, br.Q2000r21, br.Q2000r22, br.Q2000r23, br.Q2000r24, br.Q2000r25, q1000.key as q1000
from survey_brand_respondent br
  inner join public.survey_respondent r on br.respondent_id=r.id
  inner join public.data_domain q1000 on q1000.id=br.q1000
  %EXTRA_JOINS%
where
  br.brand_id=%BRAND_ID% and
  q1000.value is not null and q1000.key between 1 and 5
  %EXTRA_WHERE%