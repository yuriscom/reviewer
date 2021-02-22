select brand, '%NAME%'::varchar as name, (cast(sum(%FLD%::int * weight) as decimal) / coalesce(nullif(sum(weight), 0), 999999))::decimal as val
from pool where brand_id in (%IDS%) and %FLD% is not null group by brand