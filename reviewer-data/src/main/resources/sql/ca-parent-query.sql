with pool as (
  %MAIN_QUERY%
	) 
select brand, "name", val from (
  %BRANDS%
) a order by brand, name