with respondents_rec as
(
  %MAIN_QUERY%
),
pool as (select %GROUP_COL% count(*) cnt from respondents_rec where q1000 between 3 and 5 %GROUP_CLAUSE%),
respondents as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec  %GROUP_CLAUSE%),
familiar as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1000 between 3 and 5  %GROUP_CLAUSE%),
corporate as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r1  %GROUP_CLAUSE%),
customer_centric as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r2  %GROUP_CLAUSE%),
dependable as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r3  %GROUP_CLAUSE%),
fun as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r4  %GROUP_CLAUSE%),
good_value as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r5  %GROUP_CLAUSE%),
hip as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r6  %GROUP_CLAUSE%),
innovative  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r7  %GROUP_CLAUSE%),
intelligent  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r8  %GROUP_CLAUSE%),
practical  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r9  %GROUP_CLAUSE%),
premium  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r10  %GROUP_CLAUSE%),
"simple"  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r11  %GROUP_CLAUSE%),
socially_conscious  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r12  %GROUP_CLAUSE%),
smart  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r13  %GROUP_CLAUSE%),
stylish  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r14  %GROUP_CLAUSE%),
traditional  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r15  %GROUP_CLAUSE%),
trustworthy  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r16  %GROUP_CLAUSE%),
unconventional  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r17  %GROUP_CLAUSE%),
visionary  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r18  %GROUP_CLAUSE%),
wholesome  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r19  %GROUP_CLAUSE%),
classy  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r20  %GROUP_CLAUSE%),
sophisticated  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r21  %GROUP_CLAUSE%),
young  as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r22  %GROUP_CLAUSE%),
energetic as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r23  %GROUP_CLAUSE%),
confident as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r24  %GROUP_CLAUSE%),
bold as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2000r25 %GROUP_CLAUSE%)
select %GROUP_COL% coalesce(pool.cnt,0) as pool, coalesce(respondents.cnt,0) as respondents, coalesce(familiar.cnt,0) as familiar,
coalesce(corporate.cnt,0) as corporate, coalesce(customer_centric.cnt,0) as customer_centric,
coalesce(dependable.cnt,0) as dependable, coalesce(fun.cnt,0) as fun, coalesce(good_value.cnt,0) as good_value,
coalesce(hip.cnt,0) as hip, coalesce(innovative.cnt,0) as innovative, coalesce(intelligent.cnt,0) as intelligent,
coalesce(practical.cnt,0) as practical, coalesce(premium.cnt,0) as premium, coalesce("simple".cnt,0) as "simple",
coalesce(socially_conscious.cnt,0) as socially_conscious, coalesce(smart.cnt,0) as smart, coalesce(stylish.cnt,0) as stylish,
coalesce(traditional.cnt,0) as traditional, coalesce(trustworthy.cnt,0) as trustworthy, coalesce(unconventional.cnt,0) as unconventional,
coalesce(visionary.cnt,0) as visionary, coalesce(wholesome.cnt,0) as wholesome, coalesce(classy.cnt,0) as classy, coalesce(sophisticated.cnt,0) as sophisticated,
coalesce(young.cnt,0) as young, coalesce(energetic.cnt,0) as energetic, coalesce(confident.cnt,0) as confident, coalesce(bold.cnt,0) as bold
from pool
%JOIN_TYPE% join respondents %JOIN_CLAUSE%
%JOIN_TYPE% join familiar %JOIN_CLAUSE%
%JOIN_TYPE% join corporate %JOIN_CLAUSE%
%JOIN_TYPE% join customer_centric %JOIN_CLAUSE%
%JOIN_TYPE% join dependable %JOIN_CLAUSE%
%JOIN_TYPE% join fun %JOIN_CLAUSE%
%JOIN_TYPE% join good_value %JOIN_CLAUSE%
%JOIN_TYPE% join hip %JOIN_CLAUSE%
%JOIN_TYPE% join innovative %JOIN_CLAUSE%
%JOIN_TYPE% join intelligent %JOIN_CLAUSE%
%JOIN_TYPE% join practical %JOIN_CLAUSE%
%JOIN_TYPE% join premium %JOIN_CLAUSE%
%JOIN_TYPE% join "simple" %JOIN_CLAUSE%
%JOIN_TYPE% join socially_conscious %JOIN_CLAUSE%
%JOIN_TYPE% join smart %JOIN_CLAUSE%
%JOIN_TYPE% join stylish %JOIN_CLAUSE%
%JOIN_TYPE% join traditional %JOIN_CLAUSE%
%JOIN_TYPE% join trustworthy %JOIN_CLAUSE%
%JOIN_TYPE% join unconventional %JOIN_CLAUSE%
%JOIN_TYPE% join visionary %JOIN_CLAUSE%
%JOIN_TYPE% join wholesome %JOIN_CLAUSE%
%JOIN_TYPE% join classy %JOIN_CLAUSE%
%JOIN_TYPE% join sophisticated %JOIN_CLAUSE%
%JOIN_TYPE% join young %JOIN_CLAUSE%
%JOIN_TYPE% join energetic %JOIN_CLAUSE%
%JOIN_TYPE% join confident %JOIN_CLAUSE%
%JOIN_TYPE% join bold %JOIN_CLAUSE%
%ORDER_CLAUSE%