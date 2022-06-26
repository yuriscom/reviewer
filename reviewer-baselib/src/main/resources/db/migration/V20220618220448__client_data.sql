alter table client add column data jsonb DEFAULT '{}'::jsonb;

update client set data='{"address":"8054 Yonge St, Thornhill, ON L4J 1W3, First Floor", "phone": "+1 (905) 886-1212"}' where uname='accuro';
update client set data='{"address":"8054 Yonge St, Thornhill, ON L4J 1W3, First Floor", "phone": "+1 (807) 770-1743"}' where uname='cosmetic';
