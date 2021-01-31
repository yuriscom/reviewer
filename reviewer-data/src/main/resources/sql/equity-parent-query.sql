with respondents_rec as
(
	%MAIN_QUERY%
),
pool as (select %GROUP_COL% count(*) cnt from respondents_rec where q1000 between 3 and 5 %GROUP_CLAUSE%),
respondents as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec  %GROUP_CLAUSE%),
familiar as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1000 between 3 and 5  %GROUP_CLAUSE%),
quality as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q1005 between 8 and 10  %GROUP_CLAUSE%),
consideration as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q1010 between 3 and 4  %GROUP_CLAUSE%),
up as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q3010=1  %GROUP_CLAUSE%),
down as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q3010=2  %GROUP_CLAUSE%),
steady as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q3010=3 %GROUP_CLAUSE%)
select %GROUP_COL% coalesce(pool.cnt,0) as pool, coalesce(respondents.cnt,0) as respondents, coalesce(familiar.cnt,0) as familiar,
coalesce(quality.cnt,0) as quality, coalesce(consideration.cnt,0) as consideration, coalesce(up.cnt,0) as up,
coalesce(down.cnt,0) as down, coalesce(steady.cnt,0) as steady
from pool
%JOIN_TYPE% join respondents %JOIN_CLAUSE%
%JOIN_TYPE% join familiar %JOIN_CLAUSE%
%JOIN_TYPE% join quality %JOIN_CLAUSE%
%JOIN_TYPE% join consideration %JOIN_CLAUSE%
%JOIN_TYPE% join up %JOIN_CLAUSE%
%JOIN_TYPE% join down %JOIN_CLAUSE%
%JOIN_TYPE% join steady %JOIN_CLAUSE%
%ORDER_CLAUSE%