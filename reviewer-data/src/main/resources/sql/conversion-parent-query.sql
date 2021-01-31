with respondents_rec as
(
  %MAIN_QUERY%
),
pool as (select %GROUP_COL% count(*) cnt from respondents_rec where q1000 between 3 and 5 %GROUP_CLAUSE% ),
respondents as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec %GROUP_CLAUSE%),
aware as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1000 between 2 and 5 %GROUP_CLAUSE%),
familiar as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1000 between 3 and 5 %GROUP_CLAUSE%),
use_ever as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1020 is not null and q1020 between 1 and 3 %GROUP_CLAUSE%),
use_current as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1020 is not null and q1020 between 1 and 2 %GROUP_CLAUSE%),
use_regular as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1020 = 1 %GROUP_CLAUSE%),
promoter as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1030 is not null and q1030 between 9 and 10 %GROUP_CLAUSE%),
detractor as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1030 is not null and q1030 between 0 and 6 %GROUP_CLAUSE%),
passive as (select %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1030 is not null and q1030 between 7 and 8 %GROUP_CLAUSE%)
select %GROUP_COL% coalesce(pool.cnt,0) as pool, coalesce(respondents.cnt,0) as respondents, coalesce(aware.cnt,0) as aware, coalesce(familiar.cnt,0) as familiar,
coalesce(use_ever.cnt,0) as use_ever, coalesce(use_current.cnt,0) as use_current, coalesce(use_regular.cnt,0) as use_regular,
coalesce(promoter.cnt,0) as promoter, coalesce(detractor.cnt,0) as detractor, coalesce(passive.cnt,0) as passive
from pool
%JOIN_TYPE% join respondents %JOIN_CLAUSE%
%JOIN_TYPE% join aware %JOIN_CLAUSE%
%JOIN_TYPE% join familiar %JOIN_CLAUSE%
%JOIN_TYPE% join use_ever %JOIN_CLAUSE%
%JOIN_TYPE% join use_current %JOIN_CLAUSE%
%JOIN_TYPE% join use_regular %JOIN_CLAUSE%
%JOIN_TYPE% join promoter %JOIN_CLAUSE%
%JOIN_TYPE% join detractor %JOIN_CLAUSE%
%JOIN_TYPE% join passive %JOIN_CLAUSE%
%ORDER_CLAUSE%