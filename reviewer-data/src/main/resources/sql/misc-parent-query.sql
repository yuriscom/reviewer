with respondents_rec as
(
  %MAIN_QUERY%
),
pool as (select %GROUP_COL% count(*) cnt from respondents_rec where q1000 between 3 and 5 %GROUP_CLAUSE%),
respondents as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec %GROUP_CLAUSE%),
familiar as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1000 between 3 and 5 %GROUP_CLAUSE%),
willing_pay_more as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q1040=1 %GROUP_CLAUSE%),
irreplaceable as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q3000 in (4,5) %GROUP_CLAUSE%)
select %GROUP_COL% coalesce(pool.cnt,0) as pool, coalesce(respondents.cnt,0) as respondents, coalesce(familiar.cnt,0) as familiar,
coalesce(willing_pay_more.cnt,0) as willing_pay_more, coalesce(irreplaceable.cnt,0) as irreplaceable
from pool
%JOIN_TYPE% join respondents %JOIN_CLAUSE%
%JOIN_TYPE% join familiar %JOIN_CLAUSE%
%JOIN_TYPE% join willing_pay_more %JOIN_CLAUSE%
%JOIN_TYPE% join irreplaceable %JOIN_CLAUSE%
%ORDER_CLAUSE%