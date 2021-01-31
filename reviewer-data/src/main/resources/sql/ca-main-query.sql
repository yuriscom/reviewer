select br.*, b.name as brand, r.weight from survey_brand_respondent br
	join survey_respondent r on r.id=br.respondent_id
	join brand b on b.id=br.brand_id
	where br.brand_id in (%IDS%) and weight is not null