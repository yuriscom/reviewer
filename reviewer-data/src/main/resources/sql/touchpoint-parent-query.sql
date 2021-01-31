with respondents_rec as
(
  %MAIN_QUERY%
),
pool as (select %GROUP_COL% count(*) cnt from respondents_rec where q1000 between 3 and 5 %GROUP_CLAUSE%),
respondents as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec %GROUP_CLAUSE%),
familiar as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where q1000 between 3 and 5 %GROUP_CLAUSE%),
tv_ad as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r1 %GROUP_CLAUSE%),
radio_ad as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r2 %GROUP_CLAUSE%),
direct_mail_ad as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r3 %GROUP_CLAUSE%),
print_ad as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r4 %GROUP_CLAUSE%),
social_media_ad as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r5 %GROUP_CLAUSE%),
digital_ad as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r6 %GROUP_CLAUSE%),
podcast_ad as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r7 %GROUP_CLAUSE%),
email_ad as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r8 %GROUP_CLAUSE%),
blog_post as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r9 %GROUP_CLAUSE%),
sponsored_event as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r10 %GROUP_CLAUSE%),
partnership as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r11 %GROUP_CLAUSE%),
product_placement as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r12 %GROUP_CLAUSE%),
word_of_mouth as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r13 %GROUP_CLAUSE%),
celebrity_endorsment as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r14 %GROUP_CLAUSE%),
news_media as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r15 %GROUP_CLAUSE%),
other as (select  %GROUP_COL% coalesce(sum(weight),0) cnt from respondents_rec where Q2010r96 %GROUP_CLAUSE%)
select %GROUP_COL% coalesce(pool.cnt,0) as pool, coalesce(respondents.cnt,0) as respondents, coalesce(familiar.cnt,0) as familiar,
coalesce(tv_ad.cnt,0) as tv_ad, coalesce(radio_ad.cnt,0) as radio_ad, coalesce(direct_mail_ad.cnt,0) as direct_mail_ad,
coalesce(print_ad.cnt,0) as print_ad, coalesce(social_media_ad.cnt,0) as social_media_ad, coalesce(digital_ad.cnt,0) as digital_ad,
coalesce(podcast_ad.cnt,0) as podcast_ad, coalesce(email_ad.cnt,0) as email_ad, coalesce(blog_post.cnt,0) as blog_post,
coalesce(sponsored_event.cnt,0) as sponsored_event, coalesce(partnership.cnt,0) as partnership, coalesce(product_placement.cnt,0) as product_placement,
coalesce(word_of_mouth.cnt,0) as word_of_mouth, coalesce(celebrity_endorsment.cnt,0) as celebrity_endorsment, coalesce(news_media.cnt,0) as news_media, coalesce(other.cnt,0) as other
from pool
%JOIN_TYPE% join respondents %JOIN_CLAUSE%
%JOIN_TYPE% join familiar %JOIN_CLAUSE%
%JOIN_TYPE% join tv_ad %JOIN_CLAUSE%
%JOIN_TYPE% join radio_ad %JOIN_CLAUSE%
%JOIN_TYPE% join direct_mail_ad %JOIN_CLAUSE%
%JOIN_TYPE% join print_ad %JOIN_CLAUSE%
%JOIN_TYPE% join social_media_ad %JOIN_CLAUSE%
%JOIN_TYPE% join digital_ad %JOIN_CLAUSE%
%JOIN_TYPE% join podcast_ad %JOIN_CLAUSE%
%JOIN_TYPE% join email_ad %JOIN_CLAUSE%
%JOIN_TYPE% join blog_post %JOIN_CLAUSE%
%JOIN_TYPE% join sponsored_event %JOIN_CLAUSE%
%JOIN_TYPE% join partnership %JOIN_CLAUSE%
%JOIN_TYPE% join product_placement %JOIN_CLAUSE%
%JOIN_TYPE% join word_of_mouth %JOIN_CLAUSE%
%JOIN_TYPE% join celebrity_endorsment %JOIN_CLAUSE%
%JOIN_TYPE% join news_media %JOIN_CLAUSE%
%JOIN_TYPE% join other %JOIN_CLAUSE%
%ORDER_CLAUSE%